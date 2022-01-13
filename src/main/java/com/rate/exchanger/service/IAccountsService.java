package com.rate.exchanger.service;

import java.math.BigDecimal;

public interface IAccountsService {

    BigDecimal exchangeRate(String iban);

}
