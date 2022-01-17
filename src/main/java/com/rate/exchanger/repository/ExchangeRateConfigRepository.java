package com.rate.exchanger.repository;

import com.rate.exchanger.entity.BankAccount;
import com.rate.exchanger.entity.ExchangeRateConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateConfigRepository extends CrudRepository<ExchangeRateConfig, Long> {

    Optional<ExchangeRateConfig> findByEnabled(boolean isEnabled);

}
