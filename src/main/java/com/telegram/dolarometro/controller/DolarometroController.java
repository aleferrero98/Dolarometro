package com.telegram.dolarometro.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telegram.dolarometro.service.ExchangeRateSchedulerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dolarometro/v1")
public class DolarometroController {

    private final ExchangeRateSchedulerService exchangeRateSchedulerService;

    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/fx/check")
    public ResponseEntity<?> checkExchangeRates() {
        Integer exchAmount = exchangeRateSchedulerService.checkExchangeRates();
        String message = String.format("Successfully processed %d exchange rates", exchAmount);

        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }
    
}
