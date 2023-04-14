#!/bin/bash

echo "Performing a clean Maven build"
mvn clean package -DskipTests=true

echo "Building the UAA"
cd uaa
docker build --tag example-oauth2-2-client-credentials-uaa .
cd ..

echo "Building the Resource Server"
cd resource-server-app
docker build --tag example-oauth2-2-client-credentials-rs .
cd ..

echo "Building the Client"
cd client-app
docker build --tag example-oauth2-2-client-credentials-client .
cd ..
