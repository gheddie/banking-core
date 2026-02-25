package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.TradingPartner;

public interface TradingPartnerRepository extends JpaRepository<TradingPartner, Long> {

	TradingPartner findByTradingKey(String tradingKey);
}