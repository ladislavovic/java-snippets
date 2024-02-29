package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.PrintUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * How to return n-th greatest row for the group
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class Nth_greatest_per_group
{

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            
            StringBuilder scheme = new StringBuilder();
            scheme.append("CREATE TABLE phone (id INTEGER, name VARCHAR(64), price INTEGER, country VARCHAR(64), os VARCHAR(64));");
            
            StringBuilder data = new StringBuilder();
            data.append("INSERT INTO phone VALUES (1, 'Iphone6', 1000, 'us', 'osx');");
            data.append("INSERT INTO phone VALUES (2, 'Iphone6', 1200, 'cs', 'osx');");
            data.append("INSERT INTO phone VALUES (3, 'SamsungG5', 800, 'us', 'android');");
            data.append("INSERT INTO phone VALUES (4, 'SamsungG5', 900, 'cs', 'android');");
            data.append("INSERT INTO phone VALUES (5, 'LG700', 600, 'us', 'android');");
           
            Statement stm = conn.createStatement();
            stm.executeUpdate(scheme.toString());
            stm.executeUpdate(data.toString());

            // Find the rows with cheapest cell phone for each country and each operation system
            // Another possibility is to use subquery with aggregation, but it is more elegant
            {
                StringBuilder q = new StringBuilder();
                q.append("SELECT p1.* ");
                q.append("FROM phone p1 ");
                q.append("LEFT JOIN phone p2 ON (p1.country, p1.os) = (p2.country, p2.os) AND p1.price > p2.price ");
                q.append("WHERE p2.id IS NULL ");
                q.append("ORDER BY p1.country, p1.os ");
                PrintUtils.exectuteAndPrint("Query 1", conn, q.toString());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
