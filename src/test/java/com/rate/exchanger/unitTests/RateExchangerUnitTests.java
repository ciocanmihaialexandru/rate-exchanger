package com.rate.exchanger.unitTests;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.entity.ExchangeRateConfig;
import com.rate.exchanger.exception.ObjectNotFoundException;
import com.rate.exchanger.repository.BankAccountRepository;
import com.rate.exchanger.repository.ExchangeRateConfigRepository;
import com.rate.exchanger.service.AccountsService;
import com.rate.exchanger.service.ExchangeRateConfigService;
import com.rate.exchanger.service.RateExchangeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RateExchangerUnitTests {

    private BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private ExchangeRateConfigRepository exchangeRateConfigRepository = Mockito.mock(ExchangeRateConfigRepository.class);
    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private ExchangeRateConfigService exchangeRateConfigService;
    private RateExchangeService rateExchangeService;
    private AccountsService accountsService;

    @BeforeEach
    void initUseCase() {
        accountsService = new AccountsService(bankAccountRepository, rateExchangeService);
        exchangeRateConfigService = new ExchangeRateConfigService(exchangeRateConfigRepository);
        rateExchangeService = new RateExchangeService(restTemplate, exchangeRateConfigService);
    }

    @Test
    void testAccountNotFound() {
        BankAccount bankAccount = new BankAccount(1L, 0L, "RO33RZBR9238994926845252", "RON", new BigDecimal(2500), new Date());
        Assertions.assertThrows(ObjectNotFoundException.class, () -> accountsService.exchangeRate("RO33RZBR9238994926845250"));
    }

    @Test
    void testAccountFound_ratesNotCached() {
        BankAccount bankAccount = new BankAccount(2L, 0L, "RO33RZBR9238994926845256", "RON", new BigDecimal(2500), new Date());
        BigDecimal cachedRate = rateExchangeService.getCachedExchangeRate(bankAccount);
        Assertions.assertEquals(cachedRate, new BigDecimal(Integer.MIN_VALUE));
    }

    @Test
    void testAccountFound_definedRate() {
        BankAccount bankAccount = new BankAccount(3L, 0L, "RO33RZBR9238994926845257", "RON", new BigDecimal(2500), new Date());
        ExchangeRateConfig exchangeRateConfig = new ExchangeRateConfig(1L, 0L, "http://api.exchangeratesapi.io/latest?access_key=", "key", true);
        given(exchangeRateConfigRepository.findByEnabled(true)).willReturn(java.util.Optional.of(exchangeRateConfig));
        given(restTemplate.getForObject("http://api.exchangeratesapi.io/latest?access_key=key", String.class)).willReturn("{\"rates\":{\"RON\":5}}");
        Assertions.assertEquals(rateExchangeService.getExchangedRate(bankAccount).compareTo(new BigDecimal(5)),0);
    }

}
