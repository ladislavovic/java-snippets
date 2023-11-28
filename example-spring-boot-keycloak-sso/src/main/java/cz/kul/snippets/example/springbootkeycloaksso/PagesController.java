package cz.kul.snippets.example.springbootkeycloaksso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Map;

@Controller
public class PagesController {

	@Autowired
	OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	@GetMapping(value = {"/", ""})
	public String home(Model model) {
		model.addAttribute("attr", "foo");
		return "home";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws Exception {
		request.logout();
		return "redirect:/";
	}

	@GetMapping("/secured")
	public String secured(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

		System.out.println("Authentication class: " + authentication.getClass().getName());

		OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
				oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName());

		String accessToken = client.getAccessToken().getTokenValue();
		System.out.println("Access token: " + accessToken);


		// Try to refresh
		OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().refreshToken().build();
		OAuth2AuthorizationContext ctx = OAuth2AuthorizationContext
				.withAuthorizedClient(client)
				.principal(authentication)
				.build();
		OAuth2AuthorizedClient newClient = provider.authorize(ctx);
		if (newClient != null) {
			oAuth2AuthorizedClientService.saveAuthorizedClient(newClient, authentication);
			String newToken = newClient.getAccessToken().getTokenValue();
			System.out.println("NEW\n" + newToken);
		} else {
			System.out.println("NEW client is null");
		}


		Instant tokenExp = getTokenExp(accessToken);
		model.addAttribute("token", accessToken);
		model.addAttribute("exp", LocalDateTime.ofInstant(tokenExp, ZoneOffset.UTC));
		model.addAttribute("valid", tokenExp.isAfter(Instant.now()));
		return "secured";
	}


	private Instant getTokenExp(String jwtToken) {
		try {
			String[] parts = jwtToken.split("\\.");
			byte[] partBytes = Base64.getDecoder().decode(parts[1]);
			String partStr = new String(partBytes);
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(partStr, Map.class);
			long exp = ((Number) map.get("exp")).longValue();
			return Instant.ofEpochSecond(exp);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}






}
