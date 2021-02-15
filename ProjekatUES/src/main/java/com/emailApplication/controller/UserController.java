package com.emailApplication.controller;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.emailApplication.modelDTO.UserDTO;
import com.emailApplication.service.impl.UserService;

@RestController
@RequestMapping(value="/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(consumes = "application/json",value = "/login")
	public ResponseEntity<UserDTO> getAUser(@RequestBody UserDTO userDTO) throws ParseException{
		System.out.println("\nPocinjem da trazim user-a!<----------------------\n");
		User user = userService.findByUsername(userDTO.getUsername());
		if(user.getPassword().equals(userDTO.getPassword())){
			return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(consumes = "application/json", value = "/signup")
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO, UriComponentsBuilder builder){
		User user = userService.findByUsername(userDTO.getUsername());
		if(user!=null) {
			return ResponseEntity.unprocessableEntity().build();
		}
		System.out.println("\nPoceo upisivanje user-a<----------------\n");
		user=userService.save(userDTO);
		URI location = builder.replacePath("/users/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping
	@RequestMapping(value = "/users/{username}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username){
		
		System.out.println("Trazim user-a sa korisnickim imenom: "+username);
		User user=userService.findByUsername(username);
		if(user!=null) {
			return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
		}
		return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping(value = "/users/{username}", consumes="application/json")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO,@PathVariable("username") String username){
		
		System.out.println("Cuvam izmene user-a sa korisnickim imenom: "+username);
		User user=userService.findByUsername(username);
		if(user!=null) {
			user.setFirstname(userDTO.getFirstname());
			user.setLastname(userDTO.getLastname());
			user.setPassword(userDTO.getPassword());
			
			user=userService.save(userDTO);
			
			return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
		}
		return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
	}

}
