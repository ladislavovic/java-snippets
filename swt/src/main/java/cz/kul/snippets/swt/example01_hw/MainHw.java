package cz.kul.snippets.swt.example01_hw;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainHw {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.open();

        Text helloWorldTest = new Text(shell, SWT.NONE);
        helloWorldTest.setText("Hello World SWT");
        helloWorldTest.pack();

        // run the event loop as long as the window is open
        while (!shell.isDisposed()) {
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
