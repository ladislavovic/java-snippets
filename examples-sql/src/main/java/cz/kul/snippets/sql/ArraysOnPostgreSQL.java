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

public class ArraysOnPostgreSQL
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
                  accounts TEXT[], -- variable length
                  address TEXT[3] -- exact size; ONLY DOCUMENTATION!!! Current implementation ignored any supplied array size.
                );
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
    void insertingValueIntoArray() throws SQLException
    {
        // language=SQL
        try (var db = Database.connect(postgresContainer)) {

            // You can use the following literal for arrays
            db.executeUpdate("""
                INSERT INTO person (name, accounts) VALUES ('Monica', '{google,   microsoft}');
                """);

            // The ARRAY constructor syntax can also be used:
            db.executeUpdate("""
                INSERT INTO person (name, accounts) VALUES ('Rachel', ARRAY['google', 'microsoft']);
                """);

            // array is indexed from 1
            Assertions.assertEquals(
                "google",
                db.executeSelect("SELECT p.accounts[1] FROM person p WHERE p.name = 'Monica'").getData(0, 0)
            );

            // Whitechars after comma are ignored, "microsoft" does not have whitechars at the beginning
            Assertions.assertEquals(
                "microsoft",
                db.executeSelect("SELECT p.accounts[2] FROM person p WHERE p.name = 'Monica';").getData(0, 0)
            );
        }
    }

    @Test
    void arrayLength() throws SQLException
    {
        // array_length()
        //   * returns length of the array
        //   * param1 - the array
        //   * param2 - the dimension of the array

        // language=SQL
        try (var db = Database.connect(postgresContainer)) {

            // You can use the following literal for arrays
            db.executeUpdate("""
                INSERT INTO person (name, accounts) VALUES ('Monica', '{google, microsoft}');
                """);

            Assertions.assertEquals(
                2,
                db.executeSelect("SELECT ARRAY_LENGTH(p.accounts, 1) FROM person p").getData(0, 0)
            );
        }
    }

    @Test
    void cardinality() throws SQLException
    {
        // cardinality()
        //   * returns the total number of elements in the given array across all dimensions

        // language=SQL
        try (var db = Database.connect(postgresContainer)) {

            // You can use the following literal for arrays
            db.executeUpdate("""
                INSERT INTO person (name, accounts) VALUES ('Monica', '{google, microsoft}');
                """);

            Assertions.assertEquals(
                2,
                db.executeSelect("SELECT CARDINALITY(p.accounts) FROM person p").getData(0, 0)
            );
        }
    }

    @Test
    void testIfTheArrayContainsTheGivenValue() throws SQLException
    {
        // language=SQL
        try (var db = Database.connect(postgresContainer)) {

            db.executeUpdate("""
                INSERT INTO person (name, accounts) VALUES ('Monica', '{google, microsoft}');
                INSERT INTO person (name, accounts) VALUES ('Rachel', '{google, facebook}');
                """);

            // This way, you can test, if the
            Data data = db.executeSelect("SELECT p.name FROM person p WHERE 'microsoft' = ANY(p.accounts)");
            Assertions.assertEquals("Monica", data.getData(0, 0));
            Assertions.assertEquals(1, data.getRowsCount());
        }
    }

    @Test
    void unnestFunction() throws SQLException
    {
        // unnest() function expand an array into a set of rows

        // 1. Convert array into rows
        try (var db = Database.connect(postgresContainer)) {
            // language=SQL
            Data data = db.executeSelect("SELECT UNNEST(ARRAY[1,2])");
            Assertions.assertEquals(
                List.of(
                    List.of(1),
                    List.of(2)
                ),
                data.getRowsData()
            );
        }

        // 2. If the row contains other values, they are duplicated for each array element
        try (var db = Database.connect(postgresContainer)) {
            // language=SQL
            Data data = db.executeSelect("SELECT 'foo', UNNEST(ARRAY[1,2])");
            Assertions.assertEquals(
                List.of(
                    List.of("foo", 1),
                    List.of("foo", 2)
                ),
                data.getRowsData()
            );
        }

        // 3. unnest can be used with CROSS JOIN LATERAL
        try (var db = Database.connect(postgresContainer)) {

            // language=SQL
            db.executeUpdate("""
                INSERT INTO person (name, accounts) VALUES ('Monica', '{google, microsoft}');
                INSERT INTO person (name, accounts) VALUES ('Rachel', '{google, facebook}');
                """);

            // language=SQL
            Data data = db.executeSelect("""
                SELECT p.name, acc
                FROM person p
                CROSS JOIN LATERAL unnest(p.accounts) acc
                """);

            Assertions.assertEquals(
                List.of(
                    List.of("Monica", "google"),
                    List.of("Monica", "microsoft"),
                    List.of("Rachel", "google"),
                    List.of("Rachel", "facebook")
                ),
                data.getRowsData()
            );
        }

    }

}
