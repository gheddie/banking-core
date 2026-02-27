package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;

@RestController
public class PurposeCategoryController implements PersistableEntityController<PurposeCategory> {
	
	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;

	@RequestMapping(value = "purposecategorys", method = RequestMethod.GET)
	public ResponseEntity<List<PurposeCategory>> findAll() {
		return new ResponseEntity<List<PurposeCategory>>(purposeCategoryRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PurposeCategory> patch(PurposeCategory entity) {
		// TODO Auto-generated method stub
		return null;
	}
}