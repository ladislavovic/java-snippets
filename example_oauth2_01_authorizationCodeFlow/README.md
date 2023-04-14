## OAuth2 - Authorization Code Flow

### About this example
This module demonstrates OAuth Authorization Code Flow.

TODO
* explain the communication in more detail. Now the flow description is not complete

### Modules

#### uaa
User authentication and authorization server from Cloudflare. It is a docker image. Main server configuration is in
`uaa.yml` file.

#### resource-server-app
Resource server. It has `/articles` endpoint.

#### client-app
Oauth2 client. It reads articles from resource server by `web-client`.

### How to run it
1. Modify the `/etc/hosts` file and add the entry `127.0.0.1 auth-server`
2. Build the docker image of the authorization server: `./build-uaa-image.sh`
3. Run UAA server by docker compose: `docker compose up` It binds to the port 8881.
4. Run the Resource Server: `cd resource-server-app; mvn spring-boot:run` It binds to the port 8882.
5. Run the Client: `cd client-app; mvn spring-boot:run` It binds to the port 8883.
6. Open the anonymous web browser window and go to `http://localhost:8883/news-aggregator`.
   It redirects to uaa server. Login with username "user1", password "password".
   Then click on "Authorize". It redirects you back to the client app and you can see the articles.

### The communication in detail
Nodes:\
auth-server:8881 - auth server\
localhost:8883 - client\
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
