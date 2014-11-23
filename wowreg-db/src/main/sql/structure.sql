USE wowreg;

CREATE TABLE `attendee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registration_id` int(11) DEFAULT NULL,
  `event_id` int(11) DEFAULT '0',
  `last_name` varchar(64) DEFAULT NULL,
  `first_name` varchar(64) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `state` varchar(32) DEFAULT NULL,
  `zip` varchar(16) DEFAULT NULL,
  `country` varchar(32) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `payment_status` varchar(32) DEFAULT NULL,
  `amount_paid` int(11) DEFAULT '0',
  `total_price` int(11) DEFAULT '0',
  `payment_date` datetime DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ATTENDEE_UNIQUE` (`event_id`,`last_name`,`first_name`,`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=651 DEFAULT CHARSET=utf8;

CREATE TABLE `attendee_cost` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` int(11) DEFAULT NULL,
  `event_prices_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk1_idx` (`attendee_id`),
  KEY `fk11_idx` (`event_prices_id`),
  CONSTRAINT `fk1` FOREIGN KEY (`attendee_id`) REFERENCES `attendee` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk11` FOREIGN KEY (`event_prices_id`) REFERENCES `event_prices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `attendee_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `poc_id` int(11) DEFAULT NULL,
  `group_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk3_idx` (`poc_id`),
  CONSTRAINT `fk3` FOREIGN KEY (`poc_id`) REFERENCES `attendee` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

CREATE TABLE `attendee_meta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` int(11) DEFAULT NULL,
  `meta_key` varchar(256) DEFAULT NULL,
  `meta_value` longtext,
  `meta_type` varchar(32) DEFAULT NULL,
  `date_added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk2_idx` (`attendee_id`),
  CONSTRAINT `fk2` FOREIGN KEY (`attendee_id`) REFERENCES `attendee` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `attendee_to_attendee_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` int(11) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk4_idx` (`attendee_id`),
  KEY `fk5_idx` (`group_id`),
  CONSTRAINT `fk4` FOREIGN KEY (`attendee_id`) REFERENCES `attendee` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk5` FOREIGN KEY (`group_id`) REFERENCES `attendee_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `event_prices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item` varchar(64) DEFAULT NULL,
  `category` varchar(32) DEFAULT NULL,
  `desc` varchar(256) DEFAULT NULL,
  `price` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `item_price` (`item`,`category`,`price`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

CREATE TABLE `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_name` varchar(64) DEFAULT NULL,
  `event_desc` varchar(256) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `registration_start` datetime DEFAULT NULL,
  `registration_end` datetime DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `state` varchar(32) DEFAULT NULL,
  `zip` varchar(16) DEFAULT NULL,
  `country` varchar(32) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `venue_name` varchar(128) DEFAULT NULL,
  `venue_url` varchar(128) DEFAULT NULL,
  `venue_phone` varchar(16) DEFAULT NULL,
  `venue_email` varchar(64) DEFAULT NULL,
  `registration_limit` int(11) DEFAULT '1000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `events_personnel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_name` varchar(64) DEFAULT NULL,
  `first_name` varchar(64) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `role` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `ln_fn_email` (`last_name`,`first_name`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `paypal_payment_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paypal_token` varchar(64) DEFAULT NULL,
  `paypal_payer_id` varchar(64) DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `token_payer_id` (`paypal_token`,`paypal_payer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

CREATE TABLE `paypal_payment_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paypal_id` varchar(64) DEFAULT NULL,
  `paypal_create_time` datetime DEFAULT NULL,
  `paypal_update_time` datetime DEFAULT NULL,
  `paypal_state` varchar(16) DEFAULT NULL,
  `paypal_tx_amount` int(11) DEFAULT NULL,
  `paypal_tx_desc` varchar(64) DEFAULT NULL,
  `paypal_approval_url` varchar(256) DEFAULT NULL,
  `paypal_execute_url` varchar(256) DEFAULT NULL,
  `paypal_self_url` varchar(256) DEFAULT NULL,
  `paypal_token` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

CREATE TABLE `paypal_payment_tx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paypal_id` varchar(64) DEFAULT NULL,
  `paypal_payer_id` varchar(64) DEFAULT NULL,
  `paypal_payer_email` varchar(64) DEFAULT NULL,
  `paypal_payer_firstname` varchar(64) DEFAULT NULL,
  `paypal_payer_lastname` varchar(64) DEFAULT NULL,
  `paypal_tx_amount` int(11) DEFAULT NULL,
  `paypal_tx_id` varchar(64) DEFAULT NULL,
  `paypal_tx_state` varchar(16) DEFAULT NULL,
  `paypal_tx_self_url` varchar(256) DEFAULT NULL,
  `paypal_tx_refund_url` varchar(256) DEFAULT NULL,
  `paypal_tx_parent_payment_url` varchar(256) DEFAULT NULL,
  `paypal_create_time` datetime DEFAULT NULL,
  `paypal_update_time` datetime DEFAULT NULL,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE `registered` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attendee_id` int(11) DEFAULT NULL,
  `registered_date` datetime DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
