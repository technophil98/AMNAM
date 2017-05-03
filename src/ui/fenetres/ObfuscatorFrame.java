/*
 * Created by JFormDesigner on Sat Mar 18 21:30:07 EDT 2017
 */

package ui.fenetres;

import io.amnam.EtatsFichiersAMNAM;
import io.amnam.FichierAMNAM;
import obfuscation.ObfuscatedEventGenerator;
import obfuscation.ObfuscationParameters;
import ui.UICommonUtils;
import utils.processor.ProgressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;

import static ui.UICommonUtils.PATH_IMG_AMNAM_LOGO_URL;

/**
 * Fenêtre affichant une animation de « Obfuscation des évènements en cours » durant l'obfuscation des évènements.
 *
 * @author Philippe Rivest
 */
public class ObfuscatorFrame extends JFrame {

	private static final long serialVersionUID = 6287268108939443384L;

	private FichierAMNAM fichierAMNAM;
	private ObfuscatedEventGenerator eventGenerator;

	/**
	 * Instancies un nouveau ObfuscatorFrame.
	 *
	 * @param fichierAMNAM le fichierAMNAM contnant les évènemnts à obfusquer
	 * @param parameters   les paramètres d'obfuscation
	 */
	public ObfuscatorFrame(FichierAMNAM fichierAMNAM, ObfuscationParameters[] parameters) {
		this.fichierAMNAM = fichierAMNAM;

		initComponents();

		eventGenerator = new ObfuscatedEventGenerator(fichierAMNAM, parameters);
		eventGenerator.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(double progress) {
				progressBar.setValue(((int) Math.round(progress * 100)));
			}

			@Override
			public void onCompletion() {
				try {

					fichierAMNAM.setObfuscatedEvents(eventGenerator.getResult());
					fichierAMNAM.getInfo().setEtat(EtatsFichiersAMNAM.OBFUSQUATION_EVENEMENTS);
					fichierAMNAM.getInfo().getEtape(EtatsFichiersAMNAM.OBFUSQUATION_EVENEMENTS).setDateExecution(new Date());
					fichierAMNAM.getInfo().getEtape(EtatsFichiersAMNAM.OBFUSQUATION_EVENEMENTS).setExecute(true);

					fichierAMNAM.saveFile();

				} catch (IOException e) {

					e.printStackTrace();
					JOptionPane.showMessageDialog(ObfuscatorFrame.this, FichierAMNAM.SAVE_ERROR_DIALOG_MESSAGE, FichierAMNAM.SAVE_ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);

				}finally {
					UICommonUtils.transfererEtape(ObfuscatorFrame.this, new InspecteurFichierAMNAMFrame(fichierAMNAM));
				}

			}
		});
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Annuler ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonAnnulerActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new InspecteurFichierAMNAMFrame(fichierAMNAM));
	}

	/**
	 * Méthode exécutée lors de l'ouverture de la fenêtre.
	 *
	 * @param e Évènement d'ouverture
	 */
	private void thisWindowOpened(WindowEvent e) {
		eventGenerator.start();
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.labelTitre = new JLabel();
		this.logoAMNAM = new JLabel();
		this.progressBar = new JProgressBar();
		this.buttonAnnuler = new JButton();

		//======== this ========
		setTitle("Obfuscation des \u00e9v\u00e8nements | AMNAM"); //$NON-NLS-1$ //NON-NLS
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				thisWindowOpened(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- labelTitre ----
		this.labelTitre.setText("<html><center>Obfuscation des<br/>\u00e9v\u00e8nements en cours</center></html>"); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setFont(new Font(".SF NS Text", Font.PLAIN, 18)); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.labelTitre);
		this.labelTitre.setBounds(70, 10, 237, 45);

		//---- logoAMNAM ----
		this.logoAMNAM.setIcon(null);
		this.logoAMNAM.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.logoAMNAM);
		this.logoAMNAM.setBounds(70, 65, 237, 240);
		contentPane.add(this.progressBar);
		this.progressBar.setBounds(70, 315, 237, 12);

		//---- buttonAnnuler ----
		this.buttonAnnuler.setText("Annuler"); //$NON-NLS-1$ //NON-NLS
		this.buttonAnnuler.addActionListener(e -> buttonAnnulerActionPerformed(e));
		contentPane.add(this.buttonAnnuler);
		this.buttonAnnuler.setBounds(70, 335, 237, 32);

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
		setSize(385, 400);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		//Logo
		UICommonUtils.addIconToLabel(this.logoAMNAM, PATH_IMG_AMNAM_LOGO_URL);

		//Progess bar
		this.progressBar.setMaximum(this.fichierAMNAM.getVehiculeEvents().length);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel labelTitre;
	private JLabel logoAMNAM;
	private JProgressBar progressBar;
	private JButton buttonAnnuler;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
