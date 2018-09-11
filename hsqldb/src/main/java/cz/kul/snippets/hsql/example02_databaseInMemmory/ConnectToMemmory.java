package cz.kul.snippets.hsql.example02_databaseInMemmory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * Connection to database in memmory.
 * 
 * The database is not persistent. The database will be destroyed
 * with the JVM shutdown.
 * 
 */
public class ConnectToMemmory {

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            ResultSet result = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS").executeQuery();
            while (result.next()) {
                System.out.println(result.getObject(1));
                System.out.println(result.getObject(2));
                System.out.println(result.getObject(3));
                System.out.println(result.getObject(4));
                System.out.println(result.getObject(5));
            }

            Statement stm = conn.createStatement();
            stm.executeUpdate("create table foobar (id int, name varchar(64))");
            stm.executeUpdate("insert into foobar (id, name) values (10, 'bla')");
            result = stm.executeQuery("select * from foobar");
            while (result.next()) {
                System.out.println(result.getObject(1));
            }
            stm.close();

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
