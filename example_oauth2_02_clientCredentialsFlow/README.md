# Example on OAuth2 Client Credentials flow

### Description
In comparison with "Authorization Code Flow", there are only two
entities - Resource Server and Client. There is no User, because
Client request the resource on its own, there is no User log in.
So the flow is more simple.

The flow description:
1. Client requests ticket from UAA server. It sends its username and
   password.
2. Client make a request to Resource Server and put the token to 
   Authorization header.
3. Resource Server validate the ticket on UAA server. If it is OK, it
   returns the resource.

### How to run
1. `build.sh` - it builds docker images
2. `docker-compose up` - run all services
3. `run.sh` - send test requests

### Notes
* Spring map each authority from JWT token to Spring authority. It adds prefix
  SCOPE_. Example "node.write" -> SCOPE_node.write
* Docker use container name for DNS lookup. For example url http://uaa:8881/foo goes to UAA server, because UAA docker container has name "uaa".
