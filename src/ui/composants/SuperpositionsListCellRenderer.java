/*
 * Created by JFormDesigner on Wed Mar 29 16:49:45 EDT 2017
 */

package ui.composants;

import ui.dessin.Superposition;
import ui.panneaux.inspection.SuperpositionPreviewPanel;
import utils.UtilStrings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Une extension de {@link ListCellRenderer} pour permettre un affichage personnalisé d'une {@link JList}.
 *
 * @see ListCellRenderer
 * @see JList
 * @see Superposition
 * @author Philippe Rivest
 */
public class SuperpositionsListCellRenderer extends JPanel implements ListCellRenderer<Superposition> {

	private static final long serialVersionUID = 5295719936536880250L;

	private static final String INVALID_TYPE_MESSAGE = "Erreur! Entr\u00e9e invalide.";
	public static final Dimension CELL_DEFAULT_DIMENSION = new Dimension(320, 140);

	/**
	 * Instancies un nouveau SuperpositionsListCellRenderer.
	 */
	public SuperpositionsListCellRenderer() {
		initComponents();
	}

	/**
	 * Initialise tous les composants graphiques du composant.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.panelPreviewSuperposition = new SuperpositionPreviewPanel();
		this.labelSuperpositionName = new JLabel();

		//======== this ========
		setBorder(new EtchedBorder());
		setLayout(new GridLayout(1, 2));

		//---- panelPreviewSuperposition ----
		this.panelPreviewSuperposition.setBorder(LineBorder.createBlackLineBorder());
		add(this.panelPreviewSuperposition);

		//---- labelSuperpositionName ----
		this.labelSuperpositionName.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelSuperpositionName.setFont(new Font(".SF NS Text", Font.PLAIN, 18)); //$NON-NLS-1$ //NON-NLS
		add(this.labelSuperpositionName);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		setPreferredSize(CELL_DEFAULT_DIMENSION);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Superposition> list, Superposition value, int index, boolean isSelected, boolean cellHasFocus) {

		if (value != null){

			labelSuperpositionName.setText((value.getName().length() > 14 ? UtilStrings.diviserLignesHTML(value.getName(), 3, true) : value.getName()));
			panelPreviewSuperposition.setSuperposition(value);
			labelSuperpositionName.setEnabled(value.isEnabled());

		}else
			labelSuperpositionName.setText(INVALID_TYPE_MESSAGE);

		setBackground(UIManager.getColor((isSelected && cellHasFocus ? "ComboBox.selectionBackground" : "Button.background")));

		return this;
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private SuperpositionPreviewPanel panelPreviewSuperposition;
	private JLabel labelSuperpositionName;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
