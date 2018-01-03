package cz.kul.snippets.springmvc._05.securityservices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController {

	@Autowired
	FooService fooService;

	@RequestMapping(value = "/services")
	public ModelAndView showPage() {

		List<String> messages = new ArrayList<String>();

		try {
			fooService.adminMethod(1, 2);
			messages.add("adminMethod() permitted");
		} catch (Exception e) {
			messages.add("adminMethod() denied. Exception: "
					+ e.getClass().getName() + " " + e.getMessage());
		}
		
		try {
			fooService.superAdminMethod(1, 2);
			messages.add("superAdminMethod() permitted");
		} catch (Exception e) {
			messages.add("superAdminMethod() denied. Exception: "
					+ e.getClass().getName() + " " + e.getMessage());
		}

		ModelAndView model = new ModelAndView();
		model.addObject("messages", messages);
		model.setViewName("services");
		return model;
	}

}
