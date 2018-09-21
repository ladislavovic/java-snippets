package cz.kul.snippets.java._25_regex;

import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex extends SnippetsTest {



    @Test
    public void prepareFromPart() {
    //		List<FromPart> parse = sqlAnalyzer.parse("select suba0_1_.valA as col_0_0_ from SubAEntityT suba0_ inner join AEntityT suba0_1_ on suba0_.id=suba0_1_.id" );

        // get from part from the select
        String sqlString = "select suba0_1_.valA as col_0_0_ from SubAEntityT suba0_ inner join AEntityT suba0_1_ on suba0_.id=suba0_1_.id where aaa=bbb";
//        Pattern pattern = Pattern.compile( ".*from\\s+(.*)\\s+(where|order|having|\\z)" );
        Pattern pattern = Pattern.compile( ".*\\s+from\\s+(.*?)(\\z|(\\s+(where|order|having).*))" );
        Matcher matcher = pattern.matcher( sqlString );
        if (matcher.matches() ) {
            System.out.println("|" + matcher.group( 1 ) + "|");
        }
    }

    @Test
    public void getFirstTable() {
        String from  = "SubAEntityT suba0_ inner join AEntityT suba0_1_ on suba0_.id=suba0_1_.id";
        Pattern pattern = Pattern.compile( "(\\S+)\\s+(\\S*)\\s*(.*)" );
        Matcher matcher = pattern.matcher( from );
        if (matcher.matches()) {
            System.out.println("|" + matcher.group(1) + "|");
            System.out.println("|" + matcher.group(2) + "|");
            System.out.println("|" + matcher.group(3) + "|");
        }
    }

    @Test
    public void getJoins() {
        String from  = "inner join AEntityT suba0_1_ on suba0_.id=suba0_1_.id join TTT aaa";
        Pattern pattern = Pattern.compile( "(?<jointype>join|inner join|left join|left outer join)\\s+(?<table>\\S+)\\s+(?<alias>\\S+)" );
        Matcher matcher = pattern.matcher( from );
        while (matcher.find()) {
            System.out.println("|" + matcher.group("jointype") + "|");
            System.out.println("|" + matcher.group("table") + "|");
            System.out.println("|" + matcher.group("alias") + "|");
        }
    }

    @Test
    public void test1() {
    //		List<FromPart> parse = sqlAnalyzer.parse("select suba0_1_.valA as col_0_0_ from SubAEntityT suba0_ inner join AEntityT suba0_1_ on suba0_.id=suba0_1_.id" );

        // get from part from the select
        String sqlString = "ahoj end";
//        String sqlString = "ahoj";
        Pattern pattern = Pattern.compile( "(.*?)(\\z|end)" );
        Matcher matcher = pattern.matcher( sqlString );
        if (matcher.matches() ) {
            System.out.println("|" + matcher.group( 1 ) + "|");
        }
    }



    @Test
    public void testNonCapturing() {
//        assertNotMatches("((?!prefix).)*STR", "prefixSTR");
//        assertMatches("((?!prefix).)*STR", "aaaSTR");
    }



}
