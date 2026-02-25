package de.gravitex.banking_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.repository.CreditInstituteRepository;

@RestController
// public class CreditInstituteController implements BaseController<CreditInstitute> {
public class CreditInstituteController {
	
	@Autowired
	private CreditInstituteRepository creditInstituteRepository;

	@RequestMapping(value = "creditinstitutes", method = RequestMethod.GET)
	public List<CreditInstitute> findAll() {
		return creditInstituteRepository.findAll();
	}

	/*
	// @Override
	@PatchMapping(path = "creditinstitute")
	public void patch(@RequestParam("entity") CreditInstitute entity) {
		System.out.println("patching credit institute ["+entity+"]...");
	}
	*/
	
	@PatchMapping(path = "creditinstitute")
	public void patch(@RequestBody CreditInstitute entity) {
		System.out.println("patching credit institute ["+entity+"]...");
		creditInstituteRepository.save(entity);
	}
}