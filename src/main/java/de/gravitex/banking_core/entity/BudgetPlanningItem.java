package de.gravitex.banking_core.entity;

import java.math.BigDecimal;

import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "budget_planning_id", "purpose_category_id" }) })
public class BudgetPlanningItem extends IdEntity {

	@ManyToOne
	@JoinColumn(nullable = false)
	private BudgetPlanning budgetPlanning;

	@Column(nullable = false)
	private BigDecimal amount;

	@OneToOne
	@JoinColumn(nullable = false)
	private PurposeCategory purposeCategory;
}