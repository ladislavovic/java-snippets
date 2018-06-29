package cz.kul.snippets.swt.example05_customDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class CustomDialog extends Dialog {

    Object result;

    public CustomDialog (Shell parent, int style) {
        super (parent, style);
    }

    public CustomDialog (Shell parent) {
        this (parent, 0);
    }

    public Object open () {
        Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.SYSTEM_MODAL);
        shell.setText(getText());
        shell.setLayout(new GridLayout(1, false));

        // Your code goes here (widget creation, set result, etc).
        final ProgressBar progressBar = new ProgressBar(shell, SWT.INDETERMINATE);
        progressBar.setState(SWT.PAUSED);
        Button button = new Button(shell, SWT.PUSH);
        button.setText("Start operation");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                progressBar.setState(SWT.NORMAL);
            }
        });

        shell.pack();
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }
}
