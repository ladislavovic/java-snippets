package com.baeldung.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {

    @GetMapping("/articles")
    public String[] getArticles() {
        // TODO return principal name here

        return new String[]{"Article 1", "Article 2", "Article 3"};
    }
}