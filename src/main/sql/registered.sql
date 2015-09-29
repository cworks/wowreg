USE wowreg;
SELECT 
    att.id,
    att.registration_id,
    att.last_name,
    att.first_name,
    att.address,
    att.city,
    att.state,
    att.zip,
    att.email,
    att.phone,
    att.payment_status,
    att.payment_date,
    cost.shirt_size,
    cost.shirt_cost,
    cost.room_cost,
    cost.donation_cost,
    cost.age_class,
    cost.total_cost
FROM attendee att LEFT JOIN attendee_cost cost ON att.id = cost.attendee_id
WHERE att.registration_id IS NOT NULL
ORDER BY id DESC;