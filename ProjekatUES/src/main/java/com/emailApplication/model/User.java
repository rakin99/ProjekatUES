package com.emailApplication.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User implements Serializable{

	@Id                                 // atribut je deo primarnog kljuca
	@GeneratedValue(strategy=IDENTITY)  // vrednost se generise automatski, u bazi
	@Column(name="user_id", unique=true, nullable=false)
	private long id;
	
	@Column(name="username", unique=false, nullable=false)
	private String username;
	
	@Column(name="password", unique=false, nullable=false)
	private String password;
	
	@Column(name="firstname", unique=false, nullable=false)
	private String firstname;
	
	@Column(name="lastname", unique=false, nullable=false)
	private String lastname;
	
	@Column(name="active", unique=false, nullable = false)
	private boolean active=true;
	
	@OneToMany(cascade={ALL}, fetch=LAZY, mappedBy="user")
	private List<Account> accounts = new ArrayList<Account>();
	
	@OneToMany(cascade={ALL}, fetch=LAZY, mappedBy="user")
	private List<Contact> contacts = new ArrayList<Contact>();
	
	public User() {
	}

	public User(long id, String username, String password, String firstname, String lastname) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
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
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
}
