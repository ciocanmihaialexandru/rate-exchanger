CREATE DATABASE rate_exchanger
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
GRANT ALL PRIVILEGES ON DATABASE rate_exchanger TO postgres;
\c rate_exchanger

CREATE TABLE bank_account (
  id bigint NOT NULL PRIMARY KEY,
  version bigint NOT NULL,
  iban VARCHAR(32) NOT NULL UNIQUE,
  currency VARCHAR(3) NOT NULL,
  balance numeric(19, 2) NOT NULL DEFAULT 0.00,
  last_updated timestamp without time zone
);

CREATE TABLE exchange_rate_config (
  id bigint NOT NULL PRIMARY KEY,
  version bigint NOT NULL,
  url VARCHAR(255) NOT NULL UNIQUE,
  token VARCHAR(255),
  enabled boolean default true
);

INSERT INTO bank_account values(1, 0, 'RO66RZBR6837312324226695', 'RON', 3500, current_date);
INSERT INTO bank_account values(2, 0, 'RO72RZBR2998497575697246', 'RON', 25000, current_date);
INSERT INTO bank_account values(3, 0, 'RO12PORL8235726762578739', 'RON', 75000, current_date);
INSERT INTO bank_account values(4, 0, 'RO65RZBR4965861368341393', 'RON', 120000, current_date);
INSERT INTO bank_account values(5, 0, 'RO62PORL8157642722117553', 'RON', 9000, current_date);

INSERT INTO exchange_rate_config values(1, 0, 'http://api.exchangeratesapi.io/latest?access_key=', '40c018b15f3f3a969c8be91b043f549a', true);