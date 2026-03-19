package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.TradingPartnerBookingHistory;

public interface TradingPartnerBookingHistoryRepository extends JpaRepository<TradingPartnerBookingHistory, Long> {

}