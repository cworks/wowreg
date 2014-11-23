#!/bin/sh
# Workbench Table Data copy script
# 
# Execute this to copy table data from a source RDBMS to MySQL.
# Edit the options below to customize it. You will need to provide passwords, at least.
# 
# Source DB: Mysql@127.0.0.1:3306 (MySQL)
# Target DB: Mysql@54.186.67.102:3306


# Source and target DB passwords
arg_source_password=
arg_target_password=

if [ -z "$arg_source_password" ] && [ -z "$arg_target_password" ] ; then
    echo WARNING: Both source and target RDBMSes passwords are empty. You should edit this file to set them.
fi
arg_worker_count=2
# Uncomment the following options according to your needs

# Whether target tables should be truncated before copy
# arg_truncate_target=--truncate-target
# Enable debugging output
# arg_debug_output=--log-level=debug3

/Applications/MySQLWorkbench.app/Contents/MacOS/wbcopytables --mysql-source="root@127.0.0.1:3306" --target="wowuser@54.186.67.102:3306" --source-password="$arg_source_password" --target-password="$arg_target_password" --thread-count=$arg_worker_count $arg_truncate_target $arg_debug_output --table '`wowreg`' '`registered`' '`wowreg`' '`registered`' '`id`, `attendee_id`, `registered_date`, `status`' --table '`wowreg`' '`paypal_payment_tx`' '`wowreg`' '`paypal_payment_tx`' '`id`, `paypal_id`, `paypal_payer_id`, `paypal_payer_email`, `paypal_payer_firstname`, `paypal_payer_lastname`, `paypal_tx_amount`, `paypal_tx_id`, `paypal_tx_state`, `paypal_tx_self_url`, `paypal_tx_refund_url`, `paypal_tx_parent_payment_url`, `paypal_create_time`, `paypal_update_time`, `date_added`' --table '`wowreg`' '`attendee`' '`wowreg`' '`attendee`' '`id`, `registration_id`, `event_id`, `last_name`, `first_name`, `address`, `city`, `state`, `zip`, `country`, `email`, `phone`, `payment_status`, `amount_paid`, `total_price`, `payment_date`, `date_added`' --table '`wowreg`' '`events`' '`wowreg`' '`events`' '`id`, `event_name`, `event_desc`, `start_date`, `end_date`, `registration_start`, `registration_end`, `address`, `city`, `state`, `zip`, `country`, `email`, `phone`, `venue_name`, `venue_url`, `venue_phone`, `venue_email`, `registration_limit`' --table '`wowreg`' '`attendee_group`' '`wowreg`' '`attendee_group`' '`id`, `poc_id`, `group_name`' --table '`wowreg`' '`event_prices`' '`wowreg`' '`event_prices`' '`id`, `item`, `category`, `desc`, `price`' --table '`wowreg`' '`paypal_payment_info`' '`wowreg`' '`paypal_payment_info`' '`id`, `paypal_id`, `paypal_create_time`, `paypal_update_time`, `paypal_state`, `paypal_tx_amount`, `paypal_tx_desc`, `paypal_approval_url`, `paypal_execute_url`, `paypal_self_url`, `paypal_token`, `group_id`' --table '`wowreg`' '`paypal_payment_auth`' '`wowreg`' '`paypal_payment_auth`' '`id`, `paypal_token`, `paypal_payer_id`, `date_added`' --table '`wowreg`' '`attendee_to_attendee_group`' '`wowreg`' '`attendee_to_attendee_group`' '`id`, `attendee_id`, `group_id`' --table '`wowreg`' '`attendee_cost`' '`wowreg`' '`attendee_cost`' '`id`, `attendee_id`, `event_prices_id`' --table '`wowreg`' '`events_personnel`' '`wowreg`' '`events_personnel`' '`id`, `last_name`, `first_name`, `email`, `phone`, `role`' --table '`wowreg`' '`attendee_meta`' '`wowreg`' '`attendee_meta`' '`id`, `attendee_id`, `meta_key`, `meta_value`, `meta_type`, `date_added`'

