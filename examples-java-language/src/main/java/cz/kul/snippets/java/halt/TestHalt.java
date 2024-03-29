package cz.kul.snippets.java.halt;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestHalt {
    
    @Test
    public void haltDoesNotExecuteFinallyBlock() {
        // It print only MSG 1, because then VM stop
        // and neither finally block is not executed.
        //
        // For that reason is not a good idea to do IO operations in Daemon
        // threads, because when only deamon threads are in VM, it halts
        // and resources are not closed.
        
        try {
            System.out.println("MSG 1");
            System.exit(0);
            System.out.println("MSG 2");
        } finally {
            System.out.println("MSG 3");
        }
        
    }

    @Test
    public void shutdownHooksAreExecutedWhenSysteExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("SHUTDOWN HOOK 1")));
        System.exit(0);
    }

    @Test
    public void shutdownHooksAreNOTExecutedWhenRuntimeHalt() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("SHUTDOWN HOOK 1")));
        Runtime.getRuntime().halt(0);
    }
    
}
