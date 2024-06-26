package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.VendorRepo;
import com.prs.model.Vendor;

@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorRepo vendorRepo;

	@GetMapping("/")
	public List<Vendor> getAllVendors() {
		return vendorRepo.findAll();
	}

	@GetMapping("/{id}")
	public Vendor getVendorById(@PathVariable int id) {
		

		Optional<Vendor> v = vendorRepo.findById(id);
		if(v == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This vendor does not exist");
		}
		
		return v.get();
	}
	
	@PostMapping("")
	public Vendor addVendor(@RequestBody Vendor vendor) {
		return vendorRepo.save(vendor);
	}
	
	@PutMapping("/{id}")
	public Vendor updateVendor(@PathVariable int id, @RequestBody Vendor vendor) {
		Vendor v = null;
		if(id != vendor.getId()) {
			System.err.println("Vendor id does not match path id.");
			//TODO return error to front end.
		}
		else if(!vendorRepo.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This vendor does not exist");
		}
		else {
		v = vendorRepo.save(vendor);
		}
		return v;
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteVendor(@PathVariable int id) {
		boolean success = false;
		if(vendorRepo.existsById(id)) {
			vendorRepo.deleteById(id);
			success = true;
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This vendor does not exist");
		}
		
		
		return success;
	}

}
