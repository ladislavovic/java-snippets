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

### Heap dump
* there is more ways how to get heap dump - VisualVM, jmap (deprecated), calling JMX bean, ...
* when you are taking heap dump it stops java process for a while. On my computer (Core i7 3GHz, SSD) to create 3.5 GB
  dump it takes about 30s.
  
### Annotation processor
* In essence, any Java class could become a full-blow annotation processor just by implementing a single interface:
 javax.annotation.processing.Processor
* each implementation must provide a public no-argument constructor

### VisualVM
* a tool for JVM monitoring
* it is newer than JConsole (part of JDK since version 6 Update 7). In comparison with that JConsole uses JMX only but 
  Visual VM uses also other APIs to monitore JVM (Jvmstat, Attach API and SA).
  
### Java vs Javaw
* java - run a java application and associate it with console
* java - run a java application and do not associate it with console. So the user can not see
         black console window, the standard output is not visible etc. Used mainly for GUI
         applications.

### 'Hiding' terms

#### Overloading
void m(int i) {...}
void m(String s) {...}

#### Overriding
Just method overriding in descendant

#### Shadowning
```Java
class C {
    int i = 0;

    void m() {
        int i = 1;
        System.out.println(i); // local i variable shadow i field. You must use this.i to access field
    }
}
```

#### Hiding
```Java
class Animal {
    int legs = 2;
}

class Dog extends Animal {
    int legs = 4; // Dog.legs hides Animal.legs = 4. Use super.legs to access superclass field
}
```

#### Obscuring
```Java
class C {
    void m() {
        String System = "";
        System.out.println("Hello World"); 
        // Will not compile. System is obscured by the local variable. You must use static import
        // or full name java.lang.System
    }
}
```

### Services
Java has a services feature out of the box. For more details see ServiceLoader javadoc.

The main idea is there is an interface or set or interfaces (it is called a Service) and there is also one
or more implementations of the service (implementation is called the Service Provider). There is
also configuration file META-INF/services/package.and.name.of.the.Service, which contains all
implementations     of the service, again class with the full name. In the runtime you can get particular
provider by the ServiceLoader class.

### Serialization
* only class implementing Serializable interface can be serialized
* all subtypes of class which implement Serializable are also serializable
* you can customize serialization by magic methods writeObject() and readObject()
* you can write completely new serialization protocol by implementing Externalizable interface
* some changes on the class are incompatible and cause you can not deserialize the old class anymore.
  It is recommended to have always seraialVersionUID to enable deserialization after modifications. Modifications:
  * add a field - compatible
  * remove a field - compatible
  * move class in hierarchy - incompatible
  * change a field type - incompatible



* nio
  * http://tutorials.jenkov.com/java-nio/index.html
  * https://dzone.com/articles/java-nio-vs-io
* nio2
* JVM tuning
* XProf
* https://www.oracle.com/technetwork/java/whitepaper-135217.html
* security manager
* interesting articles: https://www.javacodegeeks.com/2015/09/advanced-java.html
* look on all tools in bin directory

