package com.telegram.dolarometro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usd_statistics")
public class UsdStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usd_statistics_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "usd_type")
    private EUsdType usdType;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private EPeriod period;

    @Column(name = "max_value", precision = 20, scale = 8)
    private BigDecimal maxValue;

    @Column(name = "max_timestamp")
    private LocalDateTime maxTimestamp;

    @Column(name = "min_value", precision = 20, scale = 8)
    private BigDecimal minValue;

    @Column(name = "min_timestamp")
    private LocalDateTime minTimestamp;
    
}
