package de.knewcleus.openradar.gui.flightstrips.rules;

import java.util.ArrayList;

import de.knewcleus.openradar.gui.flightstrips.FlightStrip;

/* This rule is true for any contact
 * 
 */
public class AnyRule extends AbstractRule {

	@Override
	public Boolean isAppropriate(FlightStrip flightstrip) {
		return true;
	}

	@Override
	public ArrayList<String> getText() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("any contact.");
		return result;
	}

}
