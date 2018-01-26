package cz.kul.snippets.springmvc._03.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {
	
	public static class Item {
		int id;
		String label;
		
		public Item(int id, String label) {
			super();
			this.id = id;
			this.label = label;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		
	}

	@RequestMapping(method = RequestMethod.GET)
	public String viewRegistration(Map<String, Object> model) {
		User userForm = new User();
		model.put("userForm", userForm);

		List<String> professionList = new ArrayList<String>();
		professionList.add("Developer");
		professionList.add("Designer");
		professionList.add("IT Manager");
		model.put("professionList", professionList);
		
		// select
		List<Item> objectItems = new ArrayList<Item>();
		objectItems.add(new Item(1, "first"));
		objectItems.add(new Item(2, "second"));
		objectItems.add(new Item(3, "third"));
		objectItems.add(new Item(4, "fourth"));
		objectItems.add(new Item(5, "fivths"));
		model.put("objectItems", objectItems);
		

		return "registration";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processRegistration(@Valid @ModelAttribute("userForm") User user, BindingResult br, Map<String, Object> model) {

		if (br.hasErrors()) {
			return "registration";
		} else {
			return "registrationSuccess";
		}
	}

}
