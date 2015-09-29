--
-- Rules for inserting attendees
-- 1) attendee record
-- 2) attendee_cost record to create items the attendee is registering for
-- 3) attendee_meta record to create metadata about the attendee such as age_class {teen | adult}
--
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Martin', 'Bucky', 'buckymartin@gmail.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Libre', 'Nacho', 'nacho@nacho.net');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Norris', 'Chuck', 'thechuck@thepain.net');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Bob', 'Sponge', 'spongebob@nick.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Brown', 'Charlie', 'cbrown@peanuts.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Skywalker', 'Luke', 'luke@theforce.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Vader', 'Darth', 'vader@thedarkside.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Crunch', 'Captain', 'capcrunch@gmail.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Boyardee', 'Chef', 'chef@food.com');
INSERT INTO `wowreg`.`attendee` (`last_name`, `first_name`, `email`) VALUES ('Ruth', 'Babe', 'thebabe@mlb.com');

INSERT INTO `wowreg`.`attendee_group` (`poc_id`, `group_name`) VALUES (1, 'rockers');
INSERT INTO `wowreg`.`attendee_group` (`poc_id`, `group_name`) VALUES (5, 'geeks');
INSERT INTO `wowreg`.`attendee_group` (`poc_id`, `group_name`) VALUES (8, 'freaks');

INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (1, 1);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (2, 1);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (3, 1);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (4, 1);

INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (5, 2);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (6, 2);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (7, 2);

INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (8, 3);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (9, 3);
INSERT INTO `wowreg`.`attendee_to_attendee_group` (`attendee_id`, `group_id`) VALUES (10, 3);

INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('XS Shirt', 'merchandise', 'Xtra small Wow Conference 2015 shirtSize', 1000);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('S Shirt', 'merchandise', 'Small Wow Conference 2015 shirtSize', 1000);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('M Shirt', 'merchandise', 'Medium Wow Conference 2015 shirtSize', 1000);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('L Shirt', 'merchandise', 'Large Wow Conference 2015 shirtSize', 1200);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('XL Shirt', 'merchandise', 'Xtra Large Wow Conference 2015 shirtSize', 1200);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('XXL Shirt', 'merchandise', 'Xtra Xtra Large Wow Conference 2015 shirtSize', 1300);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('S Shout Out', 'ad', 'Small 1/8 page Shout Out', 2500);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('M Shout Out', 'ad', 'Medium 1/4 page Shout Out', 5000);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('L Shout Out', 'ad', 'Large 1/2 page Shout Out', 7500);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('XL Shout Out', 'ad', 'Xtra Large full page Shout Out', 10000);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$1 donation', 'donation', '$1 donation', 100);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$2 donation', 'donation', '$2 donation', 200);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$3 donation', 'donation', '$3 donation', 300);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$4 donation', 'donation', '$4 donation', 400);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$5 donation', 'donation', '$5 donation', 500);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$6 donation', 'donation', '$6 donation', 600);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$7 donation', 'donation', '$7 donation', 700);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$8 donation', 'donation', '$8 donation', 800);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$9 donation', 'donation', '$9 donation', 900);
INSERT INTO `wowreg`.`event_prices` (`item`, `category`, `desc`, `price`) VALUES('$10 donation', 'donation', '$10 donation', 1000);

INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (1, 3);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (1, 7);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (1, 11);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (2, 2);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (2, 8);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (2, 12);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (3, 6);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (3, 9);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (4, 4);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (4, 10);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (4, 15);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (5, 6);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (6, 5);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (6, 7);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (6, 16);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (7, 3);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (7, 20);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (8, 5);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (8, 7);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (8, 18);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (9, 1);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (9, 12);
INSERT INTO `wowreg`.`attendee_cost` (`attendee_id`,`event_prices_id`) VALUES (10, 15);

INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (1, 'age_class', 'adult', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (2, 'age_class', 'adult', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (3, 'age_class', 'teen', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (4, 'age_class', 'adult', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (5, 'age_class', 'teen', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (6, 'age_class', 'adult', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (7, 'age_class', 'teen', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (8, 'age_class', 'adult', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (9, 'age_class', 'adult', 'string');
INSERT INTO `wowreg`.`attendee_meta` (`attendee_id`, `meta_key`, `meta_value`, `meta_type`) VALUES (10, 'age_class', 'adult', 'string');



