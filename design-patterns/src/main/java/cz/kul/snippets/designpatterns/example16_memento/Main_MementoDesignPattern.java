package cz.kul.snippets.designpatterns.example16_memento;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.kul.snippets.designpatterns.example16_memento.Commands.Memento;

/**
 * The memento pattern is a software design pattern that provides the ability to restore
 * an object to its previous state (undo via rollback). The memento pattern is implemented
 * with three objects: the originator, a caretaker and a memento. The originator is some
 * object that has an internal state. The caretaker is going to do something to the
 * originator, but wants to be able to undo the change. The caretaker first asks the
 * originator for a memento object. Then it does whatever operation (or sequence of
 * operations) it was going to do. To roll back to the state before the operations, it
 * returns the memento object to the originator.
 * 
 * @author kulhalad
 * @since 7.4.5
 */
public class Main_MementoDesignPattern {

    public static void main(String[] args) {
        new Handler().handle();
    }

}

/** Originator */
class Commands {
    private List<String> commands = new ArrayList<>();

    public void addCommand(String cmd) {
        commands.add(cmd);
    }

    public List<String> getCommands() {
        return commands;
    }

    public Memento saveState() {
        Memento memento = new Memento();
        memento.commands.addAll(commands);
        return memento;
    }

    public void restoreState(Memento memento) {
        this.commands = memento.commands;
    }

    public static class Memento {
        private List<String> commands = new ArrayList<>();
    }

}

/** Care taker */
class Handler {

    public void handle() {
        Commands commands = new Commands();
        commands.addCommand("add a");
        commands.addCommand("add b");
        Memento state1 = commands.saveState();
        commands.addCommand("add c");
        commands.restoreState(state1);
        assertEquals(Arrays.asList("add a", "add b"), commands.getCommands());
    }

}
