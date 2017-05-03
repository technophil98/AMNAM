/*
 * Created by JFormDesigner on Fri Feb 24 16:11:14 EST 2017
 */

package ui.panneaux.side_panels;

import math.Vecteur;
import ui.composants.ColorChooserPanel;
import ui.event.RouteSimulationSidePanelActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Un panneau latéral permettant à l'utilisateur de paramétrer la simulation d'un trajet.
 *
 * @author Philippe Rivest
 */
public class RouteSimulationSidePanel extends SidePanel {

	private static final long serialVersionUID = -8099158040438395705L;
	private static final String DEFAULT_COORD_LABEL_VALUE = "N/A";
	private static final String DEFAULT_SELECTION_BTN_TEXT = "S\u00e9lectionner sur la carte";

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();

	private Vecteur startPoint, arrivalPoint;

	/**
	 * Instancies un nouveau RouteSimulationSidePanel.
	 */
	public RouteSimulationSidePanel() {
		super();
		initComponents();
		setTitle("Simulation d'un trajet");
	}

	/**
	 * Mets à jour la coordonnée de point de départ.
	 *
	 * @param startPoint la coordonnée de point de départ
	 */
	public void setStartPoint(Vecteur startPoint) {
		this.startPoint = startPoint;
		this.labelCoordDepart.setText(String.format("[%.2f ; %.2f]", this.startPoint.getX(), this.startPoint.getY()));
		this.buttonSelectionDepart.setEnabled(true);
		this.buttonSelectionArrivee.setEnabled(true);

		buttonSelectionDepart.setText((this.startPoint != null ? "Choisir un autre point" : DEFAULT_SELECTION_BTN_TEXT));

		if (startPoint != null && arrivalPoint != null)
			for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
				listener.routeCalculationWasRequested();
	}

	/**
	 * Mets à jour la coordonnée de point d'arrivée.
	 *
	 * @param arrivalPoint la coordonnée de point d'arrivée
	 */
	public void setArrivalPoint(Vecteur arrivalPoint) {
		this.arrivalPoint = arrivalPoint;
		this.labelCoordArrivee.setText(String.format("[%.2f ; %.2f]", this.arrivalPoint.getX(), this.arrivalPoint.getY()));
		this.buttonSelectionArrivee.setEnabled(true);
		this.buttonSelectionDepart.setEnabled(true);

		buttonSelectionArrivee.setText((this.arrivalPoint != null ? "Choisir un autre point" : DEFAULT_SELECTION_BTN_TEXT));

		if (startPoint != null && arrivalPoint != null)
			for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
				listener.routeCalculationWasRequested();
	}

	/**
	 * Retourne la couleur du point de départ.
	 *
	 * @return la couleur du point de départ
	 */
	public Color getStartPointColor(){
		return this.choixCouleurDepart.getColor();
	}

