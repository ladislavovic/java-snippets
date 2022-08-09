package cz.kul.snippets.spring.example_26_configFileReloading;

import org.hsqldb.lib.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Configuration
public class ContextConfiguration {

	@Bean
	public PetsCfg petsCfg() {
		PetsCfg ret = doPetsCfg();
		FileChangeListener.createAndListen("/tmp/pets.txt", x -> {
			PetsCfg newCfg = doPetsCfg();
			ret.setPets(newCfg.getPets());
			System.out.println("Pets reloaded: " + ret.getPets());
		});
		return ret;
	}

	private PetsCfg doPetsCfg() {
		try {
			PetsCfg res = new PetsCfg();
			final String resourceLocation = "file:/tmp/pets.txt";
			DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource(resourceLocation);
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
				String line;
				while (!StringUtil.isEmpty(line = reader.readLine())) {
					String[] parts = line.split(":");
					PetCfg petCfg = new PetCfg();
					petCfg.setName(parts[0]);
					petCfg.setType(parts[1]);
					res.getPets().add(petCfg);
				}
			}
			return res;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
