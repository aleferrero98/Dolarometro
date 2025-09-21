-- Initialization script for Dolarometro Bot database
-- This script runs automatically when the MySQL container is created

CREATE DATABASE IF NOT EXISTS dolarometro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dolarometro;

-- Grant all privileges to the application user
GRANT ALL PRIVILEGES ON dolarometro.* TO 'dolarometro_bot'@'%';

FLUSH PRIVILEGES;

-- Tables
CREATE TABLE IF NOT EXISTS `usd_exchange_rate` (
    `usd_exchange_rate_id` int unsigned NOT NULL AUTO_INCREMENT,
    `usd_type` VARCHAR(32) NOT NULL,
    `buy_rate` DECIMAL(20,8) NOT NULL,
    `sell_rate` DECIMAL(20,8) NOT NULL,
    `fetched_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`usd_exchange_rate_id`)
);

CREATE TABLE IF NOT EXISTS `subscriber` (
    `subscriber_id` int unsigned NOT NULL AUTO_INCREMENT,
    `chat_id` int unsigned NOT NULL UNIQUE,
    `user_name` VARCHAR(64) DEFAULT NULL,
    `first_name` VARCHAR(64) DEFAULT NULL,
    `last_name` VARCHAR(64) DEFAULT NULL,
    `status` ENUM('ENABLED','DISABLED') NOT NULL DEFAULT 'ENABLED',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`subscriber_id`)
);

CREATE TABLE IF NOT EXISTS `usd_statistics` (
    `usd_statistics_id` int unsigned NOT NULL AUTO_INCREMENT,
    `usd_type` VARCHAR(32) NOT NULL,
    `period` ENUM('DAY','WEEK','MONTH','YEAR') NOT NULL,
    `max_value` DECIMAL(20,8) NOT NULL,
    `max_timestamp` DATETIME NOT NULL,
    `min_value` DECIMAL(20,8) NOT NULL,
    `min_timestamp` DATETIME NOT NULL,
    PRIMARY KEY (`usd_statistics_id`)
);

SELECT 'Dolarometro Bot database initialized successfully!' as status;
