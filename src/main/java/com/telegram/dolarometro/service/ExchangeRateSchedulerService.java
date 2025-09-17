package com.telegram.dolarometro.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import jakarta.annotation.PreDestroy;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.telegram.dolarometro.service.client.AmbitoFinancieroClient;
import com.telegram.dolarometro.model.UsdExchangeRateEntity;
import com.telegram.dolarometro.model.EUsdType;
import com.telegram.dolarometro.repository.UsdExchangeRateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateSchedulerService {

    private static final Integer DECIMAL_PLACES = 2;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final UsdStatisticsService usdStatisticsService;

    private final AmbitoFinancieroClient ambitoClient;

    private final UsdExchangeRateRepository usdExchangeRateRepository;

    // At 30 second every 5 minutes, 10:00-17:59, Monday to Friday
    @Scheduled(cron = "30 */5 10-17 * * MON-FRI")
    public void checkExchangeRatesPrimary() {
        this.checkExchangeRates();
    }

    // At 30 second every 5 minutes, 10:00-17:59, Monday to Friday, 1 minute out of sync with the other scheduler
    @Scheduled(cron = "30 1/5 10-17 * * MON-FRI")
    public void checkExchangeRatesSecondary() {
        this.checkExchangeRates();
    }

    public Integer checkExchangeRates() {

        List<CompletableFuture<UsdExchangeRateEntity>> futures = List.of(
            CompletableFuture.supplyAsync(() -> this.runUsdExchangeRateTask(EUsdType.CCL_DOLLAR), executorService),
            CompletableFuture.supplyAsync(() -> this.runUsdExchangeRateTask(EUsdType.MEP_DOLLAR), executorService),
            CompletableFuture.supplyAsync(() -> this.runUsdExchangeRateTask(EUsdType.BANCO_NACION_DOLLAR), executorService),
            CompletableFuture.supplyAsync(() -> this.runUsdExchangeRateTask(EUsdType.OFFICIAL_DOLLAR), executorService),
            CompletableFuture.supplyAsync(() -> this.runUsdExchangeRateTask(EUsdType.BLUE_DOLLAR), executorService),
            CompletableFuture.supplyAsync(() -> this.runUsdExchangeRateTask(EUsdType.CRYPTO_DOLLAR), executorService)
        );

        // Wait for all queries to complete and collect results
        List<UsdExchangeRateEntity> exchangeRates = futures.stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        log.info("Successfully retrieved {} exchange rates", exchangeRates.size());

        if (!exchangeRates.isEmpty()) {
            usdExchangeRateRepository.saveAll(exchangeRates);
        }

        return exchangeRates.size();


        // TODO notificar/mandar alertas segun rules
        // TODO loguear

        
        // dollarClient.getDollarCCL()
        //     .subscribe(response -> {
        //         double valor = Double.parseDouble(response.getValor().trim().replace(",", "."));
        //         log.info("Cotización CCL actual: {} - Fecha: {}", valor, response.getFecha());
                
        //         if (valor > upperThreshold) {
        //             fxAlertBot.sendAlert("⚠️ ¡Alerta! El dólar CCL superó el umbral de $" + upperThreshold + 
        //                                "\nValor actual: $" + valor);
        //         } else if (valor < lowerThreshold) {
        //             fxAlertBot.sendAlert("📉 ¡Alerta! El dólar CCL está por debajo de $" + lowerThreshold + 
        //                                "\nValor actual: $" + valor);
        //         }
        //     }, error -> log.error("Error al consultar cotización CCL: {}", error.getMessage()));
    }

    private UsdExchangeRateEntity runUsdExchangeRateTask(EUsdType usdType) {
        UsdExchangeRateEntity usdExchangeRate = this.getUsdExchangeRate(usdType);
        usdStatisticsService.updateExtremesByUsdType(usdType, usdExchangeRate.getSellRate(), usdExchangeRate.getUpdatedAt());

        return usdExchangeRate;
    }

    private UsdExchangeRateEntity getUsdExchangeRate(EUsdType usdType) {
        return ambitoClient.getUsdExchangeRate(usdType.getUrl())
                .map(fx -> {
                    BigDecimal buyRate = usdType.getHasSpread() ? fx.getBuyRate() : fx.getValue();
                    BigDecimal sellRate = usdType.getHasSpread() ? fx.getSellRate() : fx.getValue();

                    return UsdExchangeRateEntity.builder()
                            .usdType(usdType)
                            .buyRate(buyRate.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP))
                            .sellRate(sellRate.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP))
                            .updatedAt(fx.getUpdatedAt())
                            .fetchedAt(LocalDateTime.now())
                            .build();
                })
                .orElse(null);
    }

    // To close the executor service when the application stops
    @PreDestroy
    public void shutdown() {
        if (Objects.nonNull(executorService) && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}