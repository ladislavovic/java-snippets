/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package cz.kul.snippets.smooks.example01_bases;

import cz.kul.snippets.smooks.example01_bases.model.Order;
import org.junit.Assert;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.event.report.HtmlReportGenerator;
import org.milyn.payload.JavaResult;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * It is an HW example based on example from smooks repository.
 * 
 * It just read xml and create Java model according to it. 
 */
public class Main {

    private static InputStream getInputData() {
        String data = String.join(
                "\n",
                "<order>",
                "    <header>",
                "        <date>Wed Nov 15 13:45:28 EST 2006</date>",
                "        <customer number=\"123123\">Joe</customer>",
                "    </header>",
                "    <order-items>",
                "        <order-item>",
                "            <product>111</product>",
                "            <quantity>2</quantity>",
                "            <price>8.90</price>",
                "        </order-item>",
                "        <order-item>",
                "            <product>222</product>",
                "            <quantity>7</quantity>",
                "            <price>5.20</price>",
                "        </order-item>",
                "    </order-items>",
                "</order>"
        );
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws IOException, SAXException, SmooksException {
        Order order = Main.runSmooks();
        Assert.assertEquals("Joe", order.getHeader().getCustomerName());
        Assert.assertEquals(123123L, order.getHeader().getCustomerNumber().longValue());
        Assert.assertEquals(2, order.getOrderItems().size());
        Assert.assertEquals(111L, order.getOrderItems().get(0).getProductId());
        Assert.assertEquals(222L, order.getOrderItems().get(1).getProductId());
    }    

    protected static Order runSmooks() throws IOException, SAXException, SmooksException {

        // Instantiate Smooks with the config...
        Smooks smooks = new Smooks("/smooks_example01/smooks-config.xml");

        try {
             // Create an exec context - no profiles....
            ExecutionContext executionContext = smooks.createExecutionContext();
            
            // The result of this transform is a set of Java objects...
            JavaResult result = new JavaResult();

            // Configure the execution context to generate a report...
            executionContext.setEventListener(new HtmlReportGenerator("/var/smooks/report.html"));

            // Filter the input message to extract, using the execution context...
            smooks.filterSource(executionContext, new StreamSource(getInputData()), result);

            return (Order) result.getBean("order");
        } finally {
            smooks.close();
        }
    }

}
