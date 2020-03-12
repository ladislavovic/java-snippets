package cz.kul.snippets.swt.example04_messageBoxDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class MainMessageBoxDialog {

    private Display display;
    private Shell shell;

    public static void main(String[] args) {
        MainMessageBoxDialog window = new MainMessageBoxDialog();
        window.init();
    }

    public void init() {
        initWindow();
        addWidgets();
        run();
    }

    private void addWidgets() {
        Button button = new Button(shell, SWT.PUSH);
        button.setText("Push button");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // create a dialog with ok and cancel buttons and a question icon
                MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
                dialog.setText("My info");
                dialog.setMessage("Do you really want to do this?");

                int returnCode = dialog.open();
                System.out.println("Dialog return code: " + returnCode);
            }
        });
    }

    private void initWindow() {
        display = new Display();
        shell = new Shell(display);

        GridLayout gridLayout = new GridLayout(3, true);
        gridLayout.marginWidth = 10;
        gridLayout.marginHeight = 10;
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 10;
        shell.setLayout(gridLayout);
    }

    private void run() {
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

}
