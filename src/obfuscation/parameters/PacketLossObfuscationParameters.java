package obfuscation.parameters;

import obfuscation.ObfuscationParameters;
import scenario.events.ReportedEvent;

import java.util.Random;

/**
 * Paramètre de perte de paquets (ou de malfonction réseau) pour simuler l’interférence agissant sur la transmission par DSRC quoique celle-ci est extrêmement fiable.
 *
 * @author Philippe Rivest
 */
public class PacketLossObfuscationParameters implements ObfuscationParameters {

	private double packetLoss = 0.05;
	private Random rng;

	/**
	 * Instancies un nouveau PacketLossObfuscationParameters avec la proportion de d'évènement à omettre.
	 *
	 * @param packetLoss la proportion de d'évènement à omettre
	 */
	public PacketLossObfuscationParameters(double packetLoss) {

		if (packetLoss > 1) packetLoss /= 100.0;

		if (packetLoss < 0)
			throw new ExceptionInInitializerError(String.format("La proportion de perte de packet est erronée (%s)", packetLoss));

		this.packetLoss = packetLoss;
		this.rng = new Random();
	}

	@Override
	public ReportedEvent obfuscateEvent(ReportedEvent e) {
		return packetLoss < this.rng.nextDouble() ? e : null;
	}

}
