package com.telegram.dolarometro.service;

import com.telegram.dolarometro.model.SubscriberEntity;
import com.telegram.dolarometro.model.EStatus;
import com.telegram.dolarometro.repository.SubscriberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriberService {

    private static final int MAX_LENGTH = 64;
    private static final String TRUNCATION_SUFFIX = "…";

    private final SubscriberRepository subscriberRepository;


    // public SubscriberEntity unsubscribe(Long chatId) {
    //     SubscriberEntity subscriber = subscriberRepository.findByChatId(chatId)
    //             .orElseThrow(() -> new RuntimeException("Suscriptor no encontrado"));
        
    //     subscriber.setStatus(EStatus.DISABLED);
    //     return subscriberRepository.save(subscriber);
    // }

    public void createSubscriber(Long chatId, User user) {
        SubscriberEntity subscriber = subscriberRepository.findByChatId(chatId)
            .orElse(new SubscriberEntity());

        subscriber.setChatId(chatId);
        subscriber.setUserName(truncateName(user.getUserName())); 
        subscriber.setFirstName(truncateName(user.getFirstName()));
        subscriber.setLastName(truncateName(user.getLastName()));
        subscriber.setStatus(EStatus.ENABLED);

        subscriberRepository.save(subscriber);
    }

    public void pauseSubscriber(Long chatId) {
        SubscriberEntity subscriber = subscriberRepository.findByChatId(chatId)
            .orElseThrow(() -> new RuntimeException("Suscriptor no encontrado"));

        subscriber.setStatus(EStatus.DISABLED);

        subscriberRepository.save(subscriber);
    }

    public void deleteSubscriber(Long chatId) {
        SubscriberEntity subscriber = subscriberRepository.findByChatId(chatId)
            .orElseThrow(() -> new RuntimeException("Suscriptor no encontrado"));

        subscriberRepository.delete(subscriber);
        log.info("Subscriber deleted successfully: {}", subscriber);
    }

    private String truncateName(String name) {
        if (name != null && name.length() > MAX_LENGTH) {
            name = StringUtils.truncate(name, MAX_LENGTH);
            name = name.substring(0, name.length() - 1) + TRUNCATION_SUFFIX;
        }

        return name;
    }
}