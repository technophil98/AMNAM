package ui.dessin.superposition;

import scenario.events.Event;
import ui.dessin.Clickable;
import ui.dessin.Superposition;
import ui.panneaux.side_panels.SidePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
	
/**
 * La superposition qui contient les évènements de l'analyse.
 * @author Pascal Dally-Bélanger
 *
 */
public class EventSuperposition implements Superposition {

	private Event[] events;
	private double timestamp;
	private boolean isEnabled = true;
	
	/**
	 * Initialise la superposition.
	 * @param events La liste des évènements 
	 */
	public EventSuperposition(Event... events) {
		this.events = events;
	}
	
	@Override
	public void dessiner(Graphics2D g2d) {
		for(Event event : events)
			if(event.getDetectionTime() < timestamp && timestamp < event.getExpirationTime())
				event.dessiner(g2d);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		for(Event event : events)
			event.setTransform(transform);
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public SidePanel getPanneauConfiguration() {
		return null;
	}

	@Override
	public void setTemps(double t) {
		timestamp = t;
	}

	@Override
	public Clickable[] getClickables() {
		ArrayList<Clickable> list = new ArrayList<>();
		for(Event event : events)
			if(event.getDetectionTime() < timestamp && timestamp < event.getExpirationTime())
				list.add(event);
		return list.toArray(new Clickable[0]);
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public String getName() {
		return "\u00c9v\u00e8nements analys\u00e9s";
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

}
