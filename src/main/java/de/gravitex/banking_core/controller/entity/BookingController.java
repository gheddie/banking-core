package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.Booking;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.BookingRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BookingController implements PersistableEntityController<Booking> {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	BankingService bankingService;
	
	@Autowired
	private DataIntegrityService integrityService;	

	@PatchMapping(path = "booking", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Booking> patch(@RequestBody Booking entity) {
		Booking saved = bookingRepository.save(entity);
		return new ResponseEntity<Booking>(saved, HttpStatus.OK);
	}

	@Override
	@GetMapping(value = "bookings")
	public ResponseEntity<List<Booking>> findAll() {
		return new ResponseEntity<List<Booking>>(bookingRepository.findAll(), HttpStatus.OK);
	}

	@Override
	@DeleteMapping(path = "booking")
	public ResponseEntity<Booking> delete(@RequestParam("id") Long aEntityId) {
		Optional<Booking> optional = bookingRepository.findById(aEntityId);		
		integrityService.assertOptionalPresent(optional, Booking.class);
		Booking entity = optional.get();
		bookingRepository.delete(entity);
		return new ResponseEntity<Booking>(entity, HttpStatus.OK);
	}

	@Override
	@GetMapping(path = "booking")
	public ResponseEntity<Booking> findById(@RequestParam("id") Long aEntityId) {
		Booking booking = bookingRepository.findById(aEntityId).get();
		return new ResponseEntity<Booking>(booking, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Booking> put(Booking entity) {
		// TODO Auto-generated method stub
		return null;
	}
}