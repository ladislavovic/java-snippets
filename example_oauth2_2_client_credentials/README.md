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



auth-server:8881 - auth server
localhost:8883 - client
localhost:8882 - resource server

<table>

  <tr>
    <th>Request URL</th>
    <th>Description</th>
  </tr>

  <tr>
    <td>localhost:8883/articles</td>
    <td>Initial request. The user is not authenticated, so Spring redirects to the login page.</td>
  </tr>

  <tr>
    <td>localhost:8883/oauth2/authorization/articles-client-authorization-code</td>
    <td>The login "page" of the client application. It redirects you to UAA server.</td>
  </tr>

  <tr>
    <td>
  <pre>http://auth-server:8881/uaa/oauth/authorize?
  response_type=code
  &client_id=articles
  &scope=openid articles.read
  &state=rU_1vrqm49WMW0dBfFc6EhxWUOkII3bJx12H71www98=
  &redirect_uri=http://localhost:8883/login/oauth2/code/articles-client-authorization-code
  &nonce=TruuZdZH9xFxtDs8k2_9kmLIjyJVZ8nkVNW5yVjtMA4</pre>
    </td>
    <td>
<p>
<b>response_type</b> - by this param the client informs the authorization server of the desired grant type. It must be "code" (for requesting an authorization code) or "token" for requesting and access token or an extension.
</p>

<p>
<b>client_id</b> - TODO
</p>

<p>
<b>scope</b> - OPTIONAL. The authorization and token endpoints allow the client to specify the
   scope of the access request using the "scope" request parameter.
</p>

<p>
<b>state</b> - RECOMMENDED. Recommended parameter to preventing cross-site request
</p>

<p>
<b>redirect_uri</b> - where to redirect back from UAA server.
</p>
    </td>

</tr>

  <tr>
    <td>Login and authorization on auth server. Actually it is more requests and they are different for each UAA server.</td>
    <td></td>
  </tr>

  <tr>
    <td>
<pre>http://localhost:8883/login/oauth2/code/articles-client-authorization-code?
  code=V5e8Si1gfA
  &state=rU_1vrqm49WMW0dBfFc6EhxWUOkII3bJx12H71www98%3D
</pre>
    </td>
    <td>TODO</td>
  </tr>

  <tr>
    <td>localhost:8883/articles</td>
    <td></td>
  </tr>

</table>
