package cz.kul.snippets.designpatterns.example11_decorator;

/**
 * Decorator can add functionality to existing class in runtime.
 * 
 * Decorator design pattern is similar to Proxy. The implementation can be the same.
 * Differences:
 * <ul>
 * <li>Decorator adds responsibility, proxy is transparent, hidden in the background</li>
 * <li>Decorator is oriented on runtime, not on compile type. By combinig more decorators
 * you can obtain many combinations</li>
 * </ul>
 * 
 * Examples
 * <ul>
 * <li>java.io.InputStream</li>
 * <li>java.util.Collections.checkedXXX()</li>
 * <li>java.util.Collections.synchronizedXXX()</li>
 * </ul>
 * 
 */
public class Main_DecoratorDesignPattern {

    public static void main(String args[]) {

    }

}
