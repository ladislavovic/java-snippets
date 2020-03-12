package cz.kul.snippets.annotationProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

@SupportedAnnotationTypes("cz.kul.snippets.annotationProcessor.MyDeprecated")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor1 extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("*********************************** START *********************");

        System.out.println("PROP projectVersion: " + processingEnv.getOptions().get("projectVersion"));
        
        
        
        for (final Element element : roundEnv.getElementsAnnotatedWith(MyDeprecated.class)) {
            MyDeprecated annotation = element.getAnnotation(MyDeprecated.class);
            System.out.println("Date: " + annotation.date());
        }
        
        System.out.println("*********************************** END *********************");
        
        return true;
        
    }

}
