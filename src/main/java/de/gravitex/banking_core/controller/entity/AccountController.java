package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.dto.AccountInfo;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.exception.ImportTypeMandatoryException;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.CreditInstituteRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class AccountController implements PersistableEntityController<Account> {
	
	@Autowired
	private CreditInstituteRepository creditInstituteRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	BankingService bankingService;

	@Autowired
	private DataIntegrityService integrityService;

	@GetMapping(value = "accounts/creditInstitute")
	public List<Account> findByCreditInstitute(@RequestParam("id") Long creditInstituteId) {
		return accountRepository.findByCreditInstitute(creditInstituteRepository.findById(creditInstituteId).get());
	}

	@Override
	@GetMapping(value = "accounts")
	public ResponseEntity<List<Account>> findAll() {
		List<Account> accounts = accountRepository.findAll();
		for (Account account : accounts) {
			checkImportType(account);
		}
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
	}

	@Override
	@PatchMapping(path = "account")
	public ResponseEntity<Account> patch(@RequestBody Account account) {
		checkImportType(account);
		return new ResponseEntity<Account>(accountRepository.save(account), HttpStatus.OK);		
	}

	@Override
	@DeleteMapping(path = "account")
	public ResponseEntity<String> delete(@RequestParam("id") Long accountId) {
		Optional<Account> accountOptional = accountRepository.findById(accountId);		
		integrityService.assertOptionalPresent(accountOptional);
		accountRepository.delete(accountOptional.get());
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Override
	@GetMapping(path = "account")
	public ResponseEntity<Account> findById(@RequestParam("id") Long accountId) {
		Account account = accountRepository.findById(accountId).get();
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}

	@Override
	@PutMapping(path = "account")
	public ResponseEntity<Account> put(@RequestBody Account account) {
		checkImportType(account);
		bankingService.checkForImportDirectory(account);
		accountRepository.save(account);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
	
	@GetMapping(value = "accountinfos")
	public ResponseEntity<List<AccountInfo>> getAccountinfos() {
		List<AccountInfo> accountInfos = bankingService.createAccountInfo();
		return new ResponseEntity<List<AccountInfo>>(
				accountInfos, HttpStatus.OK);
	}

	private void checkImportType(Account account) {
		if (account.getImportType() == null) {
			throw new ImportTypeMandatoryException();
		}
	}
}