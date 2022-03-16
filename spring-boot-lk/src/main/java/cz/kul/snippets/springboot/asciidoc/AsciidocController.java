package cz.kul.snippets.springboot.asciidoc;

import cz.kul.snippets.springboot.restapi.HelloService;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AsciidocController {

	@Autowired
	private HelloService helloService;

	@GetMapping("/asciidoc")
	public String hello() {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		Options options = new Options();
		options.setInPlace(false);
		options.setBackend("pdf");
		options.setSafe(SafeMode.SAFE);
		options.setToStream(outputStream);

		asciidoctor.convertFile(new File(baseOutputDir + "/index.adoc"), options);


		return helloService.getWelcomeMessage();
	}
}
