/*
 * Created by JFormDesigner on Sat Mar 18 20:46:29 EDT 2017
 */

package ui.panneaux.obfuscation_parameters;

import obfuscation.ObfuscationParameters;
import obfuscation.parameters.PacketLossObfuscationParameters;
import ui.composants.SpinnerSliderInput;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de configuration d'un {@link PacketLossObfuscationParameters}.
 * @author Philippe Rivest
 */
public class PacketLossObfuscationParametersPanel extends JPanel implements ObfuscationParametersPanel {

	private static final long serialVersionUID = -1898859626522723855L;

	/**
	 * Instancies un nouveau PacketLossObfuscationParametersPanel.
	 */
	public PacketLossObfuscationParametersPanel() {
		initComponents();
	}

	@Override
	public ObfuscationParameters getParameters() {
		return new PacketLossObfuscationParameters(this.ssiPacketLossPercentage.getValeur());
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.ssiPacketLossPercentage = new SpinnerSliderInput();

		//======== this ========
		setLayout(null);

		//---- ssiPacketLossPercentage ----
		this.ssiPacketLossPercentage.setIncrement(0.5);
		this.ssiPacketLossPercentage.setNomValeur("Taux de perte (%)"); //$NON-NLS-1$ //NON-NLS
		this.ssiPacketLossPercentage.setValeur(5.0);
		add(this.ssiPacketLossPercentage);
		this.ssiPacketLossPercentage.setBounds(5, 5, 390, 95);

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
	private SpinnerSliderInput ssiPacketLossPercentage;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
