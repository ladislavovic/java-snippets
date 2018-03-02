package cz.kul.snippets.poi._01_poi;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main_POI {

    public static void main(String[] args) throws Exception {
        read_XLSX_file();
    }

    private static void read_XLSX_file() throws IOException {
        //
        // init
        //
        List<String> foundValues = new ArrayList<>();

        // 
        // Load xlsx file and read are data there
        //
        try (InputStream input = Main_POI.class.getClassLoader().getResourceAsStream("numbers.xlsx");
                XSSFWorkbook myWorkBook = new XSSFWorkbook(input);) {

            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            for (Iterator<Row> rows = mySheet.iterator(); rows.hasNext();) {
                Row row = rows.next();
                for (Iterator<Cell> cells = row.cellIterator(); cells.hasNext();) {
                    Cell cell = cells.next();
                    String value = null;
                    switch (cell.getCellTypeEnum()) {
                    case NUMERIC:
                        double number = cell.getNumericCellValue();
                        long longNumber = (long) number;
                        value = Long.toString(longNumber);
                        break;
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    default:
                        break;
                    }
                    if (value != null) {
                        foundValues.add(value);
                    }
                }
            }
        }

        //
        // test
        //
        assertEquals(Arrays.asList("10", "20", "30", "40"), foundValues);

    }

}
