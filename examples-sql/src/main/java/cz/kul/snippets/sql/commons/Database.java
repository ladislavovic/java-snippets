/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.sql.commons;

import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kulhalad
 */
public class Database implements AutoCloseable {
    
    private Connection conn;
    
    private Database(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void close() throws SQLException
    {
        conn.close();
    }

    public static Database connect(String connStr, String user, String pswd) {
        try {
            Connection connection = DriverManager.getConnection(connStr, user, pswd);
            return new Database(connection);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static Database connect(PostgreSQLContainer postgresContainer) throws SQLException {
        Connection connection = DriverManager.getConnection(
            postgresContainer.getJdbcUrl(),
            postgresContainer.getUsername(),
            postgresContainer.getPassword()
        );
        return new Database(connection);
    }
    
    public void executeUpdate(String sql) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void executeSelectAndPrint(String title, String query) {
        try {
            PrintUtils.exectuteAndPrint(title, conn, query);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Data executeSelect(String query) {
        try {
            Statement stat = conn.createStatement();
            ResultSet resultSet = stat.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<String> columnLabels = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnLabel = metaData.getColumnLabel(i);
                columnLabels.add(columnLabel);
            }
                       
            Data result = new Data(columnLabels);
            for (int y = 0; resultSet.next(); y++) {
                for (int x = 1; x <= columnLabels.size(); x++) {
                    result.add(x-1, y, resultSet.getObject(x));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
