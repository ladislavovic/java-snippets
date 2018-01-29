package cz.kul.snippets.spring._02_xmlconfig_dependencyinjection;

import static org.junit.Assert.assertEquals;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring can inject beans into another bean according to configuration.
 * 
 * You can inject through setters or as constructor parameters.
 * 
 * TODO how to choose between property and setter injection in xml config?
 */
public class Main_XMLConfig_DependencyInjection {

    public static void main(String[] args) {

        String configLocation = "cz/kul/snippets/spring/_02_xmlconfig_dependencyinjection/spring-xml-dependency-injection.xml";

        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation)) {

            OutputHelper oh1 = (OutputHelper) context.getBean("outputHelper1");
            assertEquals(CsvOutputGenerator.class, oh1.getOutputGenerator().getClass());

            OutputHelper oh2 = (OutputHelper) context.getBean("outputHelper2");
            assertEquals(CsvOutputGenerator.class, oh2.getOutputGenerator().getClass());

            OutputHelper oh3 = (OutputHelper) context.getBean("outputHelper3");
            assertEquals(JsonOutputGenerator.class, oh3.getOutputGenerator().getClass());
        }

    }

}
