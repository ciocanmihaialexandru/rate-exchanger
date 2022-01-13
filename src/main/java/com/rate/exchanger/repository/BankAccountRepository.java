package com.rate.exchanger.repository;

import com.rate.exchanger.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

    @Override
    Optional<BankAccount> findById(Long id);

    Optional<BankAccount> findByIban(String iban);
}
