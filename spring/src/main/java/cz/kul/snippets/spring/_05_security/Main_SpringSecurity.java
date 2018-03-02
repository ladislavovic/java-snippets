package cz.kul.snippets.spring._05_security;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This snippet could be described better. Describe objects and principes when I need to
 * work with this domain.
 * 
 * Authentication - this class is for two purposes: 1. Authentication request (contains
 * credentials) 2. Authenticaded principal (when authentication is complete)
 * 
 * AuthenticationManager - Attempts to authenticate the passed {@link Authentication} object, 
 * returning a fully populated <code>Authentication</code> object (including granted authorities)
 * if successful.
 * 
 * ProviderManager - Implementation of AuthenticationManager. It contains list of AuthenticationProvider
 * and authenticate via them.
 * 
 * AuthenticationProvider - can process specific authentication process. For example for
 * LDAP, for another LDAP, DB, ... There can be more providers in one application.
 * 
 * UsernamePasswordAuthenticationToken RememberMeAuthenticationToken
 * PreAuthenticatedAuthenticationToken
 */
public class Main_SpringSecurity {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("_05_spring.xml")) {
            authenticate();
            logRoles();
            FooService s = (FooService) context.getBean("fooServiceImpl");
            System.out.println(s.getMessage());
        }
    }
    
    private static void authenticate() {
        AuthenticationManager am = new SampleAuthenticationManager();
        try {
            Authentication request = new UsernamePasswordAuthenticationToken("kul", "kul");
            Authentication result = am.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            System.out.println("Successfully authenticated. Security context contains: "
                    + SecurityContextHolder.getContext().getAuthentication());
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
        }
    }
    
    private static void logRoles() {
        System.out.println("\nRoles:");
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority ga : a.getAuthorities()) {
            System.out.println(ga.getAuthority());
        }
        System.out.println("");
    }

}
