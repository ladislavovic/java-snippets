package cz.kul.snippets.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class Jackson_NullCharacter
{

    /*

     * Quotation mark, reverse solidus, and the control characters (U+0000 through U+001F) MUST be escaped.
     * Main idea of control characters escaping is to don't damage output when printing JSON document on terminal or paper
     * Null character (ux0000) is a valid character in JSON (but must be escaped)

     */

    @Test
    public void itIsAValidJSONCharacter() throws IOException
    {
        String json = """
            { "name" : "\\u0000Monica" }
            """;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(json, Map.class);

        Assertions.assertEquals(map.get("name"), "\u0000Monica");
    }

    @Test
    public void controlCharactersMustBeEscaped() throws IOException
    {
        String json = """
            { "name" : "\u0000Monica" }
            """;
        ObjectMapper objectMapper = new ObjectMapper();

        assertThatThrownBy(() -> objectMapper.readValue(json, Map.class))
            .isInstanceOf(JsonParseException.class)
            .hasMessageContaining("Illegal unquoted character ((CTRL-CHAR, code 0)): has to be escaped using backslash to be included in string value");
    }

    @Test
    public void youCanForceJsonToParseJsonLikeStringWithNotEscapedControlCharacters() throws IOException
    {
        String json = """
            { "name" : "\u0000Monica" }
            """;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        Map<String, Object> map = objectMapper.readValue(json, Map.class);

        Assertions.assertEquals(map.get("name"), "\u0000Monica");
    }

    @Test
    public void removingItByRegex() throws IOException
    {
        // This solution is not perfect, but it can be used in practise:
        //   \u0000      - OK, replace
        //   \\u0000     - OK, do not replace
        //   \\\u0000    - FAIL, should replace but do not. In general any odd number of \ does not work correctly. But it is really an edge case
        //
        // This pattern means "find all \u0000 which dos not have \ before it"
        // A good explanation of these lookahead/lookbehind groups: https://stackoverflow.com/a/2973495/2934191
        Pattern pattern = Pattern.compile("(?<!\\\\)\\\\u0000");

        String json = """
            { "name" : "the first will be replaced, the others not: \\u0000, \\\\u0000, \\\\\\u0000" }
            """;

        String jsonReplaced = pattern.matcher(json).replaceAll("NULL");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(jsonReplaced, Map.class);

        Assertions.assertEquals(
            "the first will be replaced, the others not: NULL, \\u0000, \\\u0000",
            map.get("name"));
    }


    @Test
    public void bbb() throws Exception {

        String str = "\\\\\\u0000ABC";

        String regex = "(?<!\\\\)\\\\u0000"; // (?<!\\)\\u0000              //   (?<!\)\u0000

        String replaced = str.replaceAll(regex, "");

        System.out.println(replaced);
        System.out.println(replaced.length());
    }

}
