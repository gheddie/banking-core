package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.TradingPartner;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.dto.MergeTradingPartners;
import de.gravitex.banking_core.dto.TradingPartnersMergeResult;
import de.gravitex.banking_core.repository.TradingPartnerRepository;
import de.gravitex.banking_core.repository.util.PotientallyReferenced;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class TradingPartnerController implements PersistableEntityController<TradingPartner> {
	
	private Logger logger = LoggerFactory.getLogger(TradingPartnerController.class);
	
	@Autowired
	private TradingPartnerRepository tradingPartnerRepository;
	
	@Autowired
	BankingService bankingService;

	@Autowired
	private DataIntegrityService integrityService;
	
	@GetMapping(value = "tradingpartners")
	public ResponseEntity<List<TradingPartner>> findAll() {
		return new ResponseEntity<List<TradingPartner>>(tradingPartnerRepository.findAll(), HttpStatus.OK);
	}

	@PatchMapping(path = "tradingpartner")
	public ResponseEntity<TradingPartner> patch(@RequestBody TradingPartner aTradingPartner) {
		logger.info("patching trading partner ["+aTradingPartner+"]...");
		bankingService.checkAttachRecurringPosition(aTradingPartner);
		return new ResponseEntity<TradingPartner>(tradingPartnerRepository.save(aTradingPartner), HttpStatus.OK);
	}

	@Override
	@DeleteMapping(path = "tradingpartner")
	public ResponseEntity<TradingPartner> delete(@RequestParam("id") Long aTradingPartnerId) {
		
		Optional<TradingPartner> tradingPartnerOptional = tradingPartnerRepository.findById(aTradingPartnerId);
		integrityService.assertOptionalPresent(tradingPartnerOptional, TradingPartner.class);
		TradingPartner tradingPartner = tradingPartnerOptional.get();
		integrityService.satisfyPotientallyReferenced(PotientallyReferenced.forEntity(tradingPartner)
				.autoDetectPotentiallyReferrings(integrityService.getEntityTypes())).failForActualReferences();
		tradingPartnerRepository.delete(tradingPartner);
		return new ResponseEntity<TradingPartner>(tradingPartner, HttpStatus.OK);
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