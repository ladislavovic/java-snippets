package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.Data;
import cz.kul.snippets.sql.commons.Database;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonbIntroduction
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
                  id INTEGER PRIMARY KEY,
                  data JSONB
                );
                """
            );

            // language=SQL
            db.executeUpdate("""
                INSERT INTO person (id, data) VALUES 
                    (1, '{
                        "name": "Monica",
                        "sex": 1,
                        "age": 29,
                        "body": {
                          "height": 175,
                          "type": "slender",
                          "tattoo": false
                        }
                      }'
                    ),
                    (2, '{"name": "Rachel", "sex": 1, "age": 28}'),
                    (3, '{"name": "Joey", "sex": 0, "age": 29}');
                """);
        }

    }

    @AfterAll
    public static void stopDB()
    {
        postgresContainer.stop();
    }

    @Test
    void selectingJsonbData() throws SQLException, IOException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("select data from person where id = 1");

            // JDBC driver returns that as a PGobject. This class is used for all
            // data types, which are not part of the JDBC standard
            Object jsonData = data.getData(0, 0);
            Assertions.assertEquals(PGobject.class, jsonData.getClass());

            // Datatype in PGobject is "jsonb"
            PGobject pGobject = (PGobject) jsonData;
            Assertions.assertEquals("jsonb", pGobject.getType());

            assertJsonEquals(
                "{\"name\": \"Monica\", \"sex\": 1, \"age\": 29}",
                pGobject
            );
        }
    }

    // Operator -> extract the given key value. Te returned value is again a json
    @Test
    void extractingJsonValue() throws SQLException, IOException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select
                -- without the cast the return type in Java would be again Pgobject
                    (data -> 'age')::integer as Age
                from person
                where id = 1
                """
            );

            Object value = data.getData(0, 0);
            Assertions.assertEquals(29, value);
        }
    }

    // operator ->> extract the given value and convert it to text
    @Test
    void extractingJsonValueAsText() throws SQLException, IOException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select
                    data ->> 'age' as Age
                from person
                where id = 1
                """
            );

            Object value = data.getData(0, 0);
            Assertions.assertEquals("29", value);
        }
    }

    @Test
    void extractingFromNestedObject() throws SQLException, IOException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select
                    data -> 'body' ->> 'type' as BodyType
                from person
                where id = 1
                """
            );

            Object value = data.getData(0, 0);
            Assertions.assertEquals("slender", value);
        }
    }

    // Operator -> is NPE safety, you do not check the previous value is NULL or not
    @Test
    void extractingValueAndNULLinTheMiddle() throws SQLException
    {
        // language=SQL
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select data -> 'foo' -> 'bar' -> 'baz'
                from person
                where id = 1
                """
            );
            assertNull(data.getData(0, 0));
        }
    }

    // Operators @> and <@ returns true if the json on the "lesser" side is contained
    // in the other. It is an analogy to common > and < operators.
    @Test
    void operatorContainedId() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select
                    data ->> 'name'
                from person
                where
                    data @> '{"age": 28}'
                """
            );

            Object value = data.getData(0, 0);
            Assertions.assertEquals("Rachel", value);
        }

        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select
                    data ->> 'name'
                from person
                where
                    data @> '{"body": {"type": "slender"}}'
                """
            );

            Object value = data.getData(0, 0);
            Assertions.assertEquals("Monica", value);
        }
    }

    // ? operator - return true, if the key exists
    @Test
    void operatorForKeyComparison() throws SQLException
    {
        try (var db = Database.connect(postgresContainer)) {
            Data data = db.executeSelect("""
                select
                    data ->> 'name'
                from person
                where
                    data ? 'body'
                """
            );

            Object value = data.getData(0, 0);
            Assertions.assertEquals("Monica", value);
        }
    }

    @Test
    void dataManipulation() throws SQLException, IOException
    {
        final int PHOEBOE_ID = 123;

        try (var db = Database.connect(postgresContainer)) {
            db.executeUpdate("""
                INSERT INTO person (id, data) VALUES
                (123, '
                  {
                    "name": "Phoeboe",
                    "age": 31,
                    "body": {
                      "height": 175,
                      "type": "slender"
                    }
                  }
                ')
                """);

            // Update age to 33
            {
                db.executeUpdate("""
                    UPDATE person
                    SET data = jsonb_set(
                      data,
                      '{age}', -- path consists from field names. It is NOT a "jsonpath"
                      '33')
                    WHERE id = 123
                    """);

                Data data = db.executeSelect("select data ->> 'age' from person where id = 123");
                Assertions.assertEquals("33", data.getData(0, 0));
            }

            // Update height to 175
            {
                db.executeUpdate("""
                    UPDATE person
                    SET data = jsonb_set(
                      data,
                      '{body, height}',
                      '171')
                    WHERE id = 123
                    """);

                Data data = db.executeSelect("select data -> 'body' ->> 'height' from person where id = 123");
                Assertions.assertEquals("171", data.getData(0, 0));
            }

            // Add "hair" object
            {
                db.executeUpdate("""
                    UPDATE person
                    SET data = jsonb_set(
                      data,
                      '{body, hair}',
                      '{"color":"yellow", "type": "long"}')
                    WHERE id = 123
                    """);

                Data data = db.executeSelect("""
                    select data from person where id = 123""");
                assertJsonEquals(
                    """
                        {
                          "name": "Phoeboe",
                          "age": 33,
                          "body": {
                            "height": 171,
                            "type": "slender",
                            "hair": {
                              "color": "yellow",
                              "type": "long"
                            }
                          }
                        }
                        """,
                    data.getData(0, 0)
                );
            }
        }
    }

    @Test
    void removingSingleNode() throws SQLException, IOException
    {
        final int PHOEBOE_ID = 123;

        try (var db = Database.connect(postgresContainer)) {
            db.executeUpdate("""
                INSERT INTO person (id, data) VALUES
                (123, '
                  {
                    "name": "Phoeboe",
                    "age": 31,
                    "body": {
                      "height": 175,
                      "type": "slender",
                      "hair": {
                        "colour": "yellow"
                      }
                    }
                  }
                ')
                """);

            // Remove the "body" node
            {
                db.executeUpdate("""
                    UPDATE person
                    SET data = data - 'body';
                    """);

                Data data = db.executeSelect("""
                    select data from person where id = 123""");
                assertJsonEquals(
                    """
                        {
                          "name": "Phoeboe",
                          "age": 31
                        }
                        """,
                    data.getData(0, 0)
                );
            }
        }
    }

    @Test
    void removingMultipleNodes() throws SQLException, IOException
    {
        final int PHOEBOE_ID = 123;

        try (var db = Database.connect(postgresContainer)) {
            db.executeUpdate("""
                INSERT INTO person (id, data) VALUES
                (123, '
                  {
                    "name": "Phoeboe",
                    "age": 31,
                    "body": {
                      "height": 175,
                      "type": "slender",
                      "hair": {
                        "colour": "yellow"
                      }
                    }
                  }
                ')
                """);

            // Remove the "body" node
            {
                db.executeUpdate("""
                    UPDATE person
                    SET data = data - '{body,age}'::text[];
                    """);

                Data data = db.executeSelect("""
                    select data from person where id = 123""");
                assertJsonEquals(
                    """
                        {
                          "name": "Phoeboe"
                        }
                        """,
                    data.getData(0, 0)
                );
            }
        }
    }

    @Test
    void removingNestedNode() throws SQLException, IOException
    {
        final int PHOEBOE_ID = 123;

        try (var db = Database.connect(postgresContainer)) {
            db.executeUpdate("""
                INSERT INTO person (id, data) VALUES
                (123, '
                  {
                    "name": "Phoeboe",
                    "age": 31,
                    "body": {
                      "height": 175,
                      "type": "slender",
                      "hair": {
                        "colour": "yellow"
                      }
                    }
                  }
                ')
                """);

            db.executeUpdate("""
                UPDATE person
                SET data = data #- '{body,hair}';
                """);

            Data data = db.executeSelect("""
                select data from person where id = 123""");
            assertJsonEquals(
                """
                      {
                      "name": "Phoeboe",
                      "age": 31,
                      "body": {
                        "height": 175,
                        "type": "slender"
                      }
                    }
                    """,
                data.getData(0, 0)
            );
        }
    }

    @Test
    void nullabilityCheck() throws SQLException, IOException
    {
        // language=SQL
        try (var db = Database.connect(postgresContainer)) {

            // When the field contains null, sql predicate IS NULL returns false. Because the value is not missing, it contains 'null'::jsonb value
            assertTrue(db.executeSelect("""
                SELECT *
                FROM ( VALUES ('{"name": null}'::jsonb)) AS sample (json_data)
                WHERE json_data -> 'name' IS NULL
                """
            ).isEmpty());

            // When you use ->> operator, then IS NULL works, because 'null'::jsonb is converted to NULL
            assertFalse(db.executeSelect("""
                SELECT *
                FROM ( VALUES ('{"name": null}'::jsonb)) AS sample (json_data)
                WHERE json_data ->> 'name' IS NULL
                """
            ).isEmpty());

            // You can use -> operator, when the field does notWhen you use ->> operator, then IS NULL works, because 'null'::jsonb is converted to NULL
            assertFalse(db.executeSelect("""
                SELECT *
                FROM ( VALUES ('{"name": null}'::jsonb)) AS sample (json_data)
                WHERE json_data -> 'nonExistingField' IS NULL
                """
            ).isEmpty());
        }
    }

    private void assertJsonEquals(String expected, Object pgObject) throws IOException
    {
        if (!(pgObject instanceof PGobject)) {
            Assertions.fail("The actual object is not Pgobject instance.");
        }

        String actualJsonValue = ((PGobject) pgObject).getValue();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode expectedJson = objectMapper.readTree(expected);
        JsonNode actualJson = objectMapper.readTree(actualJsonValue);
        Assertions.assertEquals(expectedJson, actualJson);
    }

}
