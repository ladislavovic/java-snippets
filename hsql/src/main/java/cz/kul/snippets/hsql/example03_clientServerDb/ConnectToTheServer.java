package cz.kul.snippets.hsql.example03_clientServerDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Connects to DB, which is in the server mode. First you have to start the database (the
 * command is below) and than run this code.
 * 
 * Client does not choose the database. The database file is specified when the server is
 * starting.
 * 
 * @author Ladislav Kulhanek
 *
 */
public class ConnectToTheServer {

    public static void main(String[] args) throws SQLException {

        // First startup HSQLDB in server mode. The command:
        // java -cp ./lib/hsqldb.jar org.hsqldb.server.Server --database.0
        // file:/tmp/hsqldb/testdb --dbname.0 xdb
        //
        // The new empty database is created, when the database does not exists. There is
        // an parameter, which
        // avoid that.

        // Load driver
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        // Connect to DB
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
        ResultSet result = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS").executeQuery();
        while (result.next()) {
            System.out.println(result.getObject(1));
            System.out.println(result.getObject(2));
            System.out.println(result.getObject(3));
            System.out.println(result.getObject(4));
            System.out.println(result.getObject(5));
        }
        conn.close();

    }

}
