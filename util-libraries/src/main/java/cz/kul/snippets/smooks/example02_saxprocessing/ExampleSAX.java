package cz.kul.snippets.smooks.example02_saxprocessing;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.Test;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.event.report.HtmlReportGenerator;
import org.milyn.payload.JavaResult;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class ExampleSAX {
    
    public static final String AGENT = "smooks_sax";
    
    @Test
    public void test1() throws Exception {
        AgentManager.addAgent(AGENT, x -> x);

        String data = String.join(
                "\n",
                "<order>",
                "  <id>10</id>",
                "  <invoiceText>toys</invoiceText>",
                "</order>"
        );
        
        String config = String.join(
                "\n",
                "<?xml version='1.0'?>",
                "<smooks-resource-list ",
                " xmlns='http://www.milyn.org/xsd/smooks-1.1.xsd'",
                " xmlns:jb='http://www.milyn.org/xsd/smooks/javabean-1.2.xsd'>",
                "",
                "  <resource-config selector='id'>",
                "    <resource>cz.kul.snippets.smooks.example02_saxprocessing.MySAXVisitor</resource>",
                "  </resource-config>",
                "",
                "</smooks-resource-list>"
        );

        // Instantiate Smooks with the config...
        Smooks smooks = new Smooks(new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));

        try {
            ExecutionContext executionContext = smooks.createExecutionContext();
            JavaResult result = new JavaResult();
            executionContext.setEventListener(new HtmlReportGenerator("/var/smooks/report.html"));
            smooks.filterSource(new StreamSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))));
        } finally {
            smooks.close();
        }

        Assert.assertEquals(2, AgentManager.getAgentLog(AGENT).getCallCount());
    }
    
}
