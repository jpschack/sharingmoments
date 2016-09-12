package com.sharingmoments.ui.config;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoutesController implements ErrorController {
	@RequestMapping({
	    "/profile",
	    "/account/**",
	    "/search",
	    "/login",
	    "/signup",
	    "/reset-password",
	    "/update-password",
	    "/user/**",
	    "/event/**",
	    "/error"
	})
	public String index() {
	    return "forward:/";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}