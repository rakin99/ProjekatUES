package com.emailApplication.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="contacts")
public class Contact {
	
	@Id                                 // atribut je deo primarnog kljuca
	@GeneratedValue(strategy=IDENTITY)	// vrednost se generise automatski, u bazi
	@Column(name="_id", unique=true, nullable=false) 
	private long id;
	
	@Column(name="_first_name", unique=false, nullable=true)
	private String firstName;
	
	@Column(name="_last_name", unique=false, nullable=true)
	private String lastName;
	
	@Column(name="_display_name", unique=false, nullable=false)
	private String displayName;
	
	@Column(name="_email", unique=false, nullable=false)
	private String email;
	
	@Column(name="_note", unique=false, nullable=true)
	private String note;
	
	@Column(name="active", unique = false, nullable = false)
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="user_id", nullable=false)
	private User user;

	public Contact() {
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.displayName = "";
		this.email = "";
		this.note = "";
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", displayName="
				+ displayName + ", email=" + email + ", note=" + note + ", active=" + active + ", user=" + user + "]";
	}
	
}
