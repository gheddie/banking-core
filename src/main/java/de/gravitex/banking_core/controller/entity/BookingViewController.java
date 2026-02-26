package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.ViewEntityController;
import de.gravitex.banking_core.entity.view.BookingView;
import de.gravitex.banking_core.repository.BookingViewRepository;

@RestController
public class BookingViewController implements ViewEntityController<BookingView> {
	
	@Autowired
	private BookingViewRepository bookingViewRepository;

	@RequestMapping(value = "bookingviews/account", method = RequestMethod.GET)
	public List<BookingView> findByAccount(@RequestParam("id") Long accountId) {
		return bookingViewRepository.findByAccountId(accountId);
	}
	
	@RequestMapping(value = "bookingviews/tradingpartner", method = RequestMethod.GET)
	public List<BookingView> findByTradingPartner(@RequestParam("id") Long tradingPartnerId) {
		return bookingViewRepository.findByTradingPartnerId(tradingPartnerId);
	}

	@Override
	public List<BookingView> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}