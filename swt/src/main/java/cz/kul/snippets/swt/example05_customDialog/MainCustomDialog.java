package cz.kul.snippets.swt.example05_customDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainCustomDialog {

    private Display display;
    private Shell shell;

    public static void main(String[] args) {
        MainCustomDialog window = new MainCustomDialog();
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
                CustomDialog customDialog = new CustomDialog(shell);
                customDialog.setText("My custom dialog");
                customDialog.open();
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
