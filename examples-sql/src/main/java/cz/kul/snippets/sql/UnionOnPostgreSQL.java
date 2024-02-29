package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.Data;
import cz.kul.snippets.sql.commons.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.List;

public class UnionOnPostgreSQL
{

    public static PostgreSQLContainer postgresContainer;

    @BeforeAll
    public static void initDB() throws Exception
    {
        postgresContainer = new PostgreSQLContainer("postgres:16.2");
        postgresContainer.start();

        try (var db = Database.connect(postgresContainer)) {

            // language=SQL
            db.executeUpdate("""
                CREATE TABLE person (
                  id SERIAL PRIMARY KEY,
                  name TEXT,
                  age SMALLINT,
                  address TEXT
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                INSERT INTO person (name, age, address) VALUES ('Monica', 29, 'New York');
                INSERT INTO person (name, age, address) VALUES ('Rachel', 28, 'New Jersey');
                """);

            // language=SQL
            db.executeUpdate("""
                CREATE TABLE company (
                  id SERIAL PRIMARY KEY,
                  name TEXT,
                  ico TEXT,
                  address TEXT,
                  noOfEmployees INTEGER
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                INSERT INTO company (name, ico, address, noOfEmployees) VALUES ('Xerox', '12345', 'Dublin', 10000);
                INSERT INTO company (name, ico, address, noOfEmployees) VALUES ('Apple', '77777', 'Silicon Valley', 5000);
                """
            );
        }

    }

    @AfterAll
    public static void stopDB()
    {
        postgresContainer.stop();
    }

    @Test
    void shouldFailBecauseEachUnionQueryMustHaveTheSameNumberOfColumns() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            Exception exception = Assertions.assertThrows(Exception.class, () -> db.executeSelect(
                // language=SQL
                """
                SELECT name, age FROM person
                UNION
                SELECT name FROM company;
                """));
        }
    }

    @Test
    void shouldFailBecauseEachUnionQueryMustHaveTheSameTypeOfColumns() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            Exception exception = Assertions.assertThrows(Exception.class, () -> db.executeSelect(
                // language=SQL
                """
                SELECT age FROM person
                UNION
                SELECT name FROM company;
                """));
        }
    }

    @Test
    void shouldRemoveDuplicateOrdersUntilUnionAllIsNotUsed() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {

            {
                Data data = db.executeSelect(
                    // language=SQL
                    """
                    SELECT * FROM person
                    UNION
                    SELECT * FROM person;
                    """);
                Assertions.assertEquals(2, data.getRowsCount());
            }

            {
                Data data2 = db.executeSelect(
                    // language=SQL
                    """
                    SELECT * FROM person
                    UNION ALL
                    SELECT * FROM person;
                    """);
                Assertions.assertEquals(4, data2.getRowsCount());
            }
        }
    }

    @Test
    void shouldSortTheResultOfTheUnionQuery() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect(
                // language=SQL
                """
                (
                  SELECT name as n FROM person
                  UNION
                  SELECT name as n FROM company
                )
                ORDER BY n;
                """);
            Assertions.assertEquals(
                List.of("Apple", "Monica", "Rachel", "Xerox"),
                data.getColumnData(0)
            );

        }
    }



}
