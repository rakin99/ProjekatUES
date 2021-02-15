package com.emailApplication.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.text.ParseException;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="messages")
public class MyMessage {

	@Id                                 // atribut je deo primarnog kljuca
	@GeneratedValue(strategy=IDENTITY)	// vrednost se generise automatski, u bazi
	@Column(name="_id", unique=true, nullable=false) 
	private long id;
	
	@Column(name="_from", unique=false, nullable=false)
	private String from;
	
	@Column(name="_to", unique=false, nullable=true)
	private String toReciver;
	
	@Column(name="_cc", unique=false, nullable=true)
	private String ccReciver;
	
	@Column(name="_bcc", unique=false, nullable=true)
	private String bccReciver;
	
	@Column(name="date_time", unique=false, nullable=false)
	private GregorianCalendar dateTime;
	
	@Column(name="_subject", unique=false, nullable=true)
	private String subject;
	
	@Column(name="content", unique=false, nullable=true, length = 10000)
	private String content;
	
	@Column(name="unread", unique=false, nullable=false)
	private boolean unread=true;
	
	@Column(name="active", unique = false, nullable = false)
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name="account_id", referencedColumnName="account_id", nullable=false)
	private Account account;

	public MyMessage() {
		this.id=0;
		this.from="";
		this.toReciver="";
		this.ccReciver="";
		this.bccReciver="";
		this.content="";
		this.dateTime=new GregorianCalendar();
		this.subject="";
		this.unread=true;
		active=true;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id=id;
	}

	public String get_from() {
		return from;
	}

	public void set_from(String _from) {
		this.from = _from;
	}

	public String get_to() {
		return toReciver;
	}

	public void set_to(String _to) {
		this.toReciver = _to;
	}

	public String get_cc() {
		return ccReciver;
	}

	public void set_cc(String _cc) {
		this.ccReciver = _cc;
	}

	public String get_bcc() {
		return bccReciver;
	}

	public void set_bcc(String _bcc) {
		this.bccReciver = _bcc;
	}

	public GregorianCalendar getDateTime() throws ParseException {
		return dateTime;
	}

	public void setDateTime(GregorianCalendar dateTime) {
		this.dateTime = dateTime;
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

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String toString() {
	    return "(Message)[\nid="+id+",_from="+from+",_to="+toReciver+",_cc="+ccReciver+",_bcc="+bccReciver+",dateTime="+dateTime+
	    		",subject="+subject+",content="+content+",unread="+unread+"]";
	  }
}
