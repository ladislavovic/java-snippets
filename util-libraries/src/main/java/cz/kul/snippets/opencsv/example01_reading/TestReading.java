package cz.kul.snippets.opencsv.example01_reading;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

public class TestReading extends SnippetsTest {
    
    @Test
    public void test() throws IOException {
        String fileName = "data.csv";
        File dir = getFilesystemHelper().createRandomDir();
        String path = Paths.get(dir.toString(), fileName).toString();
        File file = new File(path);

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            pw.println("name;age");
            pw.println("monica;27");
            pw.println("rachel;27");
            pw.println("phoeboe;28");
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();
            List<String[]> lines = csvReader.readAll();
            Assert.assertEquals(4, lines.size());
            Assert.assertEquals("name", lines.get(0)[0]);
            Assert.assertEquals("28", lines.get(3)[1]);
        }

    }
    
}
