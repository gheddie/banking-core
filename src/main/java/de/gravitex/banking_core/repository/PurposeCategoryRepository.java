package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.PurposeCategory;

public interface PurposeCategoryRepository extends JpaRepository<PurposeCategory, Long> {
	
}