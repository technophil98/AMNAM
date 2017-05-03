package ui.event;

import ui.panneaux.side_panels.SidePanel;

import java.util.EventListener;

/**
 * Interface d'évènement personnalisé pour un composant voulant faire afficher un {@link SidePanel} fils.
 */
public interface PanelChangeRequestListener extends EventListener {


	/**
	 * Demande l'affichage du panneau latéral passé en paramètre.
	 *
	 * @param panel Le panneau latéral à afficher
	 */
	public void requestChange(SidePanel panel);
}
