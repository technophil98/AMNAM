package scenario.events.event_manipulations;

import java.io.Serializable;

import scenario.events.Event;

/**
 * Classe contenant l'ensemble des informations que l'analyse est en mesure de générée.
 * @see EventAnalyser
 * @author Pascal Dally-Bélanger
 */
public class AnalysisResult implements Serializable {
	
	private static final long serialVersionUID = 6717020642268112919L;
	
	private Event[] events;
	private int discardedEvents;
	
	/**
	 * Instancie un résultat d'analyse.
	 *
	 * @param events Les évènements détectés par l'analyse.
	 * @param discardedEvents Le nombre d'évènements ignorés par l'analyse.
	 */
	public AnalysisResult(Event[] events, int discardedEvents) {
		this.events = events;
		this.discardedEvents = discardedEvents;
	}

	/**
	 * Retourne la liste d'évèmenets détectés par l'analyse.
	 * @return la Liste d'évèmenets détectés par l'analyse.
	 */
	public Event[] getEvents() {
		return events;
	}
	
	/**
	 * Retourne le nombre d'évènements ignorés par l'analyse.
	 * @return Le nombre d'évènements ignorés par l'analyse.
	 */
	public int getDiscardedEvents() {
		return discardedEvents;
	}

}
