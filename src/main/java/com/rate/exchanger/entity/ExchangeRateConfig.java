package com.rate.exchanger.entity;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class ExchangeRateConfig implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @NotNull
    @Size(min = 1, max = 255)
    private String url;

    private String token;

    private boolean enabled;

    public ExchangeRateConfig(Long id, Long version, @Size(min = 1, max = 255) String url, String token, boolean enabled) {
        this.id = id;
        this.version = version;
        this.url = url;
        this.token = token;
        this.enabled = enabled;
    }

    public ExchangeRateConfig() {

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateConfig that = (ExchangeRateConfig) o;
        return enabled == that.enabled &&
                Objects.equals(id, that.id) &&
                Objects.equals(version, that.version) &&
                Objects.equals(url, that.url) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, url, token, enabled);
    }
}
