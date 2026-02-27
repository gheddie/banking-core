package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.StandingOrder;
import de.gravitex.banking_core.repository.StandingOrderRepository;

@RestController
public class StandingOrderController implements PersistableEntityController<StandingOrder> {
	
	@Autowired
	private StandingOrderRepository standingOrderRepository;

	@RequestMapping(value = "standingorders", method = RequestMethod.GET)
	public ResponseEntity<List<StandingOrder>> findAll() {
		return new ResponseEntity<List<StandingOrder>>(standingOrderRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<StandingOrder> patch(StandingOrder entity) {
		// TODO Auto-generated method stub
		return null;
	}
}