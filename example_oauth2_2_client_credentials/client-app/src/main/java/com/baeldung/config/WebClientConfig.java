package com.baeldung.config;

import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class WebClientConfig {

    private static final Logger WC_LOGGER = LoggerFactory.getLogger("WEB_CLIENT");

    @Value("${client-app.token-uri}")
    private String tokenUri;

    @Value("${client-app.client-id}")
    private String clientId;

    @Value("${client-app.client-secret}")
    private String clientSecret;

//    @Value("${my.security.oauth2.client.registration.articles-client-authorization-code.scope}")
//    private String scope;

    @Bean
    ReactiveClientRegistrationRepository myRepo() {
        ClientRegistration registration = ClientRegistration
            .withRegistrationId("client-registration-id")
            .tokenUri(tokenUri)
            .clientId(clientId)
            .clientSecret(clientSecret)
//            .scope(scope)
            .authorizationGrantType(new AuthorizationGrantType("client_credentials"))
            .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(@Qualifier("myRepo") ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
        var oauthFilter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauthFilter.setDefaultClientRegistrationId("client-registration-id"); // TODO

        HttpClient httpClient = HttpClient
            .create();

        return WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .filter(oauthFilter)
            .build();
    }

}