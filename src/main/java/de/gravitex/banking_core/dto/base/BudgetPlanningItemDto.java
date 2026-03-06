package de.gravitex.banking_core.dto.base;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BudgetPlanningItemDto {

	private String purposeCategory;
	
	private BigDecimal quantity;
}