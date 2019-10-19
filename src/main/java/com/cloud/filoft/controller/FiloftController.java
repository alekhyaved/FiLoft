package com.cloud.filoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import javax.servlet.http.Part;

import java.util.ArrayList;

import javax.servlet.annotation.MultipartConfig;

import com.cloud.filoft.model.File;
import com.cloud.filoft.model.User;
import com.cloud.filoft.service.FiloftService;



@Controller
public class FiloftController {
	
	@Autowired
	private FiloftService filoftservice;
	
	@GetMapping("/")
	public String start() {
		return "register";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}
	
	@PostMapping("/registerUser")
	public String registerUser(@RequestParam("firstname") String firstname,@RequestParam("lastname") String lastname, @RequestParam("emailid") String emailid,
			@RequestParam("password") String password, HttpSession session) {

		User user = new User();
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setEmailid(emailid);
		user.setPassword(password);
		if(!filoftservice.checkUser(emailid)) {
			if (filoftservice.registerUser(user)) {
				session.setAttribute("message", "success");
				return "redirect:/login";
			}
			session.setAttribute("message", "User already exists!");
			return "redirect:/register";
		}
		session.setAttribute("message", "User already exists!");
		return "redirect:/register";

	}
	
	@PostMapping("/loginUser")
	public String loginUser(@RequestParam("emailid") String emailid,
			@RequestParam("password") String password, ModelMap model, HttpSession session) {
		User user = filoftservice.getUser(emailid, password);
		
		if (user == null) {
			model.addAttribute("loginError", "Invalid email or password");
			session.setAttribute("message", "Invalid username or Password");
			return "redirect:/login";			
		}
		ArrayList<File> files = filoftservice.retrieveUserFiles(emailid);
		String name = user.getFirstname();
		session.setAttribute("name", name);	
		if(files != null) {
		session.setAttribute("files",files);
		return "redirect:/dashboard";
	    }
		else
		{
			session.setAttribute("files", null);
			return "redirect:/dashboard";
		 }
	}
	
		

}
