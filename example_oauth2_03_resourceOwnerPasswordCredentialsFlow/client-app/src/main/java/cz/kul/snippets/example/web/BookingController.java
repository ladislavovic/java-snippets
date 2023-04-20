package cz.kul.snippets.example.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class BookingController {

    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/calendar")
    public String[] getCalendar() {
        return this.webClient
          .get()
          .uri("http://resource-server:8882/booking-calendar")
//          .attributes(oauth2AuthorizedClient(authorizedClient))
          .retrieve()
          .bodyToMono(String[].class)
          .block();
    }

    @GetMapping(value = "/book")
    public String[] book() {
        return this.webClient
            .get()
            .uri("http://resource-server:8882/update-booking-calendar")
            .retrieve()
            .bodyToMono(String[].class)
            .block();
    }

}