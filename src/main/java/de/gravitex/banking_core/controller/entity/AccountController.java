package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.CreditInstituteRepository;

@RestController
public class AccountController implements PersistableEntityController<Account> {
	
	@Autowired
	private CreditInstituteRepository creditInstituteRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@RequestMapping(value = "accounts/creditInstitute", method = RequestMethod.GET)
	public List<Account> findByCreditInstitute(@RequestParam("id") Long creditInstituteId) {
		return accountRepository.findByCreditInstitute(creditInstituteRepository.findById(creditInstituteId).get());
	}

	@Override
	@RequestMapping(value = "accounts", method = RequestMethod.GET)
	public ResponseEntity<List<Account>> findAll() {
		return new ResponseEntity<List<Account>>(accountRepository.findAll(), HttpStatus.OK);
	}

	@Override
	@PatchMapping(path = "account")
	public ResponseEntity<Account> patch(@RequestBody Account entity) {
		return new ResponseEntity<Account>(accountRepository.save(entity), HttpStatus.OK);
	}
}