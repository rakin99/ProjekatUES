package com.emailApplication.repository;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.emailApplication.model.Account;
import com.emailApplication.model.MyMessage;


@Component
public interface MessageRepository extends JpaRepository<MyMessage, Integer>{
 
	@Query("SELECT max(id) FROM MyMessage")
	int findMaxId();
	
	@Query("SELECT count(id) FROM MyMessage WHERE toReciver LIKE concat('%',:username,'%') OR ccReciver LIKE concat('%',:username,'%') OR bccReciver LIKE concat('%',:username,'%')")
	long count(@Param("username") String username);
	
	@Query("SELECT max(dateTime) FROM MyMessage  WHERE toReciver LIKE concat('%',:username,'%') OR ccReciver LIKE concat('%',:username,'%') OR bccReciver LIKE concat('%',:username,'%')")
	GregorianCalendar getMaxDate(@Param("username") String username);
	
	MyMessage findById(long id);
	
	//@Query("FROM MyMessage WHERE account=id AND active=true")
	List<MyMessage> findByAccountOrderBySubjectAsc(Account account);
	
	List<MyMessage> findByAccountOrderBySubjectDesc(Account account);
	
	List<MyMessage> findByAccountOrderByFromAsc(Account account);
	
	List<MyMessage> findByAccountOrderByFromDesc(Account account);
	
	List<MyMessage> findByAccountOrderByDateTimeAsc(Account account);
	
	List<MyMessage> findByAccountOrderByDateTimeDesc(Account account);
	
	@Query("FROM MyMessage WHERE _from LIKE concat('%',:username,'%')")
	List<MyMessage> findAllSentMessage(@Param("username") String username);
	
	@Query("FROM MyMessage  WHERE toReciver LIKE concat('%',:username,'%') OR ccReciver LIKE concat('%',:username,'%') OR bccReciver LIKE concat('%',:username,'%') AND active=true")
	List<MyMessage> findAllByToReciver(@Param("username") String email);
	
}
