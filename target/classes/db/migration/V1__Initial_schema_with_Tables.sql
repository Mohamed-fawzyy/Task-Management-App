CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ========================================
-- 1. Drop all tables in correct order to avoid FK constraints
-- ========================================
DROP TABLE IF EXISTS email_verification_token CASCADE;
DROP TABLE IF EXISTS user_auth CASCADE;

-- ========================================
-- 2. Recreate tables with UUID columns
-- ========================================
-- user_auth table
CREATE TABLE IF NOT EXISTS user_auth
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    user_role   VARCHAR(50)  NOT NULL
);

-- email_verification_token table
CREATE TABLE IF NOT EXISTS email_verification_token
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    code        VARCHAR(50) NOT NULL,
    expiry_date TIMESTAMP   NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id     UUID        NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES user_auth (id)
);

-- refresh_token table
CREATE TABLE IF NOT EXISTS refresh_token
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    user_id     UUID         NOT NULL UNIQUE,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES user_auth (id)
);