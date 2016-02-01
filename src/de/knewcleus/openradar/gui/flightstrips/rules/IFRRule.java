package de.knewcleus.openradar.gui.flightstrips.rules;

import java.util.ArrayList;

import org.jdom2.Element;

import de.knewcleus.openradar.gui.flightplan.FlightPlanData;
import de.knewcleus.openradar.gui.flightstrips.FlightStrip;
import de.knewcleus.openradar.gui.flightstrips.LogicManager;

public class IFRRule extends AbstractRule {

	private final boolean isIFR;
	
	public IFRRule(boolean isIFR) {
		this.isIFR = isIFR;
	}
	
	public IFRRule(Element element, LogicManager logic) {
		this.isIFR = Boolean.valueOf(element.getAttributeValue("isifr"));
	}
	
	@Override
	public boolean isAppropriate(FlightStrip flightstrip) {
		FlightPlanData flightplan = flightstrip.getContact().getFlightPlan();
		return (flightplan != null) && (flightplan.getType().equals(FlightPlanData.FlightType.IFR.toString()) == isIFR);
	}

	@Override
	public ArrayList<String> getRuleText() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("contact is " + (isIFR ? "" : "not") + " IFR.");
		return result;
	}

	// --- IDomElement ---
	
	@Override
	public void putAttributes(Element element) {
		super.putAttributes(element);
		element.setAttribute("isifr", String.valueOf(isIFR));
	}

}