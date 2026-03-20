package de.gravitex.banking_core.service.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.gravitex.banking_core.dto.BookingCurrentItem;

public class BookingCurrentModel {

	private LocalDate dateFrom;
	
	private LocalDate dateUntil;

	private Map<LocalDate, BookingCurrentItem> dateMap = new HashMap<>();

	public void initForRange(LocalDate aDateFrom, LocalDate aDateUntil) {
		
		this.dateFrom = aDateFrom;
		this.dateUntil = aDateUntil;
		
		for (LocalDate aDate : aDateFrom.datesUntil(aDateUntil).collect(Collectors.toList())) {			
			BookingCurrentItem item = new BookingCurrentItem();
			item.setDate(aDate);
			item.setAmount(BigDecimal.ZERO);
			dateMap.put(aDate, item);
		} 
	}

	public void accept(LocalDate aBookingDate, BigDecimal amount) {		
		BookingCurrentItem item = dateMap.get(aBookingDate);
		if (item != null) {
			item.acceptAmount(amount);	
		}
	}

	public List<BookingCurrentItem> getItemsSorted(boolean aFilterEmptyValues) {
		
		List<LocalDate> dates = new ArrayList<>(dateMap.keySet());
		Collections.sort(dates);
		List<BookingCurrentItem> result = new ArrayList<>();
		for (LocalDate date : dates) {
			BookingCurrentItem item = dateMap.get(date);
			if (processItem(item, aFilterEmptyValues)) {
				result.add(item);	
			}					
		}
		return result;
	}

	private boolean processItem(BookingCurrentItem item, boolean aFilterEmptyValues) {
		if (!aFilterEmptyValues) {
			return true;	
		}
		return item.hasAmount();
	}
}