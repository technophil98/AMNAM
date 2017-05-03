package ui.event;

import ui.panneaux.side_panels.SidePanel;

import java.util.EventListener;

/**
 * Interface d'évènement (EventListener) pour les {@link ui.panneaux.side_panels.SuperpositionSidePanel}
 *
 * @author Philippe Rivest
 */
public interface SuperpositionSidePanelListener extends EventListener {

	/**
	 * Méthode exécutée lors de la requête de l'affichage du panneau de configuration du SuperpositionSidePanel.
	 *
	 * @param configSidePanel le panneau de configuration du SuperpositionSidePanel
	 */
	public void superpositionConfigSidePanelRequested(SidePanel configSidePanel);

	/**
	 * Méthode exécutée lors d'un changement dans l'ordre des superpositions.
	 */
	public void superpositionOrderChanged();

}
