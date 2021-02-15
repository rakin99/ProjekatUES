package com.emailApplication.service;

import java.util.List;

import com.emailApplication.model.Account;
import com.emailApplication.model.Contact; 

public interface ContactService {

	Contact findById(long contactId);
	
	List<Contact> findByAccountOrderByDisplayNameAsc(Account account);

	int maxId();
	
	long count(String username);
	
	Contact save(Contact contact);
	
	List<Contact> findAllContacts(String username);
	
	List<Contact> findByAccountOrderByFirstNameAsc(Account account);
	
	List<Contact> findByAccountOrderByLastNameAsc(Account account);
	
	List<Contact> findByAccountOrderByDisplayNameDesc(Account account);
	
	List<Contact> findByAccountOrderByFirstNameDesc(Account account);
	
	List<Contact> findByAccountOrderByLastNameDesc(Account account);
	
}
