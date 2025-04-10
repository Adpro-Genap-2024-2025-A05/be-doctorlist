#!/bin/bash

docker-compose down

docker-compose up -d

docker ps | grep be-profile