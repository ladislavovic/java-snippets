package cz.kul.snippets.java;

import com.google.common.io.CharStreams;
import com.lowagie.text.DocumentException;
import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PdfGenerator {
    
    public static void main(String[] args) throws IOException, DocumentException {
        OutputStream outputStream = new FileOutputStream("/var/doc.pdf");
        ITextRenderer renderer = new ITextRenderer();
        InputStream resourceAsStream = PdfGenerator.class.getResourceAsStream("/pdf/simple.html");
        String html = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
    }
    
}
