package ui.event;

import ui.panneaux.side_panels.SidePanel;

import java.util.EventListener;

/**
 * Interface d'évènement (EventListener) pour les {@link ui.panneaux.side_panels.SidePanel}
 *
 * @author Philippe Rivest
 */
public interface SidePanelListener extends EventListener {

	/**
	 * Méthode exécutée lors de la fermeture d'un PanneauLateral.
	 *
	 * @param panneauLateral Le panneau d'information qui c'est fermé
	 */
	public void panelClosed(SidePanel panneauLateral);

}
