package cz.kul.snippets.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Creates {@link WebClient} instance, which authenticate by OAuth2.
 */
@Configuration
public class WebClientConfig {

    @Value("${client-app.token-uri}")
    private String tokenUri;

    @Value("${client-app.client-id}")
    private String clientId;

    @Value("${client-app.client-secret}")
    private String clientSecret;

    @Bean
    ReactiveClientRegistrationRepository myRepo() {
        ClientRegistration registration = ClientRegistration
            .withRegistrationId("client-registration-id")
            .tokenUri(tokenUri)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .authorizationGrantType(new AuthorizationGrantType("client_credentials"))
            .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean
    WebClient webClient(@Qualifier("myRepo") ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
        var authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
        var oauthFilter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauthFilter.setDefaultClientRegistrationId("client-registration-id");

        HttpClient httpClient = HttpClient
            .create();

        return WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .filter(oauthFilter)
            .build();
    }

}