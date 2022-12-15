#!/bin/bash

echo "Performing a clean Maven build"
mvn clean package -DskipTests=true

echo "Building the UAA"
cd uaa
docker build --tag example-scg-with-oauth2-uaa .
cd ..

echo "Building the Gateway"
cd gateway
docker build --tag example-scg-with-oauth2-gateway .
cd ..

echo "Building the Backend Service"
cd backend-api-1
docker build --tag example-scg-with-oauth2-backend-api-1 .
cd ..

echo "Building the client"
cd client-app
docker build --tag example-scg-with-oauth2-client .
cd ..
