package ui.composants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * Extension de {@link TitledBorder} qui ajuste la taille de police du titre afin de maximiser l'espace.
 *
 * @see TitledBorder
 * @author Philippe Rivest
 */
public class AdaptableTitledBorder extends TitledBorder {

	private static final long serialVersionUID = -1586566947000024206L;
	
	private static final Font DEFAULT_FONT = UIManager.getFont("TitledBorder.font");
	private static final float MAX_SIZE = 26f;

	private Component parent;
	private int rightMargin = 40;

	/**
	 * Instancies un nouveau AdaptableTitledBorder.
	 */
	public AdaptableTitledBorder(){
		super(null, "", DEFAULT_JUSTIFICATION,DEFAULT_POSITION, DEFAULT_FONT);
	}

	/**
	 * Calcule et mets à jour la taille de police optimale pour le titre de la bordure.
	 */
	private void fitTitle() {
		if (getTitleFont() != null && !getTitle().isEmpty() && parent != null) {
			double stringWidth = titleFont.getStringBounds(title, new FontRenderContext(new AffineTransform(), true, true)).getWidth();
			int widthComponent = parent.getWidth() - rightMargin;

			if (widthComponent != 0) {

				double widthRatio = (double)widthComponent / stringWidth;

				float fittedFont = ((float) (titleFont.getSize() * widthRatio)) - 1f;

				float fontToSet = Math.min(fittedFont,MAX_SIZE);
				setTitleFont(titleFont.deriveFont(fontToSet));
			}
		}
	}

	/**
	 * Mets à jour le composant parent qui contient la bordure.
	 *
	 * @param parent le composant parent
	 */
	public void setParent(Component parent) {
		this.parent = parent;
		fitTitle();
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		fitTitle();
	}

	/**
	 * Mets à jour la marge de gauche pour le titre.
	 *
	 * @param rightMargin la marge de gauche
	 */
	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}
}
