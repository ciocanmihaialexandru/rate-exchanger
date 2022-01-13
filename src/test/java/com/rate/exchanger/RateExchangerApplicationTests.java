package com.rate.exchanger;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.exception.BankAccountNotFoundException;
import com.rate.exchanger.repository.BankAccountRepository;
import com.rate.exchanger.service.AccountsService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
class RateExchangerApplicationTests {

	@Autowired
	AccountsService accountsService;

	@MockBean
	private BankAccountRepository bankAccountRepository;

	@Test
	void testAccountNotFound() {
		BankAccount bankAccount = new BankAccount(1L, 0L, "RO33RZBR9238994926845252", "RON", new BigDecimal(2500), new Date());
		bankAccountRepository.save(bankAccount);
		Assertions.assertThrows(BankAccountNotFoundException.class, () -> accountsService.exchangeRate("RO33RZBR9238994926845250"));
	}

}
