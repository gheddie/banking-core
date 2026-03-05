package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.exception.InvalidBicException;
import de.gravitex.banking_core.repository.CreditInstituteRepository;

@RestController
public class CreditInstituteController implements PersistableEntityController<CreditInstitute> {
	
	private Logger logger = LoggerFactory.getLogger(CreditInstituteController.class);
	
	Pattern BIC_PATTERN = Pattern.compile("[A-Z]{6}[A-Z0-9]{2,5}");

	@Autowired
	private CreditInstituteRepository creditInstituteRepository;

	@RequestMapping(value = "creditinstitutes", method = RequestMethod.GET)
	public ResponseEntity<List<CreditInstitute>> findAll() {
		List<CreditInstitute> creditInstitutes = creditInstituteRepository.findAll();
		for (CreditInstitute aCreditInstitute : creditInstitutes) {
			checkBic(aCreditInstitute);
		}
		return new ResponseEntity<List<CreditInstitute>>(creditInstitutes, HttpStatus.OK);
	}

	@PatchMapping(path = "creditinstitute")
	public ResponseEntity<CreditInstitute> patch(@RequestBody CreditInstitute entity) {
		checkBic(entity);
		CreditInstitute saved = creditInstituteRepository.save(entity);
		return new ResponseEntity<CreditInstitute>(saved, HttpStatus.OK);
	}

	@Override
	@DeleteMapping(path = "creditinstitute")
	public ResponseEntity<String> delete(@RequestParam("id") Long aEntityId) {
		creditInstituteRepository.delete(creditInstituteRepository.findById(aEntityId).get());
		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CreditInstitute> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PutMapping(path = "creditinstitute")
	public ResponseEntity<CreditInstitute> put(@RequestBody CreditInstitute entity) {
		checkBic(entity);
		creditInstituteRepository.save(entity);
		return new ResponseEntity<CreditInstitute>(entity, HttpStatus.OK);
	}

	private void checkBic(CreditInstitute aCreditInstitute) {
		String bic = aCreditInstitute.getBic();
		if (!BIC_PATTERN.matcher(bic).matches()) {
			throw new InvalidBicException("Invalid BIC detected --> " + bic);
		}
	}
}