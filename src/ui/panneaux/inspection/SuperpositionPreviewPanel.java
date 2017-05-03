/*
 * Created by JFormDesigner on Thu Mar 30 22:51:54 EDT 2017
 */

package ui.panneaux.inspection;

import ui.dessin.Superposition;

import javax.swing.*;
import java.awt.*;

/**
 * Un panneau où une version miniature d'une superposition y est dessiné.
 *
 * @see ui.composants.SuperpositionsListCellRenderer
 * @author Philippe Rivest
 */
public class SuperpositionPreviewPanel extends JPanel {

	private static final long serialVersionUID = -6192409598540623788L;

	private Superposition superposition;

	/**
	 * Instancies un nouveau SuperpositionPreviewPanel.
	 */
	public SuperpositionPreviewPanel() {
		initComponents();
	}

	/**
	 * Mets à jour la superposition à dessiner.
	 *
	 * @param superposition la superposition à dessiner
	 */
	public void setSuperposition(Superposition superposition) {
		this.superposition = superposition;
		labelPreview.setText("Aperçu : " + superposition.getName());
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (superposition != null)
			superposition.dessiner(g2d);
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.labelPreview = new JLabel();

		//======== this ========
		setLayout(new BorderLayout(10, 10));

		//---- labelPreview ----
		this.labelPreview.setText("Aper\u00e7u"); //$NON-NLS-1$ //NON-NLS
		this.labelPreview.setFont(new Font(".SF NS Text", Font.PLAIN, 8)); //$NON-NLS-1$ //NON-NLS
		add(this.labelPreview, BorderLayout.SOUTH);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		setBackground(UIManager.getColor("darcula.selectionBackgroundInactive"));
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel labelPreview;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
