package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.Data;
import cz.kul.snippets.sql.commons.Database;
import org.junit.Assert;
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
                insert into person (id, name) values (1, 'Chandler');
                insert into person (id, name) values (2, 'Monica');
                insert into person (id, name, no_of_payments, sum_of_payments) values (3, 'Rachel', 3, 3000);
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
    void staticValueUpdate() throws SQLException
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
    void staticValueUpdateWithSubqueryFilter() throws SQLException
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
    void dynamicValueUpdate() throws SQLException
    {
        // This update set the result of the subquery, so the set value is dynamic. The subquery must follows the several rules:
        //   * the number of returned columns must match the number of set values
        //   * must return 0 or 1 row for each table row, otherwise an error occurred.
        //
        // Other notes
        //   * you can of course limit update by WHERE clause. Otherwise, the whole table is updated.
        //   * it executes the subquery for each row

        // Example: update payments only for Rachel and Monica
        runUpdateAndPrintPersonTable(
            //language=SQL
            """
            UPDATE person
            SET (sum_of_payments, no_of_payments) = (
                SELECT
                    coalesce(sum(p.amount), 0),
                    count(*)
                FROM payment p
                WHERE p.id_person = person.id
            )
            WHERE name in ('Rachel', 'Monica')
            """
        );

        // No of payments is updated to 2, because there are two rows in the payment table for Monica
        Assert.assertEquals(Integer.valueOf(2), selectFromDb("select no_of_payments from person where name = 'Monica'", Integer.class));

        // No of payments is updated to 0, because there are no rows in the payment table for Rachel
        Assert.assertEquals(Integer.valueOf(0), selectFromDb("select no_of_payments from person where name = 'Rachel'", Integer.class));

        // No of payments is NULL, because Chandler was not set by this update at all
        Assert.assertNull(selectFromDb("select no_of_payments from person where name = 'Chandler'", Object.class));
    }

    @Test
    void updateWithCTE() throws SQLException
    {
        // CTE means Common Table Expression and it is a way how to create "virtual" tables.
        // It is the preferred way how to update, because you can verify updated data before you execute the command.
        //
        // FROM clause notes:
        // * it lets say join the given table to the updated table. You can then use its columns in SET part and also in WHERE part
        // * where part has two roles then:
        //   1. filter which rows to update
        //   2. filter which rows to join
        // * in 99% you need to join 0 or 1 row. If you join more rows it is still a legal statement, but the result is not deterministic

        runUpdateAndPrintPersonTable(
            //language=SQL
            """
            with updated as (
                SELECT
                    pm.id_person id_person,
                    coalesce(sum(pm.amount), 0) sum_of_payments,
                    count(*) no_of_payments
                FROM payment pm
                GROUP BY pm.id_person
            )
            UPDATE person
            SET
                sum_of_payments = updated.sum_of_payments,
                no_of_payments = updated.no_of_payments
            FROM updated
            WHERE person.id = id_person
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

    private <T> T selectFromDb(String sql, Class<T> type) throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            return (T) db.executeSelect(sql).getData(0, 0);
        }
    }

}
