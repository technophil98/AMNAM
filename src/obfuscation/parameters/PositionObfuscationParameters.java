package obfuscation.parameters;

import math.MathUtils;
import math.Vecteur;
import obfuscation.ObfuscationParameters;
import scenario.events.ReportedEvent;

/**
 * Paramètre d’obfuscation de la position d’un évènement pour recréer l'imprécision GPS qui a un écart type de 3.9m
 *
 * @author Philippe Rivest
 */
public class PositionObfuscationParameters implements ObfuscationParameters {

	private double standardDeviation = 3.9;

	/**
	 * Instancies un nouveau PositionObfuscationParameters avec un écart type représentant l'imprécision GPS.
	 *
	 * @param standardDeviation l'écart type représentant l'imprécision GPS
	 */
	public PositionObfuscationParameters(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	/**
	 * Mets à jour l'écart type d'obfuscation.
	 *
	 * @param standardDeviation l'écart type d'obfuscation
	 */
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	@Override
	public ReportedEvent obfuscateEvent(ReportedEvent e) {

		final Vecteur position = e.getPosition();
		final double x = position.getX(), y = position.getY();

		e.setPosition(new Vecteur(MathUtils.normalDistribution(x, standardDeviation), MathUtils.normalDistribution(y, standardDeviation)));
		return e;
	}
}
