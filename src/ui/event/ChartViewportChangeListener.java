package ui.event;

import java.util.EventListener;

/**
 * Interface d'évènement (EventListener) pour les {@link ui.panneaux.ChartPane}.
 *
 * @author Philippe Rivest
 */
public interface ChartViewportChangeListener extends EventListener {

	/**
	 * Méthode exécutée lorsqu'un {@link ui.panneaux.ChartPane} ajuste automatiquement son zoom.
	 */
	public void chartViewportWasChanged();

}
