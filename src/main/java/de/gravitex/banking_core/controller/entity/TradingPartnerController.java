package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.repository.TradingPartnerRepository;

@RestController
public class TradingPartnerController implements PersistableEntityController<TradingPartner> {
	
	private Logger logger = LoggerFactory.getLogger(TradingPartnerController.class);
	
	@Autowired
	private TradingPartnerRepository tradingPartnerRepository;
	
	@RequestMapping(value = "tradingpartners", method = RequestMethod.GET)
	public ResponseEntity<List<TradingPartner>> findAll() {
		return new ResponseEntity<List<TradingPartner>>(tradingPartnerRepository.findAll(), HttpStatus.OK);
	}

	@PatchMapping(path = "tradingpartner")
	public ResponseEntity<TradingPartner> patch(@RequestBody TradingPartner entity) {
		logger.info("patching trading partner ["+entity+"]...");
		return new ResponseEntity<TradingPartner>(tradingPartnerRepository.save(entity), HttpStatus.OK);
	}

	@Override
	@DeleteMapping(path = "tradingpartner")
	public ResponseEntity<String> delete(@RequestParam("id") Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<TradingPartner> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PutMapping(path = "tradingpartner")
	public ResponseEntity<TradingPartner> put(@RequestBody TradingPartner entity) {
		tradingPartnerRepository.save(entity);
		return new ResponseEntity<TradingPartner>(entity, HttpStatus.OK);
	}
}