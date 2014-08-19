# select all attendees and include group info
SELECT grp.id as group_id, grp.group_name, att.first_name, att.last_name
FROM attendee att,
    attendee_group grp,
    attendee_to_attendee_group a2g
WHERE grp.id = a2g.group_id
  AND a2g.attendee_id = att.id;

# select all attendees in a group given the group's point-of-contact
SELECT grp.id as group_id, grp.group_name, att.first_name, att.last_name
FROM attendee att,
    attendee_group grp,
    attendee_to_attendee_group a2g
WHERE grp.id = a2g.group_id
  AND a2g.attendee_id = att.id
  AND grp.id = (SELECT g.id
                FROM attendee a, attendee_group g
                WHERE a.id = g.poc_id
                  AND a.email = 'cbrown@peanuts.com');

# select all group point-of-contact(s)
SELECT grp.id group_id, grp.poc_id, att.first_name, att.last_name, att.email
FROM attendee att,
    attendee_group grp
WHERE att.id = grp.poc_id;

# select a point-of-contact given an email
SELECT grp.id group_id, grp.poc_id, att.first_name, att.last_name, att.email
FROM attendee att,
    attendee_group grp
WHERE att.id = grp.poc_id
  AND att.email = 'buckymartin@gmail.com';