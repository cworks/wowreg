-- ----------------------------------------------------------------------------
-- MySQL Workbench Migration
-- Migrated Schemata: wowreg
-- Source Schemata: wowreg
-- Created: Fri Mar  6 22:23:07 2015
-- ----------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;;

-- ----------------------------------------------------------------------------
-- Schema wowreg
-- ----------------------------------------------------------------------------
DROP SCHEMA IF EXISTS `wowreg` ;
CREATE SCHEMA IF NOT EXISTS `wowreg` ;

-- ----------------------------------------------------------------------------
-- Table wowreg.attendee
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`attendee` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `registration_id` VARCHAR(128) NULL DEFAULT NULL,
  `event_id` INT(11) NULL DEFAULT '0',
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
  `payment_date` DATETIME NULL DEFAULT NULL,
  `date_added` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1049
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.attendee_cost
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`attendee_cost` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` INT(11) NULL DEFAULT NULL,
  `shirt_size` VARCHAR(45) NULL DEFAULT NULL,
  `shirt_cost` INT(11) NULL DEFAULT '0',
  `room_cost` INT(11) NULL DEFAULT '0',
  `donation_cost` INT(11) NULL DEFAULT '0',
  `age_class` VARCHAR(45) NULL DEFAULT NULL,
  `total_cost` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk1_idx` (`attendee_id` ASC),
  CONSTRAINT `fk1`
    FOREIGN KEY (`attendee_id`)
    REFERENCES `wowreg`.`attendee` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 648
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.attendee_group
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`attendee_group` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `poc_id` INT(11) NULL DEFAULT NULL,
  `group_name` VARCHAR(128) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk3_idx` (`poc_id` ASC),
  CONSTRAINT `fk3`
    FOREIGN KEY (`poc_id`)
    REFERENCES `wowreg`.`attendee` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 352
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.attendee_meta
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`attendee_meta` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` INT(11) NULL DEFAULT NULL,
  `meta_key` VARCHAR(256) NULL DEFAULT NULL,
  `meta_value` LONGTEXT NULL DEFAULT NULL,
  `meta_type` VARCHAR(32) NULL DEFAULT NULL,
  `date_added` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk2_idx` (`attendee_id` ASC),
  CONSTRAINT `fk2`
    FOREIGN KEY (`attendee_id`)
    REFERENCES `wowreg`.`attendee` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 903
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.attendee_to_attendee_group
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`attendee_to_attendee_group` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` INT(11) NULL DEFAULT NULL,
  `group_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk4_idx` (`attendee_id` ASC),
  INDEX `fk5_idx` (`group_id` ASC),
  CONSTRAINT `fk4`
    FOREIGN KEY (`attendee_id`)
    REFERENCES `wowreg`.`attendee` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk5`
    FOREIGN KEY (`group_id`)
    REFERENCES `wowreg`.`attendee_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 897
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.event_prices
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`event_prices` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `item` VARCHAR(64) NULL DEFAULT NULL,
  `category` VARCHAR(32) NULL DEFAULT NULL,
  `desc` VARCHAR(256) NULL DEFAULT NULL,
  `price` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `item_price` (`item` ASC, `category` ASC, `price` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 22
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.events
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`events` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
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
  `registration_limit` INT(11) NULL DEFAULT '1000',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.events_personnel
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`events_personnel` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `last_name` VARCHAR(64) NULL DEFAULT NULL,
  `first_name` VARCHAR(64) NULL DEFAULT NULL,
  `email` VARCHAR(64) NULL DEFAULT NULL,
  `phone` VARCHAR(16) NULL DEFAULT NULL,
  `role` VARCHAR(128) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `ln_fn_email` (`last_name` ASC, `first_name` ASC, `email` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.paypal_payment_auth
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`paypal_payment_auth` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `paypal_token` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_payer_id` VARCHAR(64) NULL DEFAULT NULL,
  `date_added` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `token_payer_id` (`paypal_token` ASC, `paypal_payer_id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 211
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.paypal_payment_info
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`paypal_payment_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `paypal_id` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_create_time` DATETIME NULL DEFAULT NULL,
  `paypal_update_time` DATETIME NULL DEFAULT NULL,
  `paypal_state` VARCHAR(16) NULL DEFAULT NULL,
  `paypal_tx_amount` INT(11) NULL DEFAULT NULL,
  `paypal_tx_desc` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_approval_url` VARCHAR(256) NULL DEFAULT NULL,
  `paypal_execute_url` VARCHAR(256) NULL DEFAULT NULL,
  `paypal_self_url` VARCHAR(256) NULL DEFAULT NULL,
  `paypal_token` VARCHAR(64) NULL DEFAULT NULL,
  `group_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 331
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.paypal_payment_tx
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`paypal_payment_tx` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `paypal_id` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_payer_id` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_payer_email` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_payer_firstname` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_payer_lastname` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_tx_amount` INT(11) NULL DEFAULT NULL,
  `paypal_tx_id` VARCHAR(64) NULL DEFAULT NULL,
  `paypal_tx_state` VARCHAR(16) NULL DEFAULT NULL,
  `paypal_tx_self_url` VARCHAR(256) NULL DEFAULT NULL,
  `paypal_tx_refund_url` VARCHAR(256) NULL DEFAULT NULL,
  `paypal_tx_parent_payment_url` VARCHAR(256) NULL DEFAULT NULL,
  `paypal_create_time` DATETIME NULL DEFAULT NULL,
  `paypal_update_time` DATETIME NULL DEFAULT NULL,
  `date_added` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 189
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table wowreg.registered
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `wowreg`.`registered` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` INT(11) NULL DEFAULT NULL,
  `registered_date` DATETIME NULL DEFAULT NULL,
  `status` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
SET FOREIGN_KEY_CHECKS = 1;;
