DROP TABLE IF EXISTS `wowreg`.`attendee_meta`;
DROP TABLE IF EXISTS `wowreg`.`attendee_cost`;
DROP TABLE IF EXISTS `wowreg`.`events`;
DROP TABLE IF EXISTS `wowreg`.`events_personnel`;
DROP TABLE IF EXISTS `wowreg`.`event_prices`;
DROP TABLE IF EXISTS `wowreg`.`attendee_to_attendee_group`;
DROP TABLE IF EXISTS `wowreg`.`attendee_group`;
DROP TABLE IF EXISTS `wowreg`.`attendee`;

CREATE TABLE `wowreg`.`attendee` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `registration_id` INT NULL DEFAULT NULL,
    `event_id` INT NULL DEFAULT NULL,
    `last_name` VARCHAR(64) NULL DEFAULT NULL,
    `first_name` VARCHAR(64) NULL DEFAULT NULL,
    `address` VARCHAR(128) NULL DEFAULT NULL,
    `city` VARCHAR(64) NULL DEFAULT NULL,
    `state` VARCHAR(32) NULL DEFAULT NULL,
    `zip` VARCHAR(16) NULL DEFAULT NULL,
    `country` VARCHAR(32) NULL DEFAULT NULL,
    `email` VARCHAR(64) NULL DEFAULT NULL,
    `phone` VARCHAR(16) NULL DEFAULT NULL,
    `payment_status` VARCHAR(32) NULL DEFAULT NULL,
    `amount_paid` INT NULL DEFAULT 0,
    `total_price` INT NULL DEFAULT 0,
    `payment_date` DATETIME NULL DEFAULT NULL,
    `date_added` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ATTENDEE_UNIQUE` USING BTREE (`last_name` ASC, `first_name` ASC, `email` ASC)
);

CREATE TABLE `attendee_cost` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` int(11) DEFAULT NULL,
  `event_prices_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk1_idx` (`attendee_id`),
  KEY `fk11_idx` (`event_prices_id`),
  CONSTRAINT `fk11` FOREIGN KEY (`event_prices_id`) REFERENCES `event_prices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk1` FOREIGN KEY (`attendee_id`) REFERENCES `attendee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `wowreg`.`attendee_cost` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `attendee_id` INT NULL,
    `cost` INT NULL DEFAULT 0,
    `item` VARCHAR(64) NULL DEFAULT NULL,
    `desc` VARCHAR(256) NULL DEFAULT NULL,
    `quantity` INT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk1_idx` (`attendee_id` ASC),
    CONSTRAINT `fk1` FOREIGN KEY (`attendee_id`) REFERENCES `wowreg`.`attendee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `wowreg`.`attendee_meta` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `attendee_id` INT NULL,
    `meta_key` VARCHAR(256) NULL DEFAULT NULL,
    `meta_value` LONGTEXT NULL DEFAULT NULL,
    `meta_type` VARCHAR(32) NULL DEFAULT NULL,
    `date_added` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk2_idx` (`attendee_id` ASC),
    CONSTRAINT `fk2` FOREIGN KEY (`attendee_id`) REFERENCES `wowreg`.`attendee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `wowreg`.`events` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `event_name` VARCHAR(64) NULL DEFAULT NULL,
    `event_desc` VARCHAR(256) NULL DEFAULT NULL,
    `start_date` DATETIME NULL DEFAULT NULL,
    `end_date` DATETIME NULL DEFAULT NULL,
    `registration_start` DATETIME NULL DEFAULT NULL,
    `registration_end` DATETIME NULL DEFAULT NULL,
    `address` VARCHAR(128) NULL DEFAULT NULL,
    `city` VARCHAR(64) NULL DEFAULT NULL,
    `state` VARCHAR(32) NULL DEFAULT NULL,
    `zip` VARCHAR(16) NULL DEFAULT NULL,
    `country` VARCHAR(32) NULL DEFAULT NULL,
    `email` VARCHAR(64) NULL DEFAULT NULL,
    `phone` VARCHAR(16) NULL DEFAULT NULL,
    `venue_name` VARCHAR(128) NULL DEFAULT NULL,
    `venue_url` VARCHAR(128) NULL DEFAULT NULL,
    `venue_phone` VARCHAR(16) NULL DEFAULT NULL,
    `venue_email` VARCHAR(64) NULL DEFAULT NULL,
    `registration_limit` INT NULL DEFAULT 1000,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC)
);

CREATE TABLE `wowreg`.`events_personnel` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `last_name` VARCHAR(64) NULL DEFAULT NULL,
    `first_name` VARCHAR(64) NULL DEFAULT NULL,
    `email` VARCHAR(64) NULL DEFAULT NULL,
    `phone` VARCHAR(16) NULL DEFAULT NULL,
    `role` VARCHAR(128) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    UNIQUE INDEX `ln_fn_email` (`last_name` ASC, `first_name` ASC, `email` ASC)
);

CREATE TABLE `wowreg`.`event_prices` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `item` VARCHAR(64) NULL DEFAULT NULL,
    `category` VARCHAR(32) NULL DEFAULT NULL,
    `desc` VARCHAR(256) NULL DEFAULT NULL,
    `price` INT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    UNIQUE INDEX `item_price` (`item` ASC, `category` ASC, `price` ASC)
);

CREATE TABLE `wowreg`.`attendee_group` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `poc_id` INT NULL,
    `group_name` VARCHAR(128) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk3_idx` (`poc_id` ASC),
    CONSTRAINT `fk3` FOREIGN KEY (`poc_id`) REFERENCES `wowreg`.`attendee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `wowreg`.`attendee_to_attendee_group` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `attendee_id` INT NULL,
    `group_id` INT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk4_idx` (`attendee_id` ASC),
    INDEX `fk5_idx` (`group_id` ASC),
    CONSTRAINT `fk4` FOREIGN KEY (`attendee_id`) REFERENCES `wowreg`.`attendee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk5` FOREIGN KEY (`group_id`) REFERENCES `wowreg`.`attendee_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `wowreg`.`registered` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `attendee_id` INT NULL,
    `registered_date` DATETIME NULL DEFAULT NULL,
    `status` VARCHAR(32) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC)
);
