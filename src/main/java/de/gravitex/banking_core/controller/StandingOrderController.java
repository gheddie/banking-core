package de.gravitex.banking_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.base.BaseController;
import de.gravitex.banking_core.entity.StandingOrder;
import de.gravitex.banking_core.repository.StandingOrderRepository;

@RestController
public class StandingOrderController implements BaseController<StandingOrder> {
	
	@Autowired
	private StandingOrderRepository standingOrderRepository;

	@RequestMapping(value = "standingorders", method = RequestMethod.GET)
	public List<StandingOrder> findAll() {
		return standingOrderRepository.findAll();
	}
}