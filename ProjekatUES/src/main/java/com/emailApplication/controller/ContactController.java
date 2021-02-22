package com.emailApplication.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
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

import com.emailApplication.lucene.indexing.analysers.SerbianAnalyzer;
import com.emailApplication.lucene.model.AdvancedQuery;
import com.emailApplication.lucene.model.ContactRD;
import com.emailApplication.lucene.model.IndexContact;
import com.emailApplication.lucene.model.MessageRD;
import com.emailApplication.lucene.model.SearchType;
import com.emailApplication.lucene.search.QueryBuilder;
import com.emailApplication.lucene.search.ResultRetriever;
import com.emailApplication.lucene.search.ResultRetrieverContact;
import com.emailApplication.model.Account;
import com.emailApplication.model.Contact;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.ContactDTO;
import com.emailApplication.service.UserService;
import com.emailApplication.service.impl.AccountService;
import com.emailApplication.service.impl.ContactService;

@RestController
@RequestMapping(value="/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	@Autowired
	private UserService userService;
	
	@GetMapping(value="/{username}")
	public ResponseEntity<List<ContactRD>> getContacts(@PathVariable("username") String username) throws MessagingException, IOException, ParseException{
		System.out.println("\n\nPokusavam naci kontakte za: "+username+"<---------------------------------------\n");
		List<ContactRD> contacts = new ArrayList<ContactRD>();
		QueryParser qp=new QueryParser("user", new SerbianAnalyzer());			
		Query query;
		try {
			query = qp.parse('"'+username+'"');
			contacts = ResultRetrieverContact.getResults(query);
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<List<ContactRD>>(contacts,HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<ContactRD>>(contacts,HttpStatus.OK);
	}
	
	@GetMapping(value="/contacts/{username}/{id}")
	public ResponseEntity<ContactDTO> getContact(@PathVariable("username") String username,@PathVariable("id") Integer id) throws ParseException{
		Contact contact = contactService.findById(id);
		if(contact == null){
			return new ResponseEntity<ContactDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("\n\nSaljem kontakt sa Id-om: "+contact.getId()+"<<--------------------------\n");
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.OK);
	}
	
	@PostMapping(consumes="application/json")
	public ResponseEntity<ContactDTO> saveContact(@RequestBody ContactDTO contactDTO) throws ParseException{
		System.out.println("\nPocetak kreiranja kontakta!<-------------------------\n");
		User user = userService.findByUsername(contactDTO.getUser());
		Contact contact = new Contact();
		contact.setFirstName(contactDTO.getFirstName());;
		contact.setLastName(contactDTO.getLastName());
		contact.setDisplayName(contactDTO.getDisplayName());
		contact.setEmail(contactDTO.getEmail());;
		contact.setNote(contactDTO.getNote());
		contact.setUser(user);
		contact.setActive(true);
		contact = contactService.save(contact);
		return new ResponseEntity<ContactDTO>(new ContactDTO(contact), HttpStatus.CREATED);	
	}
	
	@PostMapping(value="/search", consumes="application/json")
	public ResponseEntity<List<ContactRD>> search(@RequestBody AdvancedQuery advancedQuery) throws Exception {
		System.out.println("Pertraga kontakata!");
		System.out.println("User: "+advancedQuery.getUser());
		System.out.println("Field1: "+advancedQuery.getField1());
		System.out.println("Value1: "+advancedQuery.getValue1());
		System.out.println("Operation1: "+advancedQuery.getOperation1());
		Query query1;	
		Query queryReciver;
		BooleanQuery.Builder builder=new BooleanQuery.Builder();
		if(!advancedQuery.getValue1().isEmpty() && !advancedQuery.getOperation1().isEmpty()) {

			queryReciver=QueryBuilder.buildQuery(SearchType.regular, "user", '"'+advancedQuery.getUser()+'"');	
			query1=QueryBuilder.buildQuery(SearchType.fuzzy, advancedQuery.getField1(), advancedQuery.getValue1());
			
			if(advancedQuery.getOperation1().equalsIgnoreCase("AND")){
				builder.add(queryReciver,BooleanClause.Occur.MUST);
				builder.add(query1,BooleanClause.Occur.MUST);
			}else if(advancedQuery.getOperation1().equalsIgnoreCase("OR")){
				builder.add(queryReciver,BooleanClause.Occur.MUST);
				builder.add(query1,BooleanClause.Occur.SHOULD);
			}
			
		}else if(!advancedQuery.getValue1().isEmpty()) {
			queryReciver=QueryBuilder.buildQuery(SearchType.regular, "user", '"'+advancedQuery.getUser()+'"');	
			query1=QueryBuilder.buildQuery(SearchType.fuzzy, advancedQuery.getField1(), advancedQuery.getValue1());		
			builder.add(query1,BooleanClause.Occur.MUST);
			builder.add(queryReciver,BooleanClause.Occur.MUST);
		}
		
		Query query = builder.build();
		List<ContactRD> results = ResultRetrieverContact.getResults(query);
		
		return new ResponseEntity<List<ContactRD>>(results, HttpStatus.OK);
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
