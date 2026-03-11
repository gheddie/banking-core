package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.RecurringPosition;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.RecurringPositionRepository;

@RestController
public class RecurringPositionController implements PersistableEntityController<RecurringPosition> {
	
	@Autowired
	private RecurringPositionRepository recurringPositionRepository;

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
	public ResponseEntity<String> delete(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<RecurringPosition> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<RecurringPosition> put(RecurringPosition entity) {
		// TODO Auto-generated method stub
		return null;
	}
}