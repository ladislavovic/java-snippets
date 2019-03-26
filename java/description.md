Java
=============

### -client vs -server
If -client flag is used, the JVM set various parameters to prefer quick startup
and smaller memmory footprint. -server options try tuning to maximize peak operating
speed. It use more compillation optimizations, use more memmory etc. Practically
-client option is good for gui applications, -server for server applications. The default value is server.

### classpath
List of directories, *.jar and *.zip where the classes are searched. It uses fallback strategy with this order:
  -cp or -classpath
  CLASSPATH env variable
  the classpatch is the current directory only (.)
Items in classpath are separated by semicolon (;). You can use asterisk on the end. It means all *.jar and *.zip
in the directory. Then the order is not specified.

### classloaders
JVM classloaders are in parent child hierarchy. The hierarchy is following:
1. Root classloader - written in native code, load classes from rt.jar and probably from other jre libraries
2. Extension classloader - child of root, load classes from JAVA_HOME/lib/ext and from directories on java.ext.dirs
   property
3. Application classloader - child of extension classloader, load classes from classpath

Root classloader "classpath" can be modified by -Xbootclasspath/p option which prepent dirs/jars in front of
bootstrap classpath. It works with Java 8 and older, Java 9 does not support this mechanism anymore, because it
introduced module system and do not use bootclasspath anymore. On Java 9 you can achieve similar effect with
--patch-module option.

### Extension mechanism
The extension mechanism provides a standard, scalable way to make custom APIs available to all applications running on 
the Java platform. Simply Java creates Extension Classloader which load classes placed in directories specified on
java.ext.dirs property.

### Endorsed mechanism
This mechanism allows to override some parts of JRE API. It allows override API for XML processing, CORBA, etc. You
can not for example override String implementation. Now the Endorsed mechanism is deprecated and will be removed.

### Environment vs Java System Properties
Environment variables are set in OS befor the JVM is executed and they can not be changed in running JVM.
Properties are set by JVM and are modifyable.

### Java Management Extensions (JMX)
* From my perspective is is overengineered. It contains lot of components and lot of rules for so somple problem.
* technology for managing and monitoring applications
* since Java 5
* architecture:
  Layer1 - Probe level - MBeans
  Layer2 - Agent level - MBeanServer (MBeans are registered there)
  Layer3 - Remote Management - The client which can communicate with MBeanServer. It can communicate through RMI, WS, ...
* MBean vs MXBean
  * MXBean is a special case of MBean. It has more strict rules.
  * MBean can use custom model classes. JMX client than also need to have the class to communicate with the server.
    When MXBean use custom model class, it is converted to some general data structures. See javax.management.openbean
    package.
  * MXBean does not have so stupid naming rules. MBean must use interface <Name>MBean and imlementing class must name
    <Name>. MXBean does not have so strict rules. Interface can be marked by MXBean annotation and implementation
    can have any name.
* ObjectName has following structure: <domain>:<properties>. I do not know if properties are somehow standardized.



* java and monitoring
  * Flight Recorder
* java and assertions
* JVM tuning
* XProf
* https://www.oracle.com/technetwork/java/whitepaper-135217.html
* security manager
* interesting articles: https://www.javacodegeeks.com/2015/09/advanced-java.html