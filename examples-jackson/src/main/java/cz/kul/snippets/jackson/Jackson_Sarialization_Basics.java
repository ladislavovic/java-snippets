package cz.kul.snippets.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class Jackson_Sarialization_Basics
{

    @Nested
    class PublicFields {

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class Person {
            public String name = "Monica";
            private String secondName = "not-set";
        }

        @Test
        public void publicFieldIsSerializable() throws JsonProcessingException
        {
            ObjectMapper mapper = new ObjectMapper();
            assertThat(mapper.writeValueAsString(new Person()))
                .isEqualTo("{\"name\":\"Monica\"}");
        }

        @Test
        public void publicFieldIsDeserializable() throws JsonProcessingException
        {
            ObjectMapper mapper = new ObjectMapper();
            Person person = mapper.readValue("{\"name\":\"Monica\", \"secondName\":\"Geller\"}", Person.class);
            assertThat(person.name).isEqualTo("Monica");
            assertThat(person.secondName).isEqualTo("not-set");
        }

    }

    @Nested
    class PublicGetters
    {

        private static class Person {
            private String name;

            public String getName()
            {
                return name;
            }

        }

        @Test
        public void publicGetterIsSerializable() throws JsonProcessingException
        {
            ObjectMapper mapper = new ObjectMapper();
            Person person = new Person();
            person.name = "Monica";
            assertThat(mapper.writeValueAsString(person))
                .isEqualTo("{\"name\":\"Monica\"}");
        }

        @Test
        public void publicGetterIsDeserializable() throws JsonProcessingException
        {
            // It must set the value by reflection

            ObjectMapper mapper = new ObjectMapper();
            PublicGetters.Person person = mapper.readValue("{\"name\":\"Monica\"}", PublicGetters.Person.class);
            assertThat(person.getName()).isEqualTo("Monica");
        }

    }

    // todo getter without field
    // todo setter
    @Nested
    class TODO
    {

        private static class Person {
            private String name;

            public String getName()
            {
                return name;
            }

        }

        @Test
        public void TODOtest() throws JsonProcessingException
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.DEFAULT)

            Person person = new Person();
            person.name = "Monica";
            assertThat(mapper.writeValueAsString(person))
                .isEqualTo("{\"name\":\"Monica\"}");
        }

    }



}
