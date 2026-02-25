package de.gravitex.banking_core.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByAccount(Account account);
	
	@Query("select max(b.bookingDate) from Booking b where account = :account")
	Date findMaxBookingDate(Account account);
}