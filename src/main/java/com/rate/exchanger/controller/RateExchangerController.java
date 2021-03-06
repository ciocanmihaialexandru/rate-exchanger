package com.rate.exchanger.controller;

import com.rate.exchanger.service.AccountsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class RateExchangerController {

    private final AccountsService accountsService;

    public RateExchangerController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping(value = "/rate/account/{iban}")
    public BigDecimal getExchangeRate(@PathVariable(value = "iban") String iban) {
        return accountsService.exchangeRate(iban);
    }
}
