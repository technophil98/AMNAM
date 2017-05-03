package utils.processor;

import java.util.EventListener;

/**
 * Écouteur qui réagit au progrès d'un processus.
 * @author Pascal Dally-Bélanger
 *
 */
public interface ProgressListener extends EventListener {

	/**
	 * Évènement levé quand le progrès est mis à jour.
	 * @param progress La progression entre 0 et 1.
	 */
	public void updateProgress(double progress);
	
	/**
	 * Évènement levé quand le processus est fini.
	 */
	public void onCompletion();
}
