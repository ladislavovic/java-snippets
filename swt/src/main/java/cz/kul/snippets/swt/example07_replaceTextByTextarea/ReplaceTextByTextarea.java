package cz.kul.snippets.swt.example07_replaceTextByTextarea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import javax.swing.*;

public class ReplaceTextByTextarea {

    private static int counter = 0;

    private static Text item;
    private static boolean textarea;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(700, 500);
        shell.setText("Replace text by textarea");
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

        {
            FormLayout formLayout = new FormLayout();
            formLayout.marginWidth = 5;
            formLayout.marginHeight = 5;
            shell.setLayout(formLayout);
        }

        final Composite composite;
        {
            composite = new Composite(shell, SWT.BORDER);
            composite.setLayout(new FormLayout());
            FormData compositeLayoutData = new FormData();
            compositeLayoutData.top = new FormAttachment(0, 70);
            compositeLayoutData.left = new FormAttachment(0, 0);
            compositeLayoutData.right = new FormAttachment(100, 0);
            composite.setLayoutData(compositeLayoutData);
        }

        item = createText(composite, null, null);
        textarea = false;

        {
            Button btn = new Button(shell, SWT.NONE);
            btn.setText("Switch");
            FormData btnLayoutData = new FormData();
            btnLayoutData.top = new FormAttachment(0, 0);
            btnLayoutData.left = new FormAttachment(0, 0);
            btn.setLayoutData(btnLayoutData);
            btn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {

                    String text = item.getText();
                    Object data = item.getData();
                    item.dispose();

                    if (textarea) {
                        item = createText(composite, text, data);
                    } else {
                        item = createTextArea(composite, text, data);
                    }
                    textarea = !textarea;

                    shell.layout();
                    // It is not enough re-layout the composite.
                    // layout() on composite do not change size of the composite itself. It probably re-layout
                    // only inner part of the composit, but the composite itself is not changed.
                    // When you call layout() on the composite parent (shelf in this case) it cause also
                    // change of composite size.
                }

            });
        }

        shell.open();
    }

    private static Text createTextArea(Composite parent, String str, Object data) {
        Text textarea = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textarea.setText(str);
        textarea.setData(data);
        FormData taFormData = new FormData(100, 100);
        taFormData.left = new FormAttachment(0, 0);
        taFormData.right = new FormAttachment(100, 0);
        textarea.setLayoutData(taFormData);
        return textarea;
    }

    private static Text createText(Composite parent, String str, Object data) {
        Text text = new Text(parent, SWT.BORDER);
        if (str != null) {
            text.setText(str);
        }
        if (data != null) {
            text.setData(data);
        }
        FormData taFormData = new FormData();
        taFormData.left = new FormAttachment(0, 0);
        taFormData.right = new FormAttachment(50, 0);
        text.setLayoutData(taFormData);
        return text;
    }

}
