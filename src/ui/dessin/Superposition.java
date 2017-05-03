package ui.dessin;

import ui.panneaux.side_panels.SidePanel;


/**
 * Une superposition est un objet pouvant être rendu sur la carte afin
 * de transmettre de l'information à l'utilisateur.
 * @author Pascal Dally-Bélanger
 *
 */
public interface Superposition extends Dessinable {
	
	/**
	 * Détermine si la superposition peut être configurer.
	 * @return true, si la superposition peut être configurer.
	 */
	public boolean isConfigurable();
	
	/**
	 * Retourne le panneau de configuration de la superposition, si elle peut être configurer.
	 * @return le panneau de configuration de la superposition, si elle peut être configurer. Null sinon.
	 */
	public SidePanel getPanneauConfiguration();

	/**
	 * Modifie le temps de simulation de la superposition.
	 * @param t Le nouveau temps.
	 */
	public void setTemps(double t);
	
	/**
	 * Retourne une liste d'objet Clickable contenue dans cette superposition.
	 * @return La liste d'objet Clickable contenue dans cette superposition.
	 */
	public Clickable[] getClickables();
	
	/**
	 * Détermine si les éléments Clickable de la superposition peuvent être cliqués.
	 * @return Vrai, si les éléments Clickable de la superposition peuvent être cliqués.
	 */
	public boolean isClickable();
	
	/**
	 * Retourne le nom de la superposition.
	 * @return Le nom de la superposition.
	 */
	public String getName();
	
	/**
	 * Retourne vrai si la superposition est active.
	 * @return Vrai si la superposition est active.
	 */
	public boolean isEnabled();
	
	/**
	 * Active ou désactive la superposition.
	 * @param enabled Si vrai, active la superposition. Sinon, la désactive.
	 */
	public void setEnabled(boolean enabled);
}
