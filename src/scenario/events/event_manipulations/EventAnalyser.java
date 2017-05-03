package scenario.events.event_manipulations;

import io.amnam.FichierAMNAM;
import math.MathUtils;
import math.Vecteur;
import scenario.events.Event;
import scenario.events.EventType;
import scenario.events.ReportedEvent;
import utils.Counter;
import utils.processor.Processor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Un objet permettant l'analyse des évènements d'un scénario dans un {@link Thread} séparé.
 * @author Pascal Dally-Bélanger
 */
public class EventAnalyser extends Processor<AnalysisResult> {
	private static final double DEFAULT_EPSILON2 = 25;
	private static final int DEFAULT_MIN_PTS = 10;
	
	private double epsilon2 = DEFAULT_EPSILON2;
	private int minPts = DEFAULT_MIN_PTS;
	private AnalysisResult result;
	private FichierAMNAM fichier;
	
	/**
	 * Crée un EventAnalyser.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 */
	public EventAnalyser(FichierAMNAM fichier) {
		this.fichier = fichier;
	}
	
	/**
	 * Crée un EventAnalyser avec un objet utiliser pour la concurence.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 * @param callback un objet qui est notifier une fois la fin de l'exécution du {@link Thread}.
	 */
	public EventAnalyser(FichierAMNAM fichier, Object callback) {
		super(callback);
		this.fichier = fichier;
	}
	
	/**
	 * Crée un EventAnalyser avec les paramètres.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 * @param epsilon2 Le paramètre &epsilon;&sup2; de l'algorithme OPTICS.
	 * @param minPts Le paramètre minPts de l'algorithme OPTICS.
	 */
	public EventAnalyser(FichierAMNAM fichier, double epsilon2, int minPts) {
		this.minPts = minPts;
		this.epsilon2 = epsilon2;
		this.fichier = fichier;
	}
	
	/**
	 * Crée un EventGenerator avec les paramètres et un objet utiliser pour la concurence.
	 * @param fichier Le fichierAMNAM contenant les évènements véhiculaires
	 * @param epsilon2 Le paramètre &epsilon;&sup2; de l'algorithme OPTICS.
	 * @param minPts Le paramètre minPts de l'algorithme OPTICS.
	 * @param callback un objet qui est notifier une fois la fin de l'exécution du {@link Thread}.
	 */
	public EventAnalyser(FichierAMNAM fichier, double epsilon2, int minPts, Object callback) {
		super(callback);
		this.minPts = minPts;
		this.epsilon2 = epsilon2;
		this.fichier = fichier;
	}
	
	@Override
	protected void process() {
		ReportedEvent[] baseEvents = fichier.getObfuscatedEvents();
		HashMap<EventType, ArrayList<ReportedEvent>> obfEvents = new HashMap<>();
		for(ReportedEvent event : baseEvents) {
			if(event == null)
				continue;
			obfEvents.computeIfAbsent(event.getType(), k -> new ArrayList<>());
			obfEvents.get(event.getType()).add(event);
		}
		
		ArrayList<Event> eventList = new ArrayList<>();
		/*HashMap<EventType, ArrayList<ArrayList<Vecteur>>> clusters = new HashMap<EventType, ArrayList<ArrayList<Vecteur>>>();
		obfEvents.forEach((k, v) -> clusters.put(k, new MathUtils.OPTICS(epsilon2, minPts, v.stream().map(ReportedEvent::getPosition).toArray(Vecteur[]::new)).execute()));
		clusters.forEach((k, v) -> {
			
		});*/
		Counter counter = new Counter();
		obfEvents.forEach((k, v) ->
			new MathUtils.OPTICS(epsilon2, minPts, v.stream().map(ReportedEvent::getPosition).toArray(Vecteur[]::new)).execute().stream().filter(n -> {
				if(n.size() > 1)
					return true;
				counter.increment();
				return false;
			}).map(n -> {
				Vecteur vec = new Vecteur(0, 0);
				for(Vecteur v2 : n)
					vec = vec.plus(v2);
				return vec.div(n.size());
			}).map(vec -> new Event(vec, k, 0)).forEach(eventList::add));
		result = new AnalysisResult(eventList.toArray(new Event[0]), counter.getCount());
	}
	
	@Override
	public AnalysisResult getResult() {
		return result;
	}
}