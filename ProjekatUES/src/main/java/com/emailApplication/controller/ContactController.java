package com.emailApplication.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailApplication.model.Account;
import com.emailApplication.model.Contact;
import com.emailApplication.modelDTO.ContactDTO;
import com.emailApplication.service.impl.AccountService;
import com.emailApplication.service.impl.ContactService;

@RestController
@RequestMapping(value="/api")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	@Autowired
	private AccountService accountService;
	
	@GetMapping(value="/contacts/{username}/{sort}")
	public ResponseEntity<List<ContactDTO>> getContacts(@PathVariable("username") String username,@PathVariable("sort") String sort) throws MessagingException, IOException, ParseException{
		System.out.println("\n\nPokusavam naci kontakte za: "+username+"<---------------------------------------\n");
		System.out.println("\n\nSort: "+sort+"<---------------------------------------\n");
		Account account=accountService.findByUsername(username.split("@")[0]);
		//GregorianCalendar dateTime=DateUtil.getLastOneHour();
		System.out.println("\nUsername: "+account.getUsername());
		//long count=contactService.count(username);
		System.out.println("\nBroj kontakata u bazi je: "+contactService.count(username)+"\n");
		/*if(count!=0) {
			dateTime=contactService.getMaxDate(username);
		}
		System.out.println("Vreme poslednje poruke je:"+DateUtil.formatTimeWithSecond(dateTime));
		ReadMail.receiveEmail(account.getSmtpAddress(), account.getInServerAddress(), account,dateTime,count,"INBOX",messageService);
		*/List<Contact> contacts=new ArrayList<Contact>();
		if(sort.equals("displayName|asc")) {
			contacts= contactService.findByAccountOrderByDisplayNameAsc(account);
		}else if(sort.equals("firstName|asc")) {
			contacts= contactService.findByAccountOrderByFirstNameAsc(account);
		}else if(sort.equals("lastName|asc")) {
			contacts= contactService.findByAccountOrderByLastNameAsc(account);
		}
		else if(sort.equals("displayName|desc")) {
			contacts= contactService.findByAccountOrderByDisplayNameDesc(account);
		}else if(sort.equals("firstName|desc")) {
			contacts= contactService.findByAccountOrderByFirstNameDesc(account);
		}else if(sort.equals("lastName|desc")) {
			contacts= contactService.findByAccountOrderByLastNameDesc(account);
		}
		System.out.println("\n\n\n\nBroj kontakata: "+contacts.size());
		List<ContactDTO> contactsDTO=new ArrayList<ContactDTO>();
		/*for (MyMessage myMessage : messages) {
			if(myMessage.isActive()) {
				if(!myMessage.get_from().equals(account.getUsername()+"@"+account.getSmtpAddress())) {
					messagesDTO.add(new MessageDTO(myMessage));
				}
			}
		}
		if(messages.size()!=0) {
			System.out.println("\n\nSaljem poruku sa Id-om: "+messages.get(messages.size()-1).getId()+"<<--------------------------\n");
		}*/
		
		return new ResponseEntity<List<ContactDTO>>(contactsDTO,HttpStatus.OK);
	}
	
	/*@GetMapping
	@RequestMapping(value="/messages/sent-messages/{username}")
	public ResponseEntity<List<MessageDTO>> getSentMessages(@PathVariable("username") String username) throws MessagingException, IOException, ParseException{
		System.out.println("Trazim poslate poruke...");
		Account account=accountService.findByUsername(username.split("@")[0]);
		System.out.println("\nUsername: "+account.getUsername());
		List<MyMessage> messages= messageService.findAllSentMessage(username);
		System.out.println("\n\n\n\nBroj poruka: "+messages.size());
		List<MessageDTO> messagesDTO=new ArrayList<MessageDTO>();
		for (MyMessage myMessage : messages) {
			if(myMessage.isActive()) {
				messagesDTO.add(new MessageDTO(myMessage));
			}
		}
		if(messages.size()!=0) {
			System.out.println("\n\nSaljem poruku sa Id-om: "+messages.get(messages.size()-1).getId()+"<<--------------------------\n");
		}
		
		return new ResponseEntity<List<MessageDTO>>(messagesDTO,HttpStatus.OK);
	}*/
	
	@GetMapping(value="/contacts/{username}/{id}")
	public ResponseEntity<ContactDTO> getContact(@PathVariable("username") String username,@PathVariable("id") Integer id) throws ParseException{
		Contact contact = contactService.findById(id);
		if(contact == null){
			return new ResponseEntity<ContactDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("\n\nSaljem kontakt sa Id-om: "+contact.getId()+"<<--------------------------\n");
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.OK);
	}
	
	@PostMapping(consumes="application/json", value = "/contacts")
	public ResponseEntity<ContactDTO> saveContact(@RequestBody ContactDTO contactDTO) throws ParseException{
		System.out.println("\nPocetak kreiranja kontakta!<-------------------------\n");
		//System.out.println("\nContent je: "+contactDTO.getContent()+"<-------------------------\n");
		Account account=accountService.findByUsername(contactDTO.getDisplayName().split("@")[0]);
		Contact contact = new Contact();
		contact.setFirstName(contactDTO.getFirstName());;
		contact.setLastName(contactDTO.getLastName());
		contact.setDisplayName(contactDTO.getDisplayName());
		contact.setEmail(contactDTO.getEmail());;
		contact.setNote(contactDTO.getNote());
		
		//SendMail.send(contact,account);
	
		contact = contactService.save(contact);
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.CREATED);	
	}
	
	@PutMapping(value="/contacts/{id}", consumes="application/json")
	public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contactDTO, @PathVariable("id") long id) throws ParseException{
		System.out.println("\n\nAzuriram kontakt...."+contactDTO.getId());
		Contact contact=contactService.findById(id);
		if(contact==null) {
			return new ResponseEntity<ContactDTO>(HttpStatus.BAD_REQUEST);
		}
		
		contact=contactService.save(contact);
		System.out.println("\n\nVracam kontakt sa Id-om"+contact.getId());
		
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="/contacts/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable("id") Long id){
		System.out.println("\nTrazenje kontakta za brisanje! <----------------------------------------\n");
		Contact contact = contactService.findById(id);
		if (contact != null){
			System.out.println("\nPronadjen kontakt i sada pocinje brisanje! <------------------------------\n");
			contact.setActive(false);
			contactService.save(contact);
			System.out.println("\nKontakt je obrisan! <------------------------------------------------------\n");
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {	
			System.out.println("Nije pronadjen kontakt! <-----------------------------------------------");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

}
