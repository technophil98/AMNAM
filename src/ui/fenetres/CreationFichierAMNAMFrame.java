/*
 * Created by JFormDesigner on Tue Mar 07 17:40:48 EST 2017
 */

package ui.fenetres;

import com.sun.istack.internal.Nullable;
import io.amnam.FichierAMNAM;
import io.csv.LecteurCSV;
import io.csv.config.ConfigurationInitialisation;
import scenario.Obstacle;
import ui.UICommonUtils;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static scenario.Obstacle.OMITTED_POS;
import static ui.UICommonUtils.PATH_IMG_AMNAM_LOGO_URL;
import static ui.UICommonUtils.lastFileChooserDirectory;

/**
 * Fenêtre permettant à l'utilisateur de créer un {@link FichierAMNAM} à partir des CSV provenant du simulateur.
 *
 * @author Philippe Rivest
 */
public class CreationFichierAMNAMFrame extends JFrame {

	private static final long serialVersionUID = 5414504725084150044L;

	private File fichierACreer,initCSV,eventCSV;
	private File[] perdiods;
	private List<String> listeFichiersPeriodAttendus;


	private Obstacle[] obstacles = Obstacle.DEFAULT_OBSTACLES;

	private boolean standalone;

	/**
	 * Instancies un nouveau CreationFichierAMNAMFrame.
	 */
	public CreationFichierAMNAMFrame() {
		this(false);
	}

	/**
	 * Instancies un nouveau CreationFichierAMNAMFrame avec la possibilité de garder celle-ci isolée des autres fenêtres.
	 *
	 * @param standalone Si vrai, la fenêtre n'ouvrira pas d'autres fenêtres lors de la création d'un fichier ou l'annulation de l'opération
	 */
	public CreationFichierAMNAMFrame(boolean standalone) {
		this.standalone = standalone;

		this.initComponents();
		this.comboBoxObstacleActionPerformed(null);
	}


	/**
	 * Méthode exécutée lors du clic sur le bouton « Annuler ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonAnnulerActionPerformed(ActionEvent e) {

		if (!this.standalone)
			UICommonUtils.transfererEtape(this, new EcranAccueilFrame());

		this.dispose();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Choisir le chemin d'accès ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonChoisirURLActionPerformed(ActionEvent e) {

		JFileChooser chooser = new JFileChooser(lastFileChooserDirectory);
		chooser.setDialogTitle("Choisir l'emplacement de sauvegarde");
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setFileFilter(new FileNameExtensionFilter("\u00ab *.amnam \u00bb - Fichier AMNAM", "amnam"));
		chooser.setMultiSelectionEnabled(false);

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			lastFileChooserDirectory = chooser.getCurrentDirectory();

			if (!chooser.getSelectedFile().getName().endsWith(".amnam"))
				this.fichierACreer = new File(chooser.getSelectedFile().getPath() + ".amnam");

			this.labelNomFichier.setText(this.fichierACreer.getName());
			this.textAreaURL.setText(this.fichierACreer.getAbsolutePath());

			this.buttonChoisirURLInit.setEnabled(true);
			this.buttonPickDirectory.setEnabled(true);

		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Choisir le fichier « init.csv » ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonChoisirURLInitActionPerformed(ActionEvent e) {

		try {

			this.verifyInitFile(this.chargerUnFichier("Choisir le fichier \u00ab init.csv \u00bb", new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().equals("init.csv") || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "\u00ab init.csv \u00bb - Un fichier d'initialisation de simulateur";
				}
			}));

		} catch (IOException e1) {
			e1.printStackTrace();
			this.textAreaURLInit.setText("Erreur dans le chargement du fichier \u00ab init.csv \u00bb. Veuillez r\u00e9essayer.");

			this.labelStatusInit.setText("Erreur");
			this.labelStatusInit.setForeground(Color.RED);

		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Choisir le fichier « event.csv » ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonChoisirURLEventActionPerformed(ActionEvent e) {

		try {

			this.verifyEventFile(this.chargerUnFichier("Choisir le fichier \u00ab event.csv \u00bb", new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().equals("event.csv") || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "\u00ab event.csv \u00bb - Un fichier évènementiel de simulateur";
				}
			}));

		} catch (IOException e1) {
			e1.printStackTrace();
			this.textAreaURLEvent.setText("Erreur dans le chargement du fichier \u00ab event.csv \u00bb. Veuillez r\u00e9essayer.");

			this.labelStatusEvent.setText("Erreur");
			this.labelStatusEvent.setForeground(Color.RED);

		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Choisir les fichiers périodiques ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonChoisirURLPeriodActionPerformed(ActionEvent e) {

		JFileChooser chooser = new JFileChooser(lastFileChooserDirectory);
		chooser.setDialogTitle("Choisir les " + listeFichiersPeriodAttendus.size() + " fichiers p\u00e9riodiques.");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().matches("period_(\\d+)\\.csv") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "\u00ab period_*.csv \u00bb - Des fichiers p\u00e9riodiques de simulateur";
			}
		});


		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			final File[] fichiers = chooser.getSelectedFiles();
			lastFileChooserDirectory = chooser.getCurrentDirectory();
			this.verifyPeriodicFiles(fichiers);
			this.verifyCreationStatus();
		}
	}

	/**
	 * Vérifies si le fichier « init.csv » est valide
	 *
	 * @param initFile le fichier d'initialisation à vérifier
	 * @return vrai si le fichier est valide
	 */
	private boolean verifyInitFile(File initFile){

		try {

			if (initFile != null && initFile.getName().equals("init.csv")) {

				LecteurCSV<ConfigurationInitialisation.LigneInitialisation> initCSV = new LecteurCSV<>(initFile, new ConfigurationInitialisation());

				this.labelStatusInit.setText("Ok");
				this.labelStatusInit.setForeground(UIManager.getColor("Label.foreground"));

				this.textAreaURLInit.setText(initFile.getAbsolutePath());

				this.listeFichiersPeriodAttendus = initCSV.stream().map(ConfigurationInitialisation.LigneInitialisation::getIdVehicle).map(id -> "period_" + id + ".csv").collect(Collectors.toList());

				this.labelNbAttendu.setText("" + listeFichiersPeriodAttendus.size());

				this.buttonChoisirURLEvent.setEnabled(true);

				this.initCSV = initFile;

				initCSV.fermer();

				return true;

			}else {

				this.initCSV = null;

				this.textAreaURLInit.setText("Erreur dans le chargement du fichier \u00ab init.csv \u00bb. Veuillez r\u00e9essayer.");

				this.labelStatusInit.setText("Erreur");
				this.labelStatusInit.setForeground(Color.RED);

				return false;
			}

		}catch (IOException e) {

			this.textAreaURLInit.setText("Erreur dans le chargement du fichier \u00ab init.csv \u00bb. Veuillez r\u00e9essayer.");

			this.labelStatusInit.setText("Erreur");
			this.labelStatusInit.setForeground(Color.RED);

			return false;
		}
	}

