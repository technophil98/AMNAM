package scenario.events.event_manipulations;

import io.amnam.FichierAMNAM;
import io.csv.config.ConfigurationEvenement.LigneEvenement;
import math.Vecteur;
import scenario.events.EventType;
import scenario.events.VehicleEvent;
import utils.processor.Processor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Un objet permetant la création d'un {@link Thread} qui génère des {@link VehicleEvent} à partir des fichiers .csv.
 * @author Pascal Dally-Bélanger
 *
 */
public class EventGenerator extends Processor<VehicleEvent[]> {
	
	private static final double DEFAULT_DELTA_T_MAX = 1;
	private static final double DEFAULT_DELTA_P2_MAX = 1000;
	
	private double deltaTMax = DEFAULT_DELTA_T_MAX;
	private double deltaP2Max = DEFAULT_DELTA_P2_MAX;
	private VehicleEvent[] events;
	private FichierAMNAM fichier;
	
	/**
	 * Crée un EventGenerator avec les paramètres par défaut.
	 *
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 */
	public EventGenerator(FichierAMNAM fichier) {
		this.fichier = fichier;
	}
	
	/**
	 * Crée un EventGenerator avec les paramètres par défauts et un objet utiliser pour la concurence.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 * @param callback un objet qui est notifier une fois la fin de l'exécution du {@link Thread}.
	 */
	public EventGenerator(FichierAMNAM fichier, Object callback) {
		super(callback);
		this.fichier = fichier;
	}
	
	/**
	 * Crée un EventGenerator avec les paramètres.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 * @param deltaTMax La valeur maximale d'écart de temps pour le regrouppement des données.
	 * @param deltaP2Max La valeur maximale d'écart de position pour le regrouppement des données.
	 */
	public EventGenerator(FichierAMNAM fichier, double deltaTMax, double deltaP2Max) {
		this.fichier = fichier;
		this.deltaP2Max = deltaP2Max;
		this.deltaTMax = deltaTMax;
	}
	
	/**
	 * Crée un EventGenerator avec les paramètres et un objet utiliser pour la concurence.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 * @param deltaTMax La valeur maximale d'écart de temps pour le regrouppement des données.
	 * @param deltaP2Max La valeur maximale d'écart de position pour le regrouppement des données.
	 * @param callback un objet qui est notifier une fois la fin de l'exécution du {@link Thread}.
	 */
	public EventGenerator(FichierAMNAM fichier, double deltaTMax, double deltaP2Max, Object callback) {
		super(callback);
		this.fichier = fichier;
		this.deltaP2Max = deltaP2Max;
		this.deltaTMax = deltaTMax;
	}
	
	@Override
	protected synchronized void process() {
		ArrayList<VehicleEvent> list = new ArrayList<VehicleEvent>();
		LigneEvenement[] baseEvents = fichier.getLignesEventCSV();
		
		Arrays.sort(baseEvents, (a, b) -> (int)Math.signum(a.getTime() - b.getTime()));
		
		int min = 0;
		for(int i = 0; i < baseEvents.length; i++) {
			while(min < list.size() && baseEvents[i].getTime() - list.get(min).getLastTimestamp() > deltaTMax)
				min++;
			boolean added = false;
			for(int j = min; j < list.size() && !added; j++) {
				if(list.get(j).getEventType() == EventType.eventIDToEventType(baseEvents[i].getIdEvent()) &&
						baseEvents[i].getIdVehicle() == list.get(j).getSource() &&
						list.get(j).getLastPosition().moins(new Vecteur(baseEvents[i].getPositionX(), baseEvents[i].getPositionY())).norme2() < deltaP2Max) {
					list.get(j).addSecondaryEvent(new Vecteur(baseEvents[i].getPositionX(), baseEvents[i].getPositionY()), baseEvents[i].getTime());
					added = true;
				}
			}
			if(!added)
				list.add(new VehicleEvent(baseEvents[i].getIdVehicle(), new Vecteur(baseEvents[i].getPositionX(), baseEvents[i].getPositionY()), EventType.eventIDToEventType(baseEvents[i].getIdEvent()), baseEvents[i].getTime()));
			updateProgress((double)i/baseEvents.length);
		}
		updateProgress(1);
		events = list.toArray(new VehicleEvent[0]);
	}
	
	@Override
	public synchronized VehicleEvent[] getResult() {
		return events;
	}

}
