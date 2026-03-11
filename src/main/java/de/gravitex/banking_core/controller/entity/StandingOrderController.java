package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.StandingOrder;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.StandingOrderRepository;

@RestController
public class StandingOrderController implements PersistableEntityController<StandingOrder> {
	
	@Autowired
	private StandingOrderRepository standingOrderRepository;

	@GetMapping(value = "standingorders")
	public ResponseEntity<List<StandingOrder>> findAll() {
		return new ResponseEntity<List<StandingOrder>>(standingOrderRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<StandingOrder> patch(StandingOrder entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DeleteMapping(path = "standingorder")
	public ResponseEntity<String> delete(@RequestParam("id") Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<StandingOrder> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<StandingOrder> put(StandingOrder entity) {
		// TODO Auto-generated method stub
		return null;
	}
}