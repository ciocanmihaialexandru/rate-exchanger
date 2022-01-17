package com.rate.exchanger.service;

import com.rate.exchanger.entity.ExchangeRateConfig;
import com.rate.exchanger.exception.ObjectNotFoundException;
import com.rate.exchanger.repository.ExchangeRateConfigRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateConfigService {

    private ExchangeRateConfigRepository exchangeRateConfigRepository;

    public ExchangeRateConfigService(ExchangeRateConfigRepository exchangeRateConfigRepository) {
        this.exchangeRateConfigRepository = exchangeRateConfigRepository;
    }

    @Cacheable(cacheNames = "exchangeRateConfig")
    public ExchangeRateConfig getExchangeRateConfig(boolean enabled) {
        return exchangeRateConfigRepository.findByEnabled(enabled).orElseThrow(() -> new ObjectNotFoundException("Bank account not found"));
    }
}
