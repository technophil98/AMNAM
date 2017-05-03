package obfuscation;

import io.amnam.FichierAMNAM;
import scenario.events.ReportedEvent;
import utils.processor.Processor;

import java.util.Arrays;
import java.util.Objects;

/**
 * Classe générant des évènements obfusqués à partir de paramètres précis et d'évènements véhiculaires.
 *
 * @see ObfuscationParameters
 * @see ReportedEvent
 * @author Philippe Rivest
 */
public class ObfuscatedEventGenerator extends Processor<ReportedEvent[]> {

	private FichierAMNAM fichierAMNAM;
	private ReportedEvent[] events;
	private ObfuscationParameters[] parameters;

	/**
	 * Instancies un nouveau ObfuscatedEventGenerator.
	 *
	 * @param fichierAMNAM Le fichierAMNAM contenant les évènements véhiculaires
	 * @param parameters   Les paramètres d'obfuscation
	 */
	public ObfuscatedEventGenerator(FichierAMNAM fichierAMNAM, ObfuscationParameters[] parameters) {
		this(fichierAMNAM,parameters, null);
	}

	/**
	 * Instancies un nouveau ObfuscatedEventGenerator avec un objet de mise à jour (callback).
	 *
	 * @param fichierAMNAM Le fichierAMNAM contenant les évènements véhiculaires
	 * @param parameters   Les paramètres d'obfuscation
	 * @param callback     L'objet de mise à jour (callback)
	 */
	public ObfuscatedEventGenerator(FichierAMNAM fichierAMNAM, ObfuscationParameters[] parameters, Object callback) {
		super(callback);
		this.fichierAMNAM = fichierAMNAM;
		this.parameters = parameters;
	}

	@Override
	protected synchronized void process() {
		events = Arrays.copyOf(fichierAMNAM.getReportedEvents(), fichierAMNAM.getReportedEvents().length);

		for (int i = 0; i < events.length; i++) {

			for (ObfuscationParameters parameter : parameters)
				events[i] = (events[i] != null ? parameter.obfuscateEvent(events[i]) : events[i]);

			updateProgress((double)(i+1) / (events.length));
		}

		events = Arrays.stream(events).filter(Objects::nonNull).toArray(ReportedEvent[]::new);

		updateProgress(1);
	}

	@Override
	public ReportedEvent[] getResult() {
		return events;
	}
}
