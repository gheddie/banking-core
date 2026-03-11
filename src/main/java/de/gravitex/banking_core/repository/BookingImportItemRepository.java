package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking.entity.BookingImportItem;

public interface BookingImportItemRepository extends JpaRepository<BookingImportItem, Long> {

}