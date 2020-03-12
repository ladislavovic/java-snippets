package cz.kul.snippets.opencsv.example02_writing;

import com.opencsv.*;
import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

public class TestWriting extends SnippetsTest {

    @Test
    public void test() throws IOException {
        String fileName = "data.csv";
        File dir = getFilesystemHelper().createRandomDir();
        String path = Paths.get(dir.toString(), fileName).toString();
        File file = new File(path);

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            ICSVWriter writer = new CSVWriterBuilder(pw)
                    .withSeparator(';')
                    .build();
            writer.writeNext(new String[] {"name", "age"});
            writer.writeNext(new String[] {"monica", "27"});
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();
            List<String[]> lines = csvReader.readAll();
            Assert.assertEquals(2, lines.size());
            Assert.assertEquals("name", lines.get(0)[0]);
            Assert.assertEquals("27", lines.get(1)[1]);
        }
    }

}
