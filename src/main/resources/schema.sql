CREATE SCHEMA IF NOT EXISTS xchange_transfer;
CREATE TABLE IF NOT EXISTS xchange_transfer.account(
    account_id BIGINT PRIMARY KEY,
    currency VARCHAR(3) NOT NULL CHECK (LENGTH(currency) = 3),
    balance DECIMAL(19, 4) NOT NULL DEFAULT 0.0 CHECK (balance >= 0.0)
);