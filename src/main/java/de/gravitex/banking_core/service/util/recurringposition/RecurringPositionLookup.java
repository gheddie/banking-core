package de.gravitex.banking_core.service.util.recurringposition;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.gravitex.banking.entity.Booking;
import de.gravitex.banking.enumerated.RecurringInterval;
import de.gravitex.banking_core.service.util.LocalDateRange;
import de.gravitex.banking_core.util.StringHelper;

public class RecurringPositionLookup {

	private static final int DEVIATION = 5;

	private static final int MAX_FAILURE_PERCENTAGE = 10;

	private static final int MIN_BOOKING_COUNT = 4;

	private List<Booking> bookings;
	
	private RecurringInterval recurringInterval;
	
	private LocalDate startBookingDate;
	private LocalDate endBookingDateOriginal;
	private LocalDate endBookingDateSafe;

	private List<LocalDateRange> ranges = new ArrayList<>();

	private int failureCounter = 0;

	private int failurePercentage;
	
	public RecurringPositionLookup(List<Booking> aBookings, RecurringInterval aRecurringInterval) {
		
		super();
		
		this.bookings = aBookings;
		this.recurringInterval = aRecurringInterval;
	}	

	private int calculateFailurePercentage() {
		
		Collections.sort(bookings, new BookingByDateComparator());
		startBookingDate = bookings.get(0).getBookingDate();
		endBookingDateOriginal = bookings.get(bookings.size()-1).getBookingDate();
		long daysBetween = ChronoUnit.DAYS.between(startBookingDate, endBookingDateOriginal);
		endBookingDateSafe = startBookingDate.plusDays(daysBetween * 2);
		putRanges();
		applyBookings();
		
		return StringHelper.getPercentage(failureCounter, bookings.size());
	}	
	
	private void applyBookings() {
		
		for (Booking aBooking : bookings) {
			if (!matchesRange(aBooking)) {
				failureCounter++;
			}
		}
	}

	private boolean matchesRange(Booking aBooking) {
		for (LocalDateRange aRange : ranges) {
			if (aRange.contains(aBooking.getBookingDate())) {
				return true;		
			}
		}
		return false;
	}

	private void putRanges() {
		
		LocalDate actualDate = startBookingDate;
		while (actualDate.isBefore(endBookingDateSafe)) {
			putRange(actualDate);
			actualDate = actualDate.plusMonths(recurringInterval.getMonthSpan());
		}
	}

	private void putRange(LocalDate aDate) {
		System.out.println("put range around --> " + aDate);
		ranges.add(LocalDateRange.around(aDate, DEVIATION));
	}

	private class BookingByDateComparator implements Comparator<Booking>  {
		@Override
		public int compare(Booking o1, Booking o2) {
			return o1.getBookingDate().compareTo(o2.getBookingDate()) * (1);
		}
	}

	public boolean isSuitable() {
		if (bookings == null || bookings.size() < MIN_BOOKING_COUNT) {
			return false;
		}
		failurePercentage = calculateFailurePercentage();
		boolean result = failurePercentage <= MAX_FAILURE_PERCENTAGE;
		return result;
	}
}