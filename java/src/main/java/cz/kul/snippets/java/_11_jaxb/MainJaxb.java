/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._11_jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.junit.Assert;

/**
 *
 * @author kulhalad
 */
public class MainJaxb {

    public static void main(String[] args) throws Exception {
        marshall();
    }
    
    private static String convUserInput(String userInput) {
        String result = userInput.replaceAll("[^\\w\\*]", "");
        return result;
    }
    
    private static void marshall() throws Exception {
        Customer customer = new Customer();
        customer.setAddress(new Address());
        customer.getAddress().setCity("Ostrava");
        customer.setName("Tieto");
        
        JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(customer, sw);
        System.out.println(sw.toString());
    }

    private static void unmarshall() {
        try {
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
         
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Customer {
        
        @XmlAttribute
        private Long id;
        private String name;
        private Address address;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    private static class Address {

        private String city;
        private int zip;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getZip() {
            return zip;
        }

        public void setZip(int zip) {
            this.zip = zip;
        }

    }

}
