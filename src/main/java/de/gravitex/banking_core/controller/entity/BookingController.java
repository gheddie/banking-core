package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.repository.BookingRepository;

@RestController
public class BookingController implements PersistableEntityController<Booking> {
	
	@Autowired
	private BookingRepository bookingRepository;

	@PatchMapping(path = "booking", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Booking> patch(@RequestBody Booking entity) {
		return new ResponseEntity<Booking>(bookingRepository.save(entity), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "bookings", method = RequestMethod.GET)
	public ResponseEntity<List<Booking>> findAll() {
		return new ResponseEntity<List<Booking>>(bookingRepository.findAll(), HttpStatus.OK);
	}
}