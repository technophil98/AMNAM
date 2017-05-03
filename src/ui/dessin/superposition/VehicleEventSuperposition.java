package ui.dessin.superposition;

import scenario.events.VehicleEvent;
import ui.dessin.Clickable;
import ui.dessin.Superposition;
import ui.panneaux.side_panels.SidePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
	
/**
 * La superposition qui contient les évènements des véhicules.
 * @author Pascal Dally-Bélanger
 *
 */
public class VehicleEventSuperposition implements Superposition {

	private VehicleEvent[] events;
	private double timestamp;
	private boolean isEnabled = true;
	
	/**
	 * Initialise la superposition.
	 * @param events La liste des évènements 
	 */
	public VehicleEventSuperposition(VehicleEvent... events) {
		this.events = events;
	}
	
	@Override
	public void dessiner(Graphics2D g2d) {
		for(VehicleEvent event : events)
			event.dessiner(g2d);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		for(VehicleEvent event : events)
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
		for(VehicleEvent event : events)
			event.setTime(t);
	}

	@Override
	public Clickable[] getClickables() {
		ArrayList<Clickable> list = new ArrayList<>();
		for(VehicleEvent event : events)
			if(event.getFirstTimestamp() < timestamp)
				list.add(event);
		return list.toArray(new Clickable[0]);
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public String getName() {
		return "\u00c9v\u00e8nements des v\u00e9hicules";
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
