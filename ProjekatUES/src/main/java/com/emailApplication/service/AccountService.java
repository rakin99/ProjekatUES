package com.emailApplication.service;

import java.util.List;

import com.emailApplication.model.Account;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.AccountDTO;

public interface AccountService {
	
	Account findByUsername(String username);
	
	Account findByDisplayname(String email);
	
	Account findByPassword(String password);

	Account save(AccountDTO accountDTO,User user);
	
	boolean changeActive(AccountDTO accountDTO);
	
	Account findByUsernameAndPassword(String username,String password);
	
	List<Account> findByUser(User user);
	
	Account findById(long id);
	
	boolean delete(long id);
}
