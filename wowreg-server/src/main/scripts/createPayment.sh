#!/bin/bash

curl -v \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
--data @payment.json \
-X POST http://localhost:4040/wow/pay
