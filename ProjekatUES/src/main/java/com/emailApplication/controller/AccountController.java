package com.emailApplication.controller;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.emailApplication.model.Account;
import com.emailApplication.model.User;
import com.emailApplication.modelDTO.AccountDTO;
import com.emailApplication.service.AccountService;
import com.emailApplication.service.UserService;


@RestController
@RequestMapping(value="/accounts")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired 
	private UserService userService;

	@GetMapping
	@RequestMapping(value = "/{username}")
	public ResponseEntity<List<AccountDTO>> getAccounts(@PathVariable("username") String username) throws ParseException{
		System.out.println("\nPocinjem da trazim account!<----------------------\n");
		User user=userService.findByUsername(username);
		List<Account> accounts = accountService.findByUser(user);
		List<AccountDTO> accountsDTO=new ArrayList<AccountDTO>();
		for (Account a : accounts) {
			accountsDTO.add(new AccountDTO(a));
		}
		if(accountsDTO.size() != 0){
			return new ResponseEntity<List<AccountDTO>>(accountsDTO, HttpStatus.OK);
		}
		
		return new ResponseEntity<List<AccountDTO>>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(consumes = "application/json", value = "/{username}")
	public ResponseEntity<AccountDTO> saveAccount(@RequestBody AccountDTO accountDTO,@PathVariable("username") String username, UriComponentsBuilder builder){
		Account account = accountService.findByUsername(accountDTO.getUsername());
		User user=userService.findByUsername(accountDTO.getUser());
		if(account!=null && account.getUsername().equals(accountDTO.getUsername()) && account.getSmtpAddress().equals(accountDTO.getSmtpAddress())) {
			return ResponseEntity.unprocessableEntity().build();
		}
		System.out.println("\nPoceo upisivanje account-a<----------------\n");
		account=accountService.save(accountDTO,user);
		URI location = builder.replacePath("/accounts/{id}").buildAndExpand(account.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(consumes = "application/json")
	public ResponseEntity<?> activate(@RequestBody AccountDTO accountDTO) {
		System.out.println("U activate sam!");
		boolean canChange= accountService.changeActive(accountDTO);
		if(canChange) {
			return ResponseEntity.accepted().build();	
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id){
		System.out.println("\nPocinjem sa trazenjem accounta za brisanje! <----------------------------------------\n");
		boolean canFind = accountService.delete(id);
		if(canFind) {
			System.out.println("\nObrisao sam account! <------------------------------------------------------\n");
			return ResponseEntity.noContent().build();
		} else {	
			System.out.println("Nisam uspeo da pronadjem account! <-----------------------------------------------");
			return ResponseEntity.notFound().build();
		}
	}
}
