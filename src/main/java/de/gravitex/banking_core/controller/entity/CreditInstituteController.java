package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.repository.CreditInstituteRepository;

@RestController
public class CreditInstituteController implements PersistableEntityController<CreditInstitute> {

	@Autowired
	private CreditInstituteRepository creditInstituteRepository;

	@RequestMapping(value = "creditinstitutes", method = RequestMethod.GET)
	public ResponseEntity<List<CreditInstitute>> findAll() {
		return new ResponseEntity<List<CreditInstitute>>(creditInstituteRepository.findAll(), HttpStatus.OK);
	}

	@PatchMapping(path = "creditinstitute")
	public ResponseEntity<CreditInstitute> patch(@RequestBody CreditInstitute entity) {
		System.out.println("patching credit institute [" + entity + "]...");
		return new ResponseEntity<CreditInstitute>(creditInstituteRepository.save(entity), HttpStatus.OK);
	}
}