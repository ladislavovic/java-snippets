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

public class LateralJoinInPostgreSQL
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
                CREATE TABLE blog (
                  id int PRIMARY KEY,
                  name TEXT,
                  admins TEXT[]
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                CREATE TABLE article (
                  id int PRIMARY KEY,
                  id_blog int,
                  name TEXT
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                INSERT INTO blog (id, name, admins) VALUES (1, 'Ostravak', '{pepa, jarda}');
                INSERT INTO blog (id, name, admins) VALUES (2, 'Fotr', '{michal, jirka, alois}');

                INSERT INTO article (id, id_blog, name) VALUES (1, 1, 'Karolina');
                INSERT INTO article (id, id_blog, name) VALUES (2, 1, 'Vitky');
                INSERT INTO article (id, id_blog, name) VALUES (3, 1, 'Poruba');
                INSERT INTO article (id, id_blog, name) VALUES (4, 2, 'Hrad');
                INSERT INTO article (id, id_blog, name) VALUES (5, 2, 'Vaclavak');
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

            // CROSS JOIN just combine each left column with each right column
            Assertions.assertEquals(
                10,
                db.executeSelect("SELECT * FROM blog CROSS JOIN article").getRowsCount()
            );

            // You can use CROSS JOIN also with subquery.
            // This subquery do the same as the previous example. The subquery is NOT correlated, it is executed only once.
            Assertions.assertEquals(
                10,
                db.executeSelect("""
                    SELECT *
                    FROM blog b
                    CROSS JOIN (SELECT * FROM article a)
                    """).getRowsCount()
            );

            // In CROSS JOIN subquery you can not use values from the left table. This example is incorrect, it fails.
            Assertions.assertThrows(
                RuntimeException.class,
                () -> db.executeSelect("""
                    SELECT *
                    FROM blog b
                    CROSS JOIN (SELECT * FROM article a WHERE b.id = a.id_blog)
                    """)
            );

            // But with CROSS JOIN LATERAL you CAN!!!
            // So you can connect some rows to each row. This subquery IS correlated.
            //
            // I know, with this data you can do the same easily with JOIN .. ON ... But LATERAL is more powerful. You can create any query.
            Assertions.assertEquals(
                5,
                db.executeSelect("""
                    SELECT *
                    FROM blog b
                    CROSS JOIN LATERAL (SELECT * FROM article a WHERE b.id = a.id_blog)
                    """).getRowsCount()
            );

            // It is very useful, when you work with arrays
            // This example join array value to each corresponding row.
            Assertions.assertEquals(
                List.of(
                    List.of(1, "Ostravak", "pepa"),
                    List.of(1, "Ostravak", "jarda"),
                    List.of(2, "Fotr", "michal"),
                    List.of(2, "Fotr", "jirka"),
                    List.of(2, "Fotr", "alois")
                ),
                db.executeSelect(
                    """
                        SELECT b.id, b.name, admin.a_name
                        FROM blog b
                        CROSS JOIN LATERAL (SELECT b2.id, UNNEST(b2.admins) a_name FROM blog b2 WHERE b.id = b2.id) AS admin
                        """
                ).getRowsData()
            );

        }
    }

}
