package com.prs.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.db.RequestRepo;
import com.prs.model.Request;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {

	final static String statusRejected = "REJECTED";
	final static String statusApproved = "APPROVED";
	final static String statusNew = "NEW";
	final static String statusReview = "REVIEW";

	@Autowired
	private RequestRepo requestRepo;

	@GetMapping("/")
	public List<Request> getAllRequests() {
		return requestRepo.findAll();
	}

	@GetMapping("/{id}")
	public Request getRequestById(@PathVariable int id) {

		Optional<Request> r = requestRepo.findById(id);
		return r.get();
	}

	@PostMapping("")
	public Request addRequest(@RequestBody Request request) {
		request.setSubmittedDate(LocalDate.now());
		request.setTotal(0);
		return requestRepo.save(request);
	}

	@PutMapping("/{id}")
	public Request updateRequest(@PathVariable int id, @RequestBody Request request) {
		Request r = null;
		if (id != request.getId()) {
			System.err.println("Request id does not match path id.");
			// TODO return error to front end.
		} else if (!requestRepo.existsById(id)) {
			System.err.println("Request does not exist for id" + id);
		} else {
			r = requestRepo.save(request);
		}
		return r;
	}

	@DeleteMapping("/{id}")
	public boolean deleteRequest(@PathVariable int id) {
		boolean success = false;

		if (requestRepo.existsById(id)) {
			requestRepo.deleteById(id);

			success = true;
		} else {
			System.err.println("Delete Error! No request exists for id: " + id);
		}

		return success;
	}

	// finds all requests up for review, not including the requests from logged in
	// user
	@GetMapping("/reviews/{UserId}")
	public List<Request> getAllRequestForReview(@PathVariable int UserId) {

		List<Request> req = requestRepo.findByUserIdNot(UserId);
		return req;
	}

	// Approve(request) : [PUT: /api/requests/approve/5] - Sets the status of the
	// request for the id provided to "APPROVED"

	@PostMapping("/approve/{id}")
	public Request approveRequestById(@PathVariable int id) {

		Request request = requestRepo.findById(id).get();

		if (requestRepo.existsById(id))
			request.setStatus(statusApproved);

		// save status change
		requestRepo.save(request);
		return request;

	}

	// Reject(request) : [POST: /api/requests/reject/5] - Sets the status of the
	// request for the id provided to "REJECTED"

	@PostMapping("/reject/{id}")
	public Request rejectRequestById(@PathVariable int id, @RequestBody String reason) {

		Request request = requestRepo.findById(id).get();

		if (requestRepo.existsById(id)) {
			request.setStatus(statusRejected);
			request.setReasonForRejection(reason);
			requestRepo.save(request);
		}
		return request;

	}

	@PutMapping("/review/{id}")
	public Request reviewRequest(@PathVariable int id) {

		Request request = requestRepo.findById(id).get();
		
		request.setSubmittedDate(LocalDate.now());
		
		if (request.getTotal() <= 50) {
			request.setStatus(statusApproved);
		} else {
			request.setStatus(statusReview);
		}

		requestRepo.save(request);
		return request;

	}

}
