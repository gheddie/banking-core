package de.gravitex.banking_core.advice;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.gravitex.banking_core.exception.ImportTypeMandatoryException;
import de.gravitex.banking_core.exception.InvalidBicException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<String> handleException(DataAccessException aException) {
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
}