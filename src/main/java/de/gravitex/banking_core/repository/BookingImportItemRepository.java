package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.BookingImport;
import de.gravitex.banking_core.entity.BookingImportItem;

public interface BookingImportItemRepository extends JpaRepository<BookingImportItem, Long> {

}