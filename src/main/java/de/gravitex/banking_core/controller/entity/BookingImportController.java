package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking.entity.BookingImport;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.dto.BookingImportSummary;
import de.gravitex.banking_core.dto.ImportBookings;
import de.gravitex.banking_core.dto.UnprocessedBookingImport;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingImportRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BookingImportController implements PersistableEntityController<BookingImport> {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private DataIntegrityService integrityService;
	
	@Autowired
	private BookingImportRepository bookingImportRepository;
	
	@Autowired
	BankingService bankingService;

	@DeleteMapping(path = "bookingimport")
	public ResponseEntity<BookingImport> delete(@RequestParam("id") Long aBookingImportId) {
		Optional<BookingImport> bookingImportOptional = bookingImportRepository.findById(aBookingImportId);		
		integrityService.assertOptionalPresent(bookingImportOptional, BookingImport.class);
		BookingImport bookingImport = bookingImportOptional.get();
		bookingImportRepository.delete(bookingImport);
		return new ResponseEntity<BookingImport>(bookingImport, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BookingImport> patch(BookingImport entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<BookingImport> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<BookingImport> put(BookingImport entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<BookingImport>> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}