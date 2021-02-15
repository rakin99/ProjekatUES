package com.emailApplication.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="accounts")
public class Account implements Serializable{
	
	
	@Id                                 // atribut je deo primarnog kljuca
	@GeneratedValue(strategy=IDENTITY)  // vrednost se generise automatski, u bazi
	@Column(name="account_id", unique=true, nullable=false)
	private long id;
	
	@Column(name="smtpAddress", unique=false, nullable=false)
	private String smtpAddress;
	
	@Column(name="smtpPort", unique=false, nullable=false)
	private int smtpPort;
	
	@Column(name="inServerType", unique=false, nullable=false)
	private short inServerType;
	
	@Column(name="inServerAddress", unique=false, nullable=false)
	private String inServerAddress;
	
	@Column(name="inServerPort", unique=false, nullable=false)
	private int inServerPort;
	
	@Column(name="username", unique=false, nullable=false)
	private String username;
	
	
	@Column(name="password", unique=false, nullable=false)
	private String password;
	
	@Column(name="displayname", unique=false, nullable=false)
	private String displayname;
	
	@Column(name="active", unique=false, nullable=false)
	private boolean active;
	
	@OneToMany(cascade={ALL}, fetch=LAZY, mappedBy="account")
	private List<MyMessage> messages = new ArrayList<MyMessage>();
	
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="user_id", nullable=false)
	private User user;

	public Account(long id, String smtpAddress, int smtp, short inServerType, String inServerAddress, int inServerPort ,String username, 
			String password, String displayName) {
		this.id=id;
		this.smtpAddress=smtpAddress;
		this.smtpPort=smtp;
		this.inServerType=inServerType;
		this.inServerAddress=inServerAddress;
		this.inServerPort=inServerPort;
		this.username=username;
		this.password=password;
		this.displayname=displayName;
	}
	
	public Account() {
		
		this.id=0;
		this.smtpAddress="";
		this.smtpPort=0;
		this.inServerType=0;
		this.inServerAddress="";
		this.inServerPort=0;
		this.username="";
		this.password="";
		this.displayname="";
		this.active=true;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSmtpAddress() {
		return smtpAddress;
	}

	public void setSmtpAddress(String smtpAddress) {
		this.smtpAddress = smtpAddress;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtp) {
		this.smtpPort = smtp;
	}

	public short getInServerType() {
		return inServerType;
	}

	public void setInServerType(short inServerType) {
		this.inServerType = inServerType;
	}

	public String getInServerAddress() {
		return inServerAddress;
	}

	public void setInServerAddress(String inServerAddress) {
		this.inServerAddress = inServerAddress;
	}

	public int getInServerPort() {
		return inServerPort;
	}

	public void setInServerPort(int inServerPort) {
		this.inServerPort = inServerPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	
	public List<MyMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<MyMessage> messages) {
		this.messages = messages;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String toString() {
	    return "(Account)[id="+id+",smtpAddress="+smtpAddress+",smtp="+smtpPort+"inServerType="+inServerType+",inServerAddress="+inServerAddress+
	    		",inServerPort="+inServerPort+",username="+username+",password="+password+",displayname="+displayname+"]";
	  }
}
