package de.gravitex.banking_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.base.BaseController;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;

@RestController
public class PurposeCategoryController implements BaseController<PurposeCategory> {
	
	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;

	@RequestMapping(value = "purposecategorys", method = RequestMethod.GET)
	public List<PurposeCategory> findAll() {
		return purposeCategoryRepository.findAll();
	}
}