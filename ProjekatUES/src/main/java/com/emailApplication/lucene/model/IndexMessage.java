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
	private String path;
	private String attachment_content;
	
	public IndexMessage() {
		super();
		this.id = "";
		this.fromSender = "";
		this.toReciver = "";
		this.subject = "";
		this.content = "";
		this.path = "";
		this.attachment_content = "";
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAttachment_content() {
		return attachment_content;
	}

	public void setAttachment_content(String attachment_content) {
		this.attachment_content = attachment_content;
	}

	public Document getLuceneDocument(){
		Document retVal = new Document();
		retVal.add(new TextField("fromSender", fromSender, Store.YES));
		retVal.add(new TextField("toReciver", toReciver, Store.YES));
		retVal.add(new TextField("subject", subject, Store.YES));
		retVal.add(new TextField("content",content, Store.YES));
		retVal.add(new StringField("id",id, Store.YES));
		if(!path.isEmpty()) {
			retVal.add(new StringField("path",path, Store.YES));
		}
		retVal.add(new TextField("attachment_content", attachment_content, Store.YES));
		return retVal;
	}

}
