package cz.kul.snippets.hsql.example01_databaseInFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/*
 * Connection to database from selected file. The file path
 * can be absolute or relative. Relative path starts at the
 * working directory of JVM.
 * 
 * If the database does not exists, it is created.
 * 
 * Only one thread can be connected to the database from file.
 * 
 * With the first connection to DB the SYS user is created. Its name
 * and password are specified in the connection string. It is usually SA,
 * but it can be whatever you want. This user has a DBA role. The role
 * allow to do all possible administrative tasks.
 * 
 */
public class ConnectToFile {

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:file:/tmp/hsqldb/testdb", "SA", "");
            ResultSet result = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS").executeQuery();
            while (result.next()) {
                System.out.println(result.getObject(1));
                System.out.println(result.getObject(2));
                System.out.println(result.getObject(3));
                System.out.println(result.getObject(4));
                System.out.println(result.getObject(5));
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
