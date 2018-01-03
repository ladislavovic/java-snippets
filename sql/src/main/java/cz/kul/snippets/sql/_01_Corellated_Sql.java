package cz.kul.snippets.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.NVList;

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
public class _01_Corellated_Sql {

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            
            StringBuilder scheme = new StringBuilder();
            scheme.append("CREATE TABLE car (id_car INTEGER, name VARCHAR(64));");
            scheme.append("CREATE TABLE accessory (id_accessory INTEGER, name VARCHAR(64));");
            scheme.append("CREATE TABLE car_accessory (id_car INTEGER, id_accessory INTEGER);");
            
            StringBuilder data = new StringBuilder();
            data.append("INSERT INTO car VALUES (1, 'Mondeo');");
            data.append("INSERT INTO car VALUES (2, 'Pasat');");
            data.append("INSERT INTO car VALUES (3, 'Insignia');");
            data.append("INSERT INTO accessory VALUES (1, 'tempomat');");   // pasat, insignia
            data.append("INSERT INTO accessory VALUES (2, 'klima');");      // pasat, insignia, mondeo
            data.append("INSERT INTO accessory VALUES (3, 'navigace');");   // insignia
            data.append("INSERT INTO accessory VALUES (4, 'wifi');");       // nobody
            data.append("INSERT INTO car_accessory VALUES (1, 2);");
            data.append("INSERT INTO car_accessory VALUES (2, 1);");
            data.append("INSERT INTO car_accessory VALUES (2, 2);");
            data.append("INSERT INTO car_accessory VALUES (3, 1);");
            data.append("INSERT INTO car_accessory VALUES (3, 2);");
            data.append("INSERT INTO car_accessory VALUES (3, 3);");
           
            Statement stm = conn.createStatement();
            stm.executeUpdate(scheme.toString());
            stm.executeUpdate(data.toString());
            
            // select all cars, which have klima and tempomat
            {
                // Version one: correlated query.
                //   - is is not effective, especially when the table is big
                //   - if you need to check more accessory (lets say 50 kinds) it
                //     would be less and less effective
                StringBuilder q = new StringBuilder();
                q.append("SELECT * ");
                q.append("FROM car c1 ");
                q.append("WHERE EXISTS ( ");
                q.append("  SELECT * FROM car_accessory ca WHERE c1.id_car = ca.id_car and ca.id_accessory = 1 ");
                q.append(") ");
                q.append("AND EXISTS (");
                q.append("  SELECT * FROM car_accessory ca WHERE c1.id_car = ca.id_car and ca.id_accessory = 2 ");
                q.append(") ");
                PrintUtils.exectuteAndPrint("Query 1 - correlated", conn, q.toString());
            }
            {
                // Version two: optimization with grouping
                //   - you can optimize by grouping
                //   - the query is not so clear and readable. But in pratcise it is the only way how to do it.
                StringBuilder q = new StringBuilder();
                q.append("SELECT * ");
                q.append("FROM car c1 ");
                q.append("WHERE c1.id_car in ( ");
                q.append("  SELECT c2.id_car  ");
                q.append("  FROM car c2 ");
                q.append("  JOIN car_accessory ca ON c2.id_car = ca.id_car ");
                q.append("  WHERE ca.id_accessory in (1, 2) ");
                q.append("  GROUP BY c2.id_car ");
                q.append("  HAVING COUNT(*) = 2 ");   
                q.append(") ");
                PrintUtils.exectuteAndPrint("Query 2 - optimization by grouping", conn, q.toString());
            }
            
            // select all cars, which does not have navigace and wifi
            
     
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
