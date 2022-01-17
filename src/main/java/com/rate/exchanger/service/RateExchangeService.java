package com.rate.exchanger.service;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.entity.ExchangeRateConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RateExchangeService {

    private final RestTemplate restTemplate;
    private final ExchangeRateConfigService exchangeRateConfigService;

    private static final Logger log = LoggerFactory.getLogger(RateExchangeService.class);

    public RateExchangeService(RestTemplate restTemplate, ExchangeRateConfigService exchangeRateConfigService) {
        this.restTemplate = restTemplate;
        this.exchangeRateConfigService = exchangeRateConfigService;
    }

    @Cacheable(cacheNames = "exchangeRate")
    public BigDecimal getCachedExchangeRate(BankAccount bankAccount) {
        return new BigDecimal(Integer.MIN_VALUE);
    }

    @CachePut(cacheNames = "exchangeRate")
    @CircuitBreaker(name = "externalRateService")
    public BigDecimal getExchangedRate(BankAccount bankAccount) {
        log.info("getExchangedRate(): Not from cache");
        ExchangeRateConfig exchangeRateConfig = exchangeRateConfigService.getExchangeRateConfig(true);
        final String uri = exchangeRateConfig.getUrl() + exchangeRateConfig.getToken();
        String result = restTemplate.getForObject(uri, String.class);
        BigDecimal fxRate = new BigDecimal(String.valueOf(((JSONObject) new JSONObject(result).get("rates")).get(bankAccount.getCurrency()))).setScale(6, RoundingMode.HALF_EVEN);
        return fxRate;
    }
}
