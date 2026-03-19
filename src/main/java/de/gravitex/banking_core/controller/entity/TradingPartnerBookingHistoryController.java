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

import de.gravitex.banking.entity.TradingPartnerBookingHistory;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.TradingPartnerBookingHistoryRepository;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class TradingPartnerBookingHistoryController
		implements PersistableEntityController<TradingPartnerBookingHistory> {

	@Autowired
	private TradingPartnerBookingHistoryRepository tradingPartnerBookingHistoryRepository;
	
	@Autowired
	private DataIntegrityService integrityService;

	@Override
	public ResponseEntity<TradingPartnerBookingHistory> patch(TradingPartnerBookingHistory entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DeleteMapping(path = "tradingpartnerbookinghistory")
	public ResponseEntity<TradingPartnerBookingHistory> delete(@RequestParam("id") Long aEntityId) {
		Optional<TradingPartnerBookingHistory> optional = tradingPartnerBookingHistoryRepository.findById(aEntityId);		
		integrityService.assertOptionalPresent(optional, TradingPartnerBookingHistory.class);
		TradingPartnerBookingHistory history = optional.get();
		tradingPartnerBookingHistoryRepository.delete(history);
		return new ResponseEntity<TradingPartnerBookingHistory>(history, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TradingPartnerBookingHistory> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<TradingPartnerBookingHistory> put(TradingPartnerBookingHistory entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GetMapping(value = "tradingpartnerbookinghistorys")
	public ResponseEntity<List<TradingPartnerBookingHistory>> findAll() {
		List<TradingPartnerBookingHistory> histories = tradingPartnerBookingHistoryRepository.findAll();
		return new ResponseEntity<List<TradingPartnerBookingHistory>>(histories, HttpStatus.OK);
	}
}