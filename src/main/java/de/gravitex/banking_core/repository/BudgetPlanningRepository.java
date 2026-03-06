package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.BudgetPlanning;

public interface BudgetPlanningRepository extends JpaRepository<BudgetPlanning, Long> {

	BudgetPlanning findByYearAndMonth(int aYear, int aMonth);	
}