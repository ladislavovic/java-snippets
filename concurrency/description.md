Concurency
=================

### ThreadLocal
* its purpose is to store values per thread
* you can not access value for another thread stored in thread local, only value for current thread is accessible
* internal details:
  * Thread class contains field ThreadLocal.ThreadLocalMap threadLocals.
    It contains N thread locals. So data are stored directly in Thread but are fully managed by ThreadLocal class
  * key of the threadLocals map is ThreadLocal itself 
  * ThreadLocal class is no magic. Usually it get current thread
    and manage threadLocals field from the Thread object.