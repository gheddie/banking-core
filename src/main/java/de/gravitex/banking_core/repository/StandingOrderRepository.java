package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.StandingOrder;

public interface StandingOrderRepository extends JpaRepository<StandingOrder, Long> {
	
}