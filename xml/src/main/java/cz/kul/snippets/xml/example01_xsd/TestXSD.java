package cz.kul.snippets.xml.example01_xsd;

import cz.kul.snippets.SnippetsTest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestXSD extends SnippetsTest {

    @Test
    public void simpleElement() {
        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>              ");
        valid.append("<data xmlns=\"https://www.kul.com\"> ");
        valid.append("  <name>Jane</name>                  ");
        valid.append("  <age>10</age>                      ");
        valid.append("  <birthday>2001-01-20</birthday>    ");
        valid.append("</data>                              ");

        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                                ");
        xsd.append("<xs:schema                                             ");
        xsd.append("  xmlns=\"https://www.kul.com\"                        ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"        ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"              ");
        xsd.append("  elementFormDefault=\"qualified\"                     ");
        xsd.append(">                                                      ");
        xsd.append("  <xs:element name=\"data\">                           ");
        xsd.append("    <xs:complexType>                                   ");
        xsd.append("      <xs:sequence>                                    ");
        xsd.append("        <xs:element name=\"name\" type=\"xs:string\"/> ");
        xsd.append("        <xs:element name=\"age\" type=\"xs:integer\"/> ");
        xsd.append("        <xs:element name=\"birthday\" type=\"xs:date\"/> ");
        xsd.append("      </xs:sequence>                                   ");
        xsd.append("    </xs:complexType>                                  ");
        xsd.append("  </xs:element>                                        ");
        xsd.append("</xs:schema>                                           ");

        assertValidXml(xsd.toString(), valid.toString());
    }

    @Test
    public void simpleElement_decimal() {
        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>              ");
        valid.append("<data xmlns=\"https://www.kul.com\"> ");
        valid.append("  <price>10.05</price>               ");
        valid.append("</data>                              ");

        StringBuilder invalid = new StringBuilder();
        invalid.append("<?xml version=\"1.0\"?>              ");
        invalid.append("<data xmlns=\"https://www.kul.com\"> ");
        invalid.append("  <price>10,5</price>                ");
        invalid.append("</data>                              ");

        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                                       ");
        xsd.append("<xs:schema                                                    ");
        xsd.append("  xmlns=\"https://www.kul.com\"                               ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"               ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"                     ");
        xsd.append("  elementFormDefault=\"qualified\"                            ");
        xsd.append(">                                                             ");
        xsd.append("  <xs:element name=\"data\">                                  ");
        xsd.append("    <xs:complexType>                                          ");
        xsd.append("      <xs:sequence>                                           ");
        xsd.append("        <xs:element name=\"price\" type=\"xs:decimal\"/>      ");
        xsd.append("      </xs:sequence>                                          ");
        xsd.append("    </xs:complexType>                                         ");
        xsd.append("  </xs:element>                                               ");
        xsd.append("</xs:schema>                                                  ");

        assertValidAndNotValidXml(xsd.toString(), valid.toString(), invalid.toString());
    }

    @Test
    public void attribute() {
        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>                                     ");
        valid.append("<data xmlns=\"https://www.kul.com\" source=\"wikipedia\" source2=\"aaa\"/> ");

        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                                       ");
        xsd.append("<xs:schema                                                    ");
        xsd.append("  xmlns=\"https://www.kul.com\"                               ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"               ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"                     ");
        xsd.append("  elementFormDefault=\"qualified\"                            ");
        xsd.append(">                                                             ");
        xsd.append("  <xs:element name=\"data\">                                  ");
        xsd.append("    <xs:complexType>                                          ");
        xsd.append("      <xs:attribute name=\"source\" type=\"xs:string\"/>      ");
        xsd.append("      <xs:attribute name=\"source2\" type=\"xs:string\"/>      ");
        xsd.append("    </xs:complexType>                                         ");
        xsd.append("  </xs:element>                                               ");
        xsd.append("</xs:schema>                                                  ");

        assertValidXml(xsd.toString(), valid.toString());
    }

    @Test
    public void restrictions_minMax() {
        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>              ");
        valid.append("<data xmlns=\"https://www.kul.com\"> ");
        valid.append("  <age>25</age>                      ");
        valid.append("</data>                              ");

        StringBuilder invalid = new StringBuilder();
        invalid.append("<?xml version=\"1.0\"?>              ");
        invalid.append("<data xmlns=\"https://www.kul.com\"> ");
        invalid.append("  <age>150</age>                     ");
        invalid.append("</data>                              ");

        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                              ");
        xsd.append("<xs:schema                                           ");
        xsd.append("  xmlns=\"https://www.kul.com\"                      ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"      ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"            ");
        xsd.append("  elementFormDefault=\"qualified\"                   ");
        xsd.append(">                                                    ");
        xsd.append("  <xs:element name=\"data\">                         ");
        xsd.append("    <xs:complexType>                                 ");
        xsd.append("      <xs:sequence>                                  ");
        xsd.append("        <xs:element name=\"age\">                    ");
        xsd.append("          <xs:simpleType>                            ");
        xsd.append("            <xs:restriction base=\"xs:integer\">     ");
        xsd.append("              <xs:minInclusive value=\"0\"/>         ");
        xsd.append("              <xs:maxInclusive value=\"120\"/>       ");
        xsd.append("            </xs:restriction>                        ");
        xsd.append("          </xs:simpleType>                           ");
        xsd.append("        </xs:element>                                ");
        xsd.append("      </xs:sequence>                                 ");
        xsd.append("    </xs:complexType>                                ");
        xsd.append("  </xs:element>                                      ");
        xsd.append("</xs:schema>                                         ");

        assertValidAndNotValidXml(xsd.toString(), valid.toString(), invalid.toString());
    }

    @Test
    public void restrictions_regex() {
        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>              ");
        valid.append("<data xmlns=\"https://www.kul.com\"> ");
        valid.append("  <psc>73924</psc>                   ");
        valid.append("</data>                              ");

        StringBuilder invalid = new StringBuilder();
        invalid.append("<?xml version=\"1.0\"?>              ");
        invalid.append("<data xmlns=\"https://www.kul.com\"> ");
        invalid.append("  <psc>krmelin</psc>                 ");
        invalid.append("</data>                              ");

        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                              ");
        xsd.append("<xs:schema                                           ");
        xsd.append("  xmlns=\"https://www.kul.com\"                      ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"      ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"            ");
        xsd.append("  elementFormDefault=\"qualified\"                   ");
        xsd.append(">                                                    ");
        xsd.append("  <xs:element name=\"data\">                         ");
        xsd.append("    <xs:complexType>                                 ");
        xsd.append("      <xs:sequence>                                  ");
        xsd.append("        <xs:element name=\"psc\">                    ");
        xsd.append("          <xs:simpleType>                            ");
        xsd.append("            <xs:restriction base=\"xs:string\">      ");
        xsd.append("              <xs:pattern value=\"\\d{5}\" />        ");
        xsd.append("            </xs:restriction>                        ");
        xsd.append("          </xs:simpleType>                           ");
        xsd.append("        </xs:element>                                ");
        xsd.append("      </xs:sequence>                                 ");
        xsd.append("    </xs:complexType>                                ");
        xsd.append("  </xs:element>                                      ");
        xsd.append("</xs:schema>                                         ");

        assertValidAndNotValidXml(xsd.toString(), valid.toString(), invalid.toString());
    }

    @Test
    public void complexElement() {
        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>               ");
        valid.append("<data xmlns=\"https://www.kul.com\">  ");
        valid.append("  <name>Jane</name>                   ");
        valid.append("  <age>20</age>                       ");
        valid.append("</data>                               ");

        StringBuilder invalid_missingElement = new StringBuilder();
        invalid_missingElement.append("<?xml version=\"1.0\"?>              ");
        invalid_missingElement.append("<data xmlns=\"https://www.kul.com\"> ");
        invalid_missingElement.append("  <name>Jane</name>                  ");
        invalid_missingElement.append("</data>                              ");

        StringBuilder invalid_wrongOrder = new StringBuilder();
        invalid_wrongOrder.append("<?xml version=\"1.0\"?>              ");
        invalid_wrongOrder.append("<data xmlns=\"https://www.kul.com\"> ");
        invalid_wrongOrder.append("  <age>20</age>                      ");
        invalid_wrongOrder.append("  <name>Jane</name>                  ");
        invalid_wrongOrder.append("</data>                              ");

        StringBuilder invalid_moreElements = new StringBuilder();
        invalid_moreElements.append("<?xml version=\"1.0\"?>              ");
        invalid_moreElements.append("<data xmlns=\"https://www.kul.com\"> ");
        invalid_moreElements.append("  <name>Jane</name>                  ");
        invalid_moreElements.append("  <age>20</age>                      ");
        invalid_moreElements.append("  <sex>female</sex>                  ");
        invalid_moreElements.append("</data>                              ");

        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                                       ");
        xsd.append("<xs:schema                                                    ");
        xsd.append("  xmlns=\"https://www.kul.com\"                               ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"               ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"                     ");
        xsd.append("  elementFormDefault=\"qualified\"                            ");
        xsd.append(">                                                             ");
        xsd.append("  <xs:element name=\"data\">                                  ");
        xsd.append("    <xs:complexType>                                          ");
        xsd.append("      <xs:sequence>                                           ");
        xsd.append("        <xs:element name=\"name\" type=\"xs:string\" />       ");
        xsd.append("        <xs:element name=\"age\" type=\"xs:integer\" />       ");
        xsd.append("      </xs:sequence>                                          ");
        xsd.append("    </xs:complexType>                                         ");
        xsd.append("  </xs:element>                                               ");
        xsd.append("</xs:schema>                                                  ");

        assertValidAndNotValidXml(
                xsd.toString(),
                valid.toString(),
                invalid_missingElement.toString(),
                invalid_wrongOrder.toString(),
                invalid_moreElements.toString());
    }
    
    @Test
    public void choice_singleChoice() {
        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                                       ");
        xsd.append("<xs:schema                                                    ");
        xsd.append("  xmlns=\"https://www.kul.com\"                               ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"               ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"                     ");
        xsd.append("  elementFormDefault=\"qualified\"                            ");
        xsd.append(">                                                             ");
        xsd.append("  <xs:element name=\"data\">                                  ");
        xsd.append("    <xs:complexType>                                          ");
        xsd.append("      <xs:choice>                                             ");
        xsd.append("        <xs:element name=\"name\" type=\"xs:string\" />       ");
        xsd.append("        <xs:element name=\"age\" type=\"xs:integer\" />       ");
        xsd.append("      </xs:choice>                                            ");
        xsd.append("    </xs:complexType>                                         ");
        xsd.append("  </xs:element>                                               ");
        xsd.append("</xs:schema>                                                  ");

        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>               ");
        valid.append("<data xmlns=\"https://www.kul.com\">  ");
        valid.append("  <name>Jane</name>                   ");
        valid.append("</data>                               ");

        assertValidXml(xsd.toString(), valid.toString());

        StringBuilder invalid_moreChoices = new StringBuilder();
        invalid_moreChoices.append("<?xml version=\"1.0\"?>               ");
        invalid_moreChoices.append("<data xmlns=\"https://www.kul.com\">  ");
        invalid_moreChoices.append("  <name>Jane</name>                   ");
        invalid_moreChoices.append("  <age>20</age>                       ");
        invalid_moreChoices.append("</data>                               ");

        assertInvalidXml(xsd.toString(), invalid_moreChoices.toString());

    }
    
    @Test
    public void choice_multipleChoice() {
        // here in choice you can have elements in any order and in any number        
        StringBuilder xsd = new StringBuilder();
        xsd.append("<?xml version=\"1.0\"?>                                       ");
        xsd.append("<xs:schema                                                    ");
        xsd.append("  xmlns=\"https://www.kul.com\"                               ");
        xsd.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"               ");
        xsd.append("  targetNamespace=\"https://www.kul.com\"                     ");
        xsd.append("  elementFormDefault=\"qualified\"                            ");
        xsd.append(">                                                             ");
        xsd.append("  <xs:element name=\"data\">                                  ");
        xsd.append("    <xs:complexType>                                          ");
        xsd.append("      <xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\">       ");
        xsd.append("        <xs:element name=\"name\" type=\"xs:string\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>       ");
        xsd.append("        <xs:element name=\"age\" type=\"xs:integer\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>       ");
        xsd.append("      </xs:choice>                                            ");
        xsd.append("    </xs:complexType>                                         ");
        xsd.append("  </xs:element>                                               ");
        xsd.append("</xs:schema>                                                  ");

        StringBuilder valid = new StringBuilder();
        valid.append("<?xml version=\"1.0\"?>               ");
        valid.append("<data xmlns=\"https://www.kul.com\">  ");
        valid.append("  <age>30</age>                       ");
        valid.append("  <name>Jane</name>                   ");
        valid.append("  <name>Jane</name>                   ");
        valid.append("  <age>30</age>                       ");
        valid.append("</data>                               ");

        assertValidXml(xsd.toString(), valid.toString());

    }

    private void assertValidAndNotValidXml(String xsd, String validXml, String... invalidXML) {
        assertValidXml(xsd, validXml);
        for (int i = 0; i < invalidXML.length; i++) {
          assertInvalidXml(xsd, invalidXML[i]);
        }
    }

    private void assertValidXml(String xsd, String validXml) {
        try {
            validate(xsd, validXml);
        } catch (Exception e) {
            throw new AssertionError("Xml file is not valid according to given xsd: " + e.getMessage());
        }
    }

    private void assertInvalidXml(String xsd, String validXml) {
        try {
            validate(xsd, validXml);
            throw new AssertionError("Xml file is valid according to given xsd.");
        } catch (SAXException e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(String xsd, String xml) throws SAXException, IOException {
        Source xmlSource = new StreamSource(new StringReader(xml));
        Source xsdSource = new StreamSource(new StringReader(xsd));

        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsdSource);
        Validator validator = schema.newValidator();
        validator.validate(xmlSource);
    }



    @Test
    public void name() throws SAXException, IOException {

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("exchange.xsd");
        String xsdStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        StreamSource xsdSource = new StreamSource(new StringReader(xsdStr));

        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsdSource);
        Validator validator = schema.newValidator();

//		String xml = String.join(
//				"\n",
//				"<?xml version='1.0' encoding='UTF-8'?>",
//				"<ex:entities",
//				"	xmlns:ex='http://cross-ni.com/synchro/exchange'",
//				"	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'",
//				"	>",
//				"	<ex:node operation='insert_update'>",
//				"		<ex:id>AAAAAAAAAAAJ</ex:id>",
//				"		<ex:name>node1</ex:name>",
//				"		<ex:nodeType>LOCALITY</ex:nodeType>",
//				"		<ex:status>ACTIVE</ex:status>",
//				"		<ex:customAttributes typeDiscriminator='LOCALITY'>",
//				"		</ex:customAttributes>",
//				"		<ex:categories>",
//				"		</ex:categories>",
//				"		<ex:location>POINT (0 0)</ex:location>",
//				"	</ex:node>",
//				"</ex:entities>"
//		);

        		String xml = String.join(
				"\n",
				"<?xml version='1.0' encoding='UTF-8'?>",
				"<ex:entities",
				"	xmlns:ex='http://cross-ni.com/synchro/exchange'",
				"	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'",
				"	>",
				"	<ex:node operation='insert_update'>",
				"		<ex:id>AAAAAAAAAAAJ</ex:id>",
				"		<ex:name>node1</ex:name>",
				"		<ex:nodeType>LOCALITY</ex:nodeType>",
				"		<ex:status>ACTIVE</ex:status>",
//				"		<ex:customAttributes typeDiscriminator='LOCALITY'>",
//				"		</ex:customAttributes>",
//				"		<ex:categories>",
//				"		</ex:categories>",
				"		<ex:location>POINT (0 0)</ex:location>",
				"	</ex:node>",
				"</ex:entities>"
		);

        Source xmlSource = new StreamSource(new StringReader(xml));
        validator.validate(xmlSource);
    }


}
