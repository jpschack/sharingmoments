package com.sharingmoments.ui.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoutesController {
	@RequestMapping({
	    "/profile",
	    "/account/**",
	    "/search",
	    "/login",
	    "/signup",
	    "/reset-password",
	    "/update-password",
	    "/user/**",
	    "/event/**"
	})
	public String index() {
	    return "forward:/";
	}
}
