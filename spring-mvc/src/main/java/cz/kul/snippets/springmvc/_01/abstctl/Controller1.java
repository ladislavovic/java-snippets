package cz.kul.snippets.springmvc._01.abstctl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//public class Controller1 extends AbstractController {
public class Controller1 {

//	@Override
//	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
//		ModelAndView result = new ModelAndView("hello");
//		result.addObject("message", "Controller1");
//		return result;
//	}
    
    @RequestMapping(value = "/foo", method = RequestMethod.GET)
    public ResponseEntity<?> method1(HttpServletRequest req, HttpServletRequest req2) {
        BodyBuilder builder = ResponseEntity.ok();
        return builder.body("Body");
    }

}
