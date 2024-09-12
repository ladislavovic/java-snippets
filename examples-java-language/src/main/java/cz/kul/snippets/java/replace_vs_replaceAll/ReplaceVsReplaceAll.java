package cz.kul.snippets.java.replace_vs_replaceAll;

import org.junit.Assert;
import org.junit.Test;

public class ReplaceVsReplaceAll
{

    @Test
    public void replaceBackslash() {

        // You have to use 4 backslashes in regex, because you have to escape twice: Once for Java, once for the regex.
        // Because backslash is not allowed in regex, you have to also escape it by \\
        Assert.assertEquals(
            "/foo/bar",
            "\\foo\\bar".replaceAll("\\\\", "/"));

        // Use replace() method if you do not need regex. It also replace all occurences even though it names only "replace"
        Assert.assertEquals(
            "/foo/bar",
            "\\foo\\bar".replace("\\", "/"));
    }

}
