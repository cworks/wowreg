#!/bin/bash

curl -v \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
--data @attendees.json \
-X POST http://localhost:4040/wow/attendees
