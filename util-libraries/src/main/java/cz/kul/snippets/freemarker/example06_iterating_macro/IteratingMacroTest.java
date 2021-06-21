package cz.kul.snippets.freemarker.example06_iterating_macro;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IteratingMacroTest {

    @Test
    public void test() {
        String tpl =
            "" +
                "<#macro iter_terms terms>" +
                "<#list terms as t>" +
                "<#nested t><#sep>, </#sep>" +
                "</#list>" +
                "</#macro>" +
                "" +
                "<@iter_terms terms ; term>${term}</@iter_terms>";

        String res = FreemarkerUtils.processStrTpl(
            tpl,
            ImmutableMap.of("terms", Lists.newArrayList("term1", "term2")));
        Assert.assertEquals("term1, term2", res);
    }
    

    
    

}
