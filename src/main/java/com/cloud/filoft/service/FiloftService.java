package com.cloud.filoft.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloud.filoft.model.User;
import com.cloud.filoft.repository.UserRepository;


@Service
public class FiloftService {
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean registerUser(User user) {
		try {
			userRepository.save(user);
			return true;
		} catch (Exception e) {

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
}
