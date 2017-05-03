/*
 * Created by JFormDesigner on Wed Feb 22 16:27:46 EST 2017
 */

package ui.fenetres;

import io.amnam.EtatsFichiersAMNAM;
import io.amnam.FichierAMNAM;
import scenario.events.event_manipulations.EventAnalyser;
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
 * Fenêtre affichant une animation de « analyse en cours » durant l'analyse des données.
 *
 * @author Philippe Rivest
 */
public class AnalyseurFrame extends JFrame {


	private static final long serialVersionUID = 2537849205344653021L;
	private FichierAMNAM fichierObfusque;

	private final EventAnalyser eventAnalyser;

	/**
	 * Instancies un nouveau AnalyseurFrame.
	 *
	 * @param fichierAMNAM Le {@link FichierAMNAM} contenant des évènements (obfusqués ou non)
	 */
	public AnalyseurFrame(FichierAMNAM fichierAMNAM) {
		this.fichierObfusque = fichierAMNAM;
		initComponents();

		eventAnalyser = new EventAnalyser(fichierAMNAM, null);
		eventAnalyser.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(double progress) {
				progressBar.setValue(((int) Math.round(progress * 100)));
			}

			@Override
			public void onCompletion() {

				try {

					fichierAMNAM.setAnalysisResult(eventAnalyser.getResult());

					fichierAMNAM.getInfo().setEtat(EtatsFichiersAMNAM.ANALYSE);
					fichierAMNAM.getInfo().getEtape(EtatsFichiersAMNAM.ANALYSE).setDateExecution(new Date());
					fichierAMNAM.getInfo().getEtape(EtatsFichiersAMNAM.ANALYSE).setExecute(true);

					fichierAMNAM.saveFile();

				} catch (IOException e) {

					e.printStackTrace();
					JOptionPane.showMessageDialog(AnalyseurFrame.this, FichierAMNAM.SAVE_ERROR_DIALOG_MESSAGE, FichierAMNAM.SAVE_ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);

				}finally {
					UICommonUtils.transfererEtape(AnalyseurFrame.this, new InspecteurFichierAMNAMFrame(fichierAMNAM));
				}
			}
		});
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton de fermeture de la fenêtre.
	 *
	 * @param e Évènement de fermeture
	 */
	private void thisWindowClosing(WindowEvent e) {
		UICommonUtils.transfererEtape(this, new InspecteurFichierAMNAMFrame(fichierObfusque));
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Annuler ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonAnnulerActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new InspecteurFichierAMNAMFrame(fichierObfusque));
	}

	/**
	 * Méthode exécutée lors de l'ouverture de la fenêtre.
	 *
	 * @param e Évènement d'ouverture
	 */
	private void thisWindowOpened(WindowEvent e) {
		eventAnalyser.start();
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.logoAMNAM = new JLabel();
		this.buttonAnnuler = new JButton();
		this.labelTitre = new JLabel();
		this.progressBar = new JProgressBar();

		//======== this ========
		setTitle("Analyse du fichier | AMNAM"); //$NON-NLS-1$ //NON-NLS
		setResizable(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
			@Override
			public void windowOpened(WindowEvent e) {
				thisWindowOpened(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- logoAMNAM ----
		this.logoAMNAM.setIcon(null);
		this.logoAMNAM.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.logoAMNAM);
		this.logoAMNAM.setBounds(70, 45, 237, 240);

		//---- buttonAnnuler ----
		this.buttonAnnuler.setText("Annuler"); //$NON-NLS-1$ //NON-NLS
		this.buttonAnnuler.addActionListener(e -> buttonAnnulerActionPerformed(e));
		contentPane.add(this.buttonAnnuler);
		this.buttonAnnuler.setBounds(70, 335, 237, this.buttonAnnuler.getPreferredSize().height);

		//---- labelTitre ----
		this.labelTitre.setText("Analyse du fichier en cours"); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setFont(new Font(".SF NS Text", Font.PLAIN, 18)); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.labelTitre);
		this.labelTitre.setBounds(new Rectangle(new Point(73, 10), this.labelTitre.getPreferredSize()));
		contentPane.add(this.progressBar);
		this.progressBar.setBounds(70, 304, 237, this.progressBar.getPreferredSize().height);

		contentPane.setPreferredSize(new Dimension(385, 400));
		setSize(385, 400);
		setLocationRelativeTo(null);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		UICommonUtils.addIconToLabel(this.logoAMNAM, PATH_IMG_AMNAM_LOGO_URL);

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel logoAMNAM;
	private JButton buttonAnnuler;
	private JLabel labelTitre;
	private JProgressBar progressBar;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
