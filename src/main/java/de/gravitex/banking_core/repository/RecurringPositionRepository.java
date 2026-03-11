package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.RecurringPosition;

public interface RecurringPositionRepository extends JpaRepository<RecurringPosition, Long> {

}