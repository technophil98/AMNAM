package obfuscation.parameters;

import com.sun.istack.internal.Nullable;
import obfuscation.ObfuscationParameters;
import scenario.events.EventType;
import scenario.events.ReportedEvent;

import java.util.Random;

/**
 * Paramètre d’obfuscation du type d’un évènement pour simuler une erreur de détection qui peut survenir dans un cas réel.
 *
 * @author Philippe Rivest
 */
public class EventTypeObfuscationParameters implements ObfuscationParameters {

	private double errorRate = 0.075;

	private boolean randomType = true;
	private EventType invalidType;

	private Random rng;

	/**
	 * Instancies un nouvel EventTypeObfuscationParameters qui attribura un type aléatoire en cas d'obfuscation.
	 *
	 * @param errorRate le taux d'erreur/probabilité de détection
	 */
	public EventTypeObfuscationParameters(double errorRate) { this(errorRate, true, null); }

	/**
	 * Instancies un nouvel EventTypeObfuscationParameters qui attribura un type précis en fonction de la probabilité spécifiée.
	 *
	 * @param errorRate   le taux d'erreur/probabilité de détection
	 * @param invalidType le type à spésifier en cas d'obfuscation
	 */
	public EventTypeObfuscationParameters(double errorRate, EventType invalidType) { this(errorRate, false, invalidType); }

	/**
	 * Instancies un nouvel EventTypeObfuscationParameters.
	 *
	 * @param errorRate   le taux d'erreur/probabilité de détection
	 * @param invalidType le type à spésifier en cas d'obfuscation
	 * @param randomType vrai si le type est aléatoire en cas d'obfuscation
	 */
	private EventTypeObfuscationParameters(double errorRate, boolean randomType, @Nullable EventType invalidType) {

		if (errorRate > 1) errorRate /= 100.0;

		if (errorRate < 0)
			throw new ExceptionInInitializerError(String.format("Le taux d'erreur est erron\u00e9 (%s)", this.errorRate));

		this.errorRate = errorRate;
		this.randomType = randomType;
		this.invalidType = invalidType;
		this.rng = new Random();
	}

	@Override
	public ReportedEvent obfuscateEvent(ReportedEvent e) {

		if (errorRate >= this.rng.nextDouble())
			e.setType((randomType ? EventType.values()[rng.nextInt(EventType.values().length)] : invalidType));

		return e;
	}
}
