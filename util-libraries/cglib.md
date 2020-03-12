cglib
===============
* when you create a proxy from the class, the proxy does not contain any origin object implementation. It is implemented
  by subclassing so proxy is a child of the original object (class).
* you can not do it for final classes or final methods

JDK proxy
================
* the proxy implements an interface and contains InvocationHandler. So it does not contains wrapped instance directly.
  Invocation handler can contain it.
* The name of the proxy is like "$Proxy5" so you can not identify easily original class
