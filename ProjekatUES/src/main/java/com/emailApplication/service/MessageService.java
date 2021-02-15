package com.emailApplication.service;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;


public interface MessageService {
	
	MyMessage findById(long messageId);
	
	GregorianCalendar getMaxDate(String username);
	
	List<MyMessage> findByAccountOrderBySubjectAsc(Account account);
	List<MyMessage> findByAccountOrderBySubjectDesc(Account account);

	int maxId();
	
	long count(String username);
	
	MyMessage save(MyMessage message);
	
	List<MyMessage> findAllSentMessage(String username);
	
	List<MyMessage> findAllByToReciver(String email);
	
	List<MyMessage> findByAccountOrderByFromAsc(Account account);
	List<MyMessage> findByAccountOrderByFromDesc(Account account);
	
	List<MyMessage> findByAccountOrderByDateTimeAsc(Account account);
	List<MyMessage> findByAccountOrderByDateTimeDesc(Account account);
}
