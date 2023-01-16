package cz.kul.snippets.example_openapi_polymorphism;

import cz.kul.snippets.example_openapi_polymorphism.model.CustomAttribute;
import cz.kul.snippets.example_openapi_polymorphism.model.Order;
import cz.kul.snippets.example_openapi_polymorphism.model.RefValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

	@GetMapping("/orders/{id}")
	public Order getOrder(Long id) {

		Order order = new Order();
		order.setId(42);

		CustomAttribute ca = new CustomAttribute();
		ca.setVal(List.of(new RefValue("AAA-BBB", "CROSS")));
		order.setCustomAttributes(List.of(ca));

		return order;
	}

}
