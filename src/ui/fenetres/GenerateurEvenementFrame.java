/*
 * Created by JFormDesigner on Wed Feb 22 16:18:05 EST 2017
 */

package ui.fenetres;

import static ui.UICommonUtils.PATH_IMG_AMNAM_LOGO_URL;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import io.amnam.EtatsFichiersAMNAM;
import io.amnam.FichierAMNAM;
import scenario.events.ReportedEvent;
import scenario.events.VehicleEvent;
import scenario.events.event_manipulations.EventGenerator;
import ui.UICommonUtils;
import utils.processor.ProgressListener;

/**
 * Fenêtre affichant une animation de « Transposition des données en évènements » durant la transposition des données en évènements.
 *
 * @author Philippe Rivest
 */
public class GenerateurEvenementFrame extends JFrame {

	private static final long serialVersionUID = 4102526284403034010L;

	private FichierAMNAM fichierAMNAM;
	private final EventGenerator eventGenerator;

	/**
	 * Instancies un nouveau GenerateurEvenementFrame avec un {@link FichierAMNAM}.
	 *
	 * @param fichierAMNAM le fichier amnam
	 */
	public GenerateurEvenementFrame(FichierAMNAM fichierAMNAM) {
		initComponents();
		this.fichierAMNAM = fichierAMNAM;

		eventGenerator = new EventGenerator(fichierAMNAM, 1, 1000);
		eventGenerator.addProgressListener(new ProgressListener() {
			@Override
			public void updateProgress(double progress) {
				progressBar.setValue(((int) Math.round(progress * 100)));
			}

			@Override
			public void onCompletion() {

				try {

					final VehicleEvent[] result = eventGenerator.getResult();

					fichierAMNAM.setVehiculeEvents(result);
					fichierAMNAM.setReportedEvents(Arrays.stream(result).map(ReportedEvent::new).toArray(ReportedEvent[]::new));

					fichierAMNAM.getInfo().setEtat(EtatsFichiersAMNAM.EVENEMENTS);
					fichierAMNAM.getInfo().getEtape(EtatsFichiersAMNAM.EVENEMENTS).setDateExecution(new Date());
					fichierAMNAM.getInfo().getEtape(EtatsFichiersAMNAM.EVENEMENTS).setExecute(true);

					fichierAMNAM.saveFile();

				} catch (IOException e) {

					e.printStackTrace();
					JOptionPane.showMessageDialog(GenerateurEvenementFrame.this, FichierAMNAM.SAVE_ERROR_DIALOG_MESSAGE, FichierAMNAM.SAVE_ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);

				}finally {
					UICommonUtils.transfererEtape(GenerateurEvenementFrame.this, new InspecteurFichierAMNAMFrame(fichierAMNAM));
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
	 * Méthode exécutée lors du clic sur le bouton de fermeture de la fenêtre.
	 *
	 * @param e Évènement de fermeture
	 */
	private void thisWindowClosing(WindowEvent e) {
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
		setTitle("Transposition en \u00e9v\u00e8nements | AMNAM"); //$NON-NLS-1$ //NON-NLS
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

		//---- labelTitre ----
		this.labelTitre.setText("<html><center>Transposition en<br/>\u00e9v\u00e8nements en cours</center></html>"); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setFont(new Font(".SF NS Text", Font.PLAIN, 18)); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.labelTitre);
		this.labelTitre.setBounds(70, 10, 237, 45);

		//---- logoAMNAM ----
		this.logoAMNAM.setIcon(null);
		this.logoAMNAM.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.logoAMNAM);
		this.logoAMNAM.setBounds(70, 64, 237, 240);
		contentPane.add(this.progressBar);
		this.progressBar.setBounds(70, 313, 237, 12);

		//---- buttonAnnuler ----
		this.buttonAnnuler.setText("Annuler"); //$NON-NLS-1$ //NON-NLS
		this.buttonAnnuler.addActionListener(e -> buttonAnnulerActionPerformed(e));
		contentPane.add(this.buttonAnnuler);
		this.buttonAnnuler.setBounds(70, 334, 237, 32);

		contentPane.setPreferredSize(new Dimension(385, 400));
		setSize(385, 400);
		setLocationRelativeTo(null);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		UICommonUtils.addIconToLabel(this.logoAMNAM, PATH_IMG_AMNAM_LOGO_URL);

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel labelTitre;
	private JLabel logoAMNAM;
	private JProgressBar progressBar;
	private JButton buttonAnnuler;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
