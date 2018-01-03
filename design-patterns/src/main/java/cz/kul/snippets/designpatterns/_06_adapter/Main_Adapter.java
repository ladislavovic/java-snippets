package cz.kul.snippets.designpatterns._06_adapter;

import static org.junit.Assert.assertEquals;

/**
 * <h1>Description</h2> Translate one interface to another.
 * 
 * <h2>Terms</h2>
 * <ul>
 * <li>Adapter - interface, which is used by client</li>
 * <li>Adaptee - translated interface. This interface can not be used directly by the
 * client</li>
 * <ul>
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_Adapter {

    public static void main(String[] args) {
        WeirdString weirdString = new WeirdString();
        weirdString.addFirstAndLastCharacter('a', 'b');
        weirdString.addFirstAndLastCharacter(null, 'c');
        assertEquals("abc", weirdString.toString());
    }

}

class WeirdString {

    private String adaptee;

    public void addFirstAndLastCharacter(Character first, Character last) {
        if (adaptee == null) {
            adaptee = "";
        }
        if (first != null) {
            adaptee = first.toString() + adaptee;
        }
        if (last != null) {
            adaptee = adaptee + last.toString();
        }
    }

    @Override
    public String toString() {
        return adaptee;
    }
}
