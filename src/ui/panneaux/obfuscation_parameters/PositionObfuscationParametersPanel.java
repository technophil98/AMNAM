/*
 * Created by JFormDesigner on Sat Mar 18 20:51:19 EDT 2017
 */

package ui.panneaux.obfuscation_parameters;

import obfuscation.ObfuscationParameters;
import obfuscation.parameters.PositionObfuscationParameters;
import ui.composants.SpinnerSliderInput;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de configuration d'un {@link PositionObfuscationParameters}.
 *
 * @author Philippe Rivest
 */
public class PositionObfuscationParametersPanel extends JPanel implements ObfuscationParametersPanel {

	private static final long serialVersionUID = 1842383247111662230L;

	/**
	 * Instancies un nouveau PositionObfuscationParametersPanel.
	 */
	public PositionObfuscationParametersPanel() {
		initComponents();
	}

	@Override
	public ObfuscationParameters getParameters() {
		return new PositionObfuscationParameters(this.ssiStandardDeviation.getValeur());
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.ssiStandardDeviation = new SpinnerSliderInput();

		//======== this ========
		setLayout(null);

		//---- ssiStandardDeviation ----
		this.ssiStandardDeviation.setMaximum(10.0);
		this.ssiStandardDeviation.setIncrement(0.1);
		this.ssiStandardDeviation.setNomValeur("\u00c9cart type de d\u00e9viation (\u03c3)"); //$NON-NLS-1$ //NON-NLS
		this.ssiStandardDeviation.setValeur(3.9);
		add(this.ssiStandardDeviation);
		this.ssiStandardDeviation.setBounds(5, 5, 390, this.ssiStandardDeviation.getPreferredSize().height);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private SpinnerSliderInput ssiStandardDeviation;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
