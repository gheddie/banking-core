package de.gravitex.banking_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.repository.BookingRepository;

@RestController
// public class BookingController implements BaseController<Booking> {
public class BookingController {
	
	@Autowired
	private BookingRepository bookingRepository;

	// @Override
	@RequestMapping(value = "bookings", method = RequestMethod.GET)
	public List<Booking> findAll() {
		return bookingRepository.findAll();
	}

	/*
	@Override
	@PatchMapping(path = "booking")
	public void patch(Booking entity) {
		// bookingRepository.save(entity);
		System.out.println("patch --> " + entity);
	}
	*/
	
	@PatchMapping(path = "booking", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void patch(@RequestBody Booking entity) {
		System.out.println("patching booking ["+entity+"] with id = "+entity.getId()+"...");
		bookingRepository.save(entity);
	}
}