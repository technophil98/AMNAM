package ui.event;

import java.util.EventListener;

/**
 * Un écouteur d'évènement qui permet de capter quand un objet demande à un autre de ce mettre à jour.
 * @author Pascal Dally-Bélanger
 *
 */
public interface UpdateRequestListener extends EventListener {

	/**
	 * Demande une mise à jour.
	 */
	public void requestUpdate();
}
