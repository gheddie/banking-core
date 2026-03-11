package de.gravitex.banking_core.controller.entity;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gravitex.banking_core.controller.entity.base.PersistableEntityController;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.exception.ImportTypeMandatoryException;
import de.gravitex.banking_core.exception.InvalidBicException;
import de.gravitex.banking_core.repository.CreditInstituteRepository;
import de.gravitex.banking_core.repository.util.PotientallyReferenced;
import de.gravitex.banking_core.service.DataIntegrityService;

@RestController
public class CreditInstituteController implements PersistableEntityController<CreditInstitute> {
	
	private Logger logger = LoggerFactory.getLogger(CreditInstituteController.class);
	
	Pattern BIC_PATTERN = Pattern.compile("[A-Z]{6}[A-Z0-9]{2,5}");
	
	@Autowired
	DataIntegrityService integrityService;

	@Autowired
	private CreditInstituteRepository creditInstituteRepository;

	@GetMapping(value = "creditinstitutes")
	public ResponseEntity<List<CreditInstitute>> findAll() {
		List<CreditInstitute> creditInstitutes = creditInstituteRepository.findAll();
		for (CreditInstitute aCreditInstitute : creditInstitutes) {
			checkBic(aCreditInstitute);
			checkImportType(aCreditInstitute);
		}
		return new ResponseEntity<List<CreditInstitute>>(creditInstitutes, HttpStatus.OK);
	}

	@PatchMapping(path = "creditinstitute")
	public ResponseEntity<CreditInstitute> patch(@RequestBody CreditInstitute aCreditInstitute) {
		checkBic(aCreditInstitute);
		checkImportType(aCreditInstitute);
		CreditInstitute saved = creditInstituteRepository.save(aCreditInstitute);
		return new ResponseEntity<CreditInstitute>(saved, HttpStatus.OK);
	}

	@Override
	@DeleteMapping(path = "creditinstitute")
	public ResponseEntity<String> delete(@RequestParam("id") Long aEntityId) {
		Optional<CreditInstitute> creditInstituteOptional = creditInstituteRepository.findById(aEntityId);
		integrityService.assertOptionalPresent(creditInstituteOptional);
		CreditInstitute aCreditInstitute = creditInstituteOptional.get();
		integrityService.satisfyPotientallyReferenced(PotientallyReferenced.forEntity(aCreditInstitute)
				.withPotentiallyReferringEntity(Account.class, "creditInstitute")).failForActualReferences();
		;				
		creditInstituteRepository.delete(aCreditInstitute);
		return new ResponseEntity<String>("Kredit-Institut {" + aCreditInstitute.getName() + "} gelöscht!!!",
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CreditInstitute> findById(Long aEntityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PutMapping(path = "creditinstitute")
	public ResponseEntity<CreditInstitute> put(@RequestBody CreditInstitute aCreditInstitute) {
		checkImportType(aCreditInstitute);
		checkBic(aCreditInstitute);
		creditInstituteRepository.save(aCreditInstitute);
		return new ResponseEntity<CreditInstitute>(aCreditInstitute, HttpStatus.OK);
	}

	private void checkBic(CreditInstitute aCreditInstitute) {
		String bic = aCreditInstitute.getBic();
		if (!BIC_PATTERN.matcher(bic).matches()) {
			throw new InvalidBicException("Invalid BIC detected --> " + bic);
		}
	}
	
	private void checkImportType(CreditInstitute aCreditInstitute) {
		if (aCreditInstitute.getImportType() == null) {
			throw new ImportTypeMandatoryException();
		}
	}
}