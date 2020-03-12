package cz.kul.snippets.smooks.example04_csvToJava;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.Test;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.payload.JavaResult;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExampleCsvToJava {

    @Test
    public void test1() throws Exception {
        
        String data = String.join(
                "\n",
                "rachel,green,29",
                "monica,geller,28",
                ""
        );

        String config = String.join(
                "\n",
                "<?xml version='1.0'?>",
                "<smooks-resource-list ",
                " xmlns='http://www.milyn.org/xsd/smooks-1.1.xsd'",
                " xmlns:csv='http://www.milyn.org/xsd/smooks/csv-1.5.xsd'>",
                "",
                "  <csv:reader fields='firstname,lastname,age'>",
                "    <csv:listBinding beanId='people' class='cz.kul.snippets.smooks.example04_csvToJava.Person' />",
                "  </csv:reader>",
                "",
                "</smooks-resource-list>"
        );
        
        // Instantiate Smooks with the config...
        Smooks smooks = new Smooks(new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));

        try {
            ExecutionContext executionContext = smooks.createExecutionContext();
            JavaResult result = new JavaResult();
            smooks.filterSource(
                    executionContext,
                    new StreamSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))),
                    result);
            List<Person> people = (List<Person>) result.getBean("people");
            Assert.assertEquals(2, people.size());
            Assert.assertEquals("rachel", people.get(0).getFirstname());
        } finally {
            smooks.close();
        }

    }


}
