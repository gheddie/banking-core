package de.gravitex.banking_core.controller.bookingimport;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking.entity.BookingImport;
import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.dto.BookingImportSummary;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingImportRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BookingImportController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private DataIntegrityService integrityService;
	
	@Autowired
	private BookingImportRepository bookingImportRepository;
	
	@Autowired
	BankingService bankingService;

	@GetMapping(path = "importbookings")
	public ResponseEntity<ImportBookings> importBookings(@RequestParam("accountId") Long accountId) {
		Optional<Account> accountOptional = accountRepository.findById(accountId);
		integrityService.assertOptionalPresent(accountOptional, Account.class);
		List<BookingFileImportDto> importedBookings = bankingService.importBookingsForAccount(accountOptional.get());
		ImportBookings dto = new ImportBookings();
		dto.setImportedBookingFiles(importedBookings);
		return new ResponseEntity<ImportBookings>(dto, HttpStatus.OK);
	}
	
	@GetMapping(path = "importfilebookings")
	public ResponseEntity<BookingImportSummary> importBookingsFromFile(@RequestParam("accountId") Long accountId,
			@RequestParam("fileName") String aImportFileName) {
		Optional<Account> accountOptional = accountRepository.findById(accountId);
		integrityService.assertOptionalPresent(accountOptional, Account.class);
		return new ResponseEntity<BookingImportSummary>(
				bankingService.importFile(accountOptional.get(), aImportFileName), HttpStatus.OK);
	}

	@GetMapping(path = "unprocessedbookingimports")
	public ResponseEntity<List<UnprocessedBookingImport>> getUnprocessedBookingImports(
			@RequestParam("accountId") Long accountId) {
		Optional<Account> accountOptional = accountRepository.findById(accountId);
		integrityService.assertOptionalPresent(accountOptional, Account.class);
		return new ResponseEntity<List<UnprocessedBookingImport>>(
				bankingService.getUnprocessedBookingImports(accountOptional.get()), HttpStatus.OK);
	}
	
	@GetMapping(path = "bookingimports")
	public ResponseEntity<List<BookingImport>> getBookingImports() {
		return new ResponseEntity<List<BookingImport>>(bookingImportRepository.findAll(), HttpStatus.OK);		
	}
			
}