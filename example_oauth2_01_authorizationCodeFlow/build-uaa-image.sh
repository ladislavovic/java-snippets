#!/bin/bash

echo "Building the UAA"
cd uaa
docker build --tag example-oauth2-uaa .
cd ..
