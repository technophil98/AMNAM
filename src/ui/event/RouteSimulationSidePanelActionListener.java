package ui.event;

import java.awt.*;
import java.util.EventListener;

/**
 * Interface d'évènement (EventListener) pour les {@link ui.panneaux.side_panels.RouteSimulationSidePanel}
 *
 * @author Philippe Rivest
 */
public interface RouteSimulationSidePanelActionListener extends EventListener {

	/**
	 * Méthode exécutée lors du début de la sélection du point de départ.
	 */
	public void startPointSelectionStarted();

	/**
	 * Méthode exécutée lors de la sélection de la couleur du point de départ.
	 *
	 * @param color La couleur sélectionnée
	 */
	public void startPointColorSelected(Color color);

	/**
	 * Méthode exécutée lors du début de la sélection du point d'arrivée.
	 */
	public void arrivalPointSelectionStarted();

	/**
	 * Méthode exécutée lors de la sélection de la couleur du point d'arrivée.
	 *
	 * @param color La couleur sélectionnée
	 */
	public void arrivalPointColorSelected(Color color);

	/**
	 * Méthode exécuté lorsque le panneau demande le calcul de la route la plus courte.
	 */
	public void routeCalculationWasRequested();

	/**
	 * Méthode exécuté lorsque le panneau demande l'effacement de la route sur la carte.
	 */
	public void routeClearanceWasRequested();
}
