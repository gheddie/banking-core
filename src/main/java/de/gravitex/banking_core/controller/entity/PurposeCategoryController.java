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
import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;
import de.gravitex.banking_core.repository.util.PotientallyReferenced;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class PurposeCategoryController implements PersistableEntityController<PurposeCategory> {
	
	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;
	
	@Autowired
	DataIntegrityService integrityService;

	@GetMapping(value = "purposecategorys")
	public ResponseEntity<List<PurposeCategory>> findAll() {
		return new ResponseEntity<List<PurposeCategory>>(purposeCategoryRepository.findAll(), HttpStatus.OK);
	}

	@Override
	@PatchMapping(path = "purposecategory")
	public ResponseEntity<PurposeCategory> patch(@RequestBody PurposeCategory entity) {
		PurposeCategory saved = purposeCategoryRepository.save(entity);
		return new ResponseEntity<PurposeCategory>(saved, HttpStatus.OK);
	}

	@Override
	@DeleteMapping(path = "purposecategory")
	public ResponseEntity<String> delete(@RequestParam("id") Long aPurposecategoryId) {				
		Optional<PurposeCategory> purposeCategoryOptional = purposeCategoryRepository.findById(aPurposecategoryId);		
		integrityService.assertOptionalPresent(purposeCategoryOptional);
		PurposeCategory aPurposeCategory = purposeCategoryOptional.get();
		integrityService.testAndFailReferringEntities(PotientallyReferenced.forEntity(aPurposeCategory)
				.withPotentiallyReferringEntity(Booking.class, "purposeCategory")
				.withPotentiallyReferringEntity(TradingPartner.class, "purposeCategory"));
		purposeCategoryRepository.delete(aPurposeCategory);
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PurposeCategory> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PutMapping(path = "purposecategory")
	public ResponseEntity<PurposeCategory> put(@RequestBody PurposeCategory entity) {
		purposeCategoryRepository.save(entity);
		return new ResponseEntity<PurposeCategory>(entity, HttpStatus.OK);
	}
}