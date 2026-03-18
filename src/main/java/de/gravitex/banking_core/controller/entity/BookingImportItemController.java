package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking.entity.BookingImportItem;
import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.repository.BookingImportItemRepository;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class BookingImportItemController implements PersistableEntityController<BookingImportItem>  {
	
	@Autowired
	private BookingImportItemRepository bookingImportItemRepository;
	
	@Autowired
	private DataIntegrityService integrityService;

	@Override
	public ResponseEntity<BookingImportItem> patch(BookingImportItem entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DeleteMapping(path = "bookingimportitem")
	public ResponseEntity<BookingImportItem> delete(@RequestParam("id") Long aEntityId) {
		Optional<BookingImportItem> optional = bookingImportItemRepository.findById(aEntityId);		
		integrityService.assertOptionalPresent(optional, BookingImportItem.class);
		BookingImportItem entity = optional.get();
		bookingImportItemRepository.delete(entity);
		return new ResponseEntity<BookingImportItem>(entity, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BookingImportItem> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<BookingImportItem> put(BookingImportItem entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GetMapping("bookingimportitems")
	public ResponseEntity<List<BookingImportItem>> findAll() {
		return new ResponseEntity<List<BookingImportItem>>(bookingImportItemRepository.findAll(), HttpStatus.OK);
	}
}