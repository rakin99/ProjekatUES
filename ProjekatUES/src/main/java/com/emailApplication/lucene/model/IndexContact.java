package com.emailApplication.lucene.model;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;

public class IndexContact {

	private String id;
	private String firstName;
	private String lastName;
	private String note;
	private String email;
	private String user;
	
	public IndexContact() {
		super();
		this.id = "";
		this.firstName = "";
		this.lastName = "";
		this.note = "";
		this.user = "";
		this.email = "";
	}

	public IndexContact(String id, String firstName, String lastName, String note, String user,String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.note = note;
		this.user = user;
		this.user = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Document getLuceneDocument(){
		Document retVal = new Document();
		retVal.add(new TextField("firstName", firstName, Store.YES));
		retVal.add(new TextField("lastName", lastName, Store.YES));
		retVal.add(new TextField("note", note, Store.YES));
		retVal.add(new StringField("id",id, Store.YES));
		retVal.add(new StringField("user",user, Store.YES));
		retVal.add(new StringField("email",email, Store.YES));
		return retVal;
	}
}
