package de.gravitex.banking_core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking.entity.CreditInstitute;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByCreditInstitute(CreditInstitute aCreditInstitute);
}