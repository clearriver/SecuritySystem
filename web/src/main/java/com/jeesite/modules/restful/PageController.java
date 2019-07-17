package com.jeesite.modules.restful;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("PageController-v1")
@RequestMapping(value = "/api")
public class PageController {
	@RequestMapping(value = {"/p"},method = RequestMethod.GET)
	public ModelAndView p(String p){
		ModelAndView mav=new ModelAndView("modules/p");
		mav.addObject("p", p);
		return mav;
	}
}
