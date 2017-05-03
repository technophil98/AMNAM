package io.amnam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.sun.istack.internal.Nullable;
import io.csv.LecteurCSV;
import io.csv.config.ConfigurationEvenement;
import io.csv.config.ConfigurationInitialisation;
import io.csv.config.ConfigurationPeriode;
import io.csv.config.ConfigurationPeriode.InfoPeriode;
import scenario.Obstacle;
import scenario.Scenario;
import scenario.events.Event;
import scenario.events.ReportedEvent;
import scenario.events.VehicleEvent;
import scenario.events.event_manipulations.AnalysisResult;
import utils.ZipUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.*;
import java.sql.Date;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scenario.Obstacle.DEFAULT_OBSTACLES;
import static ui.UICommonUtils.lastFileChooserDirectory;

/**
 * Classe gérant toute interraction avec les fichiers personnalisés « *.amnam »
 *
 * @author Philippe Rivest
 */
public class FichierAMNAM {

	private static final String PATH_AMNAM_INFO_JSON = "amnam_info.json";

	private static final String PATH_CSV = "csv_data/";
	private static final String PATH_CSV_INIT = PATH_CSV + "init.csv";
	private static final String PATH_CSV_EVENT = PATH_CSV + "event.csv";

	private static final String PATH_VEHICULE_EVENTS = "vehicule_events/v_events.json";
	private static final String PATH_REPORTED_EVENTS = "vehicule_events/reported_events.json";

	private static final String PATH_OBFUSCATED_EVENTS = "obfuscation/obf_events.json";

	private static final String PATH_ANALYSED_EVENTS = "analysis/analys_events.json";

	public static final String SAVE_ERROR_DIALOG_MESSAGE = "Erreur dans la sauvegarde du fichier.";
	public static final String SAVE_ERROR_DIALOG_TITLE = "Erreur de sauvegarde";

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private String fichierZipAmnamName;

	private AMNAMInfo info;

	private ConfigurationInitialisation.LigneInitialisation[] lignesInitCSV;
	private ConfigurationEvenement.LigneEvenement[] lignesEventCSV;
	private InfoPeriode[] informationsPeriodiques;

	private VehicleEvent[] vehiculeEvents;
	private ReportedEvent[] reportedEvents;
	private ReportedEvent[] obfuscatedEvents;
	private AnalysisResult analysisResults;

	private Scenario scenario;

	/**
	 * Instancies un nouveau FichierAMNAM.
	 *
	 * @param fichierAmnam Un fichier local d'extension .amnam
	 * @throws IOException Lancée dans le cas ou l'initialisation du fichier est impossible
	 */
	public FichierAMNAM(Path fichierAmnam) throws IOException {
		fichierZipAmnamName = fichierAmnam.toString();

		this.initialiserFichierInternes();
		this.scenario = new Scenario(this);
	}

	/**
	 * Crée un FichierAMNAM {@link EtatsFichiersAMNAM#VIDE} au répertoire
	 * précisé. Cette méthode écrase le fichier existant en cas de conflit.
	 *
	 * @param cheminDAcces Le chemin d'accès où le ficher sera créé
	 * @param initCSV      Le fichier « init.csv » de la simulation
	 * @param eventCSV     Le fichier « event.csv » de la simulation
	 * @param periods      Les fichier périodiques de la simulation
	 * @param obstacles    Les obstacles présents dans la simulation
	 * @param comments     Les commentaires résumant la simulation
	 * @return Un FichierAMNAM {@link EtatsFichiersAMNAM#VIDE}
	 * @throws IOException Lancée dans le cas ou l'initialisation du fichier est impossible
	 */
	public static FichierAMNAM creerFicherAmnam(Path cheminDAcces, File initCSV, File eventCSV, File[] periods, @Nullable Obstacle[] obstacles, @Nullable String comments) throws IOException {

		// Écraser le fichier précédent
		if (Files.exists(cheminDAcces) && !Files.deleteIfExists(cheminDAcces))
			throw new IOException("Ecrasement du fichier " + cheminDAcces + " est impossible!");

		URI uri = URI.create("jar:" + cheminDAcces.toUri().toString());

		Map<String, String> env = new HashMap<>();
		env.put("create", "true");

		try(FileSystem fs = FileSystems.newFileSystem(uri, env)){

			//amnam_info.json
			AMNAMInfo info = new AMNAMInfo();

			info.setComments((comments != null ? comments : ""));
			info.setEtat(EtatsFichiersAMNAM.CSV_DATA);

			info.setNbVehicules(((int) new LecteurCSV<>(initCSV, new ConfigurationInitialisation()).stream().count()));

			info.getEtape(EtatsFichiersAMNAM.CSV_DATA).setExecute(true);
			info.getEtape(EtatsFichiersAMNAM.CSV_DATA).setDateExecution(Date.from(Instant.now()));

			info.setObstacles((obstacles != null ? obstacles : DEFAULT_OBSTACLES));


			ZipUtil.serializeJSONToZipFileSystem(fs, info, PATH_AMNAM_INFO_JSON);

			//Ajout du init.csv
			ZipUtil.addFileToZipFileSystem(fs, initCSV, PATH_CSV_INIT);

			//Ajout du event.csv
			ZipUtil.addFileToZipFileSystem(fs, eventCSV, PATH_CSV_EVENT);

			//Ajout des fichiers périodiques
			for (File fichierPeriod : periods) {
				ZipUtil.addFileToZipFileSystem(fs, fichierPeriod, PATH_CSV + fichierPeriod.getName());
			}
		}

		return new FichierAMNAM(cheminDAcces);
	}

