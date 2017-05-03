/*
 * Created by JFormDesigner on Wed Feb 22 16:26:09 EST 2017
 */

package ui.fenetres;

import com.sun.istack.internal.NotNull;
import io.amnam.FichierAMNAM;
import math.Vecteur;
import scenario.map.MapEdge;
import ui.UICommonUtils;
import ui.event.RouteSimulationSidePanelActionListener;
import ui.event.SidePanelListener;
import ui.event.SuperpositionSidePanelListener;
import ui.panneaux.Carte;
import ui.panneaux.WebViewer;
import ui.panneaux.side_panels.RouteSimulationSidePanel;
import ui.panneaux.side_panels.SidePanel;
import ui.panneaux.side_panels.SuperpositionSidePanel;
import utils.tuple.Triplet;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static ui.UICommonUtils.PATH_IMG_HOURGLASS_END;
import static ui.UICommonUtils.PATH_IMG_HOURGLASS_START;

/**
 * La fenêtre où les résultats du traitement des données sont représentés graphiquement.
 *
 * @author Philippe Rivest
 */
public class VisualisateurFrame extends JFrame {

	private static final long serialVersionUID = 3093748322801087777L;

	private int currentSpeedFactorIndex = 1;
	private static final TripletSpeedFontSizeTextValue[] speedFactors = new TripletSpeedFontSizeTextValue[]{
			new TripletSpeedFontSizeTextValue(0.5, 12.0f, "\u00bdx"),
			new TripletSpeedFontSizeTextValue(1.0, 16.0f, "1x"),
			new TripletSpeedFontSizeTextValue(2.0, 16.0f, "2x"),
			new TripletSpeedFontSizeTextValue(4.0, 16.0f, "4x")
	};
	private double speed = speedFactors[currentSpeedFactorIndex].getFirst();


	private final SidePanelListener panneauAFerme = VisualisateurFrame.this::panneauAFerme;
	private final SuperpositionSidePanelListener SUPERPOSITION_SIDE_PANEL_LISTENER = new SuperpositionSidePanelListener() {
		@Override
		public void superpositionConfigSidePanelRequested(SidePanel configSidePanel) {
			toggleSidePanel(configSidePanel);
		}

		@Override
		public void superpositionOrderChanged() {
			carte.repaint();
		}
	};

	private final RouteSimulationSidePanel ROUTE_SIMULATION_SIDE_PANEL = new RouteSimulationSidePanel();
	private boolean isStartPointSelectionInProgress = false , isArrivalPointSelectionInProgress = false;

	private StatsFrame statsFrame;
	private SidePanel currentSidePanel;

	private FichierAMNAM fichierAMNAM;
	private static final int RESOLUTION_SLIDER = 10;
	private static final long DELAY = 16L;

	private State state = State.STOP;
	private String maxTimeString = "00:00:00";
	private Triplet<MapEdge, Vecteur, Double> routeStartPointSelection;
	private Triplet<MapEdge, Vecteur, Double> routeArrivalPointSelection;
	private WebViewer webViewer;

