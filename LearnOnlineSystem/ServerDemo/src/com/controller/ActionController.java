package com.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

 

@Controller
public class ActionController {
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response){ 
     	return new ModelAndView("AdminLogin"); 
	}
	
	@RequestMapping("/trunto")
	public ModelAndView trunto(String page,String data){ 
		System.out.println("trunto->"+ page+";" + data);
		ModelAndView model = new ModelAndView(page);
		if(null != data)
			model.addObject("msg", data);
     	return model; 
	}
}