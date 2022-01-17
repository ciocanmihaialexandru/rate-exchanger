package com.rate.exchanger.service;

import com.rate.exchanger.entity.BankAccount;
import org.json.JSONObject;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RateExchangeService {

    @Cacheable(cacheNames = "exchangeRate")
    public BigDecimal getCachedExchangeRate(BankAccount bankAccount) {
        return new BigDecimal(Integer.MIN_VALUE);
    }

    @CachePut(cacheNames = "exchangeRate")
    public BigDecimal getExchangedRate(BankAccount bankAccount) {
        System.out.println("not from cache");
        final String uri = "http://api.exchangeratesapi.io/latest?access_key=";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        BigDecimal fxRate = new BigDecimal(String.valueOf(((JSONObject) new JSONObject(result).get("rates")).get(bankAccount.getCurrency()))).setScale(6, RoundingMode.HALF_EVEN);
        return fxRate;
    }
}
