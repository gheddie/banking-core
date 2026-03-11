package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.CreditInstitute;

public interface CreditInstituteRepository extends JpaRepository<CreditInstitute, Long> {

}