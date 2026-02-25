package de.gravitex.banking_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gravitex.banking_core.entity.BookingImport;

public interface BookingImportRepository extends JpaRepository<BookingImport, Long> {

}