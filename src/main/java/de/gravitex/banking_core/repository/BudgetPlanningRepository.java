package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.BudgetPlanning;

public interface BudgetPlanningRepository extends JpaRepository<BudgetPlanning, Long> {

	BudgetPlanning findByPlanningYearAndPlanningMonth(int aYear, int aMonth);	
}