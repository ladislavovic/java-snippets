package cz.kul.snippets.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Correlated subqueries are queries, which use values from outer query. They
 * must be executed for every row which the outer query returns.
 * 
 * It is not true exists statement = correlated query. You can create correlated
 * query in many ways - with max, function, in select part, ...
 * 
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class _02_Multicolumn_predicate {

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            
            StringBuilder scheme = new StringBuilder();
            scheme.append("CREATE TABLE car (id_car INTEGER, name VARCHAR(64));");
            
            StringBuilder data = new StringBuilder();
            data.append("INSERT INTO car VALUES (1, 'Mondeo');");
            data.append("INSERT INTO car VALUES (2, 'Pasat');");
            data.append("INSERT INTO car VALUES (3, 'Insignia');");
           
            Statement stm = conn.createStatement();
            stm.executeUpdate(scheme.toString());
            stm.executeUpdate(data.toString());

            // example how to use it
            {
                StringBuilder q = new StringBuilder();
                q.append("SELECT * ");
                q.append("FROM car c1 ");
                q.append("WHERE (c1.id_car, c1.name) in ((1, 'Mondeo')) ");
                PrintUtils.exectuteAndPrint("Query 1", conn, q.toString());
            }
            
            // example 2 - with derivated table. It is usable in pratcise.
            {
                StringBuilder q = new StringBuilder();
                q.append("SELECT * ");
                q.append("FROM car c1 ");
                q.append("WHERE (c1.id_car, c1.name) IN ( ");
                q.append("  SELECT * FROM car c2 WHERE (c2.id_car) > 1 ");
                q.append(")");
                PrintUtils.exectuteAndPrint("Query 2", conn, q.toString());
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
