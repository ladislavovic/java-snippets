package cz.kul.snippets.swt.example02_layoutManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ManualPositioning {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);

        Label label = new Label(shell, SWT.BORDER);

        int x = 60;
        int y = 20;
        int width = 400;
        int height = 200;

        label.setBounds(x, y, width, height);

        shell.setBounds(200, 50, 500, 500);

        label.setText("Label bounds: " + label.getBounds().toString() + "\nShell bounds: " + shell.getBounds());

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}
