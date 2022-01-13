package com.rate.exchanger.service;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.exception.BankAccountNotFoundException;
import com.rate.exchanger.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService implements IAccountsService {

    public final BankAccountRepository bankAccountRepository;

    public AccountsService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public BigDecimal exchangeRate(String iban) {
        BankAccount bankAccount = getBankAccount(iban);
        // TO DO - implement method
        return new BigDecimal(1.25);
    }

    private BankAccount getBankAccount(String iban) {
        return bankAccountRepository.findByIban(iban).orElseThrow(() -> new BankAccountNotFoundException(iban));
    }
}
