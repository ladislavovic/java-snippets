package cz.kul.snippets.sql.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class PrintUtils {

    public static void exectuteAndPrint(String title, Connection conn, String q) throws SQLException {
        System.out.println(title + ":");
        System.out.println();
        ResultSet rs = conn.prepareStatement(q).executeQuery();
        print(rs);
    }
    
    private static void print(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        
        StringBuilder header = new StringBuilder();
        header.append(toConstSize(""));
        for (int i = 1; i <= columnCount; i++) {
            header.append(toConstSize(rsmd.getColumnLabel(i)));
        }
        header.append("\n");
        for (int i = 1; i <= (columnCount + 1); i++) {
            header.append("------------");
        }
        header.append("\n");
        
        StringBuilder data = new StringBuilder();
        for (int i = 1; rs.next(); i++) {
            data.append(toConstSize(Integer.toString(i)));
            for (int j = 1; j <= columnCount; j++) {
                Object obj = ObjectUtils.defaultIfNull(rs.getObject(j), "NULL");
                data.append(toConstSize(obj.toString()));
            }
            data.append("\n");
        }
        
        String result = header.toString() + data.toString();
        System.out.println(result);
    }
    
    private static String toConstSize(String str) {
        int size = 11;
        str = StringUtils.abbreviate(str, size);
        str = StringUtils.rightPad(str, size);
        str = str + "|";
        return str;
    }

}
