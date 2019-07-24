package cz.kul.snippets.java.example25_TODO;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomClassLoader extends ClassLoader {

    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.contains("25_TODO.A")) {
            byte[] classData;
            final String sJarEntryPath = "customClasses/" + name.replace('.', '/') + ".class";
            try {
                classData = Files.toByteArray(new File(sJarEntryPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Class<?> resultClass = defineClass(name, classData, 0, classData.length, null);
            return resultClass;
        } else {
            return super.findClass(name);
        }
    }
}
