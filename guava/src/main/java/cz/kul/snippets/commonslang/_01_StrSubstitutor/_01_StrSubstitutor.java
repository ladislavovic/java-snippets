package cz.kul.snippets.commonslang._01_StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * You can replace named parameters by this util class.
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
public class _01_StrSubstitutor {

    public static void main(String[] args) {
        
        // Basic usage
        {
            Map<String, Object> values = new HashMap<>();
            values.put("name", "Jane");
            values.put("age", 25);
            values.put("birthYear", 1990);
            String res = StrSubstitutor.replace("The ${name} is ${age} and was born in ${birthYear}.", values);
            System.out.println(res);
        }
        
        // You can use one parameter more times in the string.
        {     
            Map<String, Object> values = new HashMap<>();
            values.put("name", "Jane");
            String res = StrSubstitutor.replace("${name} ${name} ${name}", values);
            System.out.println(res);
        }
        
        // You can replace StringBuilder, you does not have to convert to string
        {     
            Map<String, Object> values = new HashMap<>();
            values.put("instance", "StringBuilder");
            StringBuilder sb = new StringBuilder().append("You can directly replace ${instance}, you does not have to convert to String.");
            String res = StrSubstitutor.replace(sb, values);
            System.out.println(res);
        }

    }

}
