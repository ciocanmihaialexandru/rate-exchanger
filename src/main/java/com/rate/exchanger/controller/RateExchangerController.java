package com.rate.exchanger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateExchangerController {

    @GetMapping(value = "/rate/account/{iban}")
    public String getExchangeRate(@PathVariable(value = "iban") String iban) {
        return "true";
    }
}
