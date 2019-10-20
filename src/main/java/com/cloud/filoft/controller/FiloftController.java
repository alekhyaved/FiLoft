package com.cloud.filoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import javax.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.cloud.filoft.model.Files;

//import javax.servlet.annotation.MultipartConfig;

import com.cloud.filoft.model.User;
import com.cloud.filoft.service.AWSService;
import com.cloud.filoft.service.FiloftService;


@Controller
public class FiloftController {
	
	@Autowired
	private FiloftService filoftservice;
	
	@Autowired
	private AWSService awsservice;
	
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
		ArrayList<Files> files = filoftservice.retrieveUserFiles(emailid);
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
	
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestPart(value = "file") MultipartFile filepart, @RequestParam("description") String description, @RequestParam("emailid") String emailid, HttpSession session) {
		Long fileSize = filepart.getSize() / 1024 / 1024;
		if((fileSize <= 10)) {
			try {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());				
				File s3File = convertMultiPartToFile(filepart);
				String filename = s3File.getName();
				ArrayList<Files> retrievedFiles = filoftservice.retrieveUserFiles(emailid);
				String fileUrl = awsservice.uploadFileToS3(s3File, emailid);
				Files files = new Files();
				files.setEmailId(emailid);
				files.setFileName(filename);
				files.setDescription(description);
				files.setFileSize(fileSize);
				files.setUpdatedTime(timestamp);
				files.setFileUrl(fileUrl);
				if(!filoftservice.checkFile(filename, retrievedFiles)) {
					files.setCreatedTime(timestamp);
				if(filoftservice.uploadUserFile(files)) {					
					return "redirect:/dashboard";
				}
				} else {
					if(filoftservice.uploadUserFile(files)) {					
						return "redirect:/dashboard";
					}
				}				
				return "redirect:/dashboard";
			} catch(IOException e) {
				
			}
		}		
		session.setAttribute("message", "Upload failed!!! Please upload images less than 10MB.");
		return "redirect:/dashboard";
	}
	
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(ModelMap model, HttpSession session) {
		
//		String id_token = (String) session.getAttribute("id_token");
//		if (id_token != null) {
//			try {
//				GoogleIdToken.Payload payLoad = IdTokenVerifierAndParser.getPayload(id_token);
//				if (payLoad != null) {
//					payLoad.setExpirationTimeSeconds(1L);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		session.invalidate();
		return "redirect:/login";
	}
    
	public File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	
		

}
