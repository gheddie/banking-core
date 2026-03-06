package de.gravitex.banking_core.controller.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.dto.base.BudgetPlanningDto;
import de.gravitex.banking_core.service.BudgetPlanningService;

@RestController
public class BudgetPlanningController {
	
	@Autowired
	private BudgetPlanningService budgetPlanningService;

	@PostMapping(path = "budgetplanning")
	public ResponseEntity<BudgetPlanningDto> acceptBudgetPlanning(@RequestBody BudgetPlanningDto aBudgetPlanningDto) {
		budgetPlanningService.processBudgetPlanning(aBudgetPlanningDto);
		return new ResponseEntity<BudgetPlanningDto>(aBudgetPlanningDto, HttpStatus.OK);
	}
}