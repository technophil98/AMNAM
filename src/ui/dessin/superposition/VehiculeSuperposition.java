package ui.dessin.superposition;

import ui.dessin.Clickable;
import ui.dessin.Superposition;
import ui.dessin.Vehicule;
import ui.panneaux.side_panels.SidePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
	
/**
 * Une superposition qui contient tout les véhicules de la simulation.
 * @author Pascal Dally-Bélanger
 *
 */
public class VehiculeSuperposition implements Superposition {

	private Vehicule[] vehicules;
	private boolean isEnabled = true;
	
	/**
	 * Initialise cette superposition avec les véhicules passés en paramètre.
	 * @param vehicules Les véhicules de la superposition.
	 */
	public VehiculeSuperposition(Vehicule... vehicules) {
		this.vehicules = vehicules;
	}
	
	@Override
	public void dessiner(Graphics2D g2d) {
		for(Vehicule vehicule : vehicules)
			vehicule.dessiner(g2d);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		for(Vehicule vehicule : vehicules)
			vehicule.setTransform(transform);
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
		for(Vehicule vehicule : vehicules)
			vehicule.setTemps(t);
	}

	@Override
	public Clickable[] getClickables() {
		return vehicules;
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public String getName() {
		return "V\u00e9hicules";
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
