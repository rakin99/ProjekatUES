package com.emailApplication.lucene.model;

public class AdvancedQuery {
	
	private String user;
	private String field;
	private String value;
	private String operation;
	
	public AdvancedQuery() {
		super();
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getField() {
		return field;
	}
	public void setField(String field1) {
		this.field = field1;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value1) {
		this.value = value1;
	}
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	@Override
	public String toString() {
		return "AdvancedQuery [user=" + user + ", field=" + field + ", value=" + value + ", operation=" + operation
				+ "]";
	}

}
