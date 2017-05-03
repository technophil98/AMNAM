/*
 * Created by JFormDesigner on Sat Mar 18 19:33:51 EDT 2017
 */

package ui.panneaux.obfuscation_parameters;

import obfuscation.ObfuscationParameters;
import obfuscation.parameters.EventTypeObfuscationParameters;
import scenario.events.EventType;
import ui.composants.SpinnerSliderInput;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panneau de configuration d'un {@link EventTypeObfuscationParameters}.
 *
 * @author Philippe Rivest
 */
public class EventTypeObfuscationParametersPanel extends JPanel implements ObfuscationParametersPanel {

	private static final long serialVersionUID = -288488043660098808L;

	/**
	 * Instancies un nouveau EventTypeObfuscationParametersPanel.
	 */
	public EventTypeObfuscationParametersPanel() {
		initComponents();
	}

	/**
	 * Méthode exécutée lorsqu'un changement survient avec le chxbox « Assigner un type d'évènement précis en guise d'erreur ».
	 *
	 * @param e L'évènement de changement
	 */
	private void checkBoxRandomTypeActionPerformed(ActionEvent e) {
		this.comboBoxRandomType.setEnabled(this.checkBoxRandomType.isSelected());
	}

	@Override
	public ObfuscationParameters getParameters() {
		if (checkBoxRandomType.isSelected())
			return new EventTypeObfuscationParameters(this.ssiErrorRate.getValeur() / 100.0, ((EventType) this.comboBoxRandomType.getSelectedItem()));
		else
			return new EventTypeObfuscationParameters(this.ssiErrorRate.getValeur() / 100.0);
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.ssiErrorRate = new SpinnerSliderInput();
		this.panelTypeConfig = new JPanel();
		this.checkBoxRandomType = new JCheckBox();
		this.comboBoxRandomType = new JComboBox<>();

		//======== this ========
		setLayout(null);

		//---- ssiErrorRate ----
		this.ssiErrorRate.setNomValeur("Taux d'erreur (%)"); //$NON-NLS-1$ //NON-NLS
		this.ssiErrorRate.setValeur(15.0);
		this.ssiErrorRate.setIncrement(0.5);
		add(this.ssiErrorRate);
		this.ssiErrorRate.setBounds(5, 5, 390, 95);

		//======== panelTypeConfig ========
		{
			this.panelTypeConfig.setBorder(new TitledBorder("Configuration du type")); //$NON-NLS-1$ //NON-NLS
			this.panelTypeConfig.setLayout(null);

			//---- checkBoxRandomType ----
			this.checkBoxRandomType.setText("Assigner un type d'\u00e9v\u00e8nement pr\u00e9cis en guise d'erreur"); //$NON-NLS-1$ //NON-NLS
			this.checkBoxRandomType.addActionListener(e -> checkBoxRandomTypeActionPerformed(e));
			this.panelTypeConfig.add(this.checkBoxRandomType);
			this.checkBoxRandomType.setBounds(5, 20, 380, this.checkBoxRandomType.getPreferredSize().height);

			//---- comboBoxRandomType ----
			this.comboBoxRandomType.setEnabled(false);
			this.panelTypeConfig.add(this.comboBoxRandomType);
			this.comboBoxRandomType.setBounds(5, 40, 380, this.comboBoxRandomType.getPreferredSize().height);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for (int i = 0; i < this.panelTypeConfig.getComponentCount(); i++) {
					Rectangle bounds = this.panelTypeConfig.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelTypeConfig.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelTypeConfig.setMinimumSize(preferredSize);
				this.panelTypeConfig.setPreferredSize(preferredSize);
			}
		}
		add(this.panelTypeConfig);
		this.panelTypeConfig.setBounds(5, 105, this.panelTypeConfig.getPreferredSize().width, 75);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for (int i = 0; i < getComponentCount(); i++) {
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
		this.comboBoxRandomType.setModel(new DefaultComboBoxModel<>(EventType.values()));
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private SpinnerSliderInput ssiErrorRate;
	private JPanel panelTypeConfig;
	private JCheckBox checkBoxRandomType;
	private JComboBox<EventType> comboBoxRandomType;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
