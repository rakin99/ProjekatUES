package com.emailApplication.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emailApplication.model.Account;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.UserDTO;
import com.emailApplication.repository.UserRepository;

@Service
public class UserService implements com.emailApplication.service.UserService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User save(UserDTO userDTO) {
		User user=new User();
		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setAccounts(new ArrayList<Account>());
		return userRepository.save(user);
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		User user=userRepository.findByUsernameAndPassword(username,password);
		return user;
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	
}
