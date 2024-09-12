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

public class WithQueries
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
                  name TEXT
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
                CREATE TABLE person_role (
                  id SERIAL PRIMARY KEY,
                  id_person INTEGER NOT NULL,
                  role TEXT
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
                insert into person_role (id_person, role) values (1, 'ROLE_USER');
                insert into person_role (id_person, role) values (1, 'ROLE_MANAGER');
                insert into person_role (id_person, role) values (2, 'ROLE_USER');
                insert into person_role (id_person, role) values (2, 'ROLE_DIRECTOR');
                """);
        }

    }

    @AfterAll
    public static void stopDB()
    {
        postgresContainer.stop();
    }

    @Test
    void theTest() throws SQLException
    {
        /*

        * you can create N auxiliary queries by WITH clause and reference them from the "main" query
        * you can also reference an auxiliary query from another auxiliary query (do not know if the order matters)
        * these auxiliary queries are called Common Table Expressions (CTEs). So it is a temporary table, which exists only within the main query.

        */

        try (var db = Database.connect(postgresContainer)) {
            db.executeSelectAndPrint(
                "WITH auxiliary queries",
                //language=SQL
                """
                WITH total_person_income AS (
                  SELECT p.id_person, sum(p.amount) as income
                  FROM payment p
                  GROUP BY p.id_person
                ),
                person_roles AS (
                  SELECT r.id_person, string_agg(r.role, ', ') as roles
                  FROM person_role r
                  GROUP BY r.id_person
                )
                SELECT
                  p.name, pr.roles, tpi.income
                FROM
                  person p
                  join person_roles pr on pr.id_person = p.id
                  join total_person_income tpi on tpi.id_person = p.id
                """,
                30);
        }

    }

}
