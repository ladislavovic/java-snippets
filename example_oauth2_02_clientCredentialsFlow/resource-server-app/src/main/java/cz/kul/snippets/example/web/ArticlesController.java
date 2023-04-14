package cz.kul.snippets.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Enumeration;
import java.util.stream.Collectors;

@RestController
public class ArticlesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticlesController.class);

    @GetMapping("/booking-calendar")
    public String[] getBookingCalendar(HttpServletRequest request, Principal principal) {

        if (principal == null) {
            LOGGER.info("Principal: null");
        } else {

            // info about authorization header
            Enumeration<String> authorization = request.getHeaders("Authorization");
            while (authorization.hasMoreElements()) {
                String headerValue = authorization.nextElement();
                LOGGER.info("Authorization header value: " + headerValue);
            }

            // info about JWT token
            JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
            LOGGER.info("JWT token properties:\n" +
                "  name: " + token.getName() + "\n" +
                "  credentials: " + token.getCredentials() + "\n" +
                "  details: " + token.getDetails() + "\n" +
                "  attributes: " + token.getTokenAttributes() + "\n" +
                "  authorities: " + token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            LOGGER.info("Spring Security Context Authorities: " + authentication.getAuthorities());
        }

        return new String[]{"1.12.:booked", "2.12.:booked", "3.12.:free"};
    }

    @GetMapping("/update-booking-calendar")
    public void updateBookingCalendar() {
        // not implemented
    }

}