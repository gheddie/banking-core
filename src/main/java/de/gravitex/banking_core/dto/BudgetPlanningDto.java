package de.gravitex.banking_core.dto;

import java.util.List;

import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class BudgetPlanningDto extends BankingDto {

	private int month;
	
	private int year;
	
	private List<BudgetPlanningItemDto> items;

	public String buildTimeKey() {
		return month + "/" + year;
	}

	public boolean hasValidMonth() {
		return (month >= 1 && month <= 12);
	}
}