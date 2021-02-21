package com.emailApplication.lucene.model;

public class SimpleQuery {
	
	private String field;
	private String value;
	
	public SimpleQuery() {
		super();
	}
	
	public SimpleQuery(String field, String value) {
		super();
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
