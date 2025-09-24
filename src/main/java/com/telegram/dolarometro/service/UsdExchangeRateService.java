package com.telegram.dolarometro.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.telegram.dolarometro.model.EUsdType;
import com.telegram.dolarometro.model.UsdExchangeRateEntity;
import com.telegram.dolarometro.repository.UsdExchangeRateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsdExchangeRateService {

    private final Integer HISTORICAL_RATES_LIMIT = 5;

    private final Integer CURRENT_RATE_LIMIT = 1;

    private final UsdExchangeRateRepository usdExchangeRateRepository;

    public List<UsdExchangeRateEntity> getLastUsdExchangeRatesByUsdType(EUsdType usdType) {
        return usdExchangeRateRepository.getLastUsdExchangeRatesByUsdTypeLimited(usdType.name(), HISTORICAL_RATES_LIMIT);
    }

    public List<UsdExchangeRateEntity> getLastUsdExchangeRatesForEachUsdType() {

        return Arrays.asList(EUsdType.values()).stream()
                                            .map(usdType -> usdExchangeRateRepository.getLastUsdExchangeRatesByUsdTypeLimited(usdType.name(), CURRENT_RATE_LIMIT))
                                            .filter(list -> !list.isEmpty()).map(list -> list.get(0)).collect(Collectors.toList());
    }
    
}
