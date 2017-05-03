package ui.dessin;

import java.awt.Shape;

/**
 * Un objet Clickable est un objet pouvant être rendu sur la fenêtre mais qui peut aussi
 * être cliqué et réagir aux cliques. 
 * @author Pascal Dally-Bélanger
 * 
 */
public interface Clickable extends Dessinable {

	/**
	 * Retourne un objet Shape permettant de déterminer si un MouseEvent est bien sur cet objet.
	 * @return la Shape de l'objet.
	 */
	public Shape getShape();
	
}
