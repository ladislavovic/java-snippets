package cz.kul.snippets.swt.example06_addingAndRemovingWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AddingAndRemovingWidgets {

    private static int counter = 0;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(700, 500);
        shell.setText("Widgets adding and removing");
        addWidgetsToShell(display, shell);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }

    private static void addWidgetsToShell(Display display, Shell shell) {
        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = 5;
        formLayout.marginHeight = 5;
        shell.setLayout(formLayout);

        Composite composite = new Composite(shell, SWT.BORDER);
        {
            composite.setLayout(new FillLayout(SWT.VERTICAL));
            FormData formData = new FormData();
            formData.top = new FormAttachment(0, 0);
            formData.left = new FormAttachment(50, 0);
            formData.bottom = new FormAttachment(100, 0);
            formData.right = new FormAttachment(100, 0);
            composite.setLayoutData(formData);
        }

        {
            Button addLabel = new Button(shell, SWT.NONE);
            addLabel.setText("Add Label");
            FormData formData = new FormData();
            formData.top = new FormAttachment(0, 0);
            formData.left = new FormAttachment(0, 0);
            addLabel.setLayoutData(formData);
            addLabel.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    Label label = new Label(composite, SWT.BORDER);
                    label.setText("Label " + counter++);

                    // Must call it to Re "lay out" the parent
                    // Otherwise the component is not visible at all.
                    composite.layout();
                }

            });
        }

        {
            Button removeLabel = new Button(shell, SWT.NONE);
            removeLabel.setText("Remove Label");
            FormData formData = new FormData();
            formData.top = new FormAttachment(0, 50);
            formData.left = new FormAttachment(0, 0);
            removeLabel.setLayoutData(formData);
            removeLabel.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    Control[] children = composite.getChildren();
                    if (children != null && children.length > 0) {
                        children[0].dispose();
                    }
                    composite.layout();
                }
            });
        }

        shell.open();
    }

}
