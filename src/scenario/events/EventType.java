package scenario.events;

import java.awt.Color;

/**
 * Une énumération des types d'évènements possible.
 *
 * @author Philippe Rivest
 */
public enum EventType {

	/**
	 * obstacle de type nid de poule.
	 */
	POTHOLE("Nid de poule", 0, Color.orange),
	/**
	 * obstacle de type Bloqueur.
	 */
	BLOCKAGE("Bloqueur", 10000, Color.red),
	/**
	 * obstacle de type Variateur.
	 */
	VARIATOR("Variateur", 10, Color.pink.darker()),
	/**
	 * obstacle de condition neigeuse.
	 */
	SNOW("Condition neigeuse", 10, new Color(0x0000)),
	/**
	 * obstacle de condition glacée.
	 */
	ICE("Condition glacée", 20, Color.blue),
	/**
	 * obstacle de condition pluvieuse.
	 */
	RAIN("Condition pluvieuse", 5, Color.blue);

	private final Color color;
	private final String name;
	private final double weight;

	/**
	 * Instancie un nouveau type d'évènement.
	 *
	 * @param name Le nom de l'évènement
	 * @param weight Le poids de l'évènement
	 * @param color La couleur de l'évènement
	 */
	private EventType(String name, double weight, Color color) {
		this.name = name;
		this.color = color;
		this.weight = weight;
	}
	
	/**
	 * Retourne la couleur de l'évènement.
	 * @return La couleur de l'évènement.
	 */
	public Color getEventColor() {
		return color;
	}
	
	/**
	 * Retourne le poids de l'évènement.
	 * @return Le poids de l'évènement
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Traduit les identifiants d'évènements de Virage en valeur de {@link EventType}.
	 *
	 * @param eventID l'identifiant de l'évènement
	 * @return Sa traduction en valeur de {@link EventType}
	 */
	public static EventType eventIDToEventType(int eventID){

		switch (eventID) {

			case 1:
			case 2:
				return POTHOLE;
			case 3:
			case 4:
				return BLOCKAGE;
			case 5:
			case 6:
				return VARIATOR;
			case 7:
				return SNOW;
			case 8:
				return ICE;
			case 9:
				return RAIN;

			default:
				return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}