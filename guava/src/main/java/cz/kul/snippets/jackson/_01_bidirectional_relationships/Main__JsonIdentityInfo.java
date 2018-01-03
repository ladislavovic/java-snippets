package cz.kul.snippets.jackson._01_bidirectional_relationships;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * With @JsonIdentityInfo annotation you tell json how to identify an instance. During
 * serialization it is write out once. Instead of repeating one instance more times it
 * writes the identifier only
 * </p>
 * 
 * <p>
 * It is also useful to manage bidirectional relations, because it also breaks the cycle.
 * </p>
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main__JsonIdentityInfo {

    public static void main(String[] args) throws JsonProcessingException {
        A a = new A();
        B b = new B("objectB");
        C c = new C("objectC");
        a.b1 = b;
        a.b2 = b;
        a.c1 = c;
        a.c2 = c;

        String json = new ObjectMapper().writeValueAsString(a);
        // System.out.println(json);
        assertEquals(1, count(json, "nameB"));
        assertEquals(1, count(json, "nameC"));
    }

    private static int count(String str, String regex) {
        int result = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            result++;
        }
        return result;
    }

    public static class A {
        public B b1;
        public B b2;
        public C c1;
        public C c2;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nameB")
    public static class B {
        public String nameB;

        private B(String nameB) {
            super();
            this.nameB = nameB;
        }
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
    public static class C {
        public String nameC;

        private C(String nameC) {
            this.nameC = nameC;
        }
    }

}
