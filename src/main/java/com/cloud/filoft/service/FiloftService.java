package com.cloud.filoft.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.cloud.filoft.model.Files;
import com.cloud.filoft.model.User;
import com.cloud.filoft.repository.FileRepository;
import com.cloud.filoft.repository.UserRepository;


@Service
public class FiloftService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	public boolean registerUser(User user) {
		try {
			userRepository.save(user);
			return true;
		} catch (Exception e) {

		}
		return false;
	}
	
	public boolean checkUser(String emailid) {
		User user = userRepository.getUser(emailid);
		if (user != null) {
			return true;
		}
		return false;
	}
	
	public User getUser(String emailid, String password) {
		User user = userRepository.getUser(emailid);

		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
	
	public boolean uploadUserFile(Files file) {		
		try {
			fileRepository.save(file);
			return true;
		} catch (Exception e) {
		}
		return false;		
	}
	
	public boolean checkFile(String filename, ArrayList<Files> retrievedFiles) {
		for(Files files : retrievedFiles) {
			if(files.getFileName() == filename) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Files> retrieveUserFiles(String emailid) {
		try {
			ArrayList<Files> filesArray = fileRepository.retrieveUserFiles(emailid);
			return filesArray;
			
		} catch (Exception e) {
			System.out.println("retrieve exception");
		}
		return null;
	}
}
