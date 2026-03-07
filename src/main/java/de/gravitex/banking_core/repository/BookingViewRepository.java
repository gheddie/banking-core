package de.gravitex.banking_core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.view.BookingView;

public interface BookingViewRepository extends JpaRepository<BookingView, Long> {

	List<BookingView> findByAccountId(Long accountId);
	
	List<BookingView> findByTradingPartnerId(Long tradingPartnerId);
}