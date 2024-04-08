package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;
import com.prs.model.LineItem;
import com.prs.model.Request;

@CrossOrigin
@RestController
@RequestMapping("/api/lineitems")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;

	@Autowired
	private RequestRepo requestRepo;

	@GetMapping("/")
	public List<LineItem> getAllLineItems() {
		return lineItemRepo.findAll();
	}

	@GetMapping("/{id}")
	public LineItem getById(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		return li.get();
	}

	@GetMapping("lines-for-pr/{id}")
	public List<LineItem> GetLineItemsByRequestId(@PathVariable int id) {

		return lineItemRepo.findAllByRequestId(id);
	}

	@PostMapping("")
	public LineItem addLineItem(@RequestBody LineItem lineItem) {

		Request request = lineItem.getRequest();
		lineItemRepo.save(lineItem);
		recalculateTotal(request);
		return lineItem;
	}

	@PutMapping("/{id}")
	public LineItem updateLineItem(@PathVariable int id, @RequestBody LineItem lineItem) {
		LineItem li = null;
		if (id != lineItem.getId()) {
			System.err.println("LineItem id does not match path id.");
			// TODO return error to front end.
		} else if (!lineItemRepo.existsById(id)) {
			System.err.println("LineItem does not exist for id" + id);
		} else {
			Request request = lineItem.getRequest();
			li = lineItemRepo.save(lineItem);
			recalculateTotal(request);
		}
		return li;
	}

	@DeleteMapping("/{id}")
	public boolean deleteLineItem(@PathVariable int id) {
		boolean success = false;
		if (lineItemRepo.existsById(id)) {

			LineItem lineItem = lineItemRepo.findById(id).get();
			Request request = lineItem.getRequest();
			lineItemRepo.deleteById(id);
			recalculateTotal(request);
			success = true;

		} else {
			System.err.println("Delete Error! No lineitem exists for id: " + id);
		}

		return success;
	}

	private void recalculateTotal(Request request) {

		double sum = 0;

		for (LineItem li : lineItemRepo.findAllByRequest(request)) {
			double lineTotal = li.getProduct().getPrice() * li.getQuantity();
			sum += lineTotal;
		}

		request.setTotal(sum);

		// save request
		requestRepo.save(request);

	}

}
