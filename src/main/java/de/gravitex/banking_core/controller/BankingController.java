package de.gravitex.banking_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.entity.StandingOrder;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.entity.view.BookingView;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingViewRepository;
import de.gravitex.banking_core.repository.CreditInstituteRepository;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;
import de.gravitex.banking_core.repository.StandingOrderRepository;
import de.gravitex.banking_core.repository.TradingPartnerRepository;

@RestController
public class BankingController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CreditInstituteRepository creditInstituteRepository;

	@Autowired
	private TradingPartnerRepository tradingPartnerRepository;

	@Autowired
	private BookingViewRepository bookingViewRepository;

	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;

	@Autowired
	private StandingOrderRepository standingOrderRepository;

	@RequestMapping(value = "tradingpartners", method = RequestMethod.GET)
	List<TradingPartner> readTradingPartners() {
		return tradingPartnerRepository.findAll();
	}

	@RequestMapping(value = "creditinstitutes", method = RequestMethod.GET)
	List<CreditInstitute> readCreditInstitutes() {
		return creditInstituteRepository.findAll();
	}

	@RequestMapping(value = "purposecategorys", method = RequestMethod.GET)
	List<PurposeCategory> readPurposeCategorys() {
		return purposeCategoryRepository.findAll();
	}
	
	@RequestMapping(value = "standingorders", method = RequestMethod.GET)
	List<StandingOrder> readStandingOrders() {
		return standingOrderRepository.findAll();
	}

	@RequestMapping(value = "bookingviews/account", method = RequestMethod.GET)
	List<BookingView> readBookingViewsByAccount(@RequestParam("id") Long accountId) {
		return bookingViewRepository.findByAccountId(accountId);
	}
	
	@RequestMapping(value = "bookingviews/tradingpartner", method = RequestMethod.GET)
	List<BookingView> readBookingViewsByTradingPartner(@RequestParam("id") Long tradingPartnerId) {
		return bookingViewRepository.findByTradingPartnerId(tradingPartnerId);
	}
	
	@RequestMapping(value = "accounts/creditInstitute", method = RequestMethod.GET)
	List<Account> readAccounts(@RequestParam("id") Long creditInstituteId) {
		return accountRepository.findByCreditInstitute(creditInstituteRepository.findById(creditInstituteId).get());
	}
	
	@PatchMapping(path = "tradingpartners/{id}")
	public void patchTradingPartnerByPurposeCategoryId(
			@PathVariable(name = "id", required = true) Long tradingPartnerId,
			@RequestParam("purposeCategoryId") Long purposeCategoryId) {

		TradingPartner tradingPartner = tradingPartnerRepository.findById(tradingPartnerId).get();
		tradingPartner.setPurposeCategory(purposeCategoryRepository.findById(purposeCategoryId).get());
		tradingPartnerRepository.save(tradingPartner);
	}
}