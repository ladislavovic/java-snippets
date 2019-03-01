package cz.kul.snippets.jackson.example01_bidirectional_relationships;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
public class MainIdentityInfo {

    public static void main(String[] args) throws JsonProcessingException {
        A a = new A();
        B b = new B("objectB");
        a.b1 = b;
        a.b2 = b;

        String json = new ObjectMapper().writeValueAsString(a);
         System.out.println(json);
//        assertEquals(1, count(json, "nameB"));
//        assertEquals(1, count(json, "nameC"));
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

    @JsonTypeName("AAA")
    public static class A {
        B b1;
        B b2;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nameB")
    public static class B {
        String nameB;

        B(String nameB) {
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
