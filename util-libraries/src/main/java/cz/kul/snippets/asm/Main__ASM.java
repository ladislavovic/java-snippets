package cz.kul.snippets.asm;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * ASM
 * 
 * <ul>
 * <li>ASM is lowlevel java bytecode manipulating library</li>
 * <li>E.g. Javasist use it internally</li>
 * </ul>
 * *
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main__ASM {

    public static void main(String[] args) throws IOException {
        parseClass();
    }

    /**
     * Parse class. During event "visitMethod" my code is called.
     */
    private static void parseClass() throws IOException {
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                System.out.println("Method: " + name + " " + desc);
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("java/lang/String.class");
        // TODO it is another possibilities - the slash must be on the start. Weird. Why???
        //InputStream in = Main__ASM.class.getResourceAsStream("/java/lang/String.class");

        ClassReader classReader = new ClassReader(in);
        classReader.accept(classVisitor, 0);
    }

}
