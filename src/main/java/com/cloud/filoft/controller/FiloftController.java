package com.cloud.filoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class FiloftController {
	
	@GetMapping("/")
	public String start() {
		return "register";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
}
