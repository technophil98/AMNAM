package ui.dessin;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Un objet Dessinable est un objet qui peut être rendu en utilisant un contexte graphique g2d et peut être transformer
 * à l'aide d'une matrice de transformation AffineTransform.
 *
 * @author Pascal Dally-Bélanger
 */
public interface Dessinable {

	/**
	 * Dessine l'objet sur le contexte graphique.
	 *
	 * @param g2d Le contexte graphique.
	 */
	public void dessiner(Graphics2D g2d);

	/**
	 * Détermine la matrice de transformation de l'objet.
	 * L'objet doit appliquer cette transformation durant le rendu.
	 *
	 * @param transform La matrice de transformation.
	 */
	public void setTransform(AffineTransform transform);
}
