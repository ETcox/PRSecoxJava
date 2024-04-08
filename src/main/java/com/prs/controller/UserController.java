package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.UserRepo;
import com.prs.model.User;
import com.prs.model.UserLogin;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable int id) {

		Optional<User> u = userRepo.findById(id);
		return u.get();
	}

	@PostMapping("")
	public User addUser(@RequestBody User user) {
		return userRepo.save(user);
	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User user) {
		User u = null;
		if (id != user.getId()) {
			System.err.println("User id does not match path id.");
			// TODO return error to front end.
		} else if (!userRepo.existsById(id)) {
			System.err.println("User does not exist for id" + id);
		} else {
			u = userRepo.save(user);
		}
		return u;
	}

	@DeleteMapping("/{id}")
	public boolean deleteUser(@PathVariable int id) {
		boolean success = false;
		if (userRepo.existsById(id)) {
			userRepo.deleteById(id);
			success = true;
		} else {
			System.err.println("Delete Error! No user exists for id: " + id);
		}

		return success;
	}

	@PostMapping("/login")
	public User login(@RequestBody UserLogin ul) {

		User user = userRepo.findByUsernameAndPassword(ul.getUsername(), ul.getPassword());

		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username or password not found");
		}

		return user;

	}

}
