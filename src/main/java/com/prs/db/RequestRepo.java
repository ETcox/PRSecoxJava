package com.prs.db;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Request;


												       //define datatype for primary key				
public interface RequestRepo extends JpaRepository<Request,Integer> {
	
	
	List<Request> findByUserIdNot(int id);
	
	
	
}