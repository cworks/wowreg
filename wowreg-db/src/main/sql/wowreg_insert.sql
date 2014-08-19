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


