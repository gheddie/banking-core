package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.BudgetPlanningItem;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.BudgetPlanningItemRepository;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BudgetPlanningItemController implements PersistableEntityController<BudgetPlanningItem> {
	
	@Autowired
	private BudgetPlanningItemRepository budgetPlanningItemRepository;
	
	@Autowired
	private DataIntegrityService integrityService;

	@Override
	public ResponseEntity<BudgetPlanningItem> patch(BudgetPlanningItem entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DeleteMapping(path = "budgetplanningitem")
	public ResponseEntity<BudgetPlanningItem> delete(@RequestParam("id") Long aEntityId) {
		Optional<BudgetPlanningItem> budgetPlanningItemOptional = budgetPlanningItemRepository.findById(aEntityId);		
		integrityService.assertOptionalPresent(budgetPlanningItemOptional, BudgetPlanningItem.class);
		BudgetPlanningItem budgetPlanningItem = budgetPlanningItemOptional.get();
		budgetPlanningItemRepository.delete(budgetPlanningItem);
		return new ResponseEntity<BudgetPlanningItem>(budgetPlanningItem, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BudgetPlanningItem> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<BudgetPlanningItem> put(BudgetPlanningItem entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GetMapping("budgetplanningitems")
	public ResponseEntity<List<BudgetPlanningItem>> findAll() {
		return new ResponseEntity<List<BudgetPlanningItem>>(budgetPlanningItemRepository.findAll(), HttpStatus.OK);
	}
}