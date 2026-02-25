package de.gravitex.banking_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.base.BaseController;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.CreditInstituteRepository;

@RestController
public class AccountController implements BaseController<Account> {
	
	@Autowired
	private CreditInstituteRepository creditInstituteRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@RequestMapping(value = "accounts/creditInstitute", method = RequestMethod.GET)
	public List<Account> findByCreditInstitute(@RequestParam("id") Long creditInstituteId) {
		return accountRepository.findByCreditInstitute(creditInstituteRepository.findById(creditInstituteId).get());
	}

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}