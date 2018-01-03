package cz.kul.snippets.springmvc._04.security;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController {

	@RequestMapping(value = "/secured")
	public ModelAndView securedPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("secured");
		return model;
	}
	
	@RequestMapping(value = "/basic/")
	public ModelAndView securedBasicAuth() {
		ModelAndView model = new ModelAndView();
		model.setViewName("secured");
		return model;
	}
	
	@RequestMapping(value = "/basic/username")
	public ModelAndView username(Principal principal) {
		ModelAndView model = new ModelAndView();
		model.setViewName("logged-user-info");
		model.addObject("username", principal.getName());
		return model;
	}
	
	@RequestMapping(value = "/basic/username/more")
	public ModelAndView username() {
		ModelAndView model = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		model.setViewName("logged-user-info");
		model.addObject("username", auth.getName());
		model.addObject("authorities", auth.getAuthorities());
		
		return model;
	}
	
	@RequestMapping(value = "/403")
	public ModelAndView _403() {
		ModelAndView model = new ModelAndView();
		model.setViewName("403");
		return model;
	}
	
}
