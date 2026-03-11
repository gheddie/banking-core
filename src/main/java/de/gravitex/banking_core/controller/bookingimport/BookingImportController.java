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
import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BookingImportController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private DataIntegrityService integrityService;
	
	@Autowired
	BankingService bankingService;

	@GetMapping(path = "importbookings")
	public ResponseEntity<ImportBookings> importBookings(@RequestParam("accountId") Long accountId) {
		Optional<Account> accountOptional = accountRepository.findById(accountId);
		integrityService.assertOptionalPresent(accountOptional);
		List<BookingFileImportDto> importedBookings = bankingService.importBookingsForAccount(accountOptional.get());
		ImportBookings dto = new ImportBookings();
		dto.setImportedBookingFiles(importedBookings);
		return new ResponseEntity<ImportBookings>(dto, HttpStatus.OK);
	}
}