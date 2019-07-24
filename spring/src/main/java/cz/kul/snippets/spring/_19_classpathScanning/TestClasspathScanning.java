package cz.kul.snippets.spring._19_classpathScanning;

import com.google.common.collect.Sets;
import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;
import java.util.stream.Collectors;

public class TestClasspathScanning extends SnippetsTest {

    @Test
    public void findAllClassesWhichImplementsInterface() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(Interface1.class));
        Set<String> classNames = scanner.findCandidateComponents("cz.kul.snippets.spring").stream()
                .map(x -> x.getBeanClassName())
                .collect(Collectors.toSet());
        Assert.assertEquals(Sets.newHashSet(Class1.class.getName(), Class3.class.getName()), classNames);
    }

    @Test
    public void findAllClassesWithAnnotation() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Annotation1.class));
        Set<String> classNames = scanner.findCandidateComponents("cz.kul.snippets.spring").stream()
                .map(x -> x.getBeanClassName())
                .collect(Collectors.toSet());
        Assert.assertEquals(Sets.newHashSet(Class1.class.getName(), Class2.class.getName()), classNames);
    }

}
