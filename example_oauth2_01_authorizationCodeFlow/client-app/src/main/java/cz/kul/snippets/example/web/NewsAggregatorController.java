package cz.kul.snippets.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class NewsAggregatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsAggregatorController.class);

    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/news-aggregator")
    public String getArticles(
      @RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    ) {
        LOGGER.debug("authorized client: " + authorizedClient);

        String[] articles = this.webClient
                .get()
                .uri("http://127.0.0.1:8882/articles")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        LOGGER.debug("Articles from the resource server: " + articles);

        return Arrays.stream(articles).collect(Collectors.joining(", "));
    }
}