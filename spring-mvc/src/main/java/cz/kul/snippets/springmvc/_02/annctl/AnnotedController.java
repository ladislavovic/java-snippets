package cz.kul.snippets.springmvc._02.annctl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AnnotedController {
	
	@RequestMapping("/foo")
	public ModelAndView foo(HttpServletRequest request, HttpServletResponse response) {
		return createModel("foo");
	}

	@RequestMapping("/bar")
	public ModelAndView bar(HttpServletRequest request, HttpServletResponse response) {
		return createModel("bar");
	}

	private ModelAndView createModel(String msg) {
		ModelAndView result = new ModelAndView();
		result.setViewName("hello");
		result.addObject("message", msg);
		return result;
	}

}
