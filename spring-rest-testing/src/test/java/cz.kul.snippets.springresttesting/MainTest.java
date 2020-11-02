package cz.kul.snippets.springresttesting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContextConfiguration.class})
@WebAppConfiguration
public class MainTest {

	@Autowired
	private WebApplicationContext wac;

	@Test
	public void test1() throws Exception {

		System.out.println("############## Context: " + wac.getId());

		ServletContext servletContext = wac.getServletContext();

		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("personController"));

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		MvcResult mvcResult = mockMvc
				.perform(get("/person"))
				.andDo(print())
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Assert.assertTrue(contentAsString.contains("Monica"));
	}

	@Test
	public void test2() throws Exception {

		System.out.println("############## Context: " + wac.getId());


		ServletContext servletContext = wac.getServletContext();

		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("personController"));

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		MvcResult mvcResult = mockMvc
				.perform(get("/person"))
				.andDo(print())
				.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		Assert.assertTrue(contentAsString.contains("Rachel"));
	}


}
