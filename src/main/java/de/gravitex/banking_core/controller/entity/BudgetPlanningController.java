package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.BudgetPlanning;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.dto.BudgetPlanningDto;
import de.gravitex.banking_core.dto.BudgetPlanningEvaluation;
import de.gravitex.banking_core.repository.BudgetPlanningRepository;
import de.gravitex.banking_core.service.BankingService;
import de.gravitex.banking_core.service.BudgetPlanningService;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BudgetPlanningController implements PersistableEntityController<BudgetPlanning> {
	
	@Autowired
	private BudgetPlanningService budgetPlanningService;
	
	@Autowired
	private BudgetPlanningRepository budgetPlanningRepository;
	
	@Autowired
	BankingService bankingService;
	
	@Autowired
	private DataIntegrityService integrityService;

	@Override
	@GetMapping(value = "budgetplannings")
	public ResponseEntity<List<BudgetPlanning>> findAll() {
		return new ResponseEntity<List<BudgetPlanning>>(budgetPlanningRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BudgetPlanning> patch(BudgetPlanning entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<BudgetPlanning> delete(@RequestParam("id") Long aEntityId) {
		Optional<BudgetPlanning> optional = budgetPlanningRepository.findById(aEntityId);		
		integrityService.assertOptionalPresent(optional, BudgetPlanning.class);
		BudgetPlanning entity = optional.get();
		budgetPlanningRepository.delete(entity);
		return new ResponseEntity<BudgetPlanning>(entity, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BudgetPlanning> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<BudgetPlanning> put(BudgetPlanning entity) {
		// TODO Auto-generated method stub
		return null;
	}
}