package cz.kul.snippets.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;


public class Jackson_GettingsStarted_Basics
{

    /*

    Which data type Jackson use for numbers?

    writeValueAsString
    readTree
    readValue


     */

    @Test
    public void stringToMap() throws IOException
    {
        String json = """
            {
              "name" : "Monica",
              "age" : 29,
              "height" : 1.72
            }
            """;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(json, Map.class);

        Assertions.assertEquals(
            Map.of("name", "Monica", "age", 29, "height", 1.72),
            map
        );
    }

    @Test
    public void stringToObject() throws IOException
    {
        String json = """
            {
              "name" : "Monica",
              "age" : 29,
              "height" : 1.72
            }
            """;
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(json, Person.class);
        Assertions.assertEquals(new Person("Monica", 29, 1.72), person);
    }

    @Test
    public void jsonNodeBasics() throws IOException
    {
        String json = """
            {
              "name" : "Monica",
              "age" : 29,
              "height" : 1.72,
              "email" : null
            }
            """;
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert String to JsonNode by readTree()
        JsonNode rootNode = objectMapper.readTree(json);

        // This node is ObjectNode
        Assertions.assertEquals(ObjectNode.class, rootNode.getClass());

        // You can get sub nodes
        JsonNode ageNode = rootNode.get("age");
        Assertions.assertEquals(IntNode.class, ageNode.getClass());

        // JsonNode is immutable. But you can add/remove node from ObjectNode
        ObjectNode rootObjectNode = (ObjectNode) rootNode;
        rootObjectNode.remove("email");
        rootObjectNode.set("email", TextNode.valueOf("monica@gmail.com"));
    }

    record Person(String name, int age, double height) {

    }

}
