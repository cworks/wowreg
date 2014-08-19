#!/bin/bash

curl -v \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-X GET http://localhost:4567/wow/attendees
