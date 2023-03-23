package cz.kul.snippets.swt.example01_hw;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainHw {

    public static void main(String[] args) {

        // It is absolutely the basic class for SWT. It manages event loop and many more stuffs
        // You have to have a Display instance if you want a SWT aplication
        Display display = new Display();

        // It represents a window
        Shell shell = new Shell(display);
        shell.open();

        Text helloWorldTest = new Text(shell, SWT.NONE);
        helloWorldTest.setText("Hello World SWT");
        helloWorldTest.pack();

        // An event loop is needed to transfer user input events from the underlying native operating system widgets to the SWT event system.
        // The programmer explicitly starts and checks the event loop to update the user interface.
        while (!shell.isDisposed()) { // run the event loop as long as the window is open

            // read the next OS event queue and transfer it to a SWT event
            if (!display.readAndDispatch())
            {
                // if there are currently no other OS event to process
                // sleep until the next OS event is available
                display.sleep();
            }
        }

        // disposes all associated windows and their components
        display.dispose();
    }
}
