//package cz.kul.snippets.gatewayapi1;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Collections;
//
//
// TODO this is a Security config, which can use Oauth2 and also Basic authentication. Create another example for that.

//@Configuration
//public class SecurityConfig {
//
//  @Bean
//  @Order(3)
//   SecurityFilterChain apiOauth2FilterChain(HttpSecurity http) throws Exception {
//    return http
//        .authorizeRequests().anyRequest().authenticated()
//        .and()
//        .csrf().disable()
//        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//        .build();
//  }
//
//  @Bean
//  @Order(2)
//  SecurityFilterChain apiBaseAuthFilterChain(HttpSecurity http) throws Exception {
//
//    return http
//        .anonymous().disable()
//        .requestMatcher(new BasicRequestMatcher())
//        .authorizeRequests()
//        .anyRequest().authenticated()
//        .and()
//        .httpBasic()
//        .and()
//        .authenticationProvider(new DemoAuthenticationProvider())
//        .build();
//  }
//
//  private static class BasicRequestMatcher implements RequestMatcher {
//
//    @Override
//    public boolean matches(HttpServletRequest request) {
//      String auth = request.getHeader("Authorization");
//      return (auth != null && auth.startsWith("Basic"));
//    }
//  }
//
//  private static class DemoAuthenticationProvider implements AuthenticationProvider {
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//      String authUsername = String.valueOf(authentication.getPrincipal());
//      String authPassword = String.valueOf(authentication.getCredentials());
//
//      if (!"user2".equalsIgnoreCase(authUsername)) {
//        throw new UsernameNotFoundException("Can not authenticate, username not found");
//      }
//
//      if (!"pswd".equalsIgnoreCase(authPassword)) {
//        throw new BadCredentialsException("Can not authenticate, wrong password");
//      }
//
//      UserDetailsImpl userDetails = new UserDetailsImpl(authUsername, Collections.emptyList());
//      return new UsernamePasswordAuthenticationToken(
//          userDetails,
//          null,
//          Collections.emptyList());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//      return true;
//    }
//  }
//
//}