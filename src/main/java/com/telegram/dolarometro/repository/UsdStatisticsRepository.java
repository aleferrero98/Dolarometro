package com.telegram.dolarometro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.telegram.dolarometro.model.EUsdType;
import com.telegram.dolarometro.model.UsdStatisticsEntity;

@Repository
public interface UsdStatisticsRepository extends CrudRepository<UsdStatisticsEntity, Long> {

    @Query(value = "SELECT * FROM dolarometro.usd_statistics WHERE usd_type = :usdType", nativeQuery = true)
    List<UsdStatisticsEntity> findByUsdType(@Param("usdType") String usdType);
    
}
