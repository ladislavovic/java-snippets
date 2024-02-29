/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.example10_jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import cz.kul.snippets.java.example10_jaxb.model.Address;
import cz.kul.snippets.java.example10_jaxb.model.Customer;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kulhalad
 */
public class MainJaxb {

	@Test
	public void marshall() throws Exception {
		Customer customer = new Customer();
		customer.setId(10L);
		customer.setAddress(new Address());
		customer.getAddress().setCity("Ostrava");
		customer.setName("Tieto");

		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter sw = new StringWriter();
		marshaller.marshal(customer, sw);
		System.out.println(sw);
	}

	@Test
	public void unmarshall() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<customer id='10'>\n");
		sb.append("  <name>Ikea</name>\n");
		sb.append("  <address>\n");
		sb.append("    <city>Stockholm</city>\n");
		sb.append("    <zip>12345</zip>\n");
		sb.append("  </address>\n");
		sb.append("</customer>\n");

		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Customer customer = (Customer) jaxbUnmarshaller.unmarshal(new StringReader(sb.toString()));
		Assert.assertEquals(10, (long) customer.getId());
		Assert.assertEquals("Stockholm", customer.getAddress().getCity());
	}

	// This is implemented by Jacskon. Standard JAXB implementation can not
	// generate JSON output.
	@Test
	public void marshallToJson() throws Exception {
		Customer customer = new Customer();
		customer.setId(10L);
		customer.setAddress(new Address());
		customer.getAddress().setCity("Ostrava");
		customer.setName("Tieto");

		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(mapper.getTypeFactory());
		mapper.setAnnotationIntrospector(introspector);

		String result = mapper.writeValueAsString(customer);
		System.out.println(result);
	}

}
