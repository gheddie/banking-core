package de.gravitex.banking_core.entity;

import java.util.List;

import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "month", "year" }) })
public class BudgetPlanning extends IdEntity {

	private int month;
	
	private int year;
	
	@OneToMany(mappedBy = "budgetPlanning")
	private List<BudgetPlanningItem> budgetPlanningItems;
}