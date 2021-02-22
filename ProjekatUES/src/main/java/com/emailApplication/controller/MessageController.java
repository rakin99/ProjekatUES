package com.emailApplication.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.emailApplication.lucene.indexing.Indexer;
import com.emailApplication.lucene.indexing.analysers.SerbianAnalyzer;
import com.emailApplication.lucene.model.AdvancedQuery;
import com.emailApplication.lucene.model.IndexMessage;
import com.emailApplication.lucene.model.IndexUnit;
import com.emailApplication.lucene.model.RequiredHighlight;
import com.emailApplication.lucene.model.MessageRD;
import com.emailApplication.lucene.model.SearchType;
import com.emailApplication.lucene.model.SimpleQuery;
import com.emailApplication.lucene.model.UploadModel;
import com.emailApplication.lucene.search.QueryBuilder;
import com.emailApplication.lucene.search.ResultRetriever;
import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.MessageDTO;
import com.emailApplication.read_send_mail.ReadMail;
import com.emailApplication.read_send_mail.SendMail;
import com.emailApplication.service.AccountService;
import com.emailApplication.service.MessageService;
import com.emailApplication.service.UserService;
import com.emailApplication.tools.DateUtil;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.Path;


@RestController
@RequestMapping(value="/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value="recived-messages/{username}")
	public ResponseEntity<List<MessageRD>> getMessages(@PathVariable("username") String username) throws MessagingException, IOException, ParseException{
		System.out.println("\n\nPokusavam naci poruke za: "+username+"<---------------------------------------\n");
		
		User user = userService.findByUsername(username);
		List<Account> accounts = accountService.findByUser(user);
		GregorianCalendar dateTime=DateUtil.getLastOneHour();
		
		List<MessageRD> results = new ArrayList<MessageRD>();
		for (Account account : accounts) {
			if(account.isActive()) {
				//System.out.println("\nUsername: "+account.getDisplayname());
				long count=messageService.count(account.getDisplayname());
				//System.out.println("\nBroj poruka u bazi je: "+messageService.count(account.getDisplayname())+"\n");
				if(count!=0) {
					dateTime=messageService.getMaxDate(account.getDisplayname());
				}
				ReadMail.receiveEmail(account.getSmtpAddress(), account.getInServerAddress(), account,dateTime,count,"INBOX",messageService);
				//List<MyMessage> messages=messageService.findAllByToReciver(account.getDisplayname());
				//System.out.println("\n\n\n\nBroj poruka: "+messages.size());
				QueryParser qp=new QueryParser("toReciver", new SerbianAnalyzer());			
				Query query;
				try {
					query = qp.parse('"'+account.getDisplayname()+'"');
					List<MessageRD> messages = ResultRetriever.getResults(query);
					for (MessageRD myMessage : messages) {
						System.out.println("Reciver: "+myMessage.getToReciver());
						System.out.println("Subject: "+myMessage.getSubject());
						results.add(myMessage);
					}
				} catch (org.apache.lucene.queryparser.classic.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseEntity<List<MessageRD>>(results,HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		return new ResponseEntity<List<MessageRD>>(results,HttpStatus.OK);
	}
	
	@GetMapping
	@RequestMapping(value="/sent-messages/{username}")
	public ResponseEntity<List<MessageRD>> getSentMessages(@PathVariable("username") String username) throws MessagingException, IOException, ParseException{
		System.out.println("Trazim poslate poruke...");
		User user = userService.findByUsername(username);
		List<Account> accounts = accountService.findByUser(user);
		List<MessageRD> results = new ArrayList<MessageRD>();
		for (Account account : accounts) {
			if(account.isActive()) {
				System.out.println("\nUsername: "+account.getDisplayname());
				QueryParser qp=new QueryParser("fromSender", new SerbianAnalyzer());			
				Query query;
				try {
					query = qp.parse('"'+account.getDisplayname()+'"');
					List<MessageRD> messages = ResultRetriever.getResults(query);
					for (MessageRD myMessage : messages) {
						System.out.println("Reciver: "+myMessage.getToReciver());
						System.out.println("Subject: "+myMessage.getSubject());
						results.add(myMessage);
					}
				} catch (org.apache.lucene.queryparser.classic.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseEntity<List<MessageRD>>(results,HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		return new ResponseEntity<List<MessageRD>>(results,HttpStatus.OK);
	}
	
	@GetMapping(value="/{username}/{id}")
	public ResponseEntity<MessageDTO> getMessage(@PathVariable("username") String username,@PathVariable("id") Integer id) throws ParseException{
		MyMessage message = messageService.findById(id);
		if(message == null){
			return new ResponseEntity<MessageDTO>(HttpStatus.NOT_FOUND);
		}
		System.out.println("\n\nSaljem poruku sa Id-om: "+message.getId()+"<<--------------------------\n");
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.OK);
	}
	
	@PostMapping(consumes="application/json")
	public ResponseEntity<MessageDTO> saveMessage(@RequestBody MessageDTO messageDTO,UriComponentsBuilder builder) throws ParseException{
		System.out.println("\nPoceo slanje poruke!<-------------------------\n");
		System.out.println("\nContent je: "+messageDTO.getContent()+"<-------------------------\n");
		try {
			System.out.println("\nEmail je: "+messageDTO.getFromSender()+"<-------------------------\n");
			Account account=accountService.findByDisplayname(messageDTO.getFromSender());
			MyMessage message = new MyMessage();
			message.set_from(messageDTO.getFromSender());
			message.set_to(messageDTO.getToReciver());
			message.set_cc(messageDTO.get_cc());;
			message.set_bcc(messageDTO.get_bcc());;
			message.setDateTime(DateUtil.convertFromDMYHMS(messageDTO.getDateTime()));
			message.setSubject(messageDTO.getSubject());
			message.setContent(messageDTO.getContent());
			message.setAccount(account);
			SendMail.send(message,account);
		
			message = messageService.save(message);
			URI location = builder.replacePath("/messages/{id}").buildAndExpand(message.getId()).toUri();
			return ResponseEntity.created(location).build();	
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.unprocessableEntity().build();	
		}
		
	}
	
	@PostMapping(value="/search", consumes="application/json")
	public ResponseEntity<List<MessageRD>> search(@RequestBody ArrayList<AdvancedQuery> listAdvancedQuery) throws Exception {
		Query query1;
		Query queryReciver;
		BooleanQuery.Builder builder=new BooleanQuery.Builder();
		List<Account> accounts = accountService.findByUser(userService.findByUsername(listAdvancedQuery.get(0).getUser()));
		for (AdvancedQuery advancedQuery : listAdvancedQuery) {
			System.out.println("User: "+advancedQuery.getUser());
			System.out.println("Field1: "+advancedQuery.getField());
			System.out.println("Value1: "+advancedQuery.getValue());
			System.out.println("Operation: "+advancedQuery.getOperation());
			for (Account account : accounts) {
				if(account.isActive()) {
					if(!advancedQuery.getValue().isEmpty() && !advancedQuery.getOperation().isEmpty()) {

						queryReciver=QueryBuilder.buildQuery(SearchType.regular, "toReciver", account.getDisplayname());	
						query1=QueryBuilder.buildQuery(SearchType.fuzzy, advancedQuery.getField(), advancedQuery.getValue());
						
						if(advancedQuery.getOperation().equalsIgnoreCase("AND")){
							builder.add(queryReciver,BooleanClause.Occur.MUST);
							builder.add(query1,BooleanClause.Occur.MUST);
						}else if(advancedQuery.getOperation().equalsIgnoreCase("OR")){
							builder.add(queryReciver,BooleanClause.Occur.MUST);
							builder.add(query1,BooleanClause.Occur.SHOULD);
						}
						
					}else if(!advancedQuery.getValue().isEmpty()) {
						queryReciver=QueryBuilder.buildQuery(SearchType.regular, "toReciver", account.getDisplayname());	
						query1=QueryBuilder.buildQuery(SearchType.fuzzy, advancedQuery.getField(), advancedQuery.getValue());		
						builder.add(query1,BooleanClause.Occur.MUST);
						builder.add(queryReciver,BooleanClause.Occur.MUST);
					}
				}
			}
		}
		
		Query query = builder.build();
		List<MessageRD> results = ResultRetriever.getResults(query);
		
		return new ResponseEntity<List<MessageRD>>(results, HttpStatus.OK);
	}
	
	@PostMapping(value="/search-by-id", consumes="application/json")
	public ResponseEntity<List<MessageRD>> search(@RequestBody SimpleQuery simpleQuery) throws Exception {
		System.out.println("\n---->	SearchById");
		QueryParser qp=new QueryParser(simpleQuery.getField(), new SerbianAnalyzer());			
		Query query=qp.parse(simpleQuery.getValue());
		List<MessageRD> results = ResultRetriever.getResults(query);
		return new ResponseEntity<List<MessageRD>>(results, HttpStatus.OK);
	}

	@PutMapping(value="/messages/{id}", consumes="application/json")
	public ResponseEntity<MessageDTO> updateMessage(@RequestBody MessageDTO messageDTO, @PathVariable("id") long id) throws ParseException{
		System.out.println("\n\nAzuriram poruku...."+messageDTO.getId());
		MyMessage message=messageService.findById(id);
		if(message==null) {
			return new ResponseEntity<MessageDTO>(HttpStatus.BAD_REQUEST);
		}
		message.setUnread(messageDTO.isUnread());
		
		message=messageService.save(message);
		System.out.println("\n\nVracam poruku sa Id-om"+message.getId());
		
		return new ResponseEntity<MessageDTO>(new MessageDTO(message), HttpStatus.OK);
		
	}
	
	@DeleteMapping(value="/messages/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id){
		System.out.println("\nPocinjem sa trazenjem poruke za brisanje! <----------------------------------------\n");
		MyMessage myMessage = messageService.findById(id);
		if (myMessage != null){
			System.out.println("\nPronasao sam poruku i sada pocinjem sa brisanjem! <------------------------------\n");
			myMessage.setActive(false);
			messageService.save(myMessage);
			System.out.println("\nObrisao sam poruku! <------------------------------------------------------\n");
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {	
			System.out.println("Nisam uspeo da pronadjem poruku! <-----------------------------------------------");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	//save file
    /*private String saveUploadedFile(MultipartFile file) throws IOException {
    	String retVal = null;
        if (! file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(getResourceFilePath(DATA_DIR_PATH).getAbsolutePath() + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            retVal = path.toString();
        }
        return retVal;
    }*/
}