	/**
	 * Vérifies si le fichier « event.csv » est valide
	 *
	 * @param eventFile le fichier d'évènement à vérifier
	 * @return vrai si le fichier est valide
	 */
	private boolean verifyEventFile(File eventFile){

		if (eventFile != null && eventFile.getName().equals("event.csv")){

			this.labelStatusEvent.setText("Ok");
			this.labelStatusEvent.setForeground(UIManager.getColor("Label.foreground"));

			this.textAreaURLEvent.setText(eventFile.getAbsolutePath());

			this.buttonChoisirURLPeriod.setEnabled(true);

			this.eventCSV = eventFile;

			return true;

		}else {

			this.eventCSV = null;

			this.textAreaURLEvent.setText("Le fichier sp\u00e9cifi\u00e9 n'est pas un event.csv");

			this.labelStatusEvent.setText("Erreur");
			this.labelStatusEvent.setForeground(Color.RED);

			return false;
		}

	}

	/**
	 * Vérifies si les fichiers « period_*.csv » sont valides.
	 *
	 * @param fichiers les fichiers périodiques à vérifier
	 * @return vrai si les fichiers sont valides
	 */
	private boolean verifyPeriodicFiles(File[] fichiers) {

		if (fichiers.length != 0) {

			this.textAreaURLPeriod.setText(fichiers[0].getParentFile().getAbsolutePath() + File.separator);

			if (fichiers.length != listeFichiersPeriodAttendus.size()){

				this.labelNbValide.setForeground(Color.RED);
				this.labelNbValide.setText(fichiers.length + " au lieu de " + listeFichiersPeriodAttendus.size());

				if (fichiers.length > listeFichiersPeriodAttendus.size()) {

					this.labelLblFichiersManquants.setText("Fichiers en trop :");

					final String[] fichiersEnTrop = {""};
					Arrays.stream(fichiers).filter(f -> !listeFichiersPeriodAttendus.contains(f.getName())).forEach(f -> fichiersEnTrop[0] += f.getName() + "\n");

					this.textAreaFichierPeriodManquants.setText(fichiersEnTrop[0].substring(0,fichiersEnTrop[0].length() - 1));

				}else if (fichiers.length < listeFichiersPeriodAttendus.size()){
					this.labelLblFichiersManquants.setText("Fichiers manquants :");

					final String[] fichiersEnMoins = {""};
					listeFichiersPeriodAttendus.stream().filter(f -> Arrays.stream(fichiers).noneMatch(fichier -> fichier.getName().equals(f))).forEach(f -> fichiersEnMoins[0] += f + "\n");

					this.textAreaFichierPeriodManquants.setText(fichiersEnMoins[0].substring(0,fichiersEnMoins[0].length() - 1));
				}

				this.perdiods = null;

				return false;

			}else {

				final List<String> listeNomsFichiersCharges = Arrays.stream(fichiers).map(File::getName).collect(Collectors.toList());

				final LinkedList<String> fichiersManquants = new LinkedList<>(listeFichiersPeriodAttendus);
				fichiersManquants.removeAll(listeNomsFichiersCharges);

				final List<String> fichierIllegaux = new LinkedList<>(listeNomsFichiersCharges);
				fichierIllegaux.removeAll(listeFichiersPeriodAttendus);

				if (fichiersManquants.size() == 0){
					this.labelNbValide.setText(""+fichiers.length);
					this.labelNbValide.setForeground(UIManager.getColor("Label.foreground"));

					this.textAreaFichierPeriodManquants.setText("Aucun");

					this.perdiods = fichiers;

					return true;

				}else {

					this.perdiods = null;

					this.labelNbValide.setForeground(Color.RED);
					this.labelNbValide.setText(fichiersManquants.size() + " fichiers ill\u00e9gaux");

					final String[] fichiersIllegaux = {""};

					fichiersIllegaux[0] += "Manquant: \n";
					fichiersManquants.forEach(f -> fichiersIllegaux[0] += f + "\n");

					fichiersIllegaux[0] += "Ill\u00e9gaux: \n";
					fichierIllegaux.forEach(f -> fichiersIllegaux[0] += f + "\n");


					this.textAreaFichierPeriodManquants.setText(fichiersIllegaux[0].substring(0,fichiersIllegaux[0].length() - 1));

					return false;
				}
			}
		} else
			return false;

	}


