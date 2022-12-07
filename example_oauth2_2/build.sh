#!/bin/bash

echo "Performing a clean Maven build"
mvn clean package -DskipTests=true

echo "Building the UAA"
cd uaa
docker build --tag example-oauth2-uaa .
cd ..

echo "Building the resource server"
cd resource-server-app
docker build --tag example-oauth2-rs .
cd ..

echo "Building the client"
cd client-app
docker build --tag example-oauth2-client .
cd ..
