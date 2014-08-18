Wow Event Registration
=============================

POST /:event/groups {
    [
        {
            firstName: <firstName>,
            lastName:  <lastName>,
            email:     <email>,
            phone:     <phone>,
            address:   <address>,
            city:      <city>,
            state:     <state>,
            shirt:     <shirt>,
            birthday:  <birthday>,
            type:      <primary | secondary>
        }
    ]
} {
    groupId:  <groupId>,
    dateTime: <dateTime>,
    primary: <attendee>,
    groupSize: <groupSize>
}

GET /:event/groups {
    [
        {
            groupId:   <groupId>,
            dateTime:  <dateTime>,
            primary:   <attendee>,
            groupSize: <groupSize>
        }
    ]
}

GET /:event/groups/:groupId {
    { group: { }, attendees: [ attendees ] }
}

PUT /:event/groups/:groupId {

}

DELETE /:event/groups/:groupId {

}


Entities
--------

Event {
    id,
    title,
    address,
    city,
    state,
    zipCode,
    phone,
    email,
    capacity
}

Attendee {
    id,
    firstName,
    lastName,
    email,
    phone,
    address,
    city,
    state,
    shirt,
    birthday
}

AttendeeCost {
    attendeeId
}

Group {
    id,
    dateTime,
    poc,
    size
}

GroupCost {

}

GroupXRef {
    attendeeId,
    groupId
}

Registration {

}

SELECT *
FROM Attendee, GroupXRef, Group
WHERE Attendee.id = GroupXRef.attendeeId AND GroupXRef.groupId = Group.id AND Group.poc = 'spongeBob'







