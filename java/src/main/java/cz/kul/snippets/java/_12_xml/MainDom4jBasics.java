/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._12_xml;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;
import org.junit.Assert;

/**
 *
 * @author kulhalad
 */
public class MainDom4jBasics {

    public static void main(String[] args) throws DocumentException {

        basics();
        xpath();
        xpathSingleEl();

    }

    private static void basics() throws DocumentException {

        // Parse document
        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader("<root id='attr'><el1>text</el1><el2>text2</el2></root>"));

        // Get root element
        Element root = document.getRootElement();

        // Iterate elements
        for (Iterator i = root.elementIterator("el1"); i.hasNext();) {
            Element el = (Element) i.next();
            Assert.assertEquals("el1", el.getName());
        }

        // Iterate attributes
        for (Iterator i = root.attributeIterator(); i.hasNext();) {
            Attribute attr = (Attribute) i.next();
            Assert.assertEquals("id", attr.getName());
        }

    }

    private static void xpath() throws DocumentException {
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
            System.out.println("aaa" + node.getText());
        }

    }

    private static void xpathSingleEl() throws DocumentException {
        Document document = DocumentHelper.parseText("<root> <el>text</el> <el>text2</el> </root>");
        Node node = document.selectSingleNode("//el");
        Assert.assertEquals("text", node.getText());
        node = document.selectSingleNode("//missing");
        Assert.assertEquals(null, node);
    }

}
