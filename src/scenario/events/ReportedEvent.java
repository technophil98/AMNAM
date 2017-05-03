package scenario.events;

import math.Vecteur;

/**
 * Un ReportedEvent est un condensé de {@link VehicleEvent} prenant la moyenne de la position et le premier temps de détection comme valeur de référence.
 *
 * @author Philippe Rivest
 */
public class ReportedEvent {

	private double timestamp;
	private Vecteur position;
	private EventType type;

	/**
	 * Instancies un nouveau ReportedEvent.
	 *
	 * @param vehicleEvent le {@link VehicleEvent} sur lequel se base l'instance
	 */
	public ReportedEvent(VehicleEvent vehicleEvent) {

		this.timestamp = vehicleEvent.getFirstTimestamp();
		this.position = vehicleEvent.getPosition();
		this.type = vehicleEvent.getEventType();

	}

	/**
	 * Retourne le temps de détection.
	 *
	 * @return le temps de détection
	 */
	public double getTimestamp() {
		return timestamp;
	}

	/**
	 * Mets à jour le temps de détection.
	 *
	 * @param timestamp le temps de détection
	 */
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Retourne la position de l'évènement c'est-à-dire la moyenne des sous-évènements.
	 *
	 * @return la position de l'évènement
	 */
	public Vecteur getPosition() {
		return position;
	}

	/**
	 * Mets à jour la position de l'évènement.
	 *
	 * @param position la position de l'évènement
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}

	/**
	 * Retourne le type de l'évènement.
	 *
	 * @return le type de l'évènement
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Mets à jour le type de l'évènement.
	 *
	 * @param type le type de l'évènement
	 */
	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ReportedEvent))
			return false;

		ReportedEvent that = (ReportedEvent) o;

		if (Double.compare(that.getTimestamp(), getTimestamp()) != 0)
			return false;
		if (!getPosition().equals(that.getPosition()))
			return false;
		return getType() == that.getType();
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(getTimestamp());
		result = (int) (temp ^ (temp >>> 32));
		result = 31 * result + getPosition().hashCode();
		result = 31 * result + getType().hashCode();
		return result;
	}
}
