package com.telegram.dolarometro.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.telegram.dolarometro.model.UsdExchangeRateEntity;

@Repository
public interface UsdExchangeRateRepository extends CrudRepository<UsdExchangeRateEntity, Long> {

}
