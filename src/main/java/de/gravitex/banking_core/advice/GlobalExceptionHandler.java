package de.gravitex.banking_core.advice;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.gravitex.banking_core.dto.MergeTradingPartners;
import de.gravitex.banking_core.exception.BookingImportException;
import de.gravitex.banking_core.exception.BudgetPlanningException;
import de.gravitex.banking_core.exception.ImportDirectoryMandatoryException;
import de.gravitex.banking_core.exception.ImportTypeMandatoryException;
import de.gravitex.banking_core.exception.InvalidBicException;
import de.gravitex.banking_core.exception.MergeTradingPartnersException;
import de.gravitex.banking_core.exception.OptionalNotPresentException;
import de.gravitex.banking_core.exception.ReferingEntitiesException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<String> handleException(DataAccessException aException) {
		aException.printStackTrace();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DataAccessException occured...");
	}
	
	// --- custom logic
	
	@ExceptionHandler(InvalidBicException.class)
	public ResponseEntity<String> handleException(InvalidBicException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(ImportTypeMandatoryException.class)
	public ResponseEntity<String> handleException(ImportTypeMandatoryException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(ImportDirectoryMandatoryException.class)
	public ResponseEntity<String> handleException(ImportDirectoryMandatoryException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(ReferingEntitiesException.class)
	public ResponseEntity<String> handleException(ReferingEntitiesException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(OptionalNotPresentException.class)
	public ResponseEntity<String> handleException(OptionalNotPresentException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(BudgetPlanningException.class)
	public ResponseEntity<String> handleException(BudgetPlanningException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(BookingImportException.class)
	public ResponseEntity<String> handleException(BookingImportException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
	
	@ExceptionHandler(MergeTradingPartnersException.class)
	public ResponseEntity<String> handleException(MergeTradingPartnersException aException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aException.getMessage());
	}
}