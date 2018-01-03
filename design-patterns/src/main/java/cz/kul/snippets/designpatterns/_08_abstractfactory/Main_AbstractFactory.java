package cz.kul.snippets.designpatterns._08_abstractfactory;

/**
 * TODO GoF definition
 * 
 * <p>
 * Abstract factory is group of factory methods - factory method is just one method which
 * creates one product, abstract factory is a class, which contains N factory methods and
 * they returns N products. Products are usually related.
 * </p>
 * 
 * <p>
 * So during implementation you can creates several factories or you can create one
 * abstract factory
 * </p>
 * 
 * @author kulhalad
 * @since 7.4.5
 */
public class Main_AbstractFactory {

    public static void main(String[] args) {

        // TODO Auto-generated method stub

    }

}

/**
 * This is an abstract factory. It consists of three factory methods, To make the code
 * short I do not have product classes here, methods just return Object class.
 */
interface LookAndFeel {

    Object getButton();

    Object getScrollbar();

    Object getInputText();

}
