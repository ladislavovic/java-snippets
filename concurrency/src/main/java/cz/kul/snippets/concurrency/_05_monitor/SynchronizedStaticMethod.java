package cz.kul.snippets.concurrency._05_monitor;

/** 
 * You can synchronize static methods as well.
 * 
 * public static synchronized void foo() is the shortcut for
 * synchronized (ClassName.class) {
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class SynchronizedStaticMethod {
    
    public static void main(String[] args) {
        foo();
    }

    public synchronized static void foo() {
        System.out.println("It is possible. The lock object is " + SynchronizedStaticMethod.class.getSimpleName() + ".class");        
    }

}
