package cz.kul.snippets.designpatterns.example02_singleton;

import static org.junit.Assert.assertTrue;

/**
 * <h1>Description</h1>
 * <p>
 * It ensure only one instance of the class is created.
 * </p>
 * 
 * <h1>When to use</h1>
 * <ul>
 * <li>pool is often a singleton</li>
 * <li>application configuration</li>
 * <li>spring beans are singletons by default</li>
 * <li>
 * </ul>
 * 
 * <h1>When not to use</h1>
 * <ul>
 * <li>It is not good to use it to store a global state</li>
 * </ul>
 * 
 * <h1>Pitfalls</h1>
 * <ul>
 * <li>be careful when you use more classloaders - they can load a singleton multiple
 * times</li>
 * </ul>
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_singleton {

    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        assertTrue(instance == instance2);
    }

}

final class Singleton {

    private static volatile Singleton instance = null; // NOTE: Must be volatile!

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
