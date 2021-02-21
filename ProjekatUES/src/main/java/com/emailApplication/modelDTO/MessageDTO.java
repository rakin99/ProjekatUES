package com.emailApplication.modelDTO;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import com.emailApplication.lucene.model.MessageRD;
import com.emailApplication.model.MyMessage;
import com.emailApplication.tools.DateUtil;

public class MessageDTO implements Serializable{

	private long id; 
	private String fromSender; 
	private String toReciver; 
	private String _cc; 
	private String _bcc; 
	private GregorianCalendar dateTime; 
	private String subject;
	private String content; 
	private boolean unread;
	private boolean active;
	
	public MessageDTO() {
		
	}

	public MessageDTO(long id, String _from, String _to, String _cc, String _bcc, GregorianCalendar dateTime, String subject,
			String content, boolean unread, boolean active) {
		super();
		this.id = id;
		this.fromSender = _from;
		this.toReciver = _to;
		this._cc = _cc;
		this._bcc = _bcc;
		this.dateTime = dateTime;
		this.subject = subject;
		this.content = content;
		this.unread = unread;
		this.active=active;
	}
	
	public MessageDTO(MyMessage message) throws ParseException {
		this(message.getId(),message.get_from(),message.get_to(),message.get_cc(),message.get_bcc(),
				message.getDateTime(),message.getSubject(),message.getContent(),message.isUnread(),message.isActive());
	}
	
	public MessageDTO(MessageRD message) throws ParseException {
		this.id=Integer.parseInt(message.getId());
		this.fromSender = message.getFromSender();
		this.toReciver = message.getToReciver();
		this.subject = message.getSubject();
		this.content = message.getContent();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String get_cc() {
		return _cc;
	}

	public void set_cc(String _cc) {
		this._cc = _cc;
	}

	public String get_bcc() {
		return _bcc;
	}

	public void set_bcc(String _bcc) {
		this._bcc = _bcc;
	}

	public String getDateTime() throws ParseException {
		return DateUtil.formatTimeWithSecond(this.dateTime);
	}

	public void setDateTime(String dateTime) throws ParseException {
		this.dateTime = DateUtil.convertFromDMYHMS(dateTime);
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
	
}
