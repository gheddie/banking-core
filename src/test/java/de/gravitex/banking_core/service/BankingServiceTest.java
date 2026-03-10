package de.gravitex.banking_core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import de.gravitex.banking_core.BankingCoreApplication;
import de.gravitex.banking_core.dto.TradingPartnersMergeResult;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.CreditInstitute;
import de.gravitex.banking_core.entity.ImportType;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingRepository;
import de.gravitex.banking_core.repository.CreditInstituteRepository;
import de.gravitex.banking_core.repository.TradingPartnerRepository;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = BankingCoreApplication.class)
@Sql("/sql/insert_purpose_categories_h2.sql")
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
    
    @Autowired
    private TradingPartnerRepository tradingPartnerRepository;
    
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
    
    @Test
    public void testMergePurposeCategories() {    	    
    	
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
		
		int tradingPartnerCount = tradingPartnerRepository.findAll().size();
    	
    	List<TradingPartner> mcDonalds = findMcDonalds(tradingPartnerRepository.findAll());
		int mcdSize = mcDonalds.size(); 
    	
    	TradingPartnersMergeResult merge = bankingService.mergeTradingPartners(mcDonalds, "ABC");
    	
    	assertEquals(tradingPartnerCount + 1 - mcdSize, tradingPartnerRepository.findAll().size());
    	
    	// bookings transferred
		assertEquals(merge.getSwitchedBookings().size(),
				bookingRepository.findByTradingPartner(merge.getNewTradingPartner()).size());
    }

	private List<TradingPartner> findMcDonalds(List<TradingPartner> allTradingPartners) {
		List<TradingPartner> result = new ArrayList<>();
		for (TradingPartner aTradingPartner : allTradingPartners) {
			if (aTradingPartner.getTradingKey().contains("MCD")) {
				result.add(aTradingPartner);
			}
		}
		return result;
	}
}