package cz.kul.snippets.designpatterns.example07_factorymethod;

import static org.junit.Assert.assertEquals;

/**
 * <h1>Description</h1>
 * <p>
 * "Define an interface for creating an object, but let subclasses decide which class to
 * instantiate. The Factory method lets a class defer instantiation it uses to
 * subclasses." (GoF)
 * </p>
 * <p>
 * When to use
 * </p>
 * <ul>
 * <li>When creating of an object is a complex process. It is not appropriate to put this
 * code to the client.</li>
 * </ul>
 * <p>
 * Disadvantages: it can make an applicaation overcomplicated. You can use Simple Factory
 * Method instead of that.
 * </p>
 * 
 * <h1>Real world examples</h1>
 * <ul>
 * <li>Iterable.iterator() - iterator is product of the factory (we do not know which
 * particular instance is returned, it is hidden behind the interface) and collections
 * which implements this methods are factories (ArrayList, HashSet, ...)</li>
 * </ul>
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_FactoryMethod {

    public static void main(String[] args) {
        String settings = "postgre";
        DBConnectionCreator creator = null;
        if ("postgre".equals(settings)) {
            creator = new PostgreSQLConnectionCreator();
        } else if ("oracle".equals(settings)) {
            creator = new OracleConnectionCreator();
        }

        DBConnection connection = creator.connect();
        assertEquals(PostgreSQLConnection.class, connection.getClass());
    }

}

interface DBConnection {
    String runSQL(String sql);
}

class OracleConnection implements DBConnection {

    @Override
    public String runSQL(String sql) {
        return "oracle result";
    }

}

class PostgreSQLConnection implements DBConnection {

    @Override
    public String runSQL(String sql) {
        return "postgreSQL result";
    }

}

interface DBConnectionCreator {
    DBConnection connect();
}

class OracleConnectionCreator implements DBConnectionCreator {

    @Override
    public DBConnection connect() {
        return new OracleConnection();
    }

}

class PostgreSQLConnectionCreator implements DBConnectionCreator {

    @Override
    public DBConnection connect() {
        return new PostgreSQLConnection();
    }

}
