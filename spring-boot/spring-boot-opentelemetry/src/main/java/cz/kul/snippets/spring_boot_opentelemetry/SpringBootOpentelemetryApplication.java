package cz.kul.snippets.spring_boot_opentelemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootOpentelemetryApplication
{

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringBootOpentelemetryApplication.class);
		app.run(args);
	}

}
