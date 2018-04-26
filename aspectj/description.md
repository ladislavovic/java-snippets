AspectJ
===========

Terms
------
### crosscutting concerns
I understand it as problem, which is spread over an application. These
problems are difficult to solve by common OOP approach.

### Join Point
It is a well-defined point in the program flow. You can hook advices
on these points. In Java typically methods are join points. Other
programming languages can have different join points. Joint points have values
connected to them. For example joint point "before method execution"
has some method parameters.
Each method call at runtime is a different join point, even if it comes
from the same call expression in the program.

### Pointcut
An expression which picks out certain join points. And also values connected
to them.

### Advice
A pointcut plus a code which should be executed when the program reach the pointcut. 
There is several type of advices: 
* before
* after returning
* after throwing
* after (like finally)
* around

### AspectJ's aspect
It is a unit of modularity. It can contains several advices.
It is like class in Java.

### Call vs Execute
There are these two similar pointcuts, but they are different in several
aspects:
  * call means when some code is called. The caller must be weaved. Callee
    not.
  * execute means when some code is executed. The calee is weaved here.
Rule of thumb: The rule of thumb is that if you want to pick a join point
that runs when an actual piece of code runs (as is often the case for
tracing), use execution, but if you want to pick one that runs when
a particular signature is called (as is often the case for production
aspects), use call.

TODO
* what happen when aspect call method which has another aspect assign? Does it works?
