package com.emailApplication.lucene.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class IndexMessage {

	private String id;
	private String fromSender;
	private String toReciver;
	private String subject;
	private String content;
	
	public String getId() {
		return id;
	}

	public void setId(long id) {
		this.id = String.valueOf(id);
	}

	public String getFromSender() {
		return fromSender;
	}

	public void setFromSender(String fromSender) {
		this.fromSender = fromSender;
	}

	public String getToReciver() {
		return toReciver;
	}

	public void setToReciver(String toReciver) {
		this.toReciver = toReciver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Document getLuceneDocument(){
		Document retVal = new Document();
		retVal.add(new TextField("fromSender", fromSender, Store.YES));
		retVal.add(new TextField("toReciver", toReciver, Store.YES));
		retVal.add(new TextField("subject", subject, Store.YES));
		retVal.add(new TextField("content",content, Store.YES));
		retVal.add(new StringField("id",id, Store.YES));
		return retVal;
	}

	@Override
	public String toString() {
		return "IndexMessage [id=" + id + ", fromSender=" + fromSender + ", toReciver=" + toReciver + ", subject="
				+ subject + ", content=" + content + "]";
	}
	
}
