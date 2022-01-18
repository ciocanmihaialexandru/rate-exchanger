package com.rate.exchanger;

import com.rate.exchanger.controller.RateExchangerController;
import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.entity.ExchangeRateConfig;
import com.rate.exchanger.repository.BankAccountRepository;
import com.rate.exchanger.repository.ExchangeRateConfigRepository;
import com.rate.exchanger.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateExchangerController.class)
@ActiveProfiles("test")
public class FlowTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountsService accountsService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private ExchangeRateConfigRepository exchangeRateConfigRepository;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {

        BankAccount bankAccount = new BankAccount(3L, 0L, "RO33RZBR9238994926845257", "RON", new BigDecimal(2500), new Date());
        bankAccountRepository.save(bankAccount);

        ExchangeRateConfig exchangeRateConfig = new ExchangeRateConfig(1L, 0L, "http://api.exchangeratesapi.io/latest?access_key=", "40c018b15f3f3a969c8be91b043f549a", true);
        exchangeRateConfigRepository.save(exchangeRateConfig);

        when(accountsService.exchangeRate(bankAccount.getIban())).thenReturn(new BigDecimal(500));

        this.mockMvc.perform(get("/rate/account/RO33RZBR9238994926845257")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("500")));
    }

}
