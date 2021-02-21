package com.emailApplication.modelDTO;

import java.io.Serializable;
import java.text.ParseException;

import com.emailApplication.model.Contact;

public class ContactDTO implements Serializable{

	private long id; 
	private String firstName; 
	private String lastName; 
	private String displayName; 
	private String email;
	private String note;
	private String user;
	
	public ContactDTO() {
		
	}

	public ContactDTO(long id, String firstName, String lastName, String displayName, String email, String note) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName = displayName;
		this.email = email;
		this.note = note;
	}
	
	public ContactDTO(Contact contact) throws ParseException{
		this(contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getDisplayName(), contact.getEmail(), contact.getNote());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ContactDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", displayName="
				+ displayName + ", email=" + email + ", note=" + note + ", user=" + user + "]";
	}

}
