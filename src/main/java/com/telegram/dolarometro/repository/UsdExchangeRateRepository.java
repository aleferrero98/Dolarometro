package com.telegram.dolarometro.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.telegram.dolarometro.model.UsdExchangeRateEntity;

@Repository
public interface UsdExchangeRateRepository extends CrudRepository<UsdExchangeRateEntity, Long> {

    @Query(value = "SELECT * FROM dolarometro.usd_exchange_rate WHERE usd_type = :usdType ORDER BY updated_at DESC LIMIT :limit", nativeQuery = true)
    List<UsdExchangeRateEntity> getLastUsdExchangeRatesByUsdTypeLimited(@Param("usdType") String usdType, @Param("limit") Integer limit);

}
