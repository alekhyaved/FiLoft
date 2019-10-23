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
	
	@Autowired
	FiloftController(AWSService awsservice) {
		this.awsservice = awsservice;
	}
	
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
	
	@GetMapping("/adminlogin")
	public String adminLogin() {
		return "adminlogin";
	}
	
	@GetMapping("/admindashboard")
	public String adminDashboard() {
		return "admindashboard";
	}
	
	@GetMapping("/googleLogin")
	public String googleLogin() {
		return "googleLogin";
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
		session.setAttribute("emailid", emailid);
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
	
	@PostMapping("/loginWithGoogle")
	public String loginWithGoogle(@RequestParam("emailid") String emailid, @RequestParam("googleusername") String googleusername, HttpSession session) {
		ArrayList<Files> files = filoftservice.retrieveUserFiles(emailid);
		session.setAttribute("name", googleusername);	
		session.setAttribute("emailid", emailid);
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
	public String uploadFile(@RequestParam(value = "filepart") MultipartFile filepart, @RequestParam("description") String description,  @RequestParam("name") String name, @RequestParam("emailid") String emailid, HttpSession session) {
//		Long fileSize = filepart.getSize() / 1024 / 1024;
		ArrayList<Files> userFiles = filoftservice.retrieveUserFiles(emailid);
		session.setAttribute("name", name);	
		session.setAttribute("emailid", emailid);
//		if((fileSize <= 10)) {
		String filename;
		Long fileSize = 5L;
			try {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());	
				if(filepart.equals(null)) {
				File s3File = convertMultiPartToFile(filepart);
				 filename = s3File.getName();
				} else {
					filename = "test";
					fileSize = 6L;
				}
				ArrayList<Files> retrievedFiles = filoftservice.retrieveUserFiles(emailid);
//				String fileUrl = awsservice.uploadFileToS3(s3File, emailid);
				Files files = new Files();
				files.setEmailId(emailid);
				files.setFileName(filename);
				files.setDescription(description);
				files.setFileSize(fileSize);
				files.setUpdatedTime(timestamp);
				files.setFileUrl("https://filoft.s3.amazonaws.com/sdf%40sdf.com/Accuracy.png");
				Files existingFile = filoftservice.checkFile(filename, retrievedFiles);
				System.out.println("existingFile" +existingFile);
				if(existingFile == null) {					
					files.setCreatedTime(timestamp);
				if(filoftservice.uploadUserFile(files)) {
					ArrayList<Files> latestFiles = filoftservice.retrieveUserFiles(emailid);
					session.setAttribute("files", latestFiles);
					session.setAttribute("message", "Uploaded Successfully");
					return "redirect:/dashboard";
				}
				session.setAttribute("files", userFiles);
				session.setAttribute("message", "Upload Failed");
				return "redirect:/dashboard";
				} else {
					files.setCreatedTime(existingFile.getCreatedTime());
					files.setFileID(existingFile.getFileID());
					System.out.println("id" +existingFile.getFileID());
					if(filoftservice.uploadUserFile(files)) {
						ArrayList<Files> latestFiles = filoftservice.retrieveUserFiles(emailid);
						session.setAttribute("files", latestFiles);
						session.setAttribute("message", "Updated Successfully");
						return "redirect:/dashboard";
					}
					session.setAttribute("files", userFiles);
					session.setAttribute("message", "Upload Failed");
					return "redirect:/dashboard";
				}				
			} catch(IOException e) {
				
			}
//		}	
		session.setAttribute("files", userFiles);
		session.setAttribute("message", "Upload failed!!! Please upload images less than 10MB.");
		return "redirect:/dashboard";
	}
	
	@PostMapping("/delete")
	public String deleteFile(@RequestParam(value = "emailid") String emailid, @RequestParam("name") String name,
			@RequestParam("filename") String filename,
	@RequestParam("fileId") Integer fileId, HttpSession session){
		session.setAttribute("name", name);	
		session.setAttribute("emailid", emailid);
		if(filoftservice.deleteFile(fileId)) {
			awsservice.deleteFile(filename, emailid);
			ArrayList<Files> latestFiles = filoftservice.retrieveUserFiles(emailid);
			session.setAttribute("files", latestFiles);
			session.setAttribute("message", "Deleted Successfully");
			return "redirect:/dashboard";
		}
		ArrayList<Files> latestFiles = filoftservice.retrieveUserFiles(emailid);
		session.setAttribute("files", latestFiles);
		session.setAttribute("message", "Deletion Failed");
		return "redirect:/dashboard";
	}
	
	@PostMapping("/checkadmin")
	public String checkadmin(@RequestParam("emailid") String emailid,
			@RequestParam("password") String password, ModelMap model, HttpSession session) {		
		if (!emailid.equals("admin@sjsu.com") || !password.equals("adminpassword")) {
			model.addAttribute("loginError", "Invalid email or password");
			session.setAttribute("message", "Invalid username or Password");
			return "redirect:/adminlogin";			
		}
		ArrayList<Files> files = filoftservice.retrieveAllFiles();
		session.setAttribute("name", "Admin");	
		session.setAttribute("emailid", emailid);
		if(files != null) {
		session.setAttribute("files",files);
		return "redirect:/admindashboard";
	    }
		else
		{
			session.setAttribute("files", null);
			return "redirect:/admindashboard";
		 }
	}
	
	@PostMapping("/admindelete")
	public String adminDeleteFile(@RequestParam(value = "emailid") String emailid,
			@RequestParam("filename") String filename,
	@RequestParam("fileId") Integer fileId, HttpSession session){
		session.setAttribute("name", "Admin");	
		session.setAttribute("emailid", "admin@sjsu.com");
		if(filoftservice.deleteFile(fileId)) {
			awsservice.deleteFile(filename, emailid);
			ArrayList<Files> latestFiles = filoftservice.retrieveAllFiles();
			session.setAttribute("files", latestFiles);
			session.setAttribute("message", "Deleted Successfully");
			return "redirect:/admindashboard";
		}
		ArrayList<Files> latestFiles = filoftservice.retrieveAllFiles();
		session.setAttribute("files", latestFiles);
		session.setAttribute("message", "Deletion Failed");
		return "redirect:/admindashboard";
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
