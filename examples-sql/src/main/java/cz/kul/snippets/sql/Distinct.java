package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;

public class Distinct
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
                CREATE TABLE weather_report (
                  id SERIAL PRIMARY KEY,
                  location TEXT,
                  attribute TEXT,
                  value TEXT,
                  createdAt DATE
                );
                """
            );
            // language=SQL
            db.executeUpdate("""
                insert into weather_report (location, attribute, value, createdAt) values ('Ostrava', 'temperature', '10', '2024-01-01');
                insert into weather_report (location, attribute, value, createdAt) values ('Ostrava', 'wind', '4', '2024-01-01');
                insert into weather_report (location, attribute, value, createdAt) values ('Ostrava', 'sun', '0.5', '2024-01-01');

                insert into weather_report (location, attribute, value, createdAt) values ('Ostrava', 'temperature', '12', '2024-01-02');
                insert into weather_report (location, attribute, value, createdAt) values ('Ostrava', 'wind', '5', '2024-01-02');
                insert into weather_report (location, attribute, value, createdAt) values ('Ostrava', 'sun', '1', '2024-01-02');

                insert into weather_report (location, attribute, value, createdAt) values ('Prague', 'temperature', '15', '2024-01-01');
                insert into weather_report (location, attribute, value, createdAt) values ('Prague', 'wind', '1', '2024-01-01');
                insert into weather_report (location, attribute, value, createdAt) values ('Prague', 'sun', '0.3', '2024-01-01');

                insert into weather_report (location, attribute, value, createdAt) values ('Prague', 'temperature', '16', '2024-01-02');
                insert into weather_report (location, attribute, value, createdAt) values ('Prague', 'wind', '2', '2024-01-02');
                insert into weather_report (location, attribute, value, createdAt) values ('Prague', 'sun', '0.8', '2024-01-02');

                """);
        }

    }

    @AfterAll
    public static void stopDB()
    {
        postgresContainer.stop();
    }

    @Test
    void distinct() throws SQLException
    {
        /* just returns not duplicate rows */

        try (var db = Database.connect(postgresContainer)) {
            db.executeSelectAndPrint(
                "select DISTINCT",
                //language=SQL
                """
                select distinct location from weather_report
                """,
                10);
        }

    }

    @Test
    void distinctOn() throws SQLException
    {
        /*

        * DISTINCT ON () allow you specify, which tuple must be unique in the result.
        * it keeps only the "first row" from the group of duplicated row
        * it is often used with ORDER BY, otherwise the "first row" would be unpredictable
        * distinct on expression must be the same as start of the order by expression (if used). Otherwise it ends with an error.

         */

        try (var db = Database.connect(postgresContainer)) {
            db.executeSelectAndPrint(
                "The last weather report for each location",
                //language=SQL
                """
                select distinct on (location, attribute) * from weather_report order by location, attribute, createdAt desc
                """,
                14);
        }

    }

}
