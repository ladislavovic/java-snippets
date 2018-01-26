package cz.kul.snippets.springmvc._XX;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
 

import java.util.Arrays;
 

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
@ContextConfiguration(classes = {TestContext.class})
@WebAppConfiguration
public class TodoControllerTest {
 
    private MockMvc mockMvc;
 
    @Autowired
    private TodoService todoServiceMock;
 
    //Add WebApplicationContext field here
 
    //The setUp() method is omitted.
 
    @Test
    public void findAll_ShouldAddTodoEntriesToModelAndRenderTodoListView() throws Exception {
        Todo first = new TodoBuilder()
                .id(1L)
                .title("Foo")
                .build();
 
        Todo second = new TodoBuilder()
                .id(2L)
                .title("Bar")
                .build();
 
        when(todoServiceMock.findAll()).thenReturn(Arrays.asList(first, second));
 
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/list"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/todo/list.jsp"))
                .andExpect(model().attribute("todos", hasSize(2)))
                .andExpect(model().attribute("todos", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Foo"))
                        )
                )))
                .andExpect(model().attribute("todos", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Bar"))
                        )
                )));
 
        verify(todoServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(todoServiceMock);
    }
}