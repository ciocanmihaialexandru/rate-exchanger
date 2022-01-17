package com.rate.exchanger.service;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.exception.ObjectNotFoundException;
import com.rate.exchanger.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AccountsService implements IAccountsService {

    private BankAccountRepository bankAccountRepository;
    private RateExchangeService rateExchangeService;

    public AccountsService(BankAccountRepository bankAccountRepository, RateExchangeService rateExchangeService) {
        this.bankAccountRepository = bankAccountRepository;
        this.rateExchangeService = rateExchangeService;
    }

    @Override
    public BigDecimal exchangeRate(String iban) {
        BankAccount bankAccount = getBankAccount(iban);
        BigDecimal exchangeRate = rateExchangeService.getCachedExchangeRate(bankAccount);
        if (exchangeRate.compareTo(new BigDecimal(Integer.MIN_VALUE)) == 0) {
            exchangeRate = rateExchangeService.getExchangedRate(bankAccount);
        }
        return convertAmount(bankAccount, exchangeRate);
    }

    private BigDecimal convertAmount(BankAccount bankAccount, BigDecimal rate) {
        return bankAccount.getBalance().divide(rate, 2, RoundingMode.HALF_EVEN);
    }

    private BankAccount getBankAccount(String iban) {
        return bankAccountRepository.findByIban(iban).orElseThrow(() -> new ObjectNotFoundException("Bank account not found"));
    }
}
