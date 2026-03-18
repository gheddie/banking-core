package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.RecurringPosition;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.RecurringPositionRepository;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class RecurringPositionController implements PersistableEntityController<RecurringPosition> {
	
	@Autowired
	private RecurringPositionRepository recurringPositionRepository;
	
	@Autowired
	private DataIntegrityService integrityService;

	@Override
	@GetMapping(value = "recurringpositions")
	public ResponseEntity<List<RecurringPosition>> findAll() {
		List<RecurringPosition> recurringPositions = recurringPositionRepository.findAll();
		return new ResponseEntity<List<RecurringPosition>>(recurringPositions, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RecurringPosition> patch(RecurringPosition entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DeleteMapping(path = "recurringposition")
	public ResponseEntity<RecurringPosition> delete(Long aRecurringPositionId) {
		Optional<RecurringPosition> recurringPositionOptional = recurringPositionRepository.findById(aRecurringPositionId);		
		integrityService.assertOptionalPresent(recurringPositionOptional, RecurringPosition.class);
		RecurringPosition recurringPosition = recurringPositionOptional.get();
		recurringPositionRepository.delete(recurringPosition);
		return new ResponseEntity<RecurringPosition>(recurringPosition, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RecurringPosition> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PutMapping(path = "recurringposition")
	public ResponseEntity<RecurringPosition> put(RecurringPosition entity) {
		return new ResponseEntity<RecurringPosition>(recurringPositionRepository.save(entity), HttpStatus.OK);
	}
}