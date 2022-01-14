package com.rate.exchanger;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.exception.ObjectNotFoundException;
import com.rate.exchanger.repository.BankAccountRepository;
import com.rate.exchanger.service.AccountsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
class RateExchangerApplicationTests {

	@Autowired
	AccountsService accountsService;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Test
	void testAccountNotFound() {
		BankAccount bankAccount = new BankAccount(1L, 0L, "RO33RZBR9238994926845252", "RON", new BigDecimal(2500), new Date());
		bankAccountRepository.save(bankAccount);
		Assertions.assertThrows(ObjectNotFoundException.class, () -> accountsService.exchangeRate("RO33RZBR9238994926845250"));
	}

}
