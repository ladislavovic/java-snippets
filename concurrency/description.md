Concurency
=================

### ThreadLocal
* its purpose is to store values per thread
* thread can not access values from another thread
* internal details:
  * Thread class contains field ThreadLocal.ThreadLocalMap threadLocals.
    It contains N thread locals.
  * key of the threadLocals map is ThreadLocal itself 
  * ThreadLocal class is no magic. Usually it get current thread
    and manage threadLocals field from the Thread object.