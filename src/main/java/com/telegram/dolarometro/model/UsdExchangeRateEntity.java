package com.telegram.dolarometro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "usd_exchange_rate")
public class UsdExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usd_exchange_rate_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "usd_type")
    private EUsdType usdType;

    @Column(name = "buy_rate", precision = 20, scale = 8)
    private BigDecimal buyRate;

    @Column(name = "sell_rate", precision = 20, scale = 8)
    private BigDecimal sellRate;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
