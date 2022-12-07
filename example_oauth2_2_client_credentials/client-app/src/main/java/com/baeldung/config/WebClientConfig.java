package com.baeldung.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${my.security.oauth2.client.provider.spring.token-uri}")
    private String tokenUri;

    @Value("${my.security.oauth2.client.registration.articles-client-authorization-code.client-id}")
    private String clientId;

    @Value("${my.security.oauth2.client.registration.articles-client-authorization-code.client-secret}")
    private String clientSecret;

    @Value("${my.security.oauth2.client.registration.articles-client-authorization-code.scope}")
    private String scope;

    @Value("${my.security.oauth2.client.registration.articles-client-authorization-code.authorization-grant-type}")
    private String authorizationGrantType;

    @Bean
    ReactiveClientRegistrationRepository myRepo() {
        ClientRegistration registration = ClientRegistration
            .withRegistrationId("articles-client-authorization-code")
            .tokenUri(tokenUri)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .scope(scope)
            .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
            .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(@Qualifier("myRepo") ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("articles-client-authorization-code");
        return WebClient.builder()
            .filter(oauth)
            .build();
    }

//    @Bean
//    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
//          new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//        return WebClient.builder()
//          .apply(oauth2Client.oauth2Configuration())
//          .build();
//    }
//
//    @Bean
//    OAuth2AuthorizedClientManager authorizedClientManager(
//        ClientRegistrationRepository clientRegistrationRepository,
//        OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider =
//          OAuth2AuthorizedClientProviderBuilder.builder()
//            .authorizationCode()
//            .refreshToken()
//            .build();
//        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
//          clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }
}