	/**
	 * Instancies un nouveau VisualisateurFrame avec un FichierAMNAM à visualiser.
	 *
	 * @param fichierAVisualiser le FichierAMNAM à visualiser
	 */
	public VisualisateurFrame(FichierAMNAM fichierAVisualiser) {
		this.fichierAMNAM = fichierAVisualiser;
		initComponents();
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « Quitter » du menu « Visualiser ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemQuitterActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « À propos » du menu « Aide ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemAProposActionPerformed(ActionEvent e) {
		webViewer.loadURL(UICommonUtils.PATH_HTML_A_PROPOS);
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « Guide d'utilisation » du menu « Aide ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemGuideActionPerformed(ActionEvent e) {
		webViewer.loadURL(UICommonUtils.PATH_HTML_GUIDE);
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « Concepts scientifiques » du menu « Aide ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemConceptSciActionPerformed(ActionEvent e) {
		webViewer.loadURL(UICommonUtils.PATH_HTML_SCI_CONCEPTS);
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « Ouvrir » du menu « Visualisateur ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemOuvrirActionPerformed(ActionEvent e) {
		FichierAMNAM fichier = null;

		try {
			fichier = FichierAMNAM.chargerUnFichier("Choisir un fichier .amnam", this);
		} catch (IOException excep) {
			excep.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur dans le chargement du fichier .amnam.\nVeuillez r\u00e9essayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}

		if (fichier != null)
			UICommonUtils.transfererEtape(this, new InspecteurFichierAMNAMFrame(fichier));
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « Fermer » du menu « Visualisateur ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemFermerVisualisateurActionPerformed(ActionEvent e) {
		UICommonUtils.transfererEtape(this, new EcranAccueilFrame());
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Play / Pause » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonPlayPauseActionPerformed(ActionEvent e) {
		playPause();
	}

	/**
	 * Bascule l'état de lecture de la simulation.
	 */
	private void playPause() {
		if(state == State.STOP) { // Pour commencer
			UICommonUtils.addIconToButton(buttonPlayPause, UICommonUtils.PATH_BUTTON_ICON_PAUSE);
			state = State.ANIMATION;
			new Thread(() -> {
				if(sliderNavetteSimulation.getValue() == sliderNavetteSimulation.getMaximum())
					sliderNavetteSimulation.setValue(0);
				while(state == State.ANIMATION && sliderNavetteSimulation.getValue() < sliderNavetteSimulation.getMaximum()) {
					sliderNavetteSimulation.setValue(sliderNavetteSimulation.getValue()+1);
					try {
						Thread.sleep((long) (DELAY / speed));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				UICommonUtils.addIconToButton(buttonPlayPause, UICommonUtils.PATH_BUTTON_ICON_PLAY);
				state = State.STOP;
			}).start();
		} else { // pour stopper
			UICommonUtils.addIconToButton(buttonPlayPause, UICommonUtils.PATH_BUTTON_ICON_PLAY);
			state = State.STOP;
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Vitesse animation » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonVitesseAnimActionPerformed(ActionEvent e) {
		this.currentSpeedFactorIndex = ++currentSpeedFactorIndex % speedFactors.length;

		final TripletSpeedFontSizeTextValue speedFactor = speedFactors[currentSpeedFactorIndex];

		this.speed = speedFactor.getFirst();
		this.buttonVitesseAnim.setText(speedFactor.getThird());
		this.buttonVitesseAnim.setFont(this.buttonVitesseAnim.getFont().deriveFont(speedFactor.getSecond()));
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Zoom + » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonZoomInActionPerformed(ActionEvent e) {
		this.carte.zoomIn();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Zoom - » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonZoomOutActionPerformed(ActionEvent e) {
		this.carte.zoomOut();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Superpositions » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonSuperpositionsActionPerformed(ActionEvent e) {
		final SuperpositionSidePanel superpositionSidePanel = new SuperpositionSidePanel(carte.getSuperpositions());
		superpositionSidePanel.addSuperpositionSidePanelListener(SUPERPOSITION_SIDE_PANEL_LISTENER);
		toggleSidePanel(superpositionSidePanel);
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Ajouter un trajet » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonAjoutTrajetActionPerformed(ActionEvent e) {
		this.toggleSidePanel(ROUTE_SIMULATION_SIDE_PANEL);
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Statistiques » de la carte.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonStatsActionPerformed(ActionEvent e) {
		if (this.statsFrame != null)
			this.statsFrame.setVisible(true);
	}

	/**
	 * Méthode exécutée lors de la sélection d'une valeur dans le glisseur « Navette simulation ».
	 *
	 * @param e Évènement de changement de valeur
	 */
	private void sliderNavetteSimulationStateChanged(ChangeEvent e) {
		double currentTime = sliderNavetteSimulation.getValue() / (double) RESOLUTION_SLIDER;

		carte.setTemps(currentTime);
		this.updateTimestampLabel();
	}

	/**
	 * Affiche un panneau d'information par dessus la carte en l'ajoutant au {@link JLayeredPane} à l'index {@link JLayeredPane#MODAL_LAYER}.
	 *
	 * @param sidePanel Le panneau d'information à afficher
	 */
	public void toggleSidePanel(@NotNull SidePanel sidePanel) {

		if (sidePanel != null) {

			if (this.currentSidePanel != null)
				this.currentSidePanel.closePanel();

			if (!sidePanel.equals(currentSidePanel)) {
				this.currentSidePanel = sidePanel;
				this.layeredPane.add(this.currentSidePanel, JLayeredPane.MODAL_LAYER);
				this.currentSidePanel.setBounds(new Rectangle(new Point(0, 0), SidePanel.DEFAULT_SIZE));
				this.currentSidePanel.addSidePanelListener(panneauAFerme);
				this.currentSidePanel.setVisible(true);
			}
		}
	}

	/**
	 * Ferme le panneau courrant en l'enlevant du {@link JLayeredPane} et en désactivant le listener.
	 */
	public void fermerPanneauCourrant() {

		if (currentSidePanel != null) {
			this.currentSidePanel.removeSidePanelListener(panneauAFerme);
			this.layeredPane.remove(this.currentSidePanel);
			this.currentSidePanel = null;
		}
	}

	/**
	 * Méthode exécutée lors de la fermeture du {@link SidePanel} « currentSidePanel ».
	 *
	 * @param panneauLateral Le panneau d'information qui c'est fermé
	 */
	private void panneauAFerme(SidePanel panneauLateral) {
		this.fermerPanneauCourrant();
	}

	/**
	 * Méthode exécutée lors d'un clic sur la carte.
	 *
	 * @param e L'évènement de clic
	 */
	private void carteMouseClicked(MouseEvent e) {

		if (this.isStartPointSelectionInProgress) {

			routeStartPointSelection = fichierAMNAM.getScenario().getMap().getClosestPosition(new Vecteur(this.carte.transformPoint(e.getPoint()).getX(), this.carte.transformPoint(e.getPoint()).getY()));

			this.ROUTE_SIMULATION_SIDE_PANEL.setStartPoint(routeStartPointSelection.getSecond());
			carte.getRouteSuperposition().setStartPoint(routeStartPointSelection.getSecond());

			this.isStartPointSelectionInProgress = false;

		} else if (this.isArrivalPointSelectionInProgress) {

			routeArrivalPointSelection = fichierAMNAM.getScenario().getMap().getClosestPosition(new Vecteur(this.carte.transformPoint(e.getPoint()).getX(), this.carte.transformPoint(e.getPoint()).getY()));

			this.ROUTE_SIMULATION_SIDE_PANEL.setArrivalPoint(routeArrivalPointSelection.getSecond());
			carte.getRouteSuperposition().setArrivalPoint(routeArrivalPointSelection.getSecond());

			this.isArrivalPointSelectionInProgress = false;

		}

		carte.repaint();

	}

	/**
	 * Méthode exécutée lorsque le clic est pressé sur la carte.
	 *
	 * @param e L'évènement de clic
	 */
	private void carteMousePressed(MouseEvent e) {
		if (!this.carte.hasFocus())
			this.carte.requestFocus();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton de fermeture de la fenêtre.
	 *
	 * @param e Évènement de fermeture
	 */
	private void thisWindowClosing(WindowEvent e) {
		fichierAMNAM = null;
		carte = null;
		System.gc();
		UICommonUtils.transfererEtape(this, new EcranAccueilFrame());
	}

	/**
	 * Méthode exécutée lors de l'ouverture de la fenêtre.
	 *
	 * @param e Évènement d'ouverture
	 */
	private void thisWindowOpened(WindowEvent e) {
		//Button icons
		UICommonUtils.addIconToButton(buttonPlayPause, UICommonUtils.PATH_BUTTON_ICON_PLAY);
		UICommonUtils.addIconToButton(buttonZoomIn, UICommonUtils.PATH_BUTTON_ICON_ZOOM_IN);
		UICommonUtils.addIconToButton(buttonZoomOut, UICommonUtils.PATH_BUTTON_ICON_ZOOM_OUT);
		UICommonUtils.addIconToButton(buttonSuperpositions, UICommonUtils.PATH_BUTTON_ICON_SUPERPOSITIONS);
		UICommonUtils.addIconToButton(buttonAjoutTrajet, UICommonUtils.PATH_BUTTON_ICON_NEW_ROUTE);
		UICommonUtils.addIconToButton(buttonStats, UICommonUtils.PATH_BUTTON_ICON_STATS);
		UICommonUtils.addIconToButton(buttonCenterMap, UICommonUtils.PATH_BUTTON_ICON_CENTER);
	}

	/**
	 * Crée les composants graphiques personnalisés.
	 */
	private void createUIComponents() {
		this.carte = new Carte(fichierAMNAM);
	}

	/**
	 * Méthode exécutée lorsqu'une touche du clavier est pressée.
	 *
	 * @param e Évènement de clavier
	 */
	private void carteKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			playPause();
	}

	/**
	 * Méthode exécutée lors du clic sur l'item « Choisir la couleur de fond de la carte » du menu « Simulation ».
	 *
	 * @param e Évènement de clic
	 */
	private void menuItemMapColorActionPerformed(ActionEvent e) {
		Color returnedColor = JColorChooser.showDialog(null, "Choisir la couleur de fond de la carte", this.carte.getBackground());

		if (returnedColor != null)
			this.carte.setBackground(returnedColor);
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton centrer.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonCenterMapActionPerformed(ActionEvent e) {
		carte.recenter();
	}

	/**
	 * Mets à jour l'étiquette de temps de la navette.
	 */
	private void updateTimestampLabel() {

		double currentTime = sliderNavetteSimulation.getValue() / (double) RESOLUTION_SLIDER;

		int minutesElapsed = ((int) Math.floor(currentTime / 60));
		int secondsElapsed = ((int) Math.floor(currentTime));
		int milliSecondsElapsed = ((int) Math.round((currentTime - secondsElapsed) * 100));

		this.labelTimestamp.setText(String.format("(%02d:%02d:%02d / %s)",minutesElapsed,secondsElapsed % 60,milliSecondsElapsed, this.maxTimeString));
	}

	/**
	 * Méthode exécutée losque la carte demande l'affichage d'un panneau latéral.
	 *
	 * @param panel le panneau latéral à afficher
	 */
	private void carteChangePanelLateralEventReceived(SidePanel panel){
		this.toggleSidePanel(panel);
	}

	/**
	 * Méthode exécutée lorsqu'une touche du clavier est pressée sur la ligne du temps.
	 *
	 * @param e Évènement de clavier
	 */
	private void sliderNavetteSimulationKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			playPause();
	}

	/**
	 * Méthode exécutée lorsqu'une touche du clavier est pressée sur la ligne du temps.
	 *
	 * @param e Évènement de clavier
	 */
	private void sliderNavetteSimulationKeyTyped(KeyEvent e) {

		switch (e.getKeyChar()) {

			case 'd':
				sliderNavetteSimulation.setValue(sliderNavetteSimulation.getMinimum());
				break;

			case 'f':
				sliderNavetteSimulation.setValue(sliderNavetteSimulation.getMaximum());
				break;
		}
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		createUIComponents();

		this.menuBar = new JMenuBar();
		this.menuVisualisateur = new JMenu();
		this.menuItemOuvrir = new JMenuItem();
		this.menuItemMapColor = new JMenuItem();
		this.menuItemFermerVisualisateur = new JMenuItem();
		this.menuItemQuitter = new JMenuItem();
		this.menuAide = new JMenu();
		this.menuItemAPropos = new JMenuItem();
		this.menuItemGuide = new JMenuItem();
		this.menuItemConceptSci = new JMenuItem();
		this.layeredPane = new JLayeredPane();
		this.panelBoutonsCarte = new JPanel();
		this.buttonPlayPause = new JButton();
		this.buttonVitesseAnim = new JButton();
		this.vSpacerCarte1 = new JPanel(null);
		this.buttonZoomIn = new JButton();
		this.buttonZoomOut = new JButton();
		this.buttonCenterMap = new JButton();
		this.vSpacerCarte2 = new JPanel(null);
		this.buttonSuperpositions = new JButton();
		this.vSpacerCarte3 = new JPanel(null);
		this.buttonAjoutTrajet = new JButton();
		this.vSpacerCarte4 = new JPanel(null);
		this.buttonStats = new JButton();
		this.panelNavette = new JPanel();
		this.sliderNavetteSimulation = new JSlider();
		this.labelHourglassMin = new JLabel();
		this.labelHourglassMax = new JLabel();
		this.labelTimestamp = new JLabel();
		this.labelTitleTimeline = new JLabel();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle("Visualisateur | AMNAM"); //$NON-NLS-1$ //NON-NLS
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

		//======== menuBar ========
		{

			//======== menuVisualisateur ========
			{
				this.menuVisualisateur.setText("Visualisateur"); //$NON-NLS-1$ //NON-NLS

				//---- menuItemOuvrir ----
				this.menuItemOuvrir.setText("Ouvrir"); //$NON-NLS-1$ //NON-NLS
				this.menuItemOuvrir.addActionListener(e -> menuItemOuvrirActionPerformed(e));
				this.menuVisualisateur.add(this.menuItemOuvrir);

				//---- menuItemMapColor ----
				this.menuItemMapColor.setText("Choisir la couleur de la carte"); //$NON-NLS-1$ //NON-NLS
				this.menuItemMapColor.addActionListener(e -> menuItemMapColorActionPerformed(e));
				this.menuVisualisateur.add(this.menuItemMapColor);
				this.menuVisualisateur.addSeparator();

				//---- menuItemFermerVisualisateur ----
				this.menuItemFermerVisualisateur.setText("Revenir au menu principal"); //$NON-NLS-1$ //NON-NLS
				this.menuItemFermerVisualisateur.addActionListener(e -> menuItemFermerVisualisateurActionPerformed(e));
				this.menuVisualisateur.add(this.menuItemFermerVisualisateur);

				//---- menuItemQuitter ----
				this.menuItemQuitter.setText("Quitter"); //$NON-NLS-1$ //NON-NLS
				this.menuItemQuitter.addActionListener(e -> menuItemQuitterActionPerformed(e));
				this.menuVisualisateur.add(this.menuItemQuitter);
			}
			this.menuBar.add(this.menuVisualisateur);

			//======== menuAide ========
			{
				this.menuAide.setText("Aide"); //$NON-NLS-1$ //NON-NLS

				//---- menuItemAPropos ----
				this.menuItemAPropos.setText("\u00c0 propos"); //$NON-NLS-1$ //NON-NLS
				this.menuItemAPropos.addActionListener(e -> menuItemAProposActionPerformed(e));
				this.menuAide.add(this.menuItemAPropos);

				//---- menuItemGuide ----
				this.menuItemGuide.setText("Guide d'utilisation"); //$NON-NLS-1$ //NON-NLS
				this.menuItemGuide.addActionListener(e -> menuItemGuideActionPerformed(e));
				this.menuAide.add(this.menuItemGuide);

				//---- menuItemConceptSci ----
				this.menuItemConceptSci.setText("Concepts scientifiques"); //$NON-NLS-1$ //NON-NLS
				this.menuItemConceptSci.addActionListener(e -> menuItemConceptSciActionPerformed(e));
				this.menuAide.add(this.menuItemConceptSci);
			}
			this.menuBar.add(this.menuAide);
		}
		setJMenuBar(this.menuBar);

		//======== layeredPane ========
		{

			//======== panelBoutonsCarte ========
			{
				this.panelBoutonsCarte.setBackground(new Color(255, 204, 0));
				this.panelBoutonsCarte.setOpaque(false);
				this.panelBoutonsCarte.setLayout(new BoxLayout(this.panelBoutonsCarte, BoxLayout.Y_AXIS));

				//---- buttonPlayPause ----
				this.buttonPlayPause.setText("P"); //$NON-NLS-1$ //NON-NLS
				this.buttonPlayPause.setMinimumSize(new Dimension(50, 50));
				this.buttonPlayPause.setMaximumSize(new Dimension(60, 60));
				this.buttonPlayPause.setPreferredSize(new Dimension(50, 50));
				this.buttonPlayPause.setOpaque(false);
				this.buttonPlayPause.setToolTipText("Jouer/Pause"); //$NON-NLS-1$ //NON-NLS
				this.buttonPlayPause.setFocusable(false);
				this.buttonPlayPause.addActionListener(e -> buttonPlayPauseActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonPlayPause);

				//---- buttonVitesseAnim ----
				this.buttonVitesseAnim.setText("1x"); //$NON-NLS-1$ //NON-NLS
				this.buttonVitesseAnim.setPreferredSize(new Dimension(50, 50));
				this.buttonVitesseAnim.setMinimumSize(new Dimension(50, 50));
				this.buttonVitesseAnim.setMaximumSize(new Dimension(60, 60));
				this.buttonVitesseAnim.setOpaque(false);
				this.buttonVitesseAnim.setFont(new Font(".SF NS Text", Font.BOLD, 16)); //$NON-NLS-1$ //NON-NLS
				this.buttonVitesseAnim.setToolTipText("Vitesse d'animation"); //$NON-NLS-1$ //NON-NLS
				this.buttonVitesseAnim.setFocusable(false);
				this.buttonVitesseAnim.addActionListener(e -> buttonVitesseAnimActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonVitesseAnim);

				//---- vSpacerCarte1 ----
				this.vSpacerCarte1.setOpaque(false);
				this.vSpacerCarte1.setFocusable(false);
				this.panelBoutonsCarte.add(this.vSpacerCarte1);

				//---- buttonZoomIn ----
				this.buttonZoomIn.setText("+"); //$NON-NLS-1$ //NON-NLS
				this.buttonZoomIn.setMaximumSize(new Dimension(60, 60));
				this.buttonZoomIn.setMinimumSize(new Dimension(50, 50));
				this.buttonZoomIn.setPreferredSize(new Dimension(50, 50));
				this.buttonZoomIn.setOpaque(false);
				this.buttonZoomIn.setToolTipText("Zoom +"); //$NON-NLS-1$ //NON-NLS
				this.buttonZoomIn.setFocusable(false);
				this.buttonZoomIn.addActionListener(e -> buttonZoomInActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonZoomIn);

				//---- buttonZoomOut ----
				this.buttonZoomOut.setText("-"); //$NON-NLS-1$ //NON-NLS
				this.buttonZoomOut.setMaximumSize(new Dimension(60, 60));
				this.buttonZoomOut.setMinimumSize(new Dimension(50, 50));
				this.buttonZoomOut.setPreferredSize(new Dimension(50, 50));
				this.buttonZoomOut.setOpaque(false);
				this.buttonZoomOut.setToolTipText("Zoom -"); //$NON-NLS-1$ //NON-NLS
				this.buttonZoomOut.setFocusable(false);
				this.buttonZoomOut.addActionListener(e -> buttonZoomOutActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonZoomOut);

				//---- buttonCenterMap ----
				this.buttonCenterMap.setText("C"); //$NON-NLS-1$ //NON-NLS
				this.buttonCenterMap.setMaximumSize(new Dimension(60, 60));
				this.buttonCenterMap.setMinimumSize(new Dimension(50, 50));
				this.buttonCenterMap.setPreferredSize(new Dimension(50, 50));
				this.buttonCenterMap.setOpaque(false);
				this.buttonCenterMap.setToolTipText("Centrer la carte"); //$NON-NLS-1$ //NON-NLS
				this.buttonCenterMap.setFocusable(false);
				this.buttonCenterMap.addActionListener(e -> buttonCenterMapActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonCenterMap);

				//---- vSpacerCarte2 ----
				this.vSpacerCarte2.setOpaque(false);
				this.vSpacerCarte2.setFocusable(false);
				this.panelBoutonsCarte.add(this.vSpacerCarte2);

				//---- buttonSuperpositions ----
				this.buttonSuperpositions.setText("S"); //$NON-NLS-1$ //NON-NLS
				this.buttonSuperpositions.setMaximumSize(new Dimension(60, 60));
				this.buttonSuperpositions.setMinimumSize(new Dimension(50, 50));
				this.buttonSuperpositions.setPreferredSize(new Dimension(50, 50));
				this.buttonSuperpositions.setOpaque(false);
				this.buttonSuperpositions.setToolTipText("Superpositions"); //$NON-NLS-1$ //NON-NLS
				this.buttonSuperpositions.setFocusable(false);
				this.buttonSuperpositions.addActionListener(e -> buttonSuperpositionsActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonSuperpositions);

				//---- vSpacerCarte3 ----
				this.vSpacerCarte3.setOpaque(false);
				this.vSpacerCarte3.setFocusable(false);
				this.panelBoutonsCarte.add(this.vSpacerCarte3);

				//---- buttonAjoutTrajet ----
				this.buttonAjoutTrajet.setText("T"); //$NON-NLS-1$ //NON-NLS
				this.buttonAjoutTrajet.setPreferredSize(new Dimension(50, 50));
				this.buttonAjoutTrajet.setMaximumSize(new Dimension(60, 60));
				this.buttonAjoutTrajet.setMinimumSize(new Dimension(50, 50));
				this.buttonAjoutTrajet.setOpaque(false);
				this.buttonAjoutTrajet.setToolTipText("Simulation d'un trajet"); //$NON-NLS-1$ //NON-NLS
				this.buttonAjoutTrajet.setFocusable(false);
				this.buttonAjoutTrajet.addActionListener(e -> buttonAjoutTrajetActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonAjoutTrajet);

				//---- vSpacerCarte4 ----
				this.vSpacerCarte4.setOpaque(false);
				this.vSpacerCarte4.setFocusable(false);
				this.panelBoutonsCarte.add(this.vSpacerCarte4);

				//---- buttonStats ----
				this.buttonStats.setText("St"); //$NON-NLS-1$ //NON-NLS
				this.buttonStats.setMaximumSize(new Dimension(60, 60));
				this.buttonStats.setMinimumSize(new Dimension(50, 50));
				this.buttonStats.setPreferredSize(new Dimension(50, 50));
				this.buttonStats.setOpaque(false);
				this.buttonStats.setToolTipText("Statistiques"); //$NON-NLS-1$ //NON-NLS
				this.buttonStats.setFocusable(false);
				this.buttonStats.addActionListener(e -> buttonStatsActionPerformed(e));
				this.panelBoutonsCarte.add(this.buttonStats);
			}
			this.layeredPane.add(this.panelBoutonsCarte, JLayeredPane.PALETTE_LAYER);
			this.panelBoutonsCarte.setBounds(1225, 110, this.panelBoutonsCarte.getPreferredSize().width, 440);

			//---- carte ----
			this.carte.setBackground(new Color(154, 118, 50));
			this.carte.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					carteMouseClicked(e);
				}
				@Override
				public void mousePressed(MouseEvent e) {
					carteMousePressed(e);
				}
			});
			this.carte.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					carteKeyPressed(e);
				}
			});
			this.layeredPane.add(this.carte, JLayeredPane.DEFAULT_LAYER);
			this.carte.setBounds(0, 0, 1280, 645);
		}
		contentPane.add(this.layeredPane);
		this.layeredPane.setBounds(0, 0, 1280, 645);

		//======== panelNavette ========
		{
			this.panelNavette.setBorder(new EtchedBorder());
			this.panelNavette.setLayout(null);

			//---- sliderNavetteSimulation ----
			this.sliderNavetteSimulation.setPaintLabels(true);
			this.sliderNavetteSimulation.addChangeListener(e -> sliderNavetteSimulationStateChanged(e));
			this.sliderNavetteSimulation.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					sliderNavetteSimulationKeyPressed(e);
				}
				@Override
				public void keyTyped(KeyEvent e) {
					sliderNavetteSimulationKeyTyped(e);
				}
			});
			this.panelNavette.add(this.sliderNavetteSimulation);
			this.sliderNavetteSimulation.setBounds(45, 5, 1190, 35);
			this.panelNavette.add(this.labelHourglassMin);
			this.labelHourglassMin.setBounds(20, 12, 20, 20);
			this.panelNavette.add(this.labelHourglassMax);
			this.labelHourglassMax.setBounds(1240, 12, 20, 20);

			//---- labelTimestamp ----
			this.labelTimestamp.setText("(15:00:00 / 15:00:00)"); //$NON-NLS-1$ //NON-NLS
			this.panelNavette.add(this.labelTimestamp);
			this.labelTimestamp.setBounds(new Rectangle(new Point(1135, 40), this.labelTimestamp.getPreferredSize()));

			//---- labelTitleTimeline ----
			this.labelTitleTimeline.setText("Ligne du temps"); //$NON-NLS-1$ //NON-NLS
			this.panelNavette.add(this.labelTitleTimeline);
			this.labelTitleTimeline.setBounds(new Rectangle(new Point(10, 40), this.labelTitleTimeline.getPreferredSize()));
		}
		contentPane.add(this.panelNavette);
		this.panelNavette.setBounds(0, 645, 1280, 60);

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
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents


		//Do whatever you want!

		webViewer = new WebViewer(UICommonUtils.PATH_HTML_HOME, "Guide AMNAM");

		BoundedRangeModel modele = sliderNavetteSimulation.getModel();
		modele.setMinimum((int) (carte.getTempsMin() * RESOLUTION_SLIDER) + 1);
		modele.setMaximum((int) (carte.getTempsMax() * RESOLUTION_SLIDER));
		modele.setValue(modele.getMinimum());

		//Sabliers
		UICommonUtils.addIconToLabel(this.labelHourglassMin, PATH_IMG_HOURGLASS_START);
		UICommonUtils.addIconToLabel(this.labelHourglassMax, PATH_IMG_HOURGLASS_END);

		int minMax = ((int) Math.floor(carte.getTempsMax() / 60));
		int secMax = ((int) Math.floor(carte.getTempsMax()));
		int milliMax = ((int) Math.round((carte.getTempsMax() - secMax) * 100));

		this.maxTimeString = String.format("%02d:%02d:%02d", minMax, secMax % 60, milliMax);
		this.updateTimestampLabel();

		//Bouton stats
		buttonStats.setVisible(fichierAMNAM.isAnalysedEventsLoaded());

		//Écouteur carte
		this.carte.addPanelChangeRequestListener(this::carteChangePanelLateralEventReceived);

		//Écouteur Trajet
		this.ROUTE_SIMULATION_SIDE_PANEL.addRouteSimulationSidePanelActionListener(new RouteSimulationSidePanelActionListener() {
			@Override
			public void startPointSelectionStarted() {
				isStartPointSelectionInProgress = true;
			}

			@Override
			public void startPointColorSelected(Color color) {
				carte.getRouteSuperposition().setStartPointColor(color);
				carte.repaint();
			}

			@Override
			public void arrivalPointSelectionStarted() {
				isArrivalPointSelectionInProgress = true;
			}

			@Override
			public void arrivalPointColorSelected(Color color) {
				carte.getRouteSuperposition().setArrivalPointColor(color);
				carte.repaint();
			}

			@Override
			public void routeCalculationWasRequested() {
				carte.getRouteSuperposition().renderRoute(routeStartPointSelection, routeArrivalPointSelection, fichierAMNAM.getScenario().getMap().getGraph());
				carte.repaint();
			}

			@Override
			public void routeClearanceWasRequested() {
				carte.getRouteSuperposition().setPath(null);
				carte.getRouteSuperposition().setStartPoint(null);
				carte.getRouteSuperposition().setArrivalPoint(null);
				carte.repaint();
			}
		});

		if (fichierAMNAM.isAnalysedEventsLoaded()) {
			try {
				this.statsFrame = new StatsFrame(fichierAMNAM);
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			}
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JMenuBar menuBar;
	private JMenu menuVisualisateur;
	private JMenuItem menuItemOuvrir;
	private JMenuItem menuItemMapColor;
	private JMenuItem menuItemFermerVisualisateur;
	private JMenuItem menuItemQuitter;
	private JMenu menuAide;
	private JMenuItem menuItemAPropos;
	private JMenuItem menuItemGuide;
	private JMenuItem menuItemConceptSci;
	private JLayeredPane layeredPane;
	private JPanel panelBoutonsCarte;
	private JButton buttonPlayPause;
	private JButton buttonVitesseAnim;
	private JPanel vSpacerCarte1;
	private JButton buttonZoomIn;
	private JButton buttonZoomOut;
	private JButton buttonCenterMap;
	private JPanel vSpacerCarte2;
	private JButton buttonSuperpositions;
	private JPanel vSpacerCarte3;
	private JButton buttonAjoutTrajet;
	private JPanel vSpacerCarte4;
	private JButton buttonStats;
	private Carte carte;
	private JPanel panelNavette;
	private JSlider sliderNavetteSimulation;
	private JLabel labelHourglassMin;
	private JLabel labelHourglassMax;
	private JLabel labelTimestamp;
	private JLabel labelTitleTimeline;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * État dans lequel se trouve l'animation de la fenêtre.
	 * @author Pascal Dally-Bélanger
	 */
	private static enum State {
		/**
		 * État où l'animation est arrêtée.
		 */
		STOP,

		/**
		 * État où l'animation est en cours.
		 */
		ANIMATION
	}

	/**
	 * Classe d'extension d'un {@link Triplet} contenant la grosseur de police et le texte pour un facteur de vitesse.
	 * @author Philippe Rivest
	 */
	private static class TripletSpeedFontSizeTextValue extends Triplet<Double, Float, String>{


		/**
		 * Intancies un nouveau TripletSpeedFontSizeTextValue.
		 *
		 * @param first  Le facteur de vitesse
		 * @param second La taille de police à afficher
		 * @param third  Le texte à afficher pour le facteur de vitesse
		 */
		public TripletSpeedFontSizeTextValue(Double first, Float second, String third) {
			super(first, second, third);
		}
	}
}
