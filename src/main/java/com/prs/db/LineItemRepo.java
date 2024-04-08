package com.prs.db;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.LineItem;
import com.prs.model.Request;




												       //define datatype for primary key				
public interface LineItemRepo extends JpaRepository<LineItem,Integer> {
	
	
	List<LineItem> findAllByRequest(Request request);

	List<LineItem> findAllByRequestId(int id);

	
	
}