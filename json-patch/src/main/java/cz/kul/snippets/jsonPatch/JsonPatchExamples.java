package cz.kul.snippets.jsonPatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonPatchExamples
{

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void jsonPatchCreatingByDiffOperation() throws IOException
    {

        var source = """
            {
              "foo": "a",
              "bar": "b",
              "baz": "c"
            }
            """;

        var target = """
            {
              "foo": "a",
              "bar": "bb",
              "aaa": "d"
            }
            """;

        var expectedJsonPatch = """
            [ 
              {
                "op" : "remove",
                "path" : "/baz"
              },
              {
                "op" : "add",
                "path" : "/aaa",
                "value" : "d"
              },
              {
                "op" : "replace",
                "path" : "/bar",
                "value" : "bb"
              }
            ]
            """;

        final JsonNode thePatch = JsonDiff.asJson(mapper.readTree(source), mapper.readTree(target));
        assertThat(thePatch).isEqualTo(mapper.readTree(expectedJsonPatch));
    }

    @Test
    public void aaa() throws IOException
    {

        var source = """
            {
              "foo": "a",
              "bar": "b",
              "baz": "c"
            }
            """;

        var target = """
            {
              "foo": "a",
              "bar": "bb",
              "aaa": "d"
            }
            """;

        var expectedJsonPatch = """
            [ 
              {
                "op" : "remove",
                "path" : "/baz"
              },
              {
                "op" : "add",
                "path" : "/aaa",
                "value" : "d"
              },
              {
                "op" : "replace",
                "path" : "/bar",
                "value" : "bb"
              }
            ]
            """;


        JsonDiff.

        final JsonNode thePatch = JsonDiff.asJson(mapper.readTree(source), mapper.readTree(target));
        assertThat(thePatch).isEqualTo(mapper.readTree(expectedJsonPatch));
    }


}
