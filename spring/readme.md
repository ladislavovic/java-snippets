Spring
=============

Batch
-----------

* - Job - container for steps
* Job ->* JobInstance ->* JobExecution

Async
---------------
* methods marked by @Async annotation will be executed
  asynchronously
* When you do no specify your own Executor, the
  SimpleAsyncUncaughtExceptionHandler is used. It just
  create new thread for each asynchronous call
* for detailed information about how the Async works read
  javadoc of @EnableAsync
  
Spring and AOP
-------------------
* Spring supports two ways how to define aspects:
  1. Schema-based - definition in xml
  2. @AspectJ - through AspectJ annotations, but it is still
     pure Proxy AOP framework, there is not dependency on AspectJ compiler or weaver.
     It can be confusing!
* Spring AOP support only method execution join points. For example fieldf access
  interception is not implemented.
* Spring AOP vs AspectJ - AspectJ is "full" AOP framework which provides many AOP features
  and Spring AOP is limited AOP framework to solve common application probles in easy way.
  limitations:
  * field interception is not supported
  * aspects can not be advised by another aspect
* If you want to use AspectJ aspect declaration style you must:
  1. switch it on by @EnableAspectJAutoProxy
  2. to have aspectjweaver.jar
* If you want use AspectJ library for AOP you need:
  1. add spring-aspects.jar on the classpath
* features you can achieve by AspectJ
  * 
  
continue with: 5.10.2. Other Spring aspects for AspectJ


Tests
  * cglib is used automatically when there is no interface (chapter 5.3)
