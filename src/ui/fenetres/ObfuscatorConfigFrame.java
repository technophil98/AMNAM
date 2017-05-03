/*
 * Created by JFormDesigner on Wed Feb 22 16:25:06 EST 2017
 */

package ui.fenetres;

import obfuscation.ObfuscationParameters;
import ui.panneaux.obfuscation_parameters.EventTypeObfuscationParametersPanel;
import ui.panneaux.obfuscation_parameters.ObfuscationParametersPanel;
import ui.panneaux.obfuscation_parameters.PacketLossObfuscationParametersPanel;
import ui.panneaux.obfuscation_parameters.PositionObfuscationParametersPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenêtre permettant l'obfuscation des données de simulation par l'utilisateur afin rapprocher celles-ci de la réalité.
 *
 * @author Philippe Rivest
 */
public class ObfuscatorConfigFrame extends JFrame {

	private static final long serialVersionUID = -1270236958756150670L;

	private ObfuscationParameters[] obfuscationParameters;

	/**
	 * Instancies un nouveau ObfuscatorConfigFrame.
	 */
	public ObfuscatorConfigFrame() {
		initComponents();
	}

	/**
	 * Retourne les paramètres d'obfuscation.
	 *
	 * @return les paramètres d'obfuscation
	 */
	public ObfuscationParameters[] getObfuscationParameters() {
		return obfuscationParameters;
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Annuler ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonCancelActionPerformed(ActionEvent e) {
		this.obfuscationParameters = null;
		this.dispose();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Appliquer ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonApplyActionPerformed(ActionEvent e) {

		Component[] components = this.tabbedPane.getComponents();
		obfuscationParameters = new ObfuscationParameters[components.length];

		for (int i = 0; i < components.length; i++)
			if (components[i] instanceof ObfuscationParametersPanel)
				obfuscationParameters[i] = ((ObfuscationParametersPanel) components[i]).getParameters();

		this.dispose();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton de fermeture de la fenêtre.
	 *
	 * @param e Évènement de fermeture
	 */
	private void thisWindowClosing(WindowEvent e) {
		this.obfuscationParameters = null;
		this.dispose();
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.tabbedPane = new JTabbedPane();
		this.positionObfuscationParametersPanel = new PositionObfuscationParametersPanel();
		this.packetLossObfuscationParametersPanel = new PacketLossObfuscationParametersPanel();
		this.eventTypeObfuscationParametersPanel = new EventTypeObfuscationParametersPanel();
		this.buttonCancel = new JButton();
		this.buttonApply = new JButton();

		//======== this ========
		setResizable(false);
		setTitle("Configuration obfuscation | AMNAM"); //$NON-NLS-1$ //NON-NLS
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//======== tabbedPane ========
		{
			this.tabbedPane.addTab("Position", this.positionObfuscationParametersPanel); //$NON-NLS-1$ //NON-NLS
			this.tabbedPane.addTab("Perte de paquets", this.packetLossObfuscationParametersPanel); //$NON-NLS-1$ //NON-NLS
			this.tabbedPane.addTab("Type d'\u00e9v\u00e8nement", this.eventTypeObfuscationParametersPanel); //$NON-NLS-1$ //NON-NLS
		}
		contentPane.add(this.tabbedPane);
		this.tabbedPane.setBounds(0, 5, 405, 440);

		//---- buttonCancel ----
		this.buttonCancel.setText("Annuler"); //$NON-NLS-1$ //NON-NLS
		this.buttonCancel.addActionListener(e -> buttonCancelActionPerformed(e));
		contentPane.add(this.buttonCancel);
		this.buttonCancel.setBounds(5, 450, 195, this.buttonCancel.getPreferredSize().height);

		//---- buttonApply ----
		this.buttonApply.setText("Appliquer"); //$NON-NLS-1$ //NON-NLS
		this.buttonApply.addActionListener(e -> buttonApplyActionPerformed(e));
		contentPane.add(this.buttonApply);
		this.buttonApply.setBounds(205, 450, 195, this.buttonApply.getPreferredSize().height);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		setSize(405, 515);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTabbedPane tabbedPane;
	private PositionObfuscationParametersPanel positionObfuscationParametersPanel;
	private PacketLossObfuscationParametersPanel packetLossObfuscationParametersPanel;
	private EventTypeObfuscationParametersPanel eventTypeObfuscationParametersPanel;
	private JButton buttonCancel;
	private JButton buttonApply;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
