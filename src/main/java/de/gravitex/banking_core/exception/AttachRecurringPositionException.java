package de.gravitex.banking_core.exception;

import de.gravitex.banking.entity.TradingPartner;
import de.gravitex.banking_core.exception.base.BankingException;
import de.gravitex.banking_core.service.util.ShortestBookingInterval;

public class AttachRecurringPositionException extends BankingException {

	private static final long serialVersionUID = 8721789414333601636L;

	public AttachRecurringPositionException(TradingPartner aTradingPartner, ShortestBookingInterval aShortestInterval) {
		super(buildMessage(aTradingPartner, aShortestInterval));
	}

	public AttachRecurringPositionException(TradingPartner aTradingPartner, String aMessage) {
		super(buildMessage(aTradingPartner, aMessage));
	}

	private static String buildMessage(TradingPartner aTradingPartner, String aMessage) {
		return "cannot attach recurring position {" + aTradingPartner.getRecurringPosition() + "} to trading partner {"
				+ aTradingPartner + "} --> " + aMessage;
	}

	private static String buildMessage(TradingPartner aTradingPartner, ShortestBookingInterval aShortestInterval) {
		return "cannot attach recurring position {" + aTradingPartner.getRecurringPosition() + "} to trading partner {"
				+ aTradingPartner + "} --> unsuitable shortest booking interval detected {" + aShortestInterval
				+ "}!!!";
	}
}