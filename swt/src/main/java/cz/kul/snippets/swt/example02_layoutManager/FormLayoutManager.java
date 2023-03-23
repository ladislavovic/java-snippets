package cz.kul.snippets.swt.example02_layoutManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class FormLayoutManager {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);

        // TODO continue here

        GridLayout gridLayout = new GridLayout(3, true);
        gridLayout.marginWidth = 10;
        gridLayout.marginHeight = 10;
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 10;
        shell.setLayout(gridLayout);

        for (int i = 0; i < 10; i++) {
            addLabel(shell);
        }

        shell.open();

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

    private static void addLabel(Shell shell) {
        Label label = new Label(shell, SWT.NONE);
        label.setText("Label");
        label.pack();
    }

}
