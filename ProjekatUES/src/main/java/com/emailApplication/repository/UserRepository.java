package com.emailApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.emailApplication.model.User;


@Component
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("FROM User WHERE username LIKE :username AND password LIKE concat(:password,'%')")
	User findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
	
	User findByUsername(String username);
}
