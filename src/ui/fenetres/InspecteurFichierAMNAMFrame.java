/*
 * Created by JFormDesigner on Mon Mar 06 20:47:07 EST 2017
 */

package ui.fenetres;

import io.amnam.EtatsFichiersAMNAM;
import io.amnam.FichierAMNAM;
import obfuscation.ObfuscationParameters;
import ui.UICommonUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static io.amnam.EtatsFichiersAMNAM.*;
import static ui.UICommonUtils.PATH_IMG_AMNAM_LOGO_URL;

/**
 * Fenêtre affichant le compte-rendu d'un {@link FichierAMNAM} et aiguille vers les différentes étapes du traitement.
 *
 * @author Philippe Rivest
 */
public class InspecteurFichierAMNAMFrame extends JFrame {

	private static final long serialVersionUID = -5756637926118927760L;
	
	private FichierAMNAM fichierAMNAM;

	private final ObfuscatorConfigFrame CONFIG_FRAME = new ObfuscatorConfigFrame();
	private ObfuscationParameters[] obfuscationParameters;

	/**
	 * Instancies un nouveau InspecteurFichierAMNAMFrame avec un {@link FichierAMNAM} à inspecter.
	 *
	 * @param fichierAMNAM Le {@link FichierAMNAM} à inspecter
	 */
	public InspecteurFichierAMNAMFrame(FichierAMNAM fichierAMNAM) {
		this.fichierAMNAM = fichierAMNAM;

		initComponents();
		initEtiquettes();

		CONFIG_FRAME.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				obfuscationParameters = CONFIG_FRAME.getObfuscationParameters();
				buttonObf.setEnabled(obfuscationParameters != null);
			}
		});
	}

	/**
	 * Ajuste le contenu des étiquettes de la fenêtre en fonction du contenu du {@link FichierAMNAM} à inspecter.
	 */
	private void initEtiquettes() {

		//LOGO
		UICommonUtils.addIconToLabel(this.logoAMNAM, PATH_IMG_AMNAM_LOGO_URL);

		//Titre
		this.labelTitreFichier.setText(fichierAMNAM.getNom());

		//Chemin d'accès
		this.textAreaURL.setText(fichierAMNAM.getCheminDAcces());

		//État fichier
		this.labelEtat.setText(EtatsFichiersAMNAM.etatToString(this.fichierAMNAM.getInfo().getEtat()));

		//Commentaires
		if (!fichierAMNAM.getInfo().getComments().isEmpty())
			this.textAreaComments.setText(fichierAMNAM.getInfo().getComments());

		//Composants enabled
		this.setUiEnabled();

		//Ajustement du texte des étiquettes
		if (this.fichierAMNAM.getInfo().getEtat() != VIDE) {

			//CSV
			if (fichierAMNAM.isCSVCharge()) {
				this.labelInitEventCSVStatus.setText("Ok");
				this.labelPeriodCSVStatus.setText("Ok (" + this.fichierAMNAM.getInfo().getNbVehicules() + " v\u00e9hicules)");

				this.buttonTransposerEvent.setEnabled(true);

			} else {
				this.labelInitEventCSVStatus.setText("Erreur");
				this.labelInitEventCSVStatus.setForeground(Color.RED);

				this.labelPeriodCSVStatus.setText("Erreur");
				this.labelPeriodCSVStatus.setForeground(Color.RED);
			}

			if (this.fichierAMNAM.getInfo().getEtat() != VIDE && this.fichierAMNAM.getInfo().getEtat() != CSV_DATA) {

				//CSV + event
				if (fichierAMNAM.isVehiculeEventsLoaded()) {
					this.labelEventJSONStatus.setText("Ok");
					this.labelDateEventJSON.setText(String.format("%te %<tb %<tT", this.fichierAMNAM.getInfo().getEtape(EVENEMENTS).getDateExecution()));

					this.buttonTransposerEvent.setText("Transposer \u00e0 nouveau en \u00e9v\u00e8nements");
					this.buttonConfigObf.setEnabled(true);
					this.buttonObf.setEnabled(false);

				} else {
					this.labelEventJSONStatus.setText("Erreur");
					this.labelEventJSONStatus.setForeground(Color.RED);
				}

				if (this.fichierAMNAM.getInfo().getEtat() == OBFUSQUATION_EVENEMENTS || this.fichierAMNAM.getInfo().getEtat() == ANALYSE) {

					//CSV + event + obf
					if (fichierAMNAM.isObfEventsLoaded()) {
						this.labelObfEventJSONStatus.setText("Ok");
						this.labelDateObfEventJSON.setText(String.format("%te %<tb %<tT", this.fichierAMNAM.getInfo().getEtape(OBFUSQUATION_EVENEMENTS).getDateExecution()));

						this.buttonAnalyser.setEnabled(true);

					} else {
						this.labelObfEventJSONStatus.setText("Non");
						this.labelEventJSONStatus.setForeground((fichierAMNAM.isAnalysedEventsLoaded() ? Color.RED : Color.YELLOW));
					}

					if (this.fichierAMNAM.getInfo().getEtat() == ANALYSE) {

						//CSV + event + obf + result
						if (fichierAMNAM.isAnalysedEventsLoaded()) {
							this.labelAnalysEventStatus.setText("Ok");
							this.labelDateAnalysEvent.setText(String.format("%te %<tb %<tT", this.fichierAMNAM.getInfo().getEtape(ANALYSE).getDateExecution()));

							this.buttonAnalyser.setText("Analyser \u00e0 nouveau");

						} else {
							this.labelEventJSONStatus.setText("Erreur");
							this.labelEventJSONStatus.setForeground(Color.RED);
						}

					}

				}

			}

		}//FIN Ajustement du texte des étiquettes


	}

	/**
	 * Bascule l'état d'activation <em>(enable)</em> des boutons de la fenêtre en fonction du contenu du {@link FichierAMNAM} à inspecter.
	 */
	private void setUiEnabled() {

		//csv
		boolean csvEnabled = this.fichierAMNAM.getInfo().getEtat() != VIDE;
		this.panelCSV.setEnabled(csvEnabled);
		this.buttonVisualiser.setEnabled(csvEnabled);

		for (Component component : this.panelCSV.getComponents()) {
			component.setEnabled(csvEnabled);
		}

		//event
		boolean eventEnabled = csvEnabled && this.fichierAMNAM.getInfo().getEtat() != CSV_DATA;
		this.panelEvent.setEnabled(eventEnabled);

		for (Component component : this.panelEvent.getComponents()) {
			component.setEnabled(eventEnabled);
		}

		//Obf
		boolean obfEnabled = this.fichierAMNAM.getInfo().getEtat() == OBFUSQUATION_EVENEMENTS || this.fichierAMNAM.getInfo().getEtat() == ANALYSE;
		this.panelObfuscation.setEnabled(obfEnabled);

		for (Component component : this.panelObfuscation.getComponents()) {
			component.setEnabled(obfEnabled);
		}

		//Result
		boolean resultEnabed = this.fichierAMNAM.getInfo().getEtat() == ANALYSE;
		this.panelAnalyse.setEnabled(resultEnabed);

		for (Component component : this.panelAnalyse.getComponents()) {
			component.setEnabled(resultEnabed);
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Annuler ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonAnnulerActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new EcranAccueilFrame());
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Transposer en évènements ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonTransposerEventActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new GenerateurEvenementFrame(fichierAMNAM));
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Configurer l'obfuscation ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonConfigObfActionPerformed(ActionEvent e) {
		this.CONFIG_FRAME.setVisible(true);
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Obfusquer ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonObfActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new ObfuscatorFrame(fichierAMNAM, obfuscationParameters));
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Analyser les évènements ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonAnalyserActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new AnalyseurFrame(fichierAMNAM));
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Visualiser ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonVisualiserActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new VisualisateurFrame(fichierAMNAM));
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton de fermeture de la fenêtre.
	 *
	 * @param e Évènement de fermeture
	 */
	private void thisWindowClosing(WindowEvent e) {
		fichierAMNAM = null;
		System.gc();
		UICommonUtils.transfererEtape(this, new EcranAccueilFrame());
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.logoAMNAM = new JLabel();
		this.labelTitreFichier = new JLabel();
		this.labelEtat = new JLabel();
		this.panelCSV = new JPanel();
		this.labelInitEventCSV = new JLabel();
		this.labelInitEventCSVStatus = new JLabel();
		this.labelPeriodCSV = new JLabel();
		this.labelPeriodCSVStatus = new JLabel();
		this.panelEvent = new JPanel();
		this.labelEventJSON = new JLabel();
		this.labelEventJSONStatus = new JLabel();
		this.labelLblDateEventJSON = new JLabel();
		this.labelDateEventJSON = new JLabel();
		this.buttonTransposerEvent = new JButton();
		this.panelObfuscation = new JPanel();
		this.labelObfEventJSON = new JLabel();
		this.labelObfEventJSONStatus = new JLabel();
		this.labelLblDateObfEventJSON = new JLabel();
		this.labelDateObfEventJSON = new JLabel();
		this.buttonObf = new JButton();
		this.buttonConfigObf = new JButton();
		this.panelAnalyse = new JPanel();
		this.labelAnalysEvent = new JLabel();
		this.labelAnalysEventStatus = new JLabel();
		this.labelLblDateAnalysEvent = new JLabel();
		this.labelDateAnalysEvent = new JLabel();
		this.buttonAnalyser = new JButton();
		this.buttonVisualiser = new JButton();
		this.buttonAnnuler = new JButton();
		this.scrollPaneURL = new JScrollPane();
		this.textAreaURL = new JTextArea();
		this.scrollPaneComments = new JScrollPane();
		this.textAreaComments = new JTextArea();

		//======== this ========
		setTitle("Inspection fichier | AMNAM"); //$NON-NLS-1$ //NON-NLS
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- logoAMNAM ----
		this.logoAMNAM.setIcon(null);
		this.logoAMNAM.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(this.logoAMNAM);
		this.logoAMNAM.setBounds(10, 10, 100, 95);

		//---- labelTitreFichier ----
		this.labelTitreFichier.setText("Titre Fichier"); //$NON-NLS-1$ //NON-NLS
		this.labelTitreFichier.setFont(new Font(".SF NS Text", Font.PLAIN, 22)); //$NON-NLS-1$ //NON-NLS
		this.labelTitreFichier.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPane.add(this.labelTitreFichier);
		this.labelTitreFichier.setBounds(120, 10, 260, this.labelTitreFichier.getPreferredSize().height);

		//---- labelEtat ----
		this.labelEtat.setText("\u00c9tat"); //$NON-NLS-1$ //NON-NLS
		this.labelEtat.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPane.add(this.labelEtat);
		this.labelEtat.setBounds(120, 85, 260, this.labelEtat.getPreferredSize().height);

		//======== panelCSV ========
		{
			this.panelCSV.setBorder(new TitledBorder("CSV")); //$NON-NLS-1$ //NON-NLS
			this.panelCSV.setEnabled(false);
			this.panelCSV.setLayout(null);

			//---- labelInitEventCSV ----
			this.labelInitEventCSV.setText("init.csv et event.csv :"); //$NON-NLS-1$ //NON-NLS
			this.labelInitEventCSV.setEnabled(false);
			this.panelCSV.add(this.labelInitEventCSV);
			this.labelInitEventCSV.setBounds(10, 20, 140, this.labelInitEventCSV.getPreferredSize().height);

			//---- labelInitEventCSVStatus ----
			this.labelInitEventCSVStatus.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelInitEventCSVStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelInitEventCSVStatus.setEnabled(false);
			this.panelCSV.add(this.labelInitEventCSVStatus);
			this.labelInitEventCSVStatus.setBounds(230, 20, 140, this.labelInitEventCSVStatus.getPreferredSize().height);

			//---- labelPeriodCSV ----
			this.labelPeriodCSV.setText("period_*.csv :"); //$NON-NLS-1$ //NON-NLS
			this.labelPeriodCSV.setEnabled(false);
			this.panelCSV.add(this.labelPeriodCSV);
			this.labelPeriodCSV.setBounds(10, 40, 140, 16);

			//---- labelPeriodCSVStatus ----
			this.labelPeriodCSVStatus.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelPeriodCSVStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelPeriodCSVStatus.setEnabled(false);
			this.panelCSV.add(this.labelPeriodCSVStatus);
			this.labelPeriodCSVStatus.setBounds(230, 40, 140, 16);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelCSV.getComponentCount(); i++) {
					Rectangle bounds = this.panelCSV.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelCSV.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelCSV.setMinimumSize(preferredSize);
				this.panelCSV.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelCSV);
		this.panelCSV.setBounds(10, 182, 375, 65);

		//======== panelEvent ========
		{
			this.panelEvent.setBorder(new TitledBorder("Transposition en \u00e9v\u00e8nements")); //$NON-NLS-1$ //NON-NLS
			this.panelEvent.setEnabled(false);
			this.panelEvent.setLayout(null);

			//---- labelEventJSON ----
			this.labelEventJSON.setText("events.json :"); //$NON-NLS-1$ //NON-NLS
			this.labelEventJSON.setEnabled(false);
			this.panelEvent.add(this.labelEventJSON);
			this.labelEventJSON.setBounds(10, 20, 155, 16);

			//---- labelEventJSONStatus ----
			this.labelEventJSONStatus.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelEventJSONStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelEventJSONStatus.setEnabled(false);
			this.panelEvent.add(this.labelEventJSONStatus);
			this.labelEventJSONStatus.setBounds(180, 20, 190, 16);

			//---- labelLblDateEventJSON ----
			this.labelLblDateEventJSON.setText("Date de la transposition :"); //$NON-NLS-1$ //NON-NLS
			this.labelLblDateEventJSON.setEnabled(false);
			this.panelEvent.add(this.labelLblDateEventJSON);
			this.labelLblDateEventJSON.setBounds(new Rectangle(new Point(10, 42), this.labelLblDateEventJSON.getPreferredSize()));

			//---- labelDateEventJSON ----
			this.labelDateEventJSON.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelDateEventJSON.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelDateEventJSON.setEnabled(false);
			this.panelEvent.add(this.labelDateEventJSON);
			this.labelDateEventJSON.setBounds(180, 42, 190, 16);

			//---- buttonTransposerEvent ----
			this.buttonTransposerEvent.setText("Transposer en \u00e9v\u00e8nements"); //$NON-NLS-1$ //NON-NLS
			this.buttonTransposerEvent.setEnabled(false);
			this.buttonTransposerEvent.addActionListener(e -> buttonTransposerEventActionPerformed(e));
			this.panelEvent.add(this.buttonTransposerEvent);
			this.buttonTransposerEvent.setBounds(10, 65, 360, this.buttonTransposerEvent.getPreferredSize().height);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelEvent.getComponentCount(); i++) {
					Rectangle bounds = this.panelEvent.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelEvent.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelEvent.setMinimumSize(preferredSize);
				this.panelEvent.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelEvent);
		this.panelEvent.setBounds(10, 254, 375, 105);

		//======== panelObfuscation ========
		{
			this.panelObfuscation.setBorder(new TitledBorder("Obfuscation des \u00e9v\u00e8nements")); //$NON-NLS-1$ //NON-NLS
			this.panelObfuscation.setEnabled(false);
			this.panelObfuscation.setLayout(null);

			//---- labelObfEventJSON ----
			this.labelObfEventJSON.setText("obf_events.json :"); //$NON-NLS-1$ //NON-NLS
			this.labelObfEventJSON.setEnabled(false);
			this.panelObfuscation.add(this.labelObfEventJSON);
			this.labelObfEventJSON.setBounds(10, 20, 140, 16);

			//---- labelObfEventJSONStatus ----
			this.labelObfEventJSONStatus.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelObfEventJSONStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelObfEventJSONStatus.setEnabled(false);
			this.panelObfuscation.add(this.labelObfEventJSONStatus);
			this.labelObfEventJSONStatus.setBounds(180, 20, 190, 16);

			//---- labelLblDateObfEventJSON ----
			this.labelLblDateObfEventJSON.setText("Date de l'obfuscation :"); //$NON-NLS-1$ //NON-NLS
			this.labelLblDateObfEventJSON.setEnabled(false);
			this.panelObfuscation.add(this.labelLblDateObfEventJSON);
			this.labelLblDateObfEventJSON.setBounds(new Rectangle(new Point(10, 42), this.labelLblDateObfEventJSON.getPreferredSize()));

			//---- labelDateObfEventJSON ----
			this.labelDateObfEventJSON.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelDateObfEventJSON.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelDateObfEventJSON.setEnabled(false);
			this.panelObfuscation.add(this.labelDateObfEventJSON);
			this.labelDateObfEventJSON.setBounds(180, 42, 190, 16);

			//---- buttonObf ----
			this.buttonObf.setText("Obfusquer"); //$NON-NLS-1$ //NON-NLS
			this.buttonObf.setEnabled(false);
			this.buttonObf.addActionListener(e -> buttonObfActionPerformed(e));
			this.panelObfuscation.add(this.buttonObf);
			this.buttonObf.setBounds(190, 65, 180, 32);

			//---- buttonConfigObf ----
			this.buttonConfigObf.setText("Configurer"); //$NON-NLS-1$ //NON-NLS
			this.buttonConfigObf.setEnabled(false);
			this.buttonConfigObf.addActionListener(e -> buttonConfigObfActionPerformed(e));
			this.panelObfuscation.add(this.buttonConfigObf);
			this.buttonConfigObf.setBounds(10, 65, 180, 32);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelObfuscation.getComponentCount(); i++) {
					Rectangle bounds = this.panelObfuscation.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelObfuscation.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelObfuscation.setMinimumSize(preferredSize);
				this.panelObfuscation.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelObfuscation);
		this.panelObfuscation.setBounds(10, 366, 375, 105);

		//======== panelAnalyse ========
		{
			this.panelAnalyse.setBorder(new TitledBorder("Analyse des \u00e9v\u00e8nements")); //$NON-NLS-1$ //NON-NLS
			this.panelAnalyse.setEnabled(false);
			this.panelAnalyse.setLayout(null);

			//---- labelAnalysEvent ----
			this.labelAnalysEvent.setText("analys_events.json :"); //$NON-NLS-1$ //NON-NLS
			this.labelAnalysEvent.setEnabled(false);
			this.panelAnalyse.add(this.labelAnalysEvent);
			this.labelAnalysEvent.setBounds(10, 20, 140, 16);

			//---- labelAnalysEventStatus ----
			this.labelAnalysEventStatus.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelAnalysEventStatus.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelAnalysEventStatus.setEnabled(false);
			this.panelAnalyse.add(this.labelAnalysEventStatus);
			this.labelAnalysEventStatus.setBounds(180, 20, 190, 16);

			//---- labelLblDateAnalysEvent ----
			this.labelLblDateAnalysEvent.setText("Date de l'analyse :"); //$NON-NLS-1$ //NON-NLS
			this.labelLblDateAnalysEvent.setEnabled(false);
			this.panelAnalyse.add(this.labelLblDateAnalysEvent);
			this.labelLblDateAnalysEvent.setBounds(10, 42, 140, this.labelLblDateAnalysEvent.getPreferredSize().height);

			//---- labelDateAnalysEvent ----
			this.labelDateAnalysEvent.setText("N/A"); //$NON-NLS-1$ //NON-NLS
			this.labelDateAnalysEvent.setHorizontalAlignment(SwingConstants.TRAILING);
			this.labelDateAnalysEvent.setEnabled(false);
			this.panelAnalyse.add(this.labelDateAnalysEvent);
			this.labelDateAnalysEvent.setBounds(180, 42, 190, 16);

			//---- buttonAnalyser ----
			this.buttonAnalyser.setText("Analyser les \u00e9v\u00e8nements"); //$NON-NLS-1$ //NON-NLS
			this.buttonAnalyser.setEnabled(false);
			this.buttonAnalyser.addActionListener(e -> buttonAnalyserActionPerformed(e));
			this.panelAnalyse.add(this.buttonAnalyser);
			this.buttonAnalyser.setBounds(10, 65, 360, 32);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelAnalyse.getComponentCount(); i++) {
					Rectangle bounds = this.panelAnalyse.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelAnalyse.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelAnalyse.setMinimumSize(preferredSize);
				this.panelAnalyse.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelAnalyse);
		this.panelAnalyse.setBounds(10, 478, 375, 105);

		//---- buttonVisualiser ----
		this.buttonVisualiser.setText("Visualiser"); //$NON-NLS-1$ //NON-NLS
		this.buttonVisualiser.addActionListener(e -> buttonVisualiserActionPerformed(e));
		contentPane.add(this.buttonVisualiser);
		this.buttonVisualiser.setBounds(135, 590, 250, this.buttonVisualiser.getPreferredSize().height);

		//---- buttonAnnuler ----
		this.buttonAnnuler.setText("Annuler"); //$NON-NLS-1$ //NON-NLS
		this.buttonAnnuler.addActionListener(e -> buttonAnnulerActionPerformed(e));
		contentPane.add(this.buttonAnnuler);
		this.buttonAnnuler.setBounds(10, 590, 120, 32);

		//======== scrollPaneURL ========
		{

			//---- textAreaURL ----
			this.textAreaURL.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
			this.textAreaURL.setEditable(false);
			this.scrollPaneURL.setViewportView(this.textAreaURL);
		}
		contentPane.add(this.scrollPaneURL);
		this.scrollPaneURL.setBounds(120, 40, 260, 35);

		//======== scrollPaneComments ========
		{
			this.scrollPaneComments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			//---- textAreaComments ----
			this.textAreaComments.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
			this.textAreaComments.setEditable(false);
			this.textAreaComments.setText("Aucun commentaire sur le fichier"); //$NON-NLS-1$ //NON-NLS
			this.textAreaComments.setLineWrap(true);
			this.textAreaComments.setWrapStyleWord(true);
			this.scrollPaneComments.setViewportView(this.textAreaComments);
		}
		contentPane.add(this.scrollPaneComments);
		this.scrollPaneComments.setBounds(10, 110, 375, 65);

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
		setSize(400, 655);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel logoAMNAM;
	private JLabel labelTitreFichier;
	private JLabel labelEtat;
	private JPanel panelCSV;
	private JLabel labelInitEventCSV;
	private JLabel labelInitEventCSVStatus;
	private JLabel labelPeriodCSV;
	private JLabel labelPeriodCSVStatus;
	private JPanel panelEvent;
	private JLabel labelEventJSON;
	private JLabel labelEventJSONStatus;
	private JLabel labelLblDateEventJSON;
	private JLabel labelDateEventJSON;
	private JButton buttonTransposerEvent;
	private JPanel panelObfuscation;
	private JLabel labelObfEventJSON;
	private JLabel labelObfEventJSONStatus;
	private JLabel labelLblDateObfEventJSON;
	private JLabel labelDateObfEventJSON;
	private JButton buttonObf;
	private JButton buttonConfigObf;
	private JPanel panelAnalyse;
	private JLabel labelAnalysEvent;
	private JLabel labelAnalysEventStatus;
	private JLabel labelLblDateAnalysEvent;
	private JLabel labelDateAnalysEvent;
	private JButton buttonAnalyser;
	private JButton buttonVisualiser;
	private JButton buttonAnnuler;
	private JScrollPane scrollPaneURL;
	private JTextArea textAreaURL;
	private JScrollPane scrollPaneComments;
	private JTextArea textAreaComments;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
