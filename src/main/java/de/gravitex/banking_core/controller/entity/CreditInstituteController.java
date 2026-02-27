package de.gravitex.banking_core.controller.entity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private Logger logger = LoggerFactory.getLogger(CreditInstituteController.class);

	@Autowired
	private CreditInstituteRepository creditInstituteRepository;

	@RequestMapping(value = "creditinstitutes", method = RequestMethod.GET)
	public ResponseEntity<List<CreditInstitute>> findAll() {
		return new ResponseEntity<List<CreditInstitute>>(creditInstituteRepository.findAll(), HttpStatus.OK);
	}

	@PatchMapping(path = "creditinstitute")
	public ResponseEntity<CreditInstitute> patch(@RequestBody CreditInstitute entity) {		
		try {
			CreditInstitute saved = creditInstituteRepository.save(entity);
			return new ResponseEntity<CreditInstitute>(saved, HttpStatus.OK);			
		} catch (Exception e) {
			logger.error("Fehler beim Speichern eines Kredit-Institutes ["+e.getMessage()+"]");
			return new ResponseEntity<CreditInstitute>(entity, HttpStatus.BAD_REQUEST);
		}
	}
}