package cz.kul.snippets.spring._07_common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

public class Main_SpringCommon {

    public static void main(String[] args) {
        antPathMatcher();
    }

    private static void antPathMatcher() {
        // NOTE: Origin of this matcher is Ant, Spring borrowed it.
        //       It introduced double asterisk path matching.
        AntPathMatcher matcher = new AntPathMatcher();
        assertFalse(matcher.match("/foo/*", "/foo/bar/baz"));
        assertTrue(matcher.match("/foo/**", "/foo/bar/baz"));
    }
    
}
