package com.prs.db;


import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.User;

												       //define datatype for primary key				
public interface UserRepo extends JpaRepository<User,Integer> {

	User findByUsernameAndPassword(String username, String password);
	
	
}