	/**
	 * Retourne la couleur du point d'arrivée.
	 *
	 * @return la couleur du point d'arrivée
	 */
	public Color getArrivalPointColor(){
		return this.choixCouleurArrivee.getColor();
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Sélectionner sur la carte » du départ.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonSelectionDepartActionPerformed(ActionEvent e) {
		for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
			listener.startPointSelectionStarted();

		this.buttonSelectionDepart.setText("Cliquez sur la carte...");
		this.buttonSelectionArrivee.setEnabled(false);
		this.buttonSelectionDepart.setEnabled(false);
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Sélectionner sur la carte » de l'arrivée.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonSelectionArriveeActionPerformed(ActionEvent e) {
		for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
			listener.arrivalPointSelectionStarted();

		this.buttonSelectionArrivee.setText("Cliquez sur la carte...");
		this.buttonSelectionArrivee.setEnabled(false);
		this.buttonSelectionDepart.setEnabled(false);
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Effacer le trajet ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonClearRouteActionPerformed(ActionEvent e) {
		clearPanel();
	}

	/**
	 * Réinitialise l'affichage du panneau.
	 */
	private void clearPanel() {
		startPoint = null;
		arrivalPoint = null;
		labelCoordDepart.setText(DEFAULT_COORD_LABEL_VALUE);
		labelCoordArrivee.setText(DEFAULT_COORD_LABEL_VALUE);
		buttonSelectionDepart.setText(DEFAULT_SELECTION_BTN_TEXT);
		buttonSelectionArrivee.setText(DEFAULT_SELECTION_BTN_TEXT);

		for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
			listener.routeClearanceWasRequested();
	}

	/**
	 * Ajoute un RouteSimulationSidePanelActionListener à la liste des écouteurs.
	 *
	 * @param routeSimulationSidePanelActionListener Le RouteSimulationSidePanelActionListener à ajouter
	 */
	public void addRouteSimulationSidePanelActionListener(RouteSimulationSidePanelActionListener routeSimulationSidePanelActionListener) {
		OBJETS_ENREGISTRES.add(RouteSimulationSidePanelActionListener.class, routeSimulationSidePanelActionListener);
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.panelPointDeDepart = new JPanel();
		this.buttonSelectionDepart = new JButton();
		this.labelLabelCoordDepart = new JLabel();
		this.labelCoordDepart = new JLabel();
		this.labelLabelCouleurDepart = new JLabel();
		this.choixCouleurDepart = new ColorChooserPanel();
		this.panelPointDArrivee = new JPanel();
		this.buttonSelectionArrivee = new JButton();
		this.labelLabelCoordArrivee = new JLabel();
		this.labelCoordArrivee = new JLabel();
		this.labelLabelCouleurArrivee = new JLabel();
		this.choixCouleurArrivee = new ColorChooserPanel();
		this.buttonClearRoute = new JButton();

		//======== this ========
		setLayout(null);

		//======== panelPointDeDepart ========
		{
			this.panelPointDeDepart.setBorder(new TitledBorder(null, "Point de d\u00e9part", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, //$NON-NLS-1$ //NON-NLS
				new Font(".SF NS Text", Font.PLAIN, 18))); //$NON-NLS-1$ //NON-NLS
			this.panelPointDeDepart.setLayout(null);

			//---- buttonSelectionDepart ----
			this.buttonSelectionDepart.setText("S\u00e9lectionner sur la carte"); //$NON-NLS-1$ //NON-NLS
			this.buttonSelectionDepart.addActionListener(e -> buttonSelectionDepartActionPerformed(e));
			this.panelPointDeDepart.add(this.buttonSelectionDepart);
			this.buttonSelectionDepart.setBounds(5, 104, 320, this.buttonSelectionDepart.getPreferredSize().height);

			//---- labelLabelCoordDepart ----
			this.labelLabelCoordDepart.setText("Coordonn\u00e9es :"); //$NON-NLS-1$ //NON-NLS
			this.panelPointDeDepart.add(this.labelLabelCoordDepart);
			this.labelLabelCoordDepart.setBounds(10, 30, 130, this.labelLabelCoordDepart.getPreferredSize().height);

			//---- labelCoordDepart ----
			this.labelCoordDepart.setText("N/D"); //$NON-NLS-1$ //NON-NLS
			this.labelCoordDepart.setHorizontalAlignment(SwingConstants.RIGHT);
			this.panelPointDeDepart.add(this.labelCoordDepart);
			this.labelCoordDepart.setBounds(160, 30, 162, 16);

			//---- labelLabelCouleurDepart ----
			this.labelLabelCouleurDepart.setText("Couleur du rep\u00e8re :"); //$NON-NLS-1$ //NON-NLS
			this.panelPointDeDepart.add(this.labelLabelCouleurDepart);
			this.labelLabelCouleurDepart.setBounds(new Rectangle(new Point(10, 67), this.labelLabelCouleurDepart.getPreferredSize()));
			this.panelPointDeDepart.add(this.choixCouleurDepart);
			this.choixCouleurDepart.setBounds(292, 60, 30, 30);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelPointDeDepart.getComponentCount(); i++) {
					Rectangle bounds = this.panelPointDeDepart.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelPointDeDepart.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelPointDeDepart.setMinimumSize(preferredSize);
				this.panelPointDeDepart.setPreferredSize(preferredSize);
			}
		}
		add(this.panelPointDeDepart);
		this.panelPointDeDepart.setBounds(10, 55, 330, 145);

		//======== panelPointDArrivee ========
		{
			this.panelPointDArrivee.setBorder(new TitledBorder(null, "Point d'arriv\u00e9e", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, //$NON-NLS-1$ //NON-NLS
				new Font(".SF NS Text", Font.PLAIN, 18))); //$NON-NLS-1$ //NON-NLS
			this.panelPointDArrivee.setLayout(null);

			//---- buttonSelectionArrivee ----
			this.buttonSelectionArrivee.setText("S\u00e9lectionner sur la carte"); //$NON-NLS-1$ //NON-NLS
			this.buttonSelectionArrivee.addActionListener(e -> buttonSelectionArriveeActionPerformed(e));
			this.panelPointDArrivee.add(this.buttonSelectionArrivee);
			this.buttonSelectionArrivee.setBounds(5, 104, 320, this.buttonSelectionArrivee.getPreferredSize().height);

			//---- labelLabelCoordArrivee ----
			this.labelLabelCoordArrivee.setText("Coordonn\u00e9es :"); //$NON-NLS-1$ //NON-NLS
			this.panelPointDArrivee.add(this.labelLabelCoordArrivee);
			this.labelLabelCoordArrivee.setBounds(10, 30, 130, this.labelLabelCoordArrivee.getPreferredSize().height);

			//---- labelCoordArrivee ----
			this.labelCoordArrivee.setText("N/D"); //$NON-NLS-1$ //NON-NLS
			this.labelCoordArrivee.setHorizontalAlignment(SwingConstants.RIGHT);
			this.panelPointDArrivee.add(this.labelCoordArrivee);
			this.labelCoordArrivee.setBounds(160, 30, 162, 16);

			//---- labelLabelCouleurArrivee ----
			this.labelLabelCouleurArrivee.setText("Couleur du rep\u00e8re :"); //$NON-NLS-1$ //NON-NLS
			this.panelPointDArrivee.add(this.labelLabelCouleurArrivee);
			this.labelLabelCouleurArrivee.setBounds(new Rectangle(new Point(10, 67), this.labelLabelCouleurArrivee.getPreferredSize()));

			//---- choixCouleurArrivee ----
			this.choixCouleurArrivee.setColor(Color.blue);
			this.panelPointDArrivee.add(this.choixCouleurArrivee);
			this.choixCouleurArrivee.setBounds(292, 60, 30, 30);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < this.panelPointDArrivee.getComponentCount(); i++) {
					Rectangle bounds = this.panelPointDArrivee.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = this.panelPointDArrivee.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				this.panelPointDArrivee.setMinimumSize(preferredSize);
				this.panelPointDArrivee.setPreferredSize(preferredSize);
			}
		}
		add(this.panelPointDArrivee);
		this.panelPointDArrivee.setBounds(10, 210, 330, 145);

		//---- buttonClearRoute ----
		this.buttonClearRoute.setText("Effacer le trajet"); //$NON-NLS-1$ //NON-NLS
		this.buttonClearRoute.addActionListener(e -> buttonClearRouteActionPerformed(e));
		add(this.buttonClearRoute);
		this.buttonClearRoute.setBounds(10, 365, 331, 32);

		setPreferredSize(new Dimension(350, 645));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		this.choixCouleurDepart.addActionListener(e -> {
			for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
				listener.startPointColorSelected(choixCouleurDepart.getColor());
		});

		this.choixCouleurArrivee.addActionListener(e -> {
			for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
				listener.arrivalPointColorSelected(choixCouleurArrivee.getColor());
		});
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);

		if (aFlag){
			for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
				listener.startPointColorSelected(choixCouleurDepart.getColor());

			for (RouteSimulationSidePanelActionListener listener : OBJETS_ENREGISTRES.getListeners(RouteSimulationSidePanelActionListener.class))
				listener.arrivalPointColorSelected(choixCouleurArrivee.getColor());
		}

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panelPointDeDepart;
	private JButton buttonSelectionDepart;
	private JLabel labelLabelCoordDepart;
	private JLabel labelCoordDepart;
	private JLabel labelLabelCouleurDepart;
	private ColorChooserPanel choixCouleurDepart;
	private JPanel panelPointDArrivee;
	private JButton buttonSelectionArrivee;
	private JLabel labelLabelCoordArrivee;
	private JLabel labelCoordArrivee;
	private JLabel labelLabelCouleurArrivee;
	private ColorChooserPanel choixCouleurArrivee;
	private JButton buttonClearRoute;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
