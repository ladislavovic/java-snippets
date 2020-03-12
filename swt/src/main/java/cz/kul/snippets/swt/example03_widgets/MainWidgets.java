package cz.kul.snippets.swt.example03_widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class MainWidgets {

    private Display display;
    private Shell shell;
    private Label noticeBoard;

    public static void main(String[] args) {
        MainWidgets window = new MainWidgets();
        window.init();
    }

    public void init() {
        initWindow();
        addWidgets();
        run();
    }

    private void addWidgets() {
        addLabel();
        addText();
        addPushButton();
        addToggleButton();
        addCheckboxButton();
        addRadioButton();
        addGroup();
    }

    private void addLabel() {
        Label label = new Label(shell, SWT.BORDER);
        label.setText("Label");
        label.setToolTipText("Label tooltip");
    }

    private void addText() {
        Text helloWorldTest = new Text(shell, SWT.NONE);
        helloWorldTest.setText("Text");
    }

    private void addPushButton() {
        Button button = new Button(shell, SWT.PUSH);
        button.setText("Push button");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                noticeBoard.setText("Widget: pushButton, listener: widgetSelected");
            }
        });
    }

    private void addToggleButton() {
        Button button = new Button(shell, SWT.TOGGLE);
        button.setSelection(true);
        button.setText("Toggle button");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                noticeBoard.setText("Widget: toggleButton, listener: widgetSelected");
            }
        });
    }

    private void addCheckboxButton() {
        Button button = new Button(shell, SWT.CHECK);
        button.setSelection(false);
        button.setText("Checkbox");
    }

    private void addRadioButton() {
        // TODO
    }

    private void addGroup() {
        Group group = new Group(shell, SWT.SHADOW_ETCHED_IN);
        // I must set a layout here, without that nothing in group is showed
        group.setLayout(new GridLayout(1, false));
        group.setText("Group");
        for (int i = 0; i < 5; i++) {
            Label label = new Label(group, SWT.BORDER);
            label.setText("Label " + i);
        }
        group.pack();
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

        noticeBoard = new Label(shell, SWT.BORDER);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 3;
        noticeBoard.setLayoutData(gridData);
        noticeBoard.setText("Messages will be displayed here ...");
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
