package de.gravitex.banking_core.dto.base;

import java.util.List;

import lombok.Data;

@Data
public class BudgetPlanningDto {

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