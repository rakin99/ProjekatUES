package com.emailApplication.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emailApplication.model.Account;
import com.emailApplication.model.Contact;
import com.emailApplication.repository.ContactRepository;

@Service
public class ContactService implements com.emailApplication.service.ContactService{
	
	@Autowired
	ContactRepository contactRepository;
	
	@Override
	public Contact findById(long contactId) {
		return contactRepository.findById(contactId);
	}
	
	@Override
	public List<Contact> findByAccountOrderByDisplayNameAsc(Account account){
		return contactRepository.findByAccountOrderByDisplayNameAsc(account);
	}

	@Override
	public int maxId() {
		int maxId=contactRepository.findMaxId();
		return maxId;
	}

	@Override
	public long count(String username) {
		long count=contactRepository.count(username);
		return count;
	}
	
	@Override
	public Contact save(Contact contact) {
		return contactRepository.save(contact);
	}

	@Override
	public List<Contact> findAllContacts(String username) {
		return contactRepository.findAllContacts(username);
	}

	@Override
	public List<Contact> findByAccountOrderByFirstNameAsc(Account account) {
		return contactRepository.findByAccountOrderByFirstNameAsc(account);
	}

	@Override
	public List<Contact> findByAccountOrderByLastNameAsc(Account account) {
		return contactRepository.findByAccountOrderByLastNameAsc(account);
	}
	
	@Override
	public List<Contact> findByAccountOrderByDisplayNameDesc(Account account){
		return contactRepository.findByAccountOrderByDisplayNameDesc(account);
	}
	
	@Override
	public List<Contact> findByAccountOrderByFirstNameDesc(Account account) {
		return contactRepository.findByAccountOrderByFirstNameDesc(account);
	}

	@Override
	public List<Contact> findByAccountOrderByLastNameDesc(Account account) {
		return contactRepository.findByAccountOrderByLastNameDesc(account);
	}

}
