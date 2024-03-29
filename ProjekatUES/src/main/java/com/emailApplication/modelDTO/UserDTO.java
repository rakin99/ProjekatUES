package com.emailApplication.modelDTO;

import java.io.Serializable;

import com.emailApplication.model.User;

public class UserDTO implements Serializable {

	private long id;
	private String username; 
	private String password;
	private String firstname; 
	private String lastname;
	
	public UserDTO() {
		
	}

	public UserDTO(long id, String username, String password, String firstname, String lastname) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public UserDTO(User user) {
		this(user.getId(), user.getUsername(), user.getPassword(), user.getFirstname(), 
				user.getLastname());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + "]";
	}
}
