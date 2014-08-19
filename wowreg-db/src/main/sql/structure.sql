-- phpMyAdmin SQL Dump
-- version 3.5.8.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 13, 2014 at 02:51 PM
-- Server version: 5.5.38-35.2-log
-- PHP Version: 5.4.23

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


--
-- Database: `womenof3_wrd1`
--
CREATE DATABASE `womenof3_wrd1` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `womenof3_wrd1`;


-- --------------------------------------------------------

--
-- Table structure for table `wp_events_answer`
--

DROP TABLE IF EXISTS `wp_events_answer`;
CREATE TABLE IF NOT EXISTS `wp_events_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registration_id` varchar(23) NOT NULL,
  `attendee_id` int(11) NOT NULL DEFAULT '0',
  `question_id` int(11) NOT NULL DEFAULT '0',
  `answer` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `registration_id` (`registration_id`),
  KEY `attendee_id` (`attendee_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11168 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_attendee`
--

DROP TABLE IF EXISTS `wp_events_attendee`;
CREATE TABLE IF NOT EXISTS `wp_events_attendee` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `registration_id` varchar(23) DEFAULT '0',
  `lname` varchar(45) DEFAULT NULL,
  `fname` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `address2` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `zip` varchar(45) DEFAULT NULL,
  `country_id` varchar(128) DEFAULT NULL,
  `organization_name` varchar(50) DEFAULT NULL,
  `vat_number` varchar(20) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payment` varchar(45) DEFAULT NULL,
  `payment_status` varchar(45) DEFAULT 'Incomplete',
  `txn_type` varchar(45) DEFAULT NULL,
  `txn_id` varchar(45) DEFAULT NULL,
  `amount_pd` decimal(20,2) DEFAULT '0.00',
  `total_cost` decimal(20,2) DEFAULT '0.00',
  `price_option` varchar(100) DEFAULT NULL,
  `coupon_code` varchar(45) DEFAULT NULL,
  `quantity` varchar(5) DEFAULT '0',
  `payment_date` varchar(45) DEFAULT NULL,
  `event_id` varchar(45) DEFAULT NULL,
  `event_time` varchar(15) DEFAULT NULL,
  `end_time` varchar(15) DEFAULT NULL,
  `start_date` varchar(45) DEFAULT NULL,
  `end_date` varchar(45) DEFAULT NULL,
  `attendee_session` varchar(250) DEFAULT NULL,
  `transaction_details` text,
  `pre_approve` int(11) DEFAULT '1',
  `checked_in` int(1) DEFAULT '0',
  `checked_in_quantity` int(11) DEFAULT '0',
  `hashSalt` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `registration_id` (`registration_id`),
  KEY `event_id` (`event_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1504 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_attendee_cost`
--

DROP TABLE IF EXISTS `wp_events_attendee_cost`;
CREATE TABLE IF NOT EXISTS `wp_events_attendee_cost` (
  `attendee_id` int(11) DEFAULT NULL,
  `cost` decimal(20,2) DEFAULT '0.00',
  `quantity` int(11) DEFAULT NULL,
  KEY `attendee_id` (`attendee_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_attendee_meta`
--

DROP TABLE IF EXISTS `wp_events_attendee_meta`;
CREATE TABLE IF NOT EXISTS `wp_events_attendee_meta` (
  `ameta_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attendee_id` int(11) DEFAULT NULL,
  `meta_key` varchar(255) DEFAULT NULL,
  `meta_value` longtext,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`ameta_id`),
  KEY `attendee_id` (`attendee_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=540 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_category_detail`
--

DROP TABLE IF EXISTS `wp_events_category_detail`;
CREATE TABLE IF NOT EXISTS `wp_events_category_detail` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) DEFAULT NULL,
  `category_identifier` varchar(45) DEFAULT NULL,
  `category_desc` text,
  `display_desc` varchar(4) DEFAULT NULL,
  `wp_user` int(22) DEFAULT '1',
  UNIQUE KEY `id` (`id`),
  KEY `category_identifier` (`category_identifier`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_category_rel`
--

DROP TABLE IF EXISTS `wp_events_category_rel`;
CREATE TABLE IF NOT EXISTS `wp_events_category_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `cat_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_detail`
--

DROP TABLE IF EXISTS `wp_events_detail`;
CREATE TABLE IF NOT EXISTS `wp_events_detail` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_code` varchar(26) DEFAULT '0',
  `event_name` varchar(100) DEFAULT NULL,
  `event_desc` text,
  `display_desc` varchar(1) DEFAULT 'Y',
  `display_reg_form` varchar(1) DEFAULT 'Y',
  `event_identifier` varchar(75) DEFAULT NULL,
  `start_date` varchar(15) DEFAULT NULL,
  `end_date` varchar(15) DEFAULT NULL,
  `registration_start` varchar(15) DEFAULT NULL,
  `registration_end` varchar(15) DEFAULT NULL,
  `registration_startT` varchar(15) DEFAULT NULL,
  `registration_endT` varchar(15) DEFAULT NULL,
  `visible_on` varchar(15) DEFAULT NULL,
  `address` text,
  `address2` text,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `zip` varchar(11) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `venue_title` varchar(250) DEFAULT NULL,
  `venue_url` varchar(250) DEFAULT NULL,
  `venue_image` text,
  `venue_phone` varchar(15) DEFAULT NULL,
  `virtual_url` varchar(250) DEFAULT NULL,
  `virtual_phone` varchar(15) DEFAULT NULL,
  `reg_limit` varchar(25) DEFAULT '999999',
  `allow_multiple` varchar(15) DEFAULT 'N',
  `additional_limit` int(10) DEFAULT '5',
  `send_mail` varchar(2) DEFAULT 'Y',
  `is_active` varchar(1) DEFAULT 'Y',
  `event_status` varchar(1) DEFAULT 'A',
  `conf_mail` text,
  `use_coupon_code` varchar(1) DEFAULT 'N',
  `use_groupon_code` varchar(1) DEFAULT 'N',
  `category_id` text,
  `coupon_id` text,
  `tax_percentage` float DEFAULT NULL,
  `tax_mode` int(11) DEFAULT NULL,
  `member_only` varchar(1) DEFAULT NULL,
  `post_id` int(11) DEFAULT NULL,
  `post_type` varchar(50) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `externalURL` varchar(255) DEFAULT NULL,
  `early_disc` varchar(10) DEFAULT NULL,
  `early_disc_date` varchar(15) DEFAULT NULL,
  `early_disc_percentage` varchar(1) DEFAULT 'N',
  `question_groups` longtext,
  `item_groups` longtext,
  `event_type` varchar(250) DEFAULT NULL,
  `allow_overflow` varchar(1) DEFAULT 'N',
  `overflow_event_id` int(10) DEFAULT '0',
  `recurrence_id` int(11) DEFAULT '0',
  `email_id` int(11) DEFAULT '0',
  `alt_email` text,
  `event_meta` longtext,
  `wp_user` int(22) DEFAULT '1',
  `require_pre_approval` int(11) DEFAULT '0',
  `timezone_string` varchar(250) DEFAULT NULL,
  `likes` int(22) DEFAULT NULL,
  `submitted` datetime NOT NULL,
  `ticket_id` int(22) DEFAULT '0',
  `certificate_id` int(22) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `event_code` (`event_code`),
  KEY `wp_user` (`wp_user`),
  KEY `event_name` (`event_name`),
  KEY `city` (`city`),
  KEY `state` (`state`),
  KEY `start_date` (`start_date`),
  KEY `end_date` (`end_date`),
  KEY `registration_start` (`registration_start`),
  KEY `registration_end` (`registration_end`),
  KEY `reg_limit` (`reg_limit`),
  KEY `event_status` (`event_status`),
  KEY `recurrence_id` (`recurrence_id`),
  KEY `submitted` (`submitted`),
  KEY `likes` (`likes`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_discount_codes`
--

DROP TABLE IF EXISTS `wp_events_discount_codes`;
CREATE TABLE IF NOT EXISTS `wp_events_discount_codes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(50) DEFAULT NULL,
  `coupon_code_price` decimal(20,2) DEFAULT NULL,
  `use_percentage` varchar(1) DEFAULT NULL,
  `coupon_code_description` text,
  `each_attendee` varchar(1) DEFAULT NULL,
  `wp_user` int(22) DEFAULT '1',
  `quantity` int(7) NOT NULL DEFAULT '0',
  `use_limit` varchar(1) NOT NULL DEFAULT 'N',
  `use_exp_date` varchar(1) NOT NULL DEFAULT 'N',
  `exp_date` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `coupon_code` (`coupon_code`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_discount_rel`
--

DROP TABLE IF EXISTS `wp_events_discount_rel`;
CREATE TABLE IF NOT EXISTS `wp_events_discount_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `discount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_email`
--

DROP TABLE IF EXISTS `wp_events_email`;
CREATE TABLE IF NOT EXISTS `wp_events_email` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email_name` varchar(100) DEFAULT NULL,
  `email_subject` varchar(250) DEFAULT NULL,
  `email_text` text,
  `wp_user` int(22) DEFAULT '1',
  UNIQUE KEY `id` (`id`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_locale`
--

DROP TABLE IF EXISTS `wp_events_locale`;
CREATE TABLE IF NOT EXISTS `wp_events_locale` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `identifier` varchar(26) DEFAULT '0',
  `wp_user` int(22) DEFAULT '1',
  UNIQUE KEY `id` (`id`),
  KEY `identifier` (`identifier`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_locale_rel`
--

DROP TABLE IF EXISTS `wp_events_locale_rel`;
CREATE TABLE IF NOT EXISTS `wp_events_locale_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `venue_id` int(11) DEFAULT NULL,
  `locale_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `venue_id` (`venue_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_meta`
--

DROP TABLE IF EXISTS `wp_events_meta`;
CREATE TABLE IF NOT EXISTS `wp_events_meta` (
  `emeta_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `meta_key` varchar(255) DEFAULT NULL,
  `meta_value` longtext,
  `date_added` datetime DEFAULT NULL,
  PRIMARY KEY (`emeta_id`),
  KEY `event_id` (`event_id`),
  KEY `meta_key` (`meta_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_multi_event_registration_id_group`
--

DROP TABLE IF EXISTS `wp_events_multi_event_registration_id_group`;
CREATE TABLE IF NOT EXISTS `wp_events_multi_event_registration_id_group` (
  `primary_registration_id` varchar(255) DEFAULT NULL,
  `registration_id` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_personnel`
--

DROP TABLE IF EXISTS `wp_events_personnel`;
CREATE TABLE IF NOT EXISTS `wp_events_personnel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `role` varchar(250) DEFAULT NULL,
  `identifier` varchar(26) DEFAULT '0',
  `email` text,
  `meta` text,
  `wp_user` int(22) DEFAULT '1',
  UNIQUE KEY `id` (`id`),
  KEY `identifier` (`identifier`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_personnel_rel`
--

DROP TABLE IF EXISTS `wp_events_personnel_rel`;
CREATE TABLE IF NOT EXISTS `wp_events_personnel_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`),
  KEY `person_id` (`person_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_prices`
--

DROP TABLE IF EXISTS `wp_events_prices`;
CREATE TABLE IF NOT EXISTS `wp_events_prices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `price_type` varchar(50) DEFAULT NULL,
  `event_cost` decimal(20,2) NOT NULL DEFAULT '0.00',
  `surcharge` decimal(10,2) NOT NULL DEFAULT '0.00',
  `surcharge_type` varchar(10) DEFAULT NULL,
  `member_price_type` varchar(50) DEFAULT NULL,
  `member_price` decimal(20,2) NOT NULL DEFAULT '0.00',
  `max_qty` int(7) DEFAULT '0',
  `max_qty_members` int(7) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=176 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_qst_group`
--

DROP TABLE IF EXISTS `wp_events_qst_group`;
CREATE TABLE IF NOT EXISTS `wp_events_qst_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) NOT NULL DEFAULT 'NULL',
  `group_identifier` varchar(45) NOT NULL DEFAULT 'NULL',
  `group_description` text,
  `group_order` int(11) DEFAULT '0',
  `show_group_name` tinyint(1) NOT NULL DEFAULT '1',
  `show_group_description` tinyint(1) NOT NULL DEFAULT '1',
  `system_group` tinyint(1) NOT NULL DEFAULT '0',
  `wp_user` int(22) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `system_group` (`system_group`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_qst_group_rel`
--

DROP TABLE IF EXISTS `wp_events_qst_group_rel`;
CREATE TABLE IF NOT EXISTS `wp_events_qst_group_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `question_id` (`question_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=53 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_question`
--

DROP TABLE IF EXISTS `wp_events_question`;
CREATE TABLE IF NOT EXISTS `wp_events_question` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL DEFAULT '0',
  `sequence` int(11) NOT NULL DEFAULT '0',
  `question_type` enum('TEXT','TEXTAREA','MULTIPLE','SINGLE','DROPDOWN') NOT NULL DEFAULT 'TEXT',
  `question` text NOT NULL,
  `system_name` varchar(15) DEFAULT NULL,
  `response` text,
  `required` enum('Y','N') NOT NULL DEFAULT 'N',
  `required_text` text,
  `admin_only` enum('Y','N') NOT NULL DEFAULT 'N',
  `wp_user` int(22) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `wp_user` (`wp_user`),
  KEY `system_name` (`system_name`),
  KEY `admin_only` (`admin_only`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_seating_chart`
--

DROP TABLE IF EXISTS `wp_events_seating_chart`;
CREATE TABLE IF NOT EXISTS `wp_events_seating_chart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `image_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_seating_chart_event`
--

DROP TABLE IF EXISTS `wp_events_seating_chart_event`;
CREATE TABLE IF NOT EXISTS `wp_events_seating_chart_event` (
  `event_id` int(11) DEFAULT NULL,
  `seating_chart_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_seating_chart_event_seat`
--

DROP TABLE IF EXISTS `wp_events_seating_chart_event_seat`;
CREATE TABLE IF NOT EXISTS `wp_events_seating_chart_event_seat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seat_id` int(11) DEFAULT NULL,
  `event_id` int(11) DEFAULT NULL,
  `attendee_id` int(11) DEFAULT NULL,
  `purchase_price` float DEFAULT NULL,
  `purchase_datetime` datetime DEFAULT '0000-00-00 00:00:00',
  `by_admin` int(11) DEFAULT '0' COMMENT '0=No,1=marked occupied by admin',
  `occupied` int(11) DEFAULT '1' COMMENT '0=Free,1=occupied (basically entry in this table means occupied, but still keeping this option for any future functionality)',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_seating_chart_level_section_alignment`
--

DROP TABLE IF EXISTS `wp_events_seating_chart_level_section_alignment`;
CREATE TABLE IF NOT EXISTS `wp_events_seating_chart_level_section_alignment` (
  `seating_chart_id` int(11) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `section` varchar(255) DEFAULT NULL,
  `alignment` varchar(100) DEFAULT NULL,
  `sort_order` varchar(100) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_seating_chart_seat`
--

DROP TABLE IF EXISTS `wp_events_seating_chart_seat`;
CREATE TABLE IF NOT EXISTS `wp_events_seating_chart_seat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seating_chart_id` int(11) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `section` varchar(255) DEFAULT NULL,
  `row` varchar(255) DEFAULT NULL,
  `seat` varchar(255) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `member_price` float DEFAULT NULL,
  `custom_tag` text,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_start_end`
--

DROP TABLE IF EXISTS `wp_events_start_end`;
CREATE TABLE IF NOT EXISTS `wp_events_start_end` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `start_time` varchar(10) DEFAULT NULL,
  `end_time` varchar(10) DEFAULT NULL,
  `reg_limit` int(15) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_venue`
--

DROP TABLE IF EXISTS `wp_events_venue`;
CREATE TABLE IF NOT EXISTS `wp_events_venue` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `identifier` varchar(26) DEFAULT '0',
  `address` varchar(250) DEFAULT NULL,
  `address2` varchar(250) DEFAULT NULL,
  `city` varchar(250) DEFAULT NULL,
  `state` varchar(250) DEFAULT NULL,
  `zip` varchar(250) DEFAULT NULL,
  `country` varchar(250) DEFAULT NULL,
  `meta` text,
  `wp_user` int(22) DEFAULT '1',
  UNIQUE KEY `id` (`id`),
  KEY `identifier` (`identifier`),
  KEY `wp_user` (`wp_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wp_events_venue_rel`
--

DROP TABLE IF EXISTS `wp_events_venue_rel`;
CREATE TABLE IF NOT EXISTS `wp_events_venue_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) DEFAULT NULL,
  `venue_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------


--
-- Table structure for table `wp_wow`
--

DROP TABLE IF EXISTS `wp_wow`;
CREATE TABLE IF NOT EXISTS `wp_wow` (
  `wow_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `wow_registration_id` varchar(23) DEFAULT NULL,
  `fname` varchar(45) DEFAULT NULL,
  `lname` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `zip` varchar(45) DEFAULT NULL,
  `is_adult` varchar(8) DEFAULT NULL,
  `wow_tshirt` varchar(45) DEFAULT NULL,
  `wow_tshirt_cost` decimal(20,2) DEFAULT '0.00',
  `wow_room` varchar(45) DEFAULT NULL,
  `wow_room_cost` decimal(20,2) DEFAULT '0.00',
  `payment_status` varchar(45) DEFAULT NULL,
  `payment_date` varchar(45) DEFAULT NULL,
  `txn_id` varchar(64) DEFAULT NULL,
  `payment_total` decimal(20,2) DEFAULT '0.00',
  PRIMARY KEY (`wow_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1441 ;
