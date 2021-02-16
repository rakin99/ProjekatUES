package com.emailApplication.lucene.model;

public final class ResultData {
	
	private String subject;
	private String content;
	private String id;
	private String fromSender;
	private String toReciver;
	private String path;
	
	public ResultData() {
		super();
	}

	public ResultData(String subject, String content, String id, String fromSender, String toReciver, String path) {
		super();
		this.subject = subject;
		this.content = content;
		this.id = id;
		this.fromSender = fromSender;
		this.toReciver = toReciver;
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String title) {
		this.subject = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
