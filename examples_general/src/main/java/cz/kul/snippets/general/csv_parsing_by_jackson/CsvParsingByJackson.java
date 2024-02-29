package cz.kul.snippets.general.csv_parsing_by_jackson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvParsingByJackson
{

    @Test
    void parseCsvToListOfStrings() throws IOException
    {
        String csv = """
            1,2.2,true, blue
            1,3.3,false,red
            """;

        ObjectReader objectReader = new CsvMapper() // We need to create CvsMapper instead of ObjectMapper
            .readerForListOf(String.class)
            .with(CsvParser.Feature.WRAP_AS_ARRAY); // With this feature it considers each line as a separated object.
        // Do not understand it fully. But it must be here :)

        try (MappingIterator<List<String>> iterator = objectReader.readValues(csv)) {
            List<List<String>> rows = iterator.readAll();

            Assertions.assertEquals(List.of("1", "2.2", "true", " blue"), rows.get(0));
            Assertions.assertEquals(List.of("1", "3.3", "false", "red"), rows.get(1));
        }
    }

    @Test
    void parseCsvToMap() throws IOException
    {
        String csv = """
            1,2.2,true
            1,3.3,false
            """;

        CsvSchema schema = CsvSchema.builder() // this defines the schema of the CSV
            .addColumn("x")
            .addColumn("y")
            .addColumn("visible")
            .build();

        ObjectReader objectReader = new CsvMapper()
            .readerForMapOf(String.class)
            .with(schema);

        try (MappingIterator<Map<String, String>> iterator = objectReader.readValues(csv)) {
            List<Map<String, String>> rows = iterator.readAll();

            Assertions.assertEquals(Map.of("x", "1", "y", "2.2", "visible", "true"), rows.get(0));
            Assertions.assertEquals(Map.of("x", "1", "y", "3.3", "visible", "false"), rows.get(1));
        }
    }

    @Test
    void parseCsvToPojo() throws IOException
    {
        String csv = """
            x|y|visible
            1|2.2|true
            1|3.3|false
            """;

//        It is possible to build schema this way, but it is better to build it by POJO class
//
//        CsvSchema schema = CsvSchema.builder()
//            .addColumn("x")
//            .addColumn("y")
//            .addColumn("visible")
//            .build();

        CsvMapper csvMapper = new CsvMapper();

        CsvSchema schema = csvMapper // this define CSV schema according to POJO
            .schemaFor(Point.class)
            .withHeader()
            .withColumnSeparator('|');

        ObjectReader objectReader = csvMapper
            .readerFor(Point.class)
            .with(schema);

        try (MappingIterator<Point> iterator = objectReader.readValues(csv)) {
            List<Point> rows = iterator.readAll();

            Assertions.assertEquals(new Point(1.0, 2.2, true), rows.get(0));
            Assertions.assertEquals(new Point(1.0, 3.3, false), rows.get(1));
        }
    }

    @JsonPropertyOrder({ // It must be there. By default, Jackson use Alphabetical order, so the first CSV column would be mapped to "visible" field.
        "x",
        "y",
        "visible"
    })
    record Point(
        double x,
        double y,
        boolean visible
    )
    {
    }

}
