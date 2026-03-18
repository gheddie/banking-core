package de.gravitex.banking_core.controller.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking.entity.BookingImport;
import de.gravitex.banking_core.dto.AccountInfo;
import de.gravitex.banking_core.dto.BookingAdminData;
import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.dto.BookingImportSummary;
import de.gravitex.banking_core.dto.BookingProgress;
import de.gravitex.banking_core.dto.BudgetPlanningDto;
import de.gravitex.banking_core.dto.BudgetPlanningEvaluation;
import de.gravitex.banking_core.dto.ImportBookings;
import de.gravitex.banking_core.dto.MergeTradingPartners;
import de.gravitex.banking_core.dto.TradingPartnersMergeResult;
import de.gravitex.banking_core.dto.UnprocessedBookingImport;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingImportRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.BudgetPlanningService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BankingLogicController {

	@Value("${spring.datasource.username}")
	private String datasourceName;
	
	@Value("${import.rootdir}")
	private String importRoot;
	
	@Value("${spring.datasource.url}")
	private String databaseUrl;
	
	@Value("${spring.datasource.driver.class}")
	private String databaseDriverClass;

	@Autowired
	private BankingService bankingService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BookingImportRepository bookingImportRepository;
	
	@Autowired
	private DataIntegrityService integrityService;

	@Autowired
	private BudgetPlanningService budgetPlanningService;

	@GetMapping(value = "bookingadmindata")
	public BookingAdminData getAdminData() {
		BookingAdminData bookingAdminData = new BookingAdminData();
		bookingAdminData.setDatasourceName(datasourceName);
		bookingAdminData.setImportRoot(importRoot);
		bookingAdminData.setDatabaseUrl(databaseUrl);
		bookingAdminData.setDatabaseDriverClass(databaseDriverClass);
		return bookingAdminData;
	}
	
	@PostMapping(path = "bookingprogress")
	public ResponseEntity<BookingProgress> acceptBudgetPlanning(@RequestBody BookingProgress aBookingProgress) {
		return new ResponseEntity<BookingProgress>(bankingService.createBookingProgress(aBookingProgress), HttpStatus.OK);
	}
	
	@PostMapping(path = "mergetradingpartners")
	public ResponseEntity<TradingPartnersMergeResult> mergeTradingPartners(@RequestBody MergeTradingPartners aMergetradingPartners) {
		return new ResponseEntity<TradingPartnersMergeResult>(bankingService.mergeTradingPartners(
				aMergetradingPartners.getPartnersToMerge(), aMergetradingPartners.getNewTradingKey()), HttpStatus.OK);
	}
	
	@PostMapping(path = "budgetplanning")
	public ResponseEntity<BudgetPlanningDto> acceptBudgetPlanning(@RequestBody BudgetPlanningDto aBudgetPlanningDto) {
		budgetPlanningService.processBudgetPlanning(aBudgetPlanningDto);
		return new ResponseEntity<BudgetPlanningDto>(aBudgetPlanningDto, HttpStatus.OK);
	}
	
	@GetMapping(value = "budgetplanningevaluation")
	public ResponseEntity<BudgetPlanningEvaluation> evaluateBudgetPlanning(@RequestParam("month") int month,
			@RequestParam("year") int year) {
		return new ResponseEntity<BudgetPlanningEvaluation>(bankingService.createBudgetPlanningEvaluation(month, year),
				HttpStatus.OK);
	}
	
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
	
	@GetMapping(value = "accountinfos")
	public ResponseEntity<List<AccountInfo>> getAccountinfos() {
		List<AccountInfo> accountInfos = bankingService.createAccountInfo();
		return new ResponseEntity<List<AccountInfo>>(
				accountInfos, HttpStatus.OK);
	}
}