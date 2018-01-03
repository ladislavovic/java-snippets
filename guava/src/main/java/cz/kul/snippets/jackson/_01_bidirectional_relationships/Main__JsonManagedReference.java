package cz.kul.snippets.jackson._01_bidirectional_relationships;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * It is one of the possible ways, how to avoid cycle during serialization to json
 * </p>
 * 
 * <p>
 * Both sides are marked by annotation. One side is serialized and another is not. So the
 * cycle is broken
 * </p>
 * 
 * <p>
 * In serialization it is identical to jsonIgnore annotation. But the difference is during
 * deserialization. The field is set but with jsonIgnore the field is ignored and null is
 * set there
 * </p>
 * *
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main__JsonManagedReference {

    public static void main(String[] args) throws JsonProcessingException {
        A a = new A("objectA");
        B b = new B("objectB");
        a.b = b;
        b.a = a;

        String aSerialization = new ObjectMapper().writeValueAsString(a);
        String bSerialization = new ObjectMapper().writeValueAsString(b);

        assertTrue(aSerialization.contains("objectB"));
        assertFalse(bSerialization.contains("objectA"));

    }

    public static class A {
        public String nameA;

        @JsonManagedReference
        public B b;

        private A(String nameA) {
            super();
            this.nameA = nameA;
        }
    }

    public static class B {
        public String nameB;

        @JsonBackReference
        public A a;

        private B(String nameB) {
            super();
            this.nameB = nameB;
        }
    }

}
