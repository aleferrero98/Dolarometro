package com.telegram.dolarometro.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.telegram.dolarometro.model.EPeriod;
import com.telegram.dolarometro.model.EUsdType;
import com.telegram.dolarometro.model.UsdStatisticsEntity;
import com.telegram.dolarometro.repository.UsdStatisticsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsdStatisticsService {

    private final UsdStatisticsRepository usdStatisticsRepository;

    public void updateExtremesByUsdType(EUsdType usdType, BigDecimal usdExchangeRate, LocalDateTime timestamp) {
        for (EPeriod period : EPeriod.values()) {
            updateExtremesForPeriod(usdType, period, usdExchangeRate, timestamp);
        }
    }
    
    private void updateExtremesForPeriod(EUsdType usdType, EPeriod period, BigDecimal usdExchangeRate, LocalDateTime timestamp) {
        List<UsdStatisticsEntity> allUsdTypeStats = usdStatisticsRepository.findByUsdType(usdType.name());

        UsdStatisticsEntity statistics = allUsdTypeStats.stream().filter(stat -> period.equals(stat.getPeriod())).findFirst().orElse(null);
        boolean updated = false;

        if (statistics == null) {
            statistics = new UsdStatisticsEntity();

            statistics.setUsdType(usdType);
            statistics.setPeriod(period);
            statistics.setMaxValue(usdExchangeRate);
            statistics.setMaxTimestamp(timestamp);
            statistics.setMinValue(usdExchangeRate);
            statistics.setMinTimestamp(timestamp);

            updated = true;

        } else {

            LocalDateTime periodStart = period.getPeriodStart();

            if (statistics.getMaxValue() == null || statistics.getMaxTimestamp() == null
                || (usdExchangeRate.compareTo(statistics.getMaxValue()) > 0)
                || statistics.getMaxTimestamp().isBefore(periodStart)) {
                statistics.setMaxValue(usdExchangeRate);
                statistics.setMaxTimestamp(timestamp);
                updated = true;
            }

            if (statistics.getMinValue() == null || statistics.getMinTimestamp() == null
                || (usdExchangeRate.compareTo(statistics.getMinValue()) < 0)
                || statistics.getMinTimestamp().isBefore(periodStart)) {
                statistics.setMinValue(usdExchangeRate);
                statistics.setMinTimestamp(timestamp);
                updated = true;
            }
        }
        
        if (updated) {
            usdStatisticsRepository.save(statistics);
        }
    }  
}
