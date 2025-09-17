package com.telegram.dolarometro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsdExchangeRateDto {

    private BigDecimal buyRate;

    private BigDecimal sellRate;

    private LocalDateTime updatedAt;

    private BigDecimal value;

}