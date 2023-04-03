package cz.kul.snippets.swt.example02_layoutManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FormLayoutManager {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(700, 500);
        shell.setText("FormLayout Demo");
        addWidgetsToShell2(display, shell);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }

    private static void addWidgetsToShell2(Display display, Shell shell) {
        // Set FormLayout for the Shell
        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = 0; // margin on the left and right side. So it "reduces" inner size of the composite.
        formLayout.marginHeight = 0; // margin on the top and bottom side. So it "redudes" inner size of the composite.
        shell.setLayout(formLayout); // This set layout "in" the composite. So it affects child components.



        // Add Button with covers 30% of parent in upper left corner
        Button button1;
        {
            button1 = new Button(shell, SWT.NONE);
            button1.setText("Btn1");
            FormData formData = new FormData();

            // This denotes the position of the item top side.
            // The first param (numerator) means the percent of parent height. For example when it is 0, the top
            // side is at the same position as the parent, top side. If it would be 30, the item top side
            // was at the 30% of parent height.
            // The second param (offset) can adjust position calculated according to the first possition. You
            // can use also negative numbers.
            // The coordinates are always calculated from the parent upper left corner.
            formData.top = new FormAttachment(0, 0);
            formData.left = new FormAttachment(0, 0);
            formData.bottom = new FormAttachment(30, 0);
            formData.right = new FormAttachment(30, 0);
            button1.setLayoutData(formData); // This set additional information about the layout. It affects layout of this widget. Does not interchage with "setLayout()" method.
        }

        // Add Button with covers 30% of parent in bottom right corner
        {
            Button button2 = new Button(shell, SWT.NONE);
            button2.setText("Btn2");
            FormData formData = new FormData();

            formData.top = new FormAttachment(70, 0);
            formData.left = new FormAttachment(70, 0);
            formData.bottom = new FormAttachment(100, 0);
            formData.right = new FormAttachment(100, 0);
            button2.setLayoutData(formData); // This set additional information about the layout. It affects layout of this widget. Does not interchage with "setLayout()" method.
        }

        // Add Button on the right side of the first button
        {
            Button button3 = new Button(shell, SWT.NONE);
            button3.setText("Btn3");

            // You can set the position relatively to another item
            // param1 - the related item
            // param2 - offset. Can be negative
            // param3 - side of the related item. (can be default, but it is quite unclear to me)
            FormData formData = new FormData();
            formData.top = new FormAttachment(button1,10, SWT.TOP);          // 10 pixels below the top side of button1
            formData.left = new FormAttachment(button1,0, SWT.RIGHT);        // 0 pixels on the right of the button1
            formData.bottom = new FormAttachment(button1, -10, SWT.BOTTOM);   // 10 pixels above the bottom side of button1
            formData.right = new FormAttachment(50, 0);             // not related, just to the half of the parent
            button3.setLayoutData(formData); // This set additional information about the layout. It affects layout of this widget. Does not interchage with "setLayout()" method.
        }

        shell.open();
    }

}
