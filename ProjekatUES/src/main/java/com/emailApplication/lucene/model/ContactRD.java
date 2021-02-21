package com.emailApplication.lucene.model;

public final class ContactRD {
	private String id;
	private String firstName;
	private String lastName;
	private String note;
	private String user;
	
	public ContactRD() {
		super();
		this.id = "";
		this.firstName = "";
		this.lastName = "";
		this.note = "";
		this.user = "";
	}

	public ContactRD(String id, String firstName, String lastName, String note, String user) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.note = note;
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(long id) {
		this.id = String.valueOf(id);
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
}