	/**
	 * Méthode utilitaire qui modifie l'état du fichier stipulé dans « amnam_info.json » en cas d'erreur de lecture.
	 *
	 * @see AMNAMInfo
	 * @param fichierAMANM Le fichierAMNAM à modifier
	 * @param etat         L'état à forcer pour le fichier
	 */
	public static void forceState(Path fichierAMANM, EtatsFichiersAMNAM etat){

			try(FileSystem fs = FileSystems.newFileSystem(fichierAMANM, null)) {

				JsonReader reader = new JsonReader(new InputStreamReader(Files.newInputStream(fs.getPath(PATH_AMNAM_INFO_JSON))));
				AMNAMInfo info = GSON.fromJson(reader, AMNAMInfo.class);

				info.setEtat(etat);

				ZipUtil.serializeJSONToZipFileSystem(fs, info, PATH_AMNAM_INFO_JSON);

			}catch (JsonIOException | JsonSyntaxException | IOException e) {
				System.out.println("Le fichier info " + PATH_AMNAM_INFO_JSON + " n'est pas lisible.");
				e.printStackTrace();
			}
	}

	/**
	 * Affiche une fenêtre de sélection de fichier et retourne le ficher choisi.
	 *
	 * @param titre  Le titre de la fenêtre
	 * @param parent Le parent du dialogue {@link JFileChooser#parent}
	 * @return un {@link FichierAMNAM} à partir du fichier choisi ou null si le fichier n'est pas conforme
	 * @throws IOException Erreur d'initialisation du {@link FichierAMNAM}
	 */
	public static FichierAMNAM chargerUnFichier(String titre, @Nullable Frame parent) throws IOException {

		JFileChooser chooser = new JFileChooser(lastFileChooserDirectory);
		chooser.setDialogTitle(titre);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("\u00ab *.amnam \u00bb - Fichier AMNAM", "amnam"));


		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {

			lastFileChooserDirectory = chooser.getCurrentDirectory();
			return chooser.getSelectedFile() != null ? new FichierAMNAM(chooser.getSelectedFile().toPath()) : null;

		} else {
			return null;
		}
	}

	/**
	 * Charge le fichier amnam_info.json contenu dans le fichier au répertoire {@link FichierAMNAM#fichierZipAmnamName} dans une instance de {@link AMNAMInfo}.
	 *
	 * @throws IOException Le fichier amnam_info.json n'est pas valide
	 */
	private void chargerInfo() throws IOException {

		try(FileSystem fs = FileSystems.newFileSystem(Paths.get(fichierZipAmnamName), null)) {

			JsonReader reader = new JsonReader(new InputStreamReader(Files.newInputStream(fs.getPath(PATH_AMNAM_INFO_JSON))));
			final AMNAMInfo info = GSON.fromJson(reader, AMNAMInfo.class);

			if (info == null)
				throw new IOException("Le fichier " + PATH_AMNAM_INFO_JSON + " n'est pas valide.");

			this.info = info;

		}catch (JsonIOException | JsonSyntaxException e) {
			throw new IOException("Le fichier " + PATH_AMNAM_INFO_JSON + " n'est pas valide.", e);
		}
	}

	/**
	 * Charge les différents fichiers contenus dans le
	 * fichier compressé au répertoire {@link FichierAMNAM#fichierZipAmnamName} selon l'état de celui-ci.
	 *
	 * @throws IOException Erreur dans le chargement des fichiers
	 * @see EtatsFichiersAMNAM
	 */
	private void initialiserFichierInternes() throws IOException {

		this.chargerInfo();

		switch (this.info.getEtat()) {

			case ANALYSE:
				analysisResults = ZipUtil.loadFromJSONinZip(Paths.get(fichierZipAmnamName), PATH_ANALYSED_EVENTS, AnalysisResult.class);

			case OBFUSQUATION_EVENEMENTS:
				obfuscatedEvents = ZipUtil.loadFromJSONinZip(Paths.get(fichierZipAmnamName), PATH_OBFUSCATED_EVENTS, ReportedEvent[].class);

			case EVENEMENTS:
				vehiculeEvents = ZipUtil.loadFromJSONinZip(Paths.get(fichierZipAmnamName), PATH_VEHICULE_EVENTS, VehicleEvent[].class);
				reportedEvents = ZipUtil.loadFromJSONinZip(Paths.get(fichierZipAmnamName), PATH_REPORTED_EVENTS, ReportedEvent[].class);

			case CSV_DATA:
				chargerCSV();

			case VIDE:
				//Aucun chargement nécessaire
				break;

		}

	}

	/**
	 * Charge les différents fichiers CSV contenus dans le fichier au répertoire {@link FichierAMNAM#fichierZipAmnamName}.
	 *
	 * @throws IOException Erreur dans le chargement des fichiers CSV
	 */
	private void chargerCSV() throws IOException {

		try(FileSystem fs = FileSystems.newFileSystem(Paths.get(fichierZipAmnamName), null)) {

			this.lignesEventCSV = new LecteurCSV<>(new BufferedReader(new InputStreamReader(Files.newInputStream(fs.getPath(PATH_CSV_EVENT)))), new ConfigurationEvenement()).stream().toArray(ConfigurationEvenement.LigneEvenement[]::new);
			this.lignesInitCSV = new LecteurCSV<>(new BufferedReader(new InputStreamReader(Files.newInputStream(fs.getPath(PATH_CSV_INIT)))), new ConfigurationInitialisation()).stream().toArray(ConfigurationInitialisation.LigneInitialisation[]::new);

			PathMatcher periodFileMatcher = fs.getPathMatcher("glob:/"+PATH_CSV+"period_*.csv");
			List<Path> periodFiles = Files.find(fs.getPath(PATH_CSV), 1, (path, basicFileAttributes) -> periodFileMatcher.matches(path), FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());

			informationsPeriodiques = new InfoPeriode[periodFiles.size()];

			for (int i = 0; i < periodFiles.size(); i++) {
				final ConfigurationPeriode.LignePeriode[] lignesPeriode = new LecteurCSV<>(new BufferedReader(new InputStreamReader(Files.newInputStream(periodFiles.get(i)))), new ConfigurationPeriode()).stream().toArray(ConfigurationPeriode.LignePeriode[]::new);
				informationsPeriodiques[i] = new InfoPeriode(lignesPeriode);
			}
		}

	}

	/**
	 * Sauvegarde le contenu en mémoire du FichierAMNAM dans le même fichier « .amnam » utilisé lors de l'initialisation.
	 * Cette méthode calcule les changements, crée un fichier temporaire, supprime l'ancien fichier et renomme le fichier temporaire pour remplacer ce dernier.
	 *
	 * @throws IOException Une erreur lors de la sauvegarde du fichier
	 */
	public void saveFile() throws IOException {

		try(FileSystem fs = FileSystems.newFileSystem(Paths.get(fichierZipAmnamName), null)){

			//AMNAM Info
			ZipUtil.serializeJSONToZipFileSystem(fs, info, PATH_AMNAM_INFO_JSON);

			if (isVehiculeEventsLoaded())
				ZipUtil.serializeJSONToZipFileSystem(fs, vehiculeEvents, PATH_VEHICULE_EVENTS);

			if (isReportedEventsLoaded())
				ZipUtil.serializeJSONToZipFileSystem(fs, reportedEvents, PATH_REPORTED_EVENTS);

			if (isObfEventsLoaded())
				ZipUtil.serializeJSONToZipFileSystem(fs, obfuscatedEvents, PATH_OBFUSCATED_EVENTS);

			if (isAnalysedEventsLoaded())
				ZipUtil.serializeJSONToZipFileSystem(fs, analysisResults, PATH_ANALYSED_EVENTS);

		}
	}

	/**
	 * Retourne le nom du fichier. Ex: autruche.amnam
	 *
	 * @return le nom du fichier
	 */
	public String getNom() {
		return this.fichierZipAmnamName.substring(this.fichierZipAmnamName.lastIndexOf(File.separator) + 1);
	}

	/**
	 * Retourne l'instance {@link AMNAMInfo} du fichier.
	 *
	 * @see AMNAMInfo
	 * @return l 'instance {@link AMNAMInfo}
	 */
	public AMNAMInfo getInfo() {
		return this.info;
	}

	/**
	 * Retourne le chemin d'accès du fichier.
	 *
	 * @return le chemin d'accès du fichier
	 */
	public String getCheminDAcces() {
		return this.fichierZipAmnamName;
	}

	/**
	 * Retourne vrai si les CSV provenant de la simulation sont correctement initialisés.
	 *
	 * @return vrai si les CSV provenant de la simulation sont correctement initialisés
	 */
	public boolean isCSVCharge() {
		return (this.lignesEventCSV != null && this.lignesInitCSV != null && this.informationsPeriodiques != null && this.informationsPeriodiques.length != 0 );
	}

	/**
	 * Retoune les lignes du fichier « init.csv » qui a été inclu dans le fichier compressé.
	 *
	 * @return les lignes du fichier « init.csv »
	 */
	public ConfigurationInitialisation.LigneInitialisation[] getLignesInitCSV() {
		return lignesInitCSV;
	}

	/**
	 * Retoune les lignes du fichier « event.csv » qui a été inclu dans le fichier compressé.
	 *
	 * @return les lignes du fichier « event.csv »
	 */
	public ConfigurationEvenement.LigneEvenement[] getLignesEventCSV() {
		return lignesEventCSV;
	}

	/**
	 * Retourne un tableau de {@link InfoPeriode} contenant les informations des fichiers périodiques qui ont été inclus dans le fichier compressé.
	 *
	 * @return un tableau contenant les informations des fichiers périodiques
	 */
	public InfoPeriode[] getInformationsPeriodiques() {
		return informationsPeriodiques;
	}

	/**
	 * Retourne vrai si le fichier « v_events.json » a été correctement initialisé.
	 *
	 * @return vrai si le fichier « v_events.json » a été correctement initialisé
	 */
	public boolean isVehiculeEventsLoaded() {
		return vehiculeEvents != null;
	}

	/**
	 * Retourne vrai si le fichier « obf_events.json » a été correctement initialisé.
	 *
	 * @return vrai si le fichier « obf_events.json » a été correctement initialisé
	 */
	public boolean isObfEventsLoaded() {
		return obfuscatedEvents != null;
	}

	/**
	 * Retourne vrai si le fichier « reported_events.json » a été correctement initialisé.
	 *
	 * @return vrai si le fichier « reported_events.json » a été correctement initialisé
	 */
	private boolean isReportedEventsLoaded() {
		return reportedEvents != null;
	}

	/**
	 * Retourne vrai si le fichier « analys_events.json » a été correctement initialisé.
	 *
	 * @return vrai si le fichier « analys_events.json » a été correctement initialisé
	 */
	public boolean isAnalysedEventsLoaded() {
		return analysisResults != null;
	}

	/**
	 * Retourne les {@link VehicleEvent} du scénario.
	 *
	 * @return les {@link VehicleEvent} du scénario
	 */
	public VehicleEvent[] getVehiculeEvents() {
		return vehiculeEvents;
	}

	/**
	 * Met à jour les {@link VehicleEvent} du scénario.
	 *
	 * @param vehiculeEvents les {@link VehicleEvent} du scénario
	 */
	public void setVehiculeEvents(VehicleEvent[] vehiculeEvents) {
		this.vehiculeEvents = vehiculeEvents;
	}

	/**
	 * Retourne les {@link ReportedEvent} du scénario.
	 *
	 * @return les {@link ReportedEvent} du scénario
	 */
	public ReportedEvent[] getReportedEvents() {
		return reportedEvents;
	}

	/**
	 * Met à jour les {@link ReportedEvent} du scénario.
	 *
	 * @param reportedEvents les {@link ReportedEvent} du scénario
	 */
	public void setReportedEvents(ReportedEvent[] reportedEvents) {
		this.reportedEvents = reportedEvents;
	}

	/**
	 * Retourne les {@link Event} analysés du scénario.
	 *
	 * @return les {@link Event} analysés du scénario
	 */
	public AnalysisResult getAnalysisResults() {
		return analysisResults;
	}

	/**
	 * Met à jour les {@link Event} analysés du scénario.
	 *
	 * @param analysisResults les {@link Event} analysés du scénario
	 */
	public void setAnalysisResult(AnalysisResult analysisResults) {
		this.analysisResults = analysisResults;
	}

	/**
	 * Retourne le scenario engendré par les données de Virage.
	 *
	 * @return le scenario engendré par les données de Virage
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * Retourne les {@link ReportedEvent} obfusqués du scénario.
	 *
	 * @return les {@link ReportedEvent} obfusqués du scénario
	 */
	public ReportedEvent[] getObfuscatedEvents() {
		return obfuscatedEvents;
	}

	/**
	 * Met à jour les {@link ReportedEvent} obfusqués du scénario.
	 *
	 * @param obfuscatedEvents les {@link ReportedEvent} obfusqués du scénario
	 */
	public void setObfuscatedEvents(ReportedEvent[] obfuscatedEvents) {
		this.obfuscatedEvents = obfuscatedEvents;
	}
}
