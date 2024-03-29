package cz.kul.snippets.springboot.restapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HelloControllerMockitoTest {

	@Mock
	private HelloService helloService;

	@InjectMocks
	private HelloController helloController;

	@BeforeEach
	void setMockOutput() {
		when(helloService.getWelcomeMessage()).thenReturn("Hello Mockito Test");
	}

	@Test
	public void shouldReturnDefaultMessage() {
		String response = helloController.hello();
		assertThat(response).isEqualTo("Hello Mockito Test");
	}

}
