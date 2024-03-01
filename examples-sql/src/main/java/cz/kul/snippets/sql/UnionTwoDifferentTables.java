package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.Data;
import cz.kul.snippets.sql.commons.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UnionTwoDifferentTables
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
                  created_at TIMESTAMP
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                INSERT INTO person (name, created_at) VALUES 
                    ('Monica', MAKE_TIMESTAMP(2024, 1, 1, 0, 0, 0)),
                    ('Rachel', MAKE_TIMESTAMP(2024, 1, 3, 0, 0, 0));
                """);

            // language=SQL
            db.executeUpdate("""
                CREATE TABLE company (
                  id SERIAL PRIMARY KEY,
                  name TEXT,
                  number_of_employees INTEGER,
                  created_at TIMESTAMP
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                INSERT INTO company (name, number_of_employees, created_at) VALUES 
                    ('Google', 10000, MAKE_TIMESTAMP(2024, 1, 2, 0, 0, 0)),
                    ('Apple', 5000, MAKE_TIMESTAMP(2024, 1, 4, 0, 0, 0));
                """);
        }

    }

    @AfterAll
    public static void stopDB()
    {
        postgresContainer.stop();
    }

    @Test
    void shouldUnionTwoDifferentResultSets() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect(
                // language=SQL
                """
                    (
                    SELECT 
                        created_at,
                        'person' AS type,
                        name,
                        NULL
                    FROM
                        person
                    UNION
                    SELECT
                       created_at,
                       'company' AS type,
                       NULL,
                       number_of_employees
                    FROM
                       company
                    )
                    ORDER BY created_at;
                    """);
            Assertions.assertEquals(Arrays.asList("Monica", null, "Rachel", null), data.getColumnData(2));
            Assertions.assertEquals(Arrays.asList(null, 10000, null, 5000), data.getColumnData(3));
        }
    }

}
