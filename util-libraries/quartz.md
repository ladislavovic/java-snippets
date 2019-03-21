Quartz
===============

### Job
The job is a class implementing Job interface. It is the "core". It is
what the job really do.

### JobDetail
It is a class which connect together Job (implementation) and
other properties (name, group, ...)

### Trigger
Can execute jobs. It knows when to execute them. There
is 1:N association between JobDetail and Trigger

### Scheduler
Main component of the library. It binds all together. You
can register JobDetails and triggers there. SCheduler is responsible for
job execution (trigger only send message to scheduler "start it", but scheduler
is responsible). There is more implementations of Scheduler.

### Lifecycle:
* Scheduler must be started at the beginning
* always when the Job is executed, new instance of the Job is created
* at the end we have te shut it down (Scheduler is implemented by Threads,
  at the end all threads must be properly ended)
  
### JobDataMap
Job classes can not hold data, because the new instance is created with 
every execution. JobDataMap is used for that.

JobDataMap is added to JobDetail. Job then can acces
that map duging execution. Trigger can have the map too.
If the job has the setter method with the same name,
as the property in the map, the setter is callet
with the value before execution.

If you want the job change data in the data map you must mark it with
annotation @PersistJobDataAfterExecution. Otherwise no changes int the
data map will be persisted.
