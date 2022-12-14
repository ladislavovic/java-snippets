package com.baeldung.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class ArticlesController {

    @GetMapping("/articles")
    public String[] getArticles(Principal principal) {
        // TODO return principal name here

        if (principal != null) {

            JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
            System.out.println("token name: " + token.getName());
            System.out.println("credentials: " + token.getCredentials());
            System.out.println("details: " + token.getDetails());
            System.out.println("attributes: ");
            for (Map.Entry<String, Object> entry : token.getTokenAttributes().entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("authorities: ");
            for (GrantedAuthority authority : token.getAuthorities()) {
                System.out.println("  " + authority.getAuthority());
            }
        }

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println("\n\nAuthentication: " + authentication);
        System.out.println("Authorities: " + authentication.getAuthorities());

        return new String[]{"Article 1", "Article 2", "Article 3"};
    }
}