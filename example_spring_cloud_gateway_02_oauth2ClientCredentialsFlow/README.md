Spring Cloud Gateway with OAuth2
=========================================

### Description
This example shows how to create api gateway with OAuth2 authorization.

#### Services
* backend-api-1 - the api, which is behind the gateway. It has
  resource owner role in OAuth2.
* gateway - api gateway. It is also resource owner in OAuth2
* client-app - client which calls api gateway. It has "client" OAuth
  role and it uses "client credentials" flow
* uaa - user authentication and authorization server


### How the authentication works
To enable authentication you must first have authorization server running and you
have to set its url to property `security.oauth2.resourceserver.jwt.issuer-uri`.

Then the api calling consists from the following steps:
1. Client request UAA server to get an access token. Example:
```bash
http -v -a user1:cross POST uaa:8881/uaa/oauth/token grant_type==client_credentials
```
The response contains JWT token.
2. Client request Api Gateway with `Authorization` header, which contains the token:
```bash
http -v GET localhost:8883/crossapi/v1/projectType/GENERAL Authorization:"Bearer <token_here>"
```
3. Api Gateway check the token by calling UAA server.
4. Api Gateway route the request to backend service. Cross Api in this case.
5. Cross Api check the token by calling UAA server.
6. Cross Api returst the response to Api Gateway
7. Api Gateway returns the response to the Client


### How to run it
1. `build.sh` - it builds docker images
2. `docker-compose up` - run all services
3. `run.sh` - send test requests


### Notes
* it uses JWT tokens
