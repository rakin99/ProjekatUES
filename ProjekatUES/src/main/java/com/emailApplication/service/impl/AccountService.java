package com.emailApplication.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emailApplication.model.Account;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.AccountDTO;
import com.emailApplication.repository.AccountRepository;
import com.emailApplication.service.UserService;

@Service
public class AccountService implements com.emailApplication.service.AccountService{

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public Account findByUsername(String username) {
		Account account=accountRepository.findByUsername(username);
		return account;
	}
	
	@Override
	public Account save(AccountDTO accountDTO,User user) {
		Account account=new Account();
		account.setActive(true);
		account.setDisplayname(accountDTO.getUsername()+"@"+accountDTO.getSmtpAddress());
		account.setInServerAddress("");
		account.setInServerPort(0);
		account.setInServerType((short) 0);
		account.setPassword(accountDTO.getPassword());
		account.setSmtpAddress(accountDTO.getSmtpAddress());
		account.setSmtpPort(0);
		account.setUsername(accountDTO.getUsername());
		account.setUser(user);
		return accountRepository.save(account);
	}

	@Override
	public Account findByPassword(String password) {
		Account account=accountRepository.findByPassword(password);
		return account;
	}

	@Override
	public Account findByUsernameAndPassword(String username, String password) {
		Account account=accountRepository.findByUsernameAndPassword(username,password);
		return account;
	}

	@Override
	public List<Account> findByUser(User user) {
		return accountRepository.findByUser(user);
	}

	@Override
	public Account findById(long id) {
		return accountRepository.findById(id);
	}

	@Override
	public boolean delete(long id) {
		boolean canFind=false;
		Account account=findById(id);
		accountRepository.delete(account);
		return canFind;
	}

	@Override
	public boolean changeActive(AccountDTO accountDTO) {
		boolean canChange = false;
		Account existAccount = accountRepository.findByUsername(accountDTO.getUsername());
		if(existAccount!=null) {
			canChange=true;
			existAccount.setActive(accountDTO.isActive());
			accountRepository.save(existAccount);
		}
		return canChange;
	}

	@Override
	public Account findByDisplayname(String email) {
		return accountRepository.findByDisplayname(email);
	}
}
