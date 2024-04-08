package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.db.ProductRepo;
import com.prs.model.Product;



@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepo productRepo;

	@GetMapping("/")
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}


	@GetMapping("/{id}")
	public Product getProductById(@PathVariable int id) {
		

		Optional<Product> p = productRepo.findById(id);
		return p.get();
	}
	
	@PostMapping("")
	public Product addProduct(@RequestBody Product product) {
		return productRepo.save(product);
	}
	
	@PutMapping("/{id}")
	public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
		Product p = null;
		if(id != product.getId()) {
			System.err.println("Product id does not match path id.");
			//TODO return error to front end.
		}
		else if(!productRepo.existsById(id)) {
			System.err.println("Product does not exist for id" + id);
		}
		else {
		p = productRepo.save(product);
		}
		return p;
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteProduct(@PathVariable int id) {
		boolean success = false;
		if(productRepo.existsById(id)) {
			productRepo.deleteById(id);
			success = true;
		}
		else {
			System.err.println("Delete Error! No product exists for id: "+id);
		}
		
		
		return success;
	}
	
	
	
	
}
