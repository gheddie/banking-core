package de.gravitex.banking_core.dto;

import java.math.BigDecimal;

import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class BudgetPlanningItemDto extends BankingDto {

	private String purposeCategory;
	
	private BigDecimal quantity;
}