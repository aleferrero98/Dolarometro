package com.telegram.dolarometro.service.client;

import org.apache.commons.lang3.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import com.telegram.dolarometro.dto.AmbitoFinancieroUsdResponseDto;
import com.telegram.dolarometro.dto.UsdExchangeRateDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AmbitoFinancieroClient {

    private final WebClient webClient;

    private static final String AMBITO_BASE_URL = "https://mercados.ambito.com";

    private static final Integer TIMEOUT_SECONDS = 10;
    
    public AmbitoFinancieroClient() {
        this.webClient = WebClient.builder()
                .baseUrl(AMBITO_BASE_URL)
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .defaultHeader("Accept", "application/json")
                .build();
    }
    
    public Optional<UsdExchangeRateDto> getUsdExchangeRate(String uri) {
        UsdExchangeRateDto result = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(AmbitoFinancieroUsdResponseDto.class)
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .map(response -> {
                    LocalDateTime updatedAt = parseDate(response.getFecha());
                    
                    UsdExchangeRateDto dto = UsdExchangeRateDto.builder()
                            .buyRate(normalizeNumericValue(response.getCompra()))
                            .sellRate(normalizeNumericValue(response.getVenta()))
                            .value(normalizeNumericValue(response.getValor()))
                            .updatedAt(updatedAt)
                            .build();
                    
                    return dto;
                })
                .onErrorResume(error -> {
                    log.error("Error getting {}: {}", uri, error.getMessage());

                    return Mono.empty();
                })
                .block();
        
        return Optional.ofNullable(result);
    }

    private BigDecimal normalizeNumericValue(String value) {
        if (Objects.nonNull(value)) {
            String numberWithoutThousandsPoint = StringUtils.replace(value, ".", "");
            String numberWithDecimalPoint = StringUtils.replace(numberWithoutThousandsPoint, ",", ".");
    
            return new BigDecimal(numberWithDecimalPoint);
        }

        return null;
    }

    // date format: "01/09/2025 - 19:55"
    private LocalDateTime parseDate(String dateTime) {
        try {
            String[] parts = dateTime.split(" - ");
            String date = parts[0];
            String time = parts[1];

            String[] dateParts = date.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            return LocalDateTime.of(year, month, day, hour, minute, 0);
        } catch (Exception e) {
            log.error("Error parsing the date obtained in the response from Ambito Financiero", e);

            // If parsing fails, return current date
            return LocalDateTime.now();
        }
    }

}