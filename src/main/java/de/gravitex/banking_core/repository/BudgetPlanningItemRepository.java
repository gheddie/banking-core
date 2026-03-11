package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.BudgetPlanningItem;

public interface BudgetPlanningItemRepository extends JpaRepository<BudgetPlanningItem, Long> {

}