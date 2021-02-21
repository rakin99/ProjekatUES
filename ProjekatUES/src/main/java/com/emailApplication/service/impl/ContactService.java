package com.emailApplication.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emailApplication.lucene.indexing.Indexer;
import com.emailApplication.lucene.model.IndexContact;
import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.model.Account;
import com.emailApplication.model.Contact;
import com.emailApplication.model.MyMessage;
import com.emailApplication.model.User;
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
	public Contact save(Contact contact) {
		Contact contact2 = null;
		try {
			contact2 = contactRepository.save(contact);
			indexNewContact(contact2);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return contact2;
	}

	@Override
	public List<Contact> findAllContacts(String username) {
		return contactRepository.findAllContacts(username);
	}

	@Override
	public List<Contact> findByUser(User user) {
		return contactRepository.findByUser(user);
	}
	
	private void indexNewContact(Contact contact) throws IOException{
		Indexer indexer = Indexer.getInstance();
		//List<MyMessage> messages = messageRepository.findAll();
		//System.out.println("\n\tIndeksiram...");
		//for (MyMessage message : messages) {
		IndexContact indexContact = new IndexContact();
		indexContact.setId(contact.getId());
		indexContact.setFirstName(contact.getFirstName());
		indexContact.setLastName(contact.getLastName());
		indexContact.setNote(contact.getNote());
		indexContact.setUser(contact.getUser().getUsername());
		indexContact.setEmail(contact.getEmail());
		indexer.add(indexContact.getLuceneDocument());
		//}
	}
}
