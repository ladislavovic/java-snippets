package cz.kul.snippets.xml.example02_xpath;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class Example02_xpath {

    @Test
    public void testXpath() throws DocumentException {
        StringBuilder xml = new StringBuilder();
        xml.append("<AAA>                                        ");
        xml.append("  <BBB/>                                     ");
        xml.append("  <CCC>ccc</CCC>                             ");
        xml.append("  <BBB a='5' b='foo' />                      ");
        xml.append("  <DDD>                                      ");
        xml.append("       <BBB/>                                ");
        xml.append("  </DDD>                                     ");
        xml.append("  <CCC>                                      ");
        xml.append("       <DDD>                                 ");
        xml.append("            <BBB/>                           ");
        xml.append("            <BBB/>                           ");
        xml.append("       </DDD>                                ");
        xml.append("  </CCC>                                     ");
        xml.append("</AAA>                                       ");

        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(xml.toString()));

        // Exact path
        {
            List nodes = document.selectNodes("/AAA/DDD/BBB");
            Assert.assertEquals(1, nodes.size());
        }

        // Relative path
        {
            List nodes = document.selectNodes("//BBB"); // find all BBB
            Assert.assertEquals(5, nodes.size());
            List nodes2 = document.selectNodes("//DDD/BBB"); // find all BBB in DDD element
            Assert.assertEquals(3, nodes2.size());
            List nodes3 = document.selectNodes("//CCC//BBB"); // find all BBB somewhere in CCC element
            Assert.assertEquals(2, nodes3.size());
        }

        // Asterisk
        {
            List nodes = document.selectNodes("/AAA/*"); // all el direct in AAA
            Assert.assertEquals(5, nodes.size());
            List nodes2 = document.selectNodes("/*/*/BBB"); // all BBB from level 3
            Assert.assertEquals(1, nodes2.size());
            List nodes3 = document.selectNodes("//*"); // all elements
            Assert.assertEquals(10, nodes3.size());
        }

        // Predicates
        {
            List nodes = document.selectNodes("/AAA/CCC/DDD/BBB[1]"); // finds the first BBBB
            Assert.assertEquals(1, nodes.size());
            nodes = document.selectNodes("/AAA/CCC/DDD/BBB[last()]"); // finds the last BBBB
            Assert.assertEquals(1, nodes.size());
            nodes = document.selectNodes("//BBB[1]"); // find all first BBB
            Assert.assertEquals(3, nodes.size());
        }

        // Attributes
        {
            List nodes = document.selectNodes("//@a"); // select all 'a' attributes
            Assert.assertEquals(1, nodes.size());
            Assert.assertEquals("5", ((Attribute) nodes.get(0)).getValue());
            nodes = document.selectNodes("//BBB[@a]"); // selects all BBB which ahave 'a' attribute
            Assert.assertEquals(1, nodes.size());
            nodes = document.selectNodes("//BBB[@a='foo']"); // selects all <BBB a="foo">
            Assert.assertEquals(0, nodes.size());
        }

        // Text
        {
            Node node = document.selectSingleNode("/AAA/CCC/text()"); // select text of element
            Text text = (Text) node;
            Assert.assertEquals("ccc", text.getText());
        }

        // Text
        {
            Document doc = DocumentHelper.parseText("<Text>Datakonsultf&#246;retag</Text>");
            Node node = doc.selectSingleNode("/Text");
        }
    }

}
