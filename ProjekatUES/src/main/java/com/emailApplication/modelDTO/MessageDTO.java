package com.emailApplication.modelDTO;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import com.emailApplication.model.MyMessage;
import com.emailApplication.tools.DateUtil;

public class MessageDTO implements Serializable{

	private long id; 
	private String _from; 
	private String _to; 
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
		this._from = _from;
		this._to = _to;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String get_from() {
		return _from;
	}

	public void set_from(String _from) {
		this._from = _from;
	}

	public String get_to() {
		return _to;
	}

	public void set_to(String _to) {
		this._to = _to;
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
