CREATE DATABASE rate_exchanger
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
GRANT ALL PRIVILEGES ON DATABASE rate_exchanger TO postgres;
\c rate_exchanger

CREATE TABLE account (
  id bigint NOT NULL PRIMARY KEY,
  iban VARCHAR(20) NOT NULL UNIQUE,
  currency VARCHAR(3) NOT NULL,
  balance numeric(19, 2) NOT NULL DEFAULT 0.00,
  last_update timestamp without time zone
);