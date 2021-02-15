package com.emailApplication.service;

import com.emailApplication.model.User;
import com.emailApplication.modelDTO.UserDTO;

public interface UserService {
	
	User save(UserDTO userDTO);
	
	User findByUsernameAndPassword(String username,String password);
	
	User findByUsername(String username);

}