	/**
	 * Méthode exécutée lors du clic sur le bouton « Créer le fichier ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonCreerActionPerformed(ActionEvent e) {
		if (verifyCreationStatus()) {
			try {

				FichierAMNAM f = FichierAMNAM.creerFicherAmnam(fichierACreer.toPath(), initCSV, eventCSV, perdiods, obstacles, this.textAreaComments.getText());

				if(!this.standalone)
					UICommonUtils.transfererEtape(this, new InspecteurFichierAMNAMFrame(f));

				this.dispose();

			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "Erreur dans la cr\u00e9ation du fichier .amnam.\nVeuillez r\u00e9essayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton de fermeture de la fenêtre.
	 *
	 * @param e Évènement de fermeture
	 */
	private void thisWindowClosing(WindowEvent e) {
		if (standalone)
			System.exit(0);
		else
			UICommonUtils.transfererEtape(this, new EcranAccueilFrame());
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Importer dossier ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonPickDirectoryActionPerformed(ActionEvent e) {

		try {
			JFileChooser chooser = new JFileChooser(lastFileChooserDirectory);
			chooser.setDialogTitle("Choisir le dossier contenant les CSV");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);


			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				this.loadDirectory(chooser.getCurrentDirectory());
				lastFileChooserDirectory = chooser.getCurrentDirectory();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Charge les fichier requis pour la création d'un {@link FichierAMNAM} à partir d'un dossier.
	 *
	 * @param directory le dossier à charger
	 * @throws IOException Erreur lors de la lecture du dossier
	 */
	private void loadDirectory(File directory) throws IOException {

		boolean isInitOk;
		boolean isEventOK;
		boolean isPeriodOK;ArrayList<Path> discoveredInitFiles = new ArrayList<>();
		ArrayList<Path> discoveredEventFiles = new ArrayList<>();
		ArrayList<Path> discoveredPeriodicFiles = new ArrayList<>();

		try (Stream<Path> pathStream = Files.walk(directory.getParentFile().toPath())){

			pathStream.forEach(path -> {

				final String fileName = path.getFileName().toString();

				if (fileName.equals("init.csv"))
					discoveredInitFiles.add(path);

				else if (fileName.equals("event.csv"))
					discoveredEventFiles.add(path);

				else if (fileName.matches("period_(\\d+)\\.csv"))
					discoveredPeriodicFiles.add(path);

			});

		}

		if (discoveredInitFiles.size() > 1 || discoveredInitFiles.isEmpty() || discoveredEventFiles.size() > 1 || discoveredEventFiles.isEmpty()) {

			if (discoveredInitFiles.size() > 1)
				this.textAreaResultAutomatic.append(String.format("Nombre ill\u00e9gal de fichiers \u00ab init.csv \u00bb (%d)\n", discoveredInitFiles.size()));

			if (discoveredEventFiles.size() > 1)
				this.textAreaResultAutomatic.append(String.format("Nombre ill\u00e9gal de fichiers \u00ab event.csv \u00bb (%d)\n", discoveredEventFiles.size()));

			this.clearManualSelectUI();

			this.textAreaResultAutomatic.append("Erreur dans la lecture du dossier\n");

		}else {

			isInitOk = verifyInitFile(discoveredInitFiles.get(0).toFile());
			isEventOK = verifyEventFile(discoveredEventFiles.get(0).toFile());
			isPeriodOK = verifyPeriodicFiles(discoveredPeriodicFiles.stream().map(Path::toFile).toArray(File[]::new));

			if (!isInitOk)
				this.textAreaResultAutomatic.append("Erreur dans le fichier \u00ab init.csv \u00bb\n");
			if (!isEventOK)
				this.textAreaResultAutomatic.append("Erreur dans le fichier \u00ab event.csv \u00bb\n");
			if (!isPeriodOK)
				this.textAreaResultAutomatic.append("Erreur avec les fichiers p\u00e9riodiques\n");

			if (isInitOk && isEventOK && isPeriodOK) {
				this.textAreaResultAutomatic.setText(String.format("Chargement du dossier %s effectué avec succès", directory.getParentFile().getName()));
				this.verifyCreationStatus();
			}

		}
	}

	/**
	 * Vide les champs de sélection manuelle des fichiers requis.
	 */
	private void clearManualSelectUI() {
		this.initCSV = null;
		this.textAreaURLInit.setText("");
		this.labelStatusInit.setText("N/A");
		this.buttonChoisirURLInit.setEnabled(false);

		this.eventCSV = null;
		this.textAreaURLEvent.setText("");
		this.labelStatusEvent.setText("N/A");
		this.buttonChoisirURLEvent.setEnabled(false);

		this.perdiods = null;
		this.labelNbValide.setText("");
		this.labelNbAttendu.setText("");
		this.textAreaFichierPeriodManquants.setText("");
		this.textAreaURLPeriod.setText("");
		this.buttonChoisirURLPeriod.setEnabled(false);

	}

	/**
	 * Vérifies si la création du fichier est possible.
	 * @return vrai si la création du fichier est possible
	 */
	private boolean verifyCreationStatus() {
		boolean creation =fichierACreer != null &&
						  initCSV != null &&
						  eventCSV != null &&
						  perdiods != null &&
						  perdiods.length != 0 &&
						  obstacles != null &&
						  obstacles.length != 0;

		this.buttonCreer.setEnabled(creation);

		return creation;
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur de la liste déroulante « comboBoxObstacle ».
	 *
	 * @param e Évènement de changement
	 */
	private void comboBoxObstacleActionPerformed(ActionEvent e) {
		setObsTextFieldToObsContent();
	}

	/**
	 * Transfère le contenu des champs d'entrée des obstacles vers des instances de {@link Obstacle}.
	 */
	private void setObsTextFieldToObsContent() {
		Obstacle obstacle = obstacles[comboBoxObstacle.getSelectedIndex()];

		this.buttonObstacleOmit.setText((obstacle.isOmitted() ? "Inclure" : "Omettre"));
		this.textFieldObsPosX.setEnabled(!obstacle.isOmitted());
		this.textFieldObsPosY.setEnabled(!obstacle.isOmitted());
		this.textFieldObsRadius.setEnabled(!obstacle.isOmitted());

		this.labelStatusObstacle.setText("");

		if (!obstacle.isOmitted()) {
			this.textFieldObsPosX.setText(obstacle.getPosition().getX() + "");
			this.textFieldObsPosY.setText(obstacle.getPosition().getY() + "");
			this.textFieldObsRadius.setText(obstacle.getRadius() + "");
		} else {
			this.textFieldObsPosX.setText("N/A");
			this.textFieldObsPosY.setText("N/A");
			this.textFieldObsRadius.setText("0.0");
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Appliquer » des obstacles.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonObstacleApplyActionPerformed(ActionEvent e) {
		if(this.isObsTextFieldsValid()){
			this.labelStatusObstacle.setForeground(UIManager.getColor("Label.foreground"));
			this.labelStatusObstacle.setText("Ok");

			obstacles[comboBoxObstacle.getSelectedIndex()].setPosition(new Point2D.Double(Double.parseDouble(textFieldObsPosX.getText()), Double.parseDouble(textFieldObsPosY.getText())));
			obstacles[comboBoxObstacle.getSelectedIndex()].setRadius(Double.parseDouble(textFieldObsRadius.getText()));

		}else {
			this.labelStatusObstacle.setForeground(Color.RED);
			this.labelStatusObstacle.setText("Erreur");
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Effacer » des obstacles.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonObstacleResetActionPerformed(ActionEvent e) {
		this.setObsTextFieldToObsContent();
	}

	/**
	 * Vérifies si les valeurs entrées pour l'obstacle sont valides.
	 *
	 * @return vrai si les valeurs sont valides
	 */
	private boolean isObsTextFieldsValid() {
		boolean posXOk = textFieldObsPosX.getText().matches(Utils.REGEX_DECIMAL);
		boolean posYOk = textFieldObsPosY.getText().matches(Utils.REGEX_DECIMAL);
		boolean radiusOk = textFieldObsRadius.getText().matches(Utils.REGEX_DECIMAL);

		return posXOk && posYOk && radiusOk;
	}

	/**
	 * Méthode exécutée lors du mouvement de curseur des TextFields des obstacles.
	 *
	 * @param e Évènement de curseur
	 */
	private void textFieldObsCaretUpdate(CaretEvent e) {
		this.buttonObstacleApply.setEnabled(isObsTextFieldsValid());
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « omettre » des obstacles.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonObstacleOmitActionPerformed(ActionEvent e) {

		Obstacle obstacle = this.obstacles[comboBoxObstacle.getSelectedIndex()];

		obstacle.setOmitted(!obstacle.isOmitted());

		this.textFieldObsPosX.setEnabled(!obstacle.isOmitted());
		this.textFieldObsPosY.setEnabled(!obstacle.isOmitted());
		this.textFieldObsRadius.setEnabled(!obstacle.isOmitted());

		if (obstacle.isOmitted()){
			obstacle.setPosition(OMITTED_POS);
			obstacle.setRadius(0);
		}else {
			this.textFieldObsPosX.setText("0.0");
			this.textFieldObsPosY.setText("0.0");
			this.textFieldObsRadius.setText("0.0");
			obstacle.setPosition(new Point2D.Double(0,0));
		}

		this.buttonObstacleOmit.setText((obstacle.isOmitted() ? "Inclure" : "Omettre"));

		this.obstacles[comboBoxObstacle.getSelectedIndex()] = obstacle;
	}

	/**
	 * Affiche une fenêtre de sélection de fichier et retourne le ficher choisi.
	 *
	 * @param titre  Le titre de la fenêtre de sélection de fichier
	 * @param filtre Le filtre à appliquer pour la sélection de fichier
	 *
	 * @return un {@link FichierAMNAM} à partir du fichier choisi ou null si le fichier n'est pas conforme
	 * @throws IOException Erreur d'initialisation du {@link FichierAMNAM}
	 */
	private File chargerUnFichier(String titre, @Nullable FileFilter filtre) throws IOException {

		JFileChooser chooser = new JFileChooser(lastFileChooserDirectory);
		chooser.setDialogTitle(titre);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(filtre);


		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			lastFileChooserDirectory = chooser.getCurrentDirectory();
			return chooser.getSelectedFile();

		}

		return null;
	}

	/**
	 * Initialise tous les composants graphiques du panneau.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.logoAMNAM = new JLabel();
		this.labelTitle = new JLabel();
		this.panelURL = new JPanel();
		this.labelNomFichier = new JLabel();
		this.labelLblNomFichier = new JLabel();
		this.labelLblComments = new JLabel();
		this.buttonChoisirURL = new JButton();
		this.scrollPaneComments = new JScrollPane();
		this.textAreaComments = new JTextArea();
		this.scrollPaneURL = new JScrollPane();
		this.textAreaURL = new JTextArea();
		this.buttonAnnuler = new JButton();
		this.buttonCreer = new JButton();
		this.panelObstacles = new JPanel();
		this.labelStatusObstacle = new JLabel();
		this.comboBoxObstacle = new JComboBox<>();
		this.textFieldObsPosX = new JTextField();
		this.textFieldObsPosY = new JTextField();
		this.textFieldObsRadius = new JTextField();
		this.labelLblObsPosX = new JLabel();
		this.labelLblObsPosY = new JLabel();
		this.labelLblObsRadius = new JLabel();
		this.buttonObstacleApply = new JButton();
		this.buttonObstacleReset = new JButton();
		this.buttonObstacleOmit = new JButton();
		this.panelFileInput = new JPanel();
		this.panelManualInput = new JPanel();
		this.panelFichierInit = new JPanel();
		this.labelStatusInit = new JLabel();
		this.buttonChoisirURLInit = new JButton();
		this.scrollPaneURLInit = new JScrollPane();
		this.textAreaURLInit = new JTextArea();
		this.panelFichierEvent = new JPanel();
		this.labelStatusEvent = new JLabel();
		this.buttonChoisirURLEvent = new JButton();
		this.scrollPaneURLEvent = new JScrollPane();
		this.textAreaURLEvent = new JTextArea();
		this.panelPeriod = new JPanel();
		this.labelLblNbAttendu = new JLabel();
		this.labelNbAttendu = new JLabel();
		this.labelLblNbValide = new JLabel();
		this.labelNbValide = new JLabel();
		this.labelLblFichiersManquants = new JLabel();
		this.scrollPaneFichiersManquants = new JScrollPane();
		this.textAreaFichierPeriodManquants = new JTextArea();
		this.buttonChoisirURLPeriod = new JButton();
		this.labelLblPeriodCheminAcces = new JLabel();
		this.scrollPaneURLPeriod = new JScrollPane();
		this.textAreaURLPeriod = new JTextArea();
		this.panelAutomaticInput = new JPanel();
		this.buttonPickDirectory = new JButton();
		this.scrollPaneResultAutomatic = new JScrollPane();
		this.textAreaResultAutomatic = new JTextArea();
		this.labelLblResultAutomatic = new JLabel();
		this.textArea1 = new JTextArea();

		//======== this ========
		setTitle("Cr\u00e9ation d'un fichier | AMNAM"); //$NON-NLS-1$ //NON-NLS
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
		this.logoAMNAM.setBounds(5, 5, 55, 50);

		//---- labelTitle ----
		this.labelTitle.setText("Cr\u00e9ation de fichier .amnam"); //$NON-NLS-1$ //NON-NLS
		this.labelTitle.setFont(new Font(".SF NS Text", Font.PLAIN, 22)); //$NON-NLS-1$ //NON-NLS
		this.labelTitle.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(this.labelTitle);
		this.labelTitle.setBounds(65, 5, 330, 50);

		//======== panelURL ========
		{
			this.panelURL.setBorder(new TitledBorder("Chemin d'acc\u00e8s du fichier")); //$NON-NLS-1$ //NON-NLS
			this.panelURL.setLayout(null);

			//---- labelNomFichier ----
			this.labelNomFichier.setHorizontalAlignment(SwingConstants.TRAILING);
			this.panelURL.add(this.labelNomFichier);
			this.labelNomFichier.setBounds(165, 58, 215, 24);

			//---- labelLblNomFichier ----
			this.labelLblNomFichier.setText("Nom du fichier :"); //$NON-NLS-1$ //NON-NLS
			this.panelURL.add(this.labelLblNomFichier);
			this.labelLblNomFichier.setBounds(10, 58, 120, 24);

			//---- labelLblComments ----
			this.labelLblComments.setText("Commentaires :"); //$NON-NLS-1$ //NON-NLS
			this.panelURL.add(this.labelLblComments);
			this.labelLblComments.setBounds(10, 149, 120, 24);

			//---- buttonChoisirURL ----
			this.buttonChoisirURL.setText("Choisir le chemin d'acc\u00e8s"); //$NON-NLS-1$ //NON-NLS
			this.buttonChoisirURL.addActionListener(e -> buttonChoisirURLActionPerformed(e));
			this.panelURL.add(this.buttonChoisirURL);
			this.buttonChoisirURL.setBounds(10, 20, 370, this.buttonChoisirURL.getPreferredSize().height);

			//======== scrollPaneComments ========
			{
				this.scrollPaneComments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

				//---- textAreaComments ----
				this.textAreaComments.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
				this.textAreaComments.setLineWrap(true);
				this.scrollPaneComments.setViewportView(this.textAreaComments);
			}
			this.panelURL.add(this.scrollPaneComments);
			this.scrollPaneComments.setBounds(10, 179, 370, 58);

			//======== scrollPaneURL ========
			{

				//---- textAreaURL ----
				this.textAreaURL.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
				this.textAreaURL.setEditable(false);
				this.textAreaURL.setText("Cliquez sur le bouton \u00ab Choisir le chemin d'acc\u00e8s \u00bb pour d\u00e9buter"); //$NON-NLS-1$ //NON-NLS
				this.textAreaURL.setLineWrap(true);
				this.textAreaURL.setWrapStyleWord(true);
				this.scrollPaneURL.setViewportView(this.textAreaURL);
			}
			this.panelURL.add(this.scrollPaneURL);
			this.scrollPaneURL.setBounds(10, 88, 370, 55);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelURL.getComponentCount(); i++) {
					Rectangle bounds = this.panelURL.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelURL.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelURL.setMinimumSize(preferredSize);
				this.panelURL.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelURL);
		this.panelURL.setBounds(5, 140, 390, 250);

		//---- buttonAnnuler ----
		this.buttonAnnuler.setText("Annuler"); //$NON-NLS-1$ //NON-NLS
		this.buttonAnnuler.addActionListener(e -> buttonAnnulerActionPerformed(e));
		contentPane.add(this.buttonAnnuler);
		this.buttonAnnuler.setBounds(5, 568, 145, 32);

		//---- buttonCreer ----
		this.buttonCreer.setText("Cr\u00e9er le fichier"); //$NON-NLS-1$ //NON-NLS
		this.buttonCreer.setEnabled(false);
		this.buttonCreer.addActionListener(e -> buttonCreerActionPerformed(e));
		contentPane.add(this.buttonCreer);
		this.buttonCreer.setBounds(155, 568, 240, 32);

		//======== panelObstacles ========
		{
			this.panelObstacles.setBorder(new TitledBorder("Donn\u00e9es des obstacles (Il faut appliquer \u00e0 chaque fois)")); //$NON-NLS-1$ //NON-NLS
			this.panelObstacles.setLayout(null);

			//---- labelStatusObstacle ----
			this.labelStatusObstacle.setHorizontalAlignment(SwingConstants.TRAILING);
			this.panelObstacles.add(this.labelStatusObstacle);
			this.labelStatusObstacle.setBounds(315, 20, 65, 26);

			//---- comboBoxObstacle ----
			this.comboBoxObstacle.setModel(new DefaultComboBoxModel<>(new String[] {
					"1 - Nid de poule", //$NON-NLS-1$ //NON-NLS
					"2 - Nid de poule", //$NON-NLS-1$ //NON-NLS
					"3 - Bloqueur", //$NON-NLS-1$ //NON-NLS
					"4 - Bloqueur", //$NON-NLS-1$ //NON-NLS
					"5 - Variateur", //$NON-NLS-1$ //NON-NLS
					"6 - Variateur", //$NON-NLS-1$ //NON-NLS
					"7 - Neige", //$NON-NLS-1$ //NON-NLS
					"8 - Glace", //$NON-NLS-1$ //NON-NLS
					"9 - Pluie" //$NON-NLS-1$ //NON-NLS
			}));
			this.comboBoxObstacle.addActionListener(e -> comboBoxObstacleActionPerformed(e));
			this.panelObstacles.add(this.comboBoxObstacle);
			this.comboBoxObstacle.setBounds(10, 20, 180, this.comboBoxObstacle.getPreferredSize().height);

			//---- textFieldObsPosX ----
			this.textFieldObsPosX.addCaretListener(e -> textFieldObsCaretUpdate(e));
			this.panelObstacles.add(this.textFieldObsPosX);
			this.textFieldObsPosX.setBounds(10, 76, 180, this.textFieldObsPosX.getPreferredSize().height);

			//---- textFieldObsPosY ----
			this.textFieldObsPosY.addCaretListener(e -> textFieldObsCaretUpdate(e));
			this.panelObstacles.add(this.textFieldObsPosY);
			this.textFieldObsPosY.setBounds(200, 76, 180, this.textFieldObsPosY.getPreferredSize().height);

			//---- textFieldObsRadius ----
			this.textFieldObsRadius.addCaretListener(e -> textFieldObsCaretUpdate(e));
			this.panelObstacles.add(this.textFieldObsRadius);
			this.textFieldObsRadius.setBounds(10, 130, 180, this.textFieldObsRadius.getPreferredSize().height);

			//---- labelLblObsPosX ----
			this.labelLblObsPosX.setText("Position X (m) :"); //$NON-NLS-1$ //NON-NLS
			this.panelObstacles.add(this.labelLblObsPosX);
			this.labelLblObsPosX.setBounds(10, 53, 180, this.labelLblObsPosX.getPreferredSize().height);

			//---- labelLblObsPosY ----
			this.labelLblObsPosY.setText("Position Y (m):"); //$NON-NLS-1$ //NON-NLS
			this.panelObstacles.add(this.labelLblObsPosY);
			this.labelLblObsPosY.setBounds(200, 53, 175, 16);

			//---- labelLblObsRadius ----
			this.labelLblObsRadius.setText("Rayon (m) :"); //$NON-NLS-1$ //NON-NLS
			this.panelObstacles.add(this.labelLblObsRadius);
			this.labelLblObsRadius.setBounds(new Rectangle(new Point(10, 107), this.labelLblObsRadius.getPreferredSize()));

			//---- buttonObstacleApply ----
			this.buttonObstacleApply.setText("Appliquer"); //$NON-NLS-1$ //NON-NLS
			this.buttonObstacleApply.addActionListener(e -> buttonObstacleApplyActionPerformed(e));
			this.panelObstacles.add(this.buttonObstacleApply);
			this.buttonObstacleApply.setBounds(new Rectangle(new Point(291, 125), this.buttonObstacleApply.getPreferredSize()));

			//---- buttonObstacleReset ----
			this.buttonObstacleReset.setText("Effacer"); //$NON-NLS-1$ //NON-NLS
			this.buttonObstacleReset.addActionListener(e -> buttonObstacleResetActionPerformed(e));
			this.panelObstacles.add(this.buttonObstacleReset);
			this.buttonObstacleReset.setBounds(200, 125, 90, 32);

			//---- buttonObstacleOmit ----
			this.buttonObstacleOmit.setText("Omettre"); //$NON-NLS-1$ //NON-NLS
			this.buttonObstacleOmit.addActionListener(e -> buttonObstacleOmitActionPerformed(e));
			this.panelObstacles.add(this.buttonObstacleOmit);
			this.buttonObstacleOmit.setBounds(200, 20, 90, 26);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelObstacles.getComponentCount(); i++) {
					Rectangle bounds = this.panelObstacles.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelObstacles.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelObstacles.setMinimumSize(preferredSize);
				this.panelObstacles.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelObstacles);
		this.panelObstacles.setBounds(5, 395, 390, 165);

		//======== panelFileInput ========
		{
			this.panelFileInput.setBorder(new TitledBorder("Saisie de fichiers")); //$NON-NLS-1$ //NON-NLS
			this.panelFileInput.setLayout(null);

			//======== panelManualInput ========
			{
				this.panelManualInput.setBorder(new TitledBorder("Saisie manuelle")); //$NON-NLS-1$ //NON-NLS
				this.panelManualInput.setLayout(null);

				//======== panelFichierInit ========
				{
					this.panelFichierInit.setBorder(new TitledBorder("Chemin d'acc\u00e8s du fichier \u00ab init.csv \u00bb")); //$NON-NLS-1$ //NON-NLS
					this.panelFichierInit.setLayout(null);

					//---- labelStatusInit ----
					this.labelStatusInit.setHorizontalAlignment(SwingConstants.TRAILING);
					this.labelStatusInit.setText("N/A"); //$NON-NLS-1$ //NON-NLS
					this.panelFichierInit.add(this.labelStatusInit);
					this.labelStatusInit.setBounds(310, 20, 65, 35);

					//---- buttonChoisirURLInit ----
					this.buttonChoisirURLInit.setText("Choisir le fichier \u00ab init.csv \u00bb"); //$NON-NLS-1$ //NON-NLS
					this.buttonChoisirURLInit.setEnabled(false);
					this.buttonChoisirURLInit.addActionListener(e -> buttonChoisirURLInitActionPerformed(e));
					this.panelFichierInit.add(this.buttonChoisirURLInit);
					this.buttonChoisirURLInit.setBounds(10, 60, 365, this.buttonChoisirURLInit.getPreferredSize().height);

					//======== scrollPaneURLInit ========
					{

						//---- textAreaURLInit ----
						this.textAreaURLInit.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
						this.textAreaURLInit.setEditable(false);
						this.scrollPaneURLInit.setViewportView(this.textAreaURLInit);
					}
					this.panelFichierInit.add(this.scrollPaneURLInit);
					this.scrollPaneURLInit.setBounds(10, 20, 295, 35);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < this.panelFichierInit.getComponentCount(); i++) {
							Rectangle bounds = this.panelFichierInit.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = this.panelFichierInit.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						this.panelFichierInit.setMinimumSize(preferredSize);
						this.panelFichierInit.setPreferredSize(preferredSize);
					}
				}
				this.panelManualInput.add(this.panelFichierInit);
				this.panelFichierInit.setBounds(5, 20, 385, 100);

				//======== panelFichierEvent ========
				{
					this.panelFichierEvent.setBorder(new TitledBorder("Chemin d'acc\u00e8s du fichier \u00ab event.csv \u00bb")); //$NON-NLS-1$ //NON-NLS
					this.panelFichierEvent.setLayout(null);

					//---- labelStatusEvent ----
					this.labelStatusEvent.setHorizontalAlignment(SwingConstants.TRAILING);
					this.labelStatusEvent.setText("N/A"); //$NON-NLS-1$ //NON-NLS
					this.panelFichierEvent.add(this.labelStatusEvent);
					this.labelStatusEvent.setBounds(310, 20, 65, 35);

					//---- buttonChoisirURLEvent ----
					this.buttonChoisirURLEvent.setText("Choisir le fichier \u00ab event.csv \u00bb"); //$NON-NLS-1$ //NON-NLS
					this.buttonChoisirURLEvent.setEnabled(false);
					this.buttonChoisirURLEvent.addActionListener(e -> buttonChoisirURLEventActionPerformed(e));
					this.panelFichierEvent.add(this.buttonChoisirURLEvent);
					this.buttonChoisirURLEvent.setBounds(10, 60, 365, this.buttonChoisirURLEvent.getPreferredSize().height);

					//======== scrollPaneURLEvent ========
					{

						//---- textAreaURLEvent ----
						this.textAreaURLEvent.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
						this.textAreaURLEvent.setEditable(false);
						this.scrollPaneURLEvent.setViewportView(this.textAreaURLEvent);
					}
					this.panelFichierEvent.add(this.scrollPaneURLEvent);
					this.scrollPaneURLEvent.setBounds(10, 20, 295, 35);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < this.panelFichierEvent.getComponentCount(); i++) {
							Rectangle bounds = this.panelFichierEvent.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = this.panelFichierEvent.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						this.panelFichierEvent.setMinimumSize(preferredSize);
						this.panelFichierEvent.setPreferredSize(preferredSize);
					}
				}
				this.panelManualInput.add(this.panelFichierEvent);
				this.panelFichierEvent.setBounds(5, 120, 385, 100);

				//======== panelPeriod ========
				{
					this.panelPeriod.setBorder(new TitledBorder("Fichiers p\u00e9riodiques")); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.setLayout(null);

					//---- labelLblNbAttendu ----
					this.labelLblNbAttendu.setText("Nombre de fichiers attendus : "); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.add(this.labelLblNbAttendu);
					this.labelLblNbAttendu.setBounds(new Rectangle(new Point(10, 25), this.labelLblNbAttendu.getPreferredSize()));

					//---- labelNbAttendu ----
					this.labelNbAttendu.setHorizontalAlignment(SwingConstants.TRAILING);
					this.labelNbAttendu.setText("N/A"); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.add(this.labelNbAttendu);
					this.labelNbAttendu.setBounds(200, 25, 174, 16);

					//---- labelLblNbValide ----
					this.labelLblNbValide.setText("Nombre de fichiers valides : "); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.add(this.labelLblNbValide);
					this.labelLblNbValide.setBounds(10, 46, 188, 16);

					//---- labelNbValide ----
					this.labelNbValide.setHorizontalAlignment(SwingConstants.TRAILING);
					this.labelNbValide.setText("N/A"); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.add(this.labelNbValide);
					this.labelNbValide.setBounds(200, 46, 174, 16);

					//---- labelLblFichiersManquants ----
					this.labelLblFichiersManquants.setText("Fichiers manquants :"); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.add(this.labelLblFichiersManquants);
					this.labelLblFichiersManquants.setBounds(10, 67, 188, 16);

					//======== scrollPaneFichiersManquants ========
					{
						this.scrollPaneFichiersManquants.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

						//---- textAreaFichierPeriodManquants ----
						this.textAreaFichierPeriodManquants.setEditable(false);
						this.scrollPaneFichiersManquants.setViewportView(this.textAreaFichierPeriodManquants);
					}
					this.panelPeriod.add(this.scrollPaneFichiersManquants);
					this.scrollPaneFichiersManquants.setBounds(10, 88, 180, 77);

					//---- buttonChoisirURLPeriod ----
					this.buttonChoisirURLPeriod.setText("Choisir les fichiers p\u00e9riodiques"); //$NON-NLS-1$ //NON-NLS
					this.buttonChoisirURLPeriod.setEnabled(false);
					this.buttonChoisirURLPeriod.addActionListener(e -> buttonChoisirURLPeriodActionPerformed(e));
					this.panelPeriod.add(this.buttonChoisirURLPeriod);
					this.buttonChoisirURLPeriod.setBounds(10, 168, 365, this.buttonChoisirURLPeriod.getPreferredSize().height);

					//---- labelLblPeriodCheminAcces ----
					this.labelLblPeriodCheminAcces.setText("Chemin d'acc\u00e8s :"); //$NON-NLS-1$ //NON-NLS
					this.panelPeriod.add(this.labelLblPeriodCheminAcces);
					this.labelLblPeriodCheminAcces.setBounds(new Rectangle(new Point(195, 65), this.labelLblPeriodCheminAcces.getPreferredSize()));

					//======== scrollPaneURLPeriod ========
					{
						this.scrollPaneURLPeriod.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

						//---- textAreaURLPeriod ----
						this.textAreaURLPeriod.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
						this.textAreaURLPeriod.setEditable(false);
						this.textAreaURLPeriod.setLineWrap(true);
						this.textAreaURLPeriod.setWrapStyleWord(true);
						this.scrollPaneURLPeriod.setViewportView(this.textAreaURLPeriod);
					}
					this.panelPeriod.add(this.scrollPaneURLPeriod);
					this.scrollPaneURLPeriod.setBounds(195, 88, 180, 77);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < this.panelPeriod.getComponentCount(); i++) {
							Rectangle bounds = this.panelPeriod.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = this.panelPeriod.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						this.panelPeriod.setMinimumSize(preferredSize);
						this.panelPeriod.setPreferredSize(preferredSize);
					}
				}
				this.panelManualInput.add(this.panelPeriod);
				this.panelPeriod.setBounds(5, 220, 385, 210);

				{ // compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < this.panelManualInput.getComponentCount(); i++) {
						Rectangle bounds = this.panelManualInput.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = this.panelManualInput.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					this.panelManualInput.setMinimumSize(preferredSize);
					this.panelManualInput.setPreferredSize(preferredSize);
				}
			}
			this.panelFileInput.add(this.panelManualInput);
			this.panelManualInput.setBounds(5, 155, 395, 435);

			//======== panelAutomaticInput ========
			{
				this.panelAutomaticInput.setBorder(new TitledBorder("Saisie automatique")); //$NON-NLS-1$ //NON-NLS
				this.panelAutomaticInput.setLayout(null);

				//---- buttonPickDirectory ----
				this.buttonPickDirectory.setText("Importer dossier"); //$NON-NLS-1$ //NON-NLS
				this.buttonPickDirectory.setEnabled(false);
				this.buttonPickDirectory.addActionListener(e -> buttonPickDirectoryActionPerformed(e));
				this.panelAutomaticInput.add(this.buttonPickDirectory);
				this.buttonPickDirectory.setBounds(10, 20, 375, 40);

				//======== scrollPaneResultAutomatic ========
				{
					this.scrollPaneResultAutomatic.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

					//---- textAreaResultAutomatic ----
					this.textAreaResultAutomatic.setBackground(UIManager.getColor("EditorPane.inactiveBackground")); //$NON-NLS-1$ //NON-NLS
					this.textAreaResultAutomatic.setEditable(false);
					this.textAreaResultAutomatic.setLineWrap(true);
					this.textAreaResultAutomatic.setWrapStyleWord(true);
					this.scrollPaneResultAutomatic.setViewportView(this.textAreaResultAutomatic);
				}
				this.panelAutomaticInput.add(this.scrollPaneResultAutomatic);
				this.scrollPaneResultAutomatic.setBounds(10, 85, 375, 40);

				//---- labelLblResultAutomatic ----
				this.labelLblResultAutomatic.setText("R\u00e9sultats :"); //$NON-NLS-1$ //NON-NLS
				this.panelAutomaticInput.add(this.labelLblResultAutomatic);
				this.labelLblResultAutomatic.setBounds(new Rectangle(new Point(10, 65), this.labelLblResultAutomatic.getPreferredSize()));

				{ // compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < this.panelAutomaticInput.getComponentCount(); i++) {
						Rectangle bounds = this.panelAutomaticInput.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = this.panelAutomaticInput.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					this.panelAutomaticInput.setMinimumSize(preferredSize);
					this.panelAutomaticInput.setPreferredSize(preferredSize);
				}
			}
			this.panelFileInput.add(this.panelAutomaticInput);
			this.panelAutomaticInput.setBounds(5, 20, 395, 132);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelFileInput.getComponentCount(); i++) {
					Rectangle bounds = this.panelFileInput.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelFileInput.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelFileInput.setMinimumSize(preferredSize);
				this.panelFileInput.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(this.panelFileInput);
		this.panelFileInput.setBounds(395, 5, 415, 595);

		//---- textArea1 ----
		this.textArea1.setText("Fonctionnement:\n1. S\u00e9lection du r\u00e9pertoire et du nom du fichier\n2. Saisie de la position initiale et du rayon des obstacles\n3. S\u00e9lection des fichiers de donn\u00e9es de simulation"); //$NON-NLS-1$ //NON-NLS
		this.textArea1.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
		this.textArea1.setEditable(false);
		this.textArea1.setWrapStyleWord(true);
		this.textArea1.setLineWrap(true);
		this.textArea1.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
		contentPane.add(this.textArea1);
		this.textArea1.setBounds(5, 60, 390, 80);

		contentPane.setPreferredSize(new Dimension(815, 630));
		setSize(815, 630);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		if (this.standalone)
			this.setTitle(this.getTitle() + " | Version autonome");


		UICommonUtils.addIconToLabel(this.logoAMNAM, PATH_IMG_AMNAM_LOGO_URL);

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel logoAMNAM;
	private JLabel labelTitle;
	private JPanel panelURL;
	private JLabel labelNomFichier;
	private JLabel labelLblNomFichier;
	private JLabel labelLblComments;
	private JButton buttonChoisirURL;
	private JScrollPane scrollPaneComments;
	private JTextArea textAreaComments;
	private JScrollPane scrollPaneURL;
	private JTextArea textAreaURL;
	private JButton buttonAnnuler;
	private JButton buttonCreer;
	private JPanel panelObstacles;
	private JLabel labelStatusObstacle;
	private JComboBox<String> comboBoxObstacle;
	private JTextField textFieldObsPosX;
	private JTextField textFieldObsPosY;
	private JTextField textFieldObsRadius;
	private JLabel labelLblObsPosX;
	private JLabel labelLblObsPosY;
	private JLabel labelLblObsRadius;
	private JButton buttonObstacleApply;
	private JButton buttonObstacleReset;
	private JButton buttonObstacleOmit;
	private JPanel panelFileInput;
	private JPanel panelManualInput;
	private JPanel panelFichierInit;
	private JLabel labelStatusInit;
	private JButton buttonChoisirURLInit;
	private JScrollPane scrollPaneURLInit;
	private JTextArea textAreaURLInit;
	private JPanel panelFichierEvent;
	private JLabel labelStatusEvent;
	private JButton buttonChoisirURLEvent;
	private JScrollPane scrollPaneURLEvent;
	private JTextArea textAreaURLEvent;
	private JPanel panelPeriod;
	private JLabel labelLblNbAttendu;
	private JLabel labelNbAttendu;
	private JLabel labelLblNbValide;
	private JLabel labelNbValide;
	private JLabel labelLblFichiersManquants;
	private JScrollPane scrollPaneFichiersManquants;
	private JTextArea textAreaFichierPeriodManquants;
	private JButton buttonChoisirURLPeriod;
	private JLabel labelLblPeriodCheminAcces;
	private JScrollPane scrollPaneURLPeriod;
	private JTextArea textAreaURLPeriod;
	private JPanel panelAutomaticInput;
	private JButton buttonPickDirectory;
	private JScrollPane scrollPaneResultAutomatic;
	private JTextArea textAreaResultAutomatic;
	private JLabel labelLblResultAutomatic;
	private JTextArea textArea1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
