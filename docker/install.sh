#!/bin/bash

sudo apt-get update
sudo apt-get install docker

docker-compose stop
docker-compose rm -f
docker-compose up -d
