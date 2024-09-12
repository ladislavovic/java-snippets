package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;

public class Update
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
                  no_of_payments INTEGER,
                  sum_of_payments INTEGER
                );
                """
            );
            // language=SQL
            db.executeUpdate("""
                CREATE TABLE payment (
                  id SERIAL PRIMARY KEY,
                  id_person INTEGER NOT NULL,
                  amount INTEGER NOT NULL
                );
                """
            );
            // language=SQL
            db.executeUpdate("""
                insert into person (id, name) values (1, 'John');
                insert into person (id, name) values (2, 'Monica');
                insert into payment (id_person, amount) values (1, 1000);
                insert into payment (id_person, amount) values (1, 2000);
                insert into payment (id_person, amount) values (2, 3000);
                insert into payment (id_person, amount) values (2, 4000);
                """);
        }

    }

    @AfterAll
    public static void stopDB()
    {
        postgresContainer.stop();
    }

    @Test
    void simpleUpdate() throws SQLException
    {
        // This is a simple update, you set a constant value for all required rows
        runUpdateAndPrintPersonTable(
            //language=SQL
            """
            UPDATE person
            SET sum_of_payments = 1
            WHERE name = 'Monica'
            """
        );
    }

    @Test
    void updateWithSubqueryFilter() throws SQLException
    {
        // In this update we first find out all rows we want to update by subquery and then update to constant value.
        // The subquery can access any table of course.
        runUpdateAndPrintPersonTable(
            //language=SQL
            """
            UPDATE person
            SET sum_of_payments = 1
            WHERE id IN (SELECT p.id_person FROM payment p WHERE p.amount > 3000)
            """
        );
    }

    @Test
    void setResultOfTheSubquery() throws SQLException
    {
        // This update set the result of the subquery. The subquery must follows the several rules:
        //   * the number of returned columns must match the number of set values
        //   * must return 0 or 1 row. Not more, otherwise an error occurred.
        //   * does not have to refer the update table. But usual you need it

        // Example: correct example
        runUpdateAndPrintPersonTable(
            //language=SQL
            """
            UPDATE person
            SET (sum_of_payments, no_of_payments) = (SELECT sum(p.amount), count(*) FROM payment p WHERE p.id_person = person.id)
            """
        );

        // Example: does not return a row in some cases. Then it set NULL value.
        runUpdateAndPrintPersonTable(
            //language=SQL
            """
            UPDATE person
            SET (sum_of_payments, no_of_payments) = (SELECT sum(p.amount), count(*) FROM payment p WHERE p.id_person = person.id and p.amount > 2000)
            """
        );
    }

    private void runUpdateAndPrintPersonTable(String sqlUpdate) throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            db.executeUpdate(sqlUpdate);
            db.executeSelectAndPrint("All persons", "SELECT * FROM person", 17);
        }

    }

}
