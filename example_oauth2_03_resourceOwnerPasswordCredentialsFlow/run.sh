#!/bin/bash

echo -e "\n#\n# Running the example \"Oauth2 - Client Credentials\"\n#"


echo -e "\n#\n# Step 1 - Try to access resource server directly. It falls, because the user is not authenticated\n#"
http localhost:8882/booking-calendar

echo -e "\n#\n# Step 2 - Try to access resource server by client. It works, because client gets token from UAA server\n#"
http localhost:8883/calendar
