package cz.kul.snippets.java;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Sandbox3
{

    public static void main(String[] args) throws Exception
    {

        // Specify the directory path here
//        String directoryPath = "/home/lad/tmp/08-08/ooni-resend/already-manually-created";
        String directoryPath = "/home/lad/tmp/08-08/ooni-resend/to-resend-correct-vendor";

        // Create a File object for the directory
        File directory = new File(directoryPath);

        // Check if the directory exists and is indeed a directory
        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory (no recursion)

            // Iterate through each file
            for (File file : directory.listFiles()) {
                // Check if the File object is a file and not a directory
                if (file.isFile()) {
                    String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                    String depositorOrderNumber = getElementValue(fileContent, "DepositorOrderNumber");
                    String poNumber = getElementValue(fileContent, "PurchaseOrderNumber");
                    String vendor = getElementValue(fileContent, "Vendor");
                    String spsFileName = file.getName();


                    System.out.println("%s;%s;%s;%s".formatted(poNumber, depositorOrderNumber, vendor, spsFileName));
                } else if (file.isDirectory()) {
                    throw new RuntimeException("Not a file");
                }
            }
        } else {
            System.out.println("The specified path is not a directory or does not exist.");
        }
    }

    private static String getElementValue(String xmlContent, String elementName) throws Exception
    {
        // Parse the XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlContent)));

        // Normalize the XML structure
//        document.getDocumentElement().normalize();

        NodeList vendorList = document.getElementsByTagName(elementName);
        if (vendorList.getLength() > 1) {
            throw new Exception("Too many vendors.");
        }
        return vendorList.item(0).getTextContent();
    }

}

