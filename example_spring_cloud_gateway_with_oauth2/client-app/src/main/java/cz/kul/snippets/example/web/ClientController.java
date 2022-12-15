package cz.kul.snippets.example.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ClientController {

	@Autowired
	private WebClient webClient;

	@GetMapping(value = "/call-gateway", produces = "application/json")
	public String callGateway() {
		return this.webClient
				.get()
				.uri("http://gateway:8883/api1/operation1")
//          .attributes(oauth2AuthorizedClient(authorizedClient))
				.retrieve()
				.bodyToMono(String.class)
				.block();
	}

}