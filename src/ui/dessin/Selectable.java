package ui.dessin;

import math.Vecteur;

/**
 * Un objet Selectable est un objet qui est cliquable et qui a une position bien défini.
 * @author Pascal Dally-Bélanger
 *
 */
public interface Selectable extends Clickable {

	/**
	 * Retourne la position de l'objet.
	 * @return La position de l'objet.
	 */
	public Vecteur getPosition();

}
