package de.gravitex.banking_core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.CreditInstitute;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByCreditInstitute(CreditInstitute aCreditInstitute);
}