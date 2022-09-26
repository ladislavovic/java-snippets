## Spring Security OAuth Authorization Server

### Relevant information:

This module demonstrates OAuth authorization flow using Spring Authorization Server, Spring OAuth Resource Server and
Spring OAuth Client.

- Run the Authorization Server from the `spring-authorization-server` module
    - IMPORTANT: Modify the `/etc/hosts` file and add the entry `127.0.0.1 auth-server`
- Run the Resource Server from `resource-server` module
- Run the client from `client-server` module
- Go to `http://127.0.0.1:8080/articles`
    - Enter the credentials `admin/password`
- The module uses the new OAuth stack with Java 11

### Relevant Articles:

- [Spring Security OAuth Authorization Server](https://www.baeldung.com/spring-security-oauth-auth-server)

### My notes
It implements authentication&authorisation by OpenID Connect. The flow of the program:

1. go to `http://localhost:8080/articles`
2. it redirects to `http://localhost:8080/oauth2/authorization/articles-client-oidc`. It is a login page configured in client `SecurityConfig.java`
3. it redirects to `http://auth-server:9000/...`
4. it redirects to `http://auth-server:9000/login`, because the user is not logged in
5. after logging in it redirects back to the client application to `http://localhost:8080/login/oauth2/code/articles-client-oidc?...` with some oauth params

It does not work well, because it does not redirect back to original url `/articles`. There are some errors in the http communication.
