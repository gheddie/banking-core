package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.ViewEntityController;
import de.gravitex.banking_core.entity.view.BookingView;
import de.gravitex.banking_core.repository.BookingViewRepository;

@RestController
public class BookingViewController implements ViewEntityController<BookingView> {
	
	@Autowired
	private BookingViewRepository bookingViewRepository;

	@GetMapping(value = "bookingviews/account")
	public List<BookingView> findByAccount(@RequestParam("id") Long accountId) {
		return bookingViewRepository.findByAccountId(accountId);
	}
	
	@GetMapping(value = "bookingviews/tradingpartner")
	public List<BookingView> findByTradingPartner(@RequestParam("id") Long tradingPartnerId) {
		return bookingViewRepository.findByTradingPartnerId(tradingPartnerId);
	}

	@Override
	public ResponseEntity<List<BookingView>> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}