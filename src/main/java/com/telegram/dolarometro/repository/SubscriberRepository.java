package com.telegram.dolarometro.repository;

import com.telegram.dolarometro.model.SubscriberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends CrudRepository<SubscriberEntity, Long> {
    Optional<SubscriberEntity> findByChatId(Long chatId);
}