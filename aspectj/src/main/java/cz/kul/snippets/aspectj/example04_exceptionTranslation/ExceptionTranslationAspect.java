package cz.kul.snippets.aspectj.example04_exceptionTranslation;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ExceptionTranslationAspect {

    @Pointcut("execution(* cz.kul.snippets.aspectj.example04_exceptionTranslation.pkg..*(..))")
    private void exceptionTranslationPointcut() {
    }

    @AfterThrowing(pointcut="exceptionTranslationPointcut()", throwing = "e")
    public void afterThrowing(Throwable e) throws Throwable  {
        Throwable newException;
        if (e instanceof MyException) {
            newException =  e;
        } else {
            newException = new MyException(e.getMessage(), e);
        }
        throw newException;
    }
}
