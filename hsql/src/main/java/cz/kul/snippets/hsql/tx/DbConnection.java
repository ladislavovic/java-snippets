package cz.kul.snippets.hsql.tx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DbConnection {

    private Connection conn;
    
    public DbConnection(String dbName, int isolationLevel) {
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:" + dbName, "SA", "");
            conn.setTransactionIsolation(isolationLevel);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setMVCC() {
        try {
            Statement s = conn.createStatement();
            s.execute("SET DATABASE TRANSACTION CONTROL MVCC");
            s.close();
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void createTable(Table table) {
        table.create(conn);
    }
    
    public void insertRow(Table table) {
        table.insertRow(conn);
    }
    
    public int getRowCount(Table table) {
        return table.getRowCount(conn);
    }
    
    public void update(Table table, String update) {
        table.update(conn, update);
    }
    
    public List<List<String>> select(Table table, String select) {
        return table.select(conn, select);
    }
    
    public void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void close() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

interface Table {
    
    void create(Connection conn);
    
    void insertRow(Connection conn);
    
    List<Object> getAllRows(Connection conn);
    
    int getRowCount(Connection conn);
    
    List<List<String>> select(Connection conn, String select);
    
    void update(Connection conn, String update);
}

abstract class RuntimeExceptionThrowingTable implements Table {

    abstract protected void createCore(Connection conn) throws SQLException;
    
    abstract protected void insertRowCore(Connection conn) throws SQLException;
    
    abstract protected List<Object> getAllRowsCore(Connection conn) throws SQLException;
    
    abstract protected int getRowCountCore(Connection conn) throws SQLException;
    
    abstract protected List<List<String>> selectCore(Connection conn, String select) throws SQLException;

    abstract protected void updateCore(Connection conn, String update) throws SQLException;
        
    public void create(Connection conn) {
        try {
            createCore(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }        
    }

    public void insertRow(Connection conn) {
        try {
            insertRowCore(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }        
    }

    public List<Object> getAllRows(Connection conn) {
        try {
            return getAllRowsCore(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRowCount(Connection conn) {
        try {
            return getRowCountCore(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<String>> select(Connection conn, String select) {
        try {
            return selectCore(conn, select);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void update(Connection conn, String update) {
        try {
            updateCore(conn, update);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

class TablePeople extends RuntimeExceptionThrowingTable {

    @Override
    protected void createCore(Connection conn) throws SQLException {
        Statement stm = getStatement(conn);
        stm.executeUpdate("CREATE TABLE people (id INT, name VARCHAR(64))");
        stm.close();
    }

    @Override
    protected void insertRowCore(Connection conn) throws SQLException {
        Statement stm = getStatement(conn);
        stm.executeUpdate("INSERT INTO people (id, name) values (1, 'Jane')");
        stm.close();
    }

    @Override
    protected List<Object> getAllRowsCore(Connection conn) throws SQLException {
        throw new RuntimeException("not implemented");
    }

    @Override
    protected int getRowCountCore(Connection conn) throws SQLException {
        Statement stm = getStatement(conn);
        ResultSet rs = stm.executeQuery("select count(*) from people");
        rs.next();
        int result = rs.getInt(1);
        rs.close();
        stm.close();
        return result;
    }

    @Override
    protected List<List<String>> selectCore(Connection conn, String select) throws SQLException {
        Statement stm = getStatement(conn);
        ResultSet rs = stm.executeQuery(select);
        int columnCount = rs.getMetaData().getColumnCount();
        List<List<String>> result = new ArrayList<List<String>>();
        while(rs.next()) {
            List<String> row = new ArrayList<String>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i));
            }
            result.add(row);
        }
        return result;
    }
    
    @Override
    protected void updateCore(Connection conn, String update) throws SQLException {
        Statement stm = getStatement(conn);
        stm.executeUpdate(update);
    }
    
    private Statement getStatement(Connection conn) throws SQLException {
        Statement stm = conn.createStatement();
        stm.setQueryTimeout(2);
        return stm;
    }
    
}
