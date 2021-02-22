package com.emailApplication.lucene.model;

public class AdvancedQuery {
	
	private String user;
	private String field1;
	private String value1;
	private String operation1;
	
	public AdvancedQuery() {
		super();
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	
	public String getOperation1() {
		return operation1;
	}
	public void setOperation1(String operation) {
		this.operation1 = operation;
	}

}
