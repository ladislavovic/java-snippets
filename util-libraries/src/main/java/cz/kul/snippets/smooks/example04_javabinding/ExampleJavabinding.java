package cz.kul.snippets.smooks.example04_javabinding;

import cz.kul.snippets.agent.AgentManager;
import cz.kul.snippets.smooks.example04_javabinding.model.Order;
import org.junit.Assert;
import org.junit.Test;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.event.report.HtmlReportGenerator;
import org.milyn.payload.JavaResult;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExampleJavabinding {

    @Test
    public void test1() throws Exception {
        String data = String.join(
                "\n",
                "<orders>",
                "",
                "  <order>",
                "    <id>10</id>",
                "    <invoiceText>toys</invoiceText>",
                "    <order-items>",
                "      <order-item>",
                "        <product>111</product>",
                "        <quantity>2</quantity>",
                "      </order-item>",
                "      <order-item>",
                "        <product>112</product>",
                "        <quantity>3</quantity>",
                "      </order-item>",
                "    </order-items>",
                "  </order>",
                "",
//                "  <order>",
//                "    <id>11</id>",
//                "    <invoiceText>food</invoiceText>",
//                "    <order-items>",
//                "      <order-item>",
//                "        <product>112</product>",
//                "        <quantity>5</quantity>",
//                "      </order-item>",
//                "    </order-items>",
//                "  </order>",
                "",
                "</orders>"
        );

        String config = String.join(
                "\n",
                "<?xml version='1.0'?>",
                "<smooks-resource-list ",
                " xmlns='http://www.milyn.org/xsd/smooks-1.1.xsd'",
                " xmlns:jb='http://www.milyn.org/xsd/smooks/javabean-1.2.xsd'>",
                "",
                "  <jb:bean beanId='order' class='cz.kul.snippets.smooks.example04_javabinding.model.Order' createOnElement='order'>",
                "    <jb:value property='id' data='id' />",
                "    <jb:value property='invoiceText' data='invoiceText' />",
                "    <jb:wiring property='items' beanIdRef='orderItems' />",
                "  </jb:bean>",
                "",
                "  <jb:bean beanId='orderItems' class='java.util.ArrayList' createOnElement='order'>",
                "    <jb:wiring beanIdRef='orderItem' />",
                "  </jb:bean>",
                "",
                "  <jb:bean beanId='orderItem' class='cz.kul.snippets.smooks.example04_javabinding.model.OrderItem' createOnElement='order-item'>",
                "    <jb:value property='product' data='product' />",
                "    <jb:value property='quantity' data='quantity' />",
                "  </jb:bean>",
                "",
                "</smooks-resource-list>"
        );

        // Instantiate Smooks with the config...
        Smooks smooks = new Smooks(new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));

        try {
            ExecutionContext executionContext = smooks.createExecutionContext();
            executionContext.setEventListener(new HtmlReportGenerator("/var/smooks/report.html"));
            JavaResult result = new JavaResult();
            smooks.filterSource(
                    executionContext,
                    new StreamSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))),
                    result);
            Order order = (Order) result.getBean("order");
            Assert.assertEquals(10, order.getId());
        } finally {
            smooks.close();
        }
    }


}
