package cz.kul.snippets.springresttesting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContextConfiguration.class})
@WebAppConfiguration
public class ApiFileGeneratorTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void generateApiFile() throws Exception {
		String outputFileStr = System.getProperty("api.generated.file");
		Assert.assertNotNull(
				"Can not get output file for api documentation. You must set property api.generated.file",
				outputFileStr);
		File outputFile = new File(outputFileStr);
		File outputDir = outputFile.getParentFile();
		if (outputDir != null && !outputDir.exists()) {
			Files.createDirectories(Paths.get(outputDir.getAbsolutePath()));
		}

		MvcResult mvcResult = this.mockMvc.perform(get("/v2/api-docs")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		String swaggerJson = response.getContentAsString();
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileStr), StandardCharsets.UTF_8)){
			writer.write(swaggerJson);
		}
	}


}
