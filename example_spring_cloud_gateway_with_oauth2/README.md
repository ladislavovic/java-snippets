Spring Cloud Gateway with OAuth2
=========================================

### Description
This example shows how to create api gateway with OAuth2 authorization.

Services:
* backend-api-1 - the api, which is behind the gateway. It has
  resource owner role in OAuth2.
* gateway - api gateway. It is also resource owner in OAuth2
* client-app - client which calls api gateway. It has "client" OAuth
  role and it uses "client credentials" flow
* uaa - user authentication and authorization server

Flow:
1. client-app get a token from UAA
2. client-app creates a request to api gateway. It adds the token to the headers
3. gateway check the token by the UAA server. If ok, redirects the request
  to the background-api
4. background-api check the token by the UAA server. If ok, process
  the request

### How to run
1. `build.sh` - it builds docker images
2. `docker-compose up` - run all services
3. `run.sh` - send test requests

### Notes
* it uses JWT tokens
