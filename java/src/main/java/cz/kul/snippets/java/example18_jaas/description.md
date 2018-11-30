JAAS
======

JAAS was added for Java 1.3 a optional package and with 1.4 was integrated into SDK.

Terms
-------
### LoginContext ???
Client application works with this interface. It hides real
immplementation mechanism. It is created according to configuration.
Configuration can be in text file or whenever else. When you change
environment, you change login context configuration and aplication
can work without any change.

### LoginModule
LoginModule - An interface for particular authentication servide. 
You can often use existing ones for particular services.

### CallbackHandler
 When login module needs to communicate with the user to obtain
 name, password or other credentials it does not do it directly.
 It uses Callback. One CallbackHandler can communicate through
 cli, another through web, ...