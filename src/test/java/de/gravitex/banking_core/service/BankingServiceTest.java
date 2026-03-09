package de.gravitex.banking_core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.gravitex.banking_core.BankingCoreApplication;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.entity.ImportType;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingRepository;
import de.gravitex.banking_core.repository.CreditInstituteRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = BankingCoreApplication.class)
// @Sql("/generate_schema_h2.sql")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BankingServiceTest {

    @Autowired
    private CreditInstituteRepository creditInstituteRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private BankingService bankingService;
    
    @Test
    public void testImportBookingsUnquotedCsv() {
    	
    	CreditInstitute creditInstitute = new CreditInstitute();
    	creditInstitute.setBic("GENODEV1WBU");
    	creditInstitute.setName("Volksbank Wendeburg");
    	creditInstitute.setImportType(ImportType.CSV_VB);
    	
    	creditInstituteRepository.save(creditInstitute);		
		assertEquals(1, creditInstituteRepository.findAll().size());
		
		Account account = new Account();
		account.setCreditInstitute(creditInstitute);
		account.setName("Giro-Konto");
		account.setIdentifier("VOBA_GIRO");
		accountRepository.save(account);	
		
		bankingService.importBookingsForAccount(account);
		
		assertEquals(237, bookingRepository.findAll().size());
    }
    
    @Test
    public void testImportBookingsQuotedCsv() {
    	
    	CreditInstitute creditInstitute = new CreditInstitute();
    	creditInstitute.setBic("GENODEV2WBU");
    	creditInstitute.setName("KSK Peine");
    	creditInstitute.setImportType(ImportType.CSV_KSK);
    	
    	creditInstituteRepository.save(creditInstitute);		
		assertEquals(1, creditInstituteRepository.findAll().size());
		
		Account account = new Account();
		account.setCreditInstitute(creditInstitute);
		account.setName("Giro-Konto");
		account.setIdentifier("KSK_GIRO");
		accountRepository.save(account);	
		
		bankingService.importBookingsForAccount(account);
		
		assertEquals(98, bookingRepository.findAll().size());
    }
}