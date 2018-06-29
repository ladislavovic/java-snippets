package cz.kul.snippets.designpatterns.example04_state;

import static org.junit.Assert.assertEquals;

import java.util.Random;

/**
 * <h1>Description</h1>
 * <p>
 * Allows to change behaviour of the object by changing of its state. State is a class
 * which perform some work for context - context delegate work to state
 * <p>
 * <p>
 * The class which holds the state is called context
 * </p>
 * <p>
 * It is very similar to "Strategy design pattern. The difference is the state is internal
 * detail of the class. Client does not know if the state is changed. But strategy is
 * changed when client send a request, it is not internal issue.
 * </p>
 * 
 * <h1>Examples</h1>
 * <ul>
 * <li>renderers - you can render a object by several ways</li>
 * <li>validators</li>
 * <li>JAAS handlers - handlers wchich obtains credentials - they can obtain it by many
 * ways (console, gui, network, ..)</li>
 * </ul>
 * 
 */
public class Main_State {

    public static void main(String[] args) {
        Context c = new Context();

        c.setState(new StateOddNumber());
        assertEquals(1, Math.abs(c.getNumber() % 2));

        c.setState(new StateEvenNumber());
        assertEquals(0, Math.abs(c.getNumber() % 2));
    }

}

/**
 * Class which include the state is marked as a context.
 */
class Context {
    private State state;

    public void setState(State state) {
        this.state = state;
    }

    public int getNumber() {
        return state.getNumber();
    }
}

/**
 * This is an interface for state. Particular implementations implement this interface.
 * 
 * In Java8 you can use Function interface (for simple one method states).
 */
interface State {
    int getNumber();
}

class StateOddNumber implements State {

    @Override
    public int getNumber() {
        int random = (new Random()).nextInt();
        return (random % 2 == 0) ? (random + 1) : random;
    }
}

class StateEvenNumber implements State {

    @Override
    public int getNumber() {
        int random = (new Random()).nextInt();
        return (random % 2 == 0) ? random : (random + 1);
    }
}
