package cz.kul.snippets.xml.example03_howToValidateXml;

import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;

public class TestHowToValidateXml extends SnippetsTest {
    
    private static Source scheme;
    private static Source xml_valid;
    private static Source xml_invalid_missingElement;
    private static Source xml_invalid_wrongDataTypes;
    private static Source xml_invalid_missingCloseTag;
    
    @BeforeClass
    public static void init() {
        {
            StringBuilder xmlSchema = new StringBuilder();
            xmlSchema.append("<?xml version=\"1.0\"?>                                ");
            xmlSchema.append("<xs:schema                                             ");
            xmlSchema.append("  xmlns=\"https://www.kul.com\"                        ");
            xmlSchema.append("  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"        ");
            xmlSchema.append("  targetNamespace=\"https://www.kul.com\"              ");
            xmlSchema.append("  elementFormDefault=\"qualified\"                     ");
            xmlSchema.append(">                                                      ");
            xmlSchema.append("  <xs:element name=\"data\">                           ");
            xmlSchema.append("    <xs:complexType>                                   ");
            xmlSchema.append("      <xs:sequence>                                    ");
            xmlSchema.append("        <xs:element name=\"name\" type=\"xs:string\"/> ");
            xmlSchema.append("        <xs:element name=\"age\" type=\"xs:integer\"/> ");
            xmlSchema.append("        <xs:element name=\"birthday\" type=\"xs:date\"/> ");
            xmlSchema.append("      </xs:sequence>                                   ");
            xmlSchema.append("    </xs:complexType>                                  ");
            xmlSchema.append("  </xs:element>                                        ");
            xmlSchema.append("</xs:schema>                                           ");
            scheme = new StreamSource(new StringReader(xmlSchema.toString()));
        }
        {
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\"?>              ");
            xml.append("<data xmlns=\"https://www.kul.com\"> ");
            xml.append("  <name>Jane</name>                  ");
            xml.append("  <age>10</age>                      ");
            xml.append("  <birthday>2001-01-20</birthday>    ");
            xml.append("</data>                              ");
            xml_valid = new StreamSource(new StringReader(xml.toString()));
        }
        {
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\"?>              ");
            xml.append("<data xmlns=\"https://www.kul.com\"> ");
            xml.append("  <age>10</age>                      ");
            xml.append("  <birthday>2001-01-20</birthday>    ");
            xml.append("</data>                              ");
            xml_invalid_missingElement = new StreamSource(new StringReader(xml.toString()));
        }
        {
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\"?>              ");
            xml.append("<data xmlns=\"https://www.kul.com\"> ");
            xml.append("  <name>Jane</name>                  ");
            xml.append("  <age>INVALID_NUM</age>                      ");
            xml.append("  <birthday>INVALID_DATE</birthday>    ");
            xml.append("</data>                              ");
            xml_invalid_wrongDataTypes = new StreamSource(new StringReader(xml.toString()));
        }
        {
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\"?>              ");
            xml.append("<data xmlns=\"https://www.kul.com\"> ");
            xml.append("  <name>Jane                         ");
            xml.append("  <age>10</age>                      ");
            xml.append("  <birthday>2001-01-20</birthday>    ");
            xml.append("</data>                              ");
            xml_invalid_missingCloseTag = new StreamSource(new StringReader(xml.toString()));
        }
    }
    
    @Test
    public void validation() throws SAXException, IOException {
        // Get a Schema Factory. It is Java Service so it get a particular implementation
        // according to configuration
        // The parameter specify a schema language. There is not only XML Schema, there are
        // also other languages which can describe xml structure.
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // Create a Schema instance according to *.xsd file
        Schema schema = schemaFactory.newSchema(scheme);
        
        // Create validator and validate
        Validator validator = schema.newValidator();
        validator.validate(xml_valid);
        assertThrows(SAXException.class, () -> validator.validate(xml_invalid_missingElement));
        assertThrows(SAXException.class, () -> validator.validate(xml_invalid_missingCloseTag));
    }
    
    @Test
    public void validationWithErrorHandler() throws SAXException, IOException {
        //
        // You can customize error handling by your own error handler. Then you can
        // react on error the way you need.
        //
        // There are three events which validator can handle:
        //   warning - ???
        //   error - the error which the parser can recover from. For example missing compulsory element
        //   fatal error - the error which the parser can not recover from. For example missing closing tag etc.
        //
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(scheme);
        Validator validator = schema.newValidator();
        MyErrorHandler errorHandler = new MyErrorHandler();
        validator.setErrorHandler(errorHandler);
        
        validator.validate(xml_invalid_wrongDataTypes);
        Assert.assertTrue(errorHandler.errorCount > 1);

        assertThrows(SAXException.class, () -> validator.validate(xml_invalid_missingCloseTag));
    }

    private static class MyErrorHandler implements ErrorHandler {
        public int waringsCount;
        public int errorCount;
        public int fatalErrorCount;

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            waringsCount++;
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            errorCount++;
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            fatalErrorCount++;
            throw exception;
        }
    }

}
