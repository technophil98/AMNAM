package obfuscation;

import scenario.events.ReportedEvent;

/**
 * Interface définisant la structure d'un paramètre d'obfuscation.
 * Un paramètre doit contenir {@link ObfuscationParameters#obfuscateEvent(ReportedEvent)} qui obfusque des {@link ReportedEvent}.
 *
 * @author Philippe Rivest
 */
public interface ObfuscationParameters {

	/**
	 * Obfuscate un {@link ReportedEvent}.
	 *
	 * @param e un {@link ReportedEvent}
	 * @return le {@link ReportedEvent} obfusqué
	 */
	ReportedEvent obfuscateEvent(ReportedEvent e);
}
