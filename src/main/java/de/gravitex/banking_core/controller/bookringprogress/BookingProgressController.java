package de.gravitex.banking_core.controller.bookringprogress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.dto.BookingProgress;
import de.gravitex.banking_core.service.BankingService;

@RestController
public class BookingProgressController {

	private Logger logger = LoggerFactory.getLogger(BookingProgressController.class);
	
	@Autowired
	BankingService bankingService;

	@PostMapping(path = "bookingprogress")
	public ResponseEntity<BookingProgress> acceptBudgetPlanning(@RequestBody BookingProgress aBookingProgress) {
		return new ResponseEntity<BookingProgress>(bankingService.createBookingProgress(aBookingProgress), HttpStatus.OK);
	}
}