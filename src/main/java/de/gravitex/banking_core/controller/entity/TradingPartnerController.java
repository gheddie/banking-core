package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;
import de.gravitex.banking_core.repository.TradingPartnerRepository;

@RestController
public class TradingPartnerController implements PersistableEntityController<TradingPartner> {
	
	@Autowired
	private TradingPartnerRepository tradingPartnerRepository;
	
	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;

	@PatchMapping(path = "tradingpartners/{id}")
	public void patchTradingPartnerByPurposeCategoryId(
			@PathVariable(name = "id", required = true) Long tradingPartnerId,
			@RequestParam("purposeCategoryId") Long purposeCategoryId) {

		TradingPartner tradingPartner = tradingPartnerRepository.findById(tradingPartnerId).get();
		tradingPartner.setPurposeCategory(purposeCategoryRepository.findById(purposeCategoryId).get());
		tradingPartnerRepository.save(tradingPartner);
	}
	
	@RequestMapping(value = "tradingpartners", method = RequestMethod.GET)
	public List<TradingPartner> findAll() {
		return tradingPartnerRepository.findAll();
	}

	@PatchMapping(path = "tradingpartner")
	public void patch(@RequestBody TradingPartner entity) {
		System.out.println("patching trading partner ["+entity+"]...");
		tradingPartnerRepository.save(entity);
	}
}