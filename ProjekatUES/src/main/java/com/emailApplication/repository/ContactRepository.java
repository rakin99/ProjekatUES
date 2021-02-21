package com.emailApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emailApplication.model.Account;
import com.emailApplication.model.Contact;
import com.emailApplication.model.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

	@Query("SELECT max(id) FROM Contact")
	int findMaxId();
	
	@Query("SELECT count(id) FROM Contact WHERE _display_name LIKE concat('%',:username,'%') OR _first_name LIKE concat('%',:username,'%') OR _last_name LIKE concat('%',:username,'%')")
	long count(@Param("username") String username);
	
	Contact findById(long id);
	
	@Query("FROM Contact WHERE user LIKE concat('%',:username,'%')")
	List<Contact> findAllContacts(@Param("username") String username);
	List<Contact> findByUser(User user);
}
