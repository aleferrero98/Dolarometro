-- Initialization script for Dolarometro Bot database
-- This script runs automatically when the MySQL container is created

CREATE DATABASE IF NOT EXISTS dolarometro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dolarometro;

-- Grant all privileges to the application user
GRANT ALL PRIVILEGES ON dolarometro.* TO 'dolarometro_bot'@'%';

FLUSH PRIVILEGES;

-- Tables
CREATE TABLE `usd_exchange_rate` (
    `usd_exchange_rate_id` int unsigned NOT NULL AUTO_INCREMENT,
    `usd_type` VARCHAR(32) NOT NULL,
    `buy_rate` DECIMAL(20,8) NOT NULL,
    `sell_rate` DECIMAL(20,8) NOT NULL,
    `fetched_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`usd_exchange_rate_id`)
);

SELECT 'Dolarometro Bot database initialized successfully!' as status;
