package cz.kul.snippets.sql.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class PrintUtils {

    public static final int DEFAULT_COLUMN_SIZE = 11;

    public static void exectuteAndPrint(String title, Connection conn, String q) throws SQLException {
        exectuteAndPrint(title, conn, q, DEFAULT_COLUMN_SIZE);
    }

    public static void exectuteAndPrint(String title, Connection conn, String q, int columnSize) throws SQLException {
        System.out.println(title + ":");
        System.out.println();
        ResultSet rs = conn.prepareStatement(q).executeQuery();
        print(rs, columnSize);
    }
    
    private static void print(ResultSet rs, int columnSize) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        
        StringBuilder header = new StringBuilder();
        header.append(toGivenSize("", columnSize));
        for (int i = 1; i <= columnCount; i++) {
            header.append(toGivenSize(rsmd.getColumnLabel(i), columnSize));
        }
        header.append("\n");
        for (int i = 0; i < columnCount + 1; i++) {
            header.append("-".repeat(columnSize + 1));
        }
        header.append("\n");
        
        StringBuilder data = new StringBuilder();
        for (int i = 1; rs.next(); i++) {
            data.append(toGivenSize(Integer.toString(i), columnSize));
            for (int j = 1; j <= columnCount; j++) {
                Object obj = ObjectUtils.defaultIfNull(rs.getObject(j), "NULL");
                data.append(toGivenSize(obj.toString(), columnSize));
            }
            data.append("\n");
        }
        
        String result = header.append(data).toString();
        System.out.println(result);
    }
    
    private static String toGivenSize(String str, final int size) {
        str = StringUtils.abbreviate(str, size);
        str = StringUtils.rightPad(str, size);
        str = str + "|";
        return str;
    }

}
