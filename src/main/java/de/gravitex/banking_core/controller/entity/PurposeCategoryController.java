package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;

@RestController
public class PurposeCategoryController implements PersistableEntityController<PurposeCategory> {
	
	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;

	@GetMapping(value = "purposecategorys")
	public ResponseEntity<List<PurposeCategory>> findAll() {
		return new ResponseEntity<List<PurposeCategory>>(purposeCategoryRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PurposeCategory> patch(PurposeCategory entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@DeleteMapping(path = "purposecategory")
	public ResponseEntity<String> delete(@RequestParam("id") Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<PurposeCategory> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PutMapping(path = "purposecategory")
	public ResponseEntity<PurposeCategory> put(@RequestBody PurposeCategory entity) {
		purposeCategoryRepository.save(entity);
		return new ResponseEntity<PurposeCategory>(entity, HttpStatus.OK);
	}
}