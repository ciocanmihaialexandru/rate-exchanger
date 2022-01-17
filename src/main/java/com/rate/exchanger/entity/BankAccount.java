package com.rate.exchanger.entity;

import com.sun.istack.NotNull;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @NotNull
    @Column(unique=true, length = 32)
    @Size(min = 1, max = 32)
    private String iban;

    @NotNull
    @Column(length = 3)
    @Size(min = 3, max = 3)
    private String currency;

    @NotNull
    private BigDecimal balance;

    @LastModifiedDate
    private Date lastUpdated;

    public BankAccount() { }

    public BankAccount(Long id, Long version, @Size(min = 1, max = 32) String iban, @Size(min = 3, max = 3) String currency, BigDecimal balance, Date lastUpdated) {
        this.id = id;
        this.version = version;
        this.iban = iban;
        this.currency = currency;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(version, that.version) &&
                Objects.equals(iban, that.iban) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, iban, currency, balance, lastUpdated);
    }
}
