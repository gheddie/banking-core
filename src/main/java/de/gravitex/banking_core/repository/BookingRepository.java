package de.gravitex.banking_core.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking.entity.Booking;
import de.gravitex.banking.entity.PurposeCategory;
import de.gravitex.banking.entity.TradingPartner;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByAccount(Account account);
	
	@Query("select max(b.bookingDate) from Booking b where account = :account")
	LocalDate findLatestBookingDate(Account account);

	List<Booking> findByAccountAndBookingDateOrderByAmountAfterBookingDesc(Account account, LocalDate aookingDate);

	List<Booking> findByPurposeCategory(PurposeCategory aPurposeCategory);
	
	List<Booking> findByTradingPartner(TradingPartner aTradingPartner);

	@Query("select b from Booking b where (bookingDate >= :aFrom or bookingDate = :aFrom) and (bookingDate <= :aTo or bookingDate = :aTo)")
	List<Booking> findBookingsInRange(LocalDate aFrom, LocalDate aTo);
}