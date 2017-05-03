package math;

import scenario.events.VehicleEvent;
import utils.Counter;

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Classe permettant la génération des données pour le graphique.
 * 
 * @author Pascal Dally-Bélanger
 *
 */
public class Statistics {
	
	/**
	 * Constructeur vide pour empêcher l'initalisation.
	 */
	private Statistics() {
		
	}
	
	/**
	 * Génère une collection contenant les différents points pour le graphique d'évènements en fonction du temps.
	 * @param events Les évènements des véhicules.
	 * @return Une collection contenant les différents points.
	 */
	public static TreeMap<Double, Double> getFunctionVehicleEventsTime(VehicleEvent[] events) {
		TreeMap<Double, Double> map = new TreeMap<Double, Double>();
		double count = 0;
		double time;
		for(int i = 0; i < events.length; i++) {
			count++;
			time = events[i].getMeanTimestamp();
			map.put(count, time);
		}
		return map;
	}
	
	/**
	 * Génère une collection contenant les différents points pour le graphique d'évènements en fonction d'un intervalle de temps.
	 * @param events Les évènements des véhicules.
	 * @param timeInterval l'intervalle de temps.
	 * @param min Le temps minimum auqel la fonction est évaluée.
	 * @param max Le temps maximum auqel la fonction est évaluée.
	 * @return Une collection contenant les différents points.
	 */
	public static TreeMap<Double, Double> getFunctionVehicleEventsTimeInterval(VehicleEvent[] events, double timeInterval, double min, double max) {
		if(timeInterval == 0d) {
			TreeMap<Double, Double> map = new TreeMap<Double, Double>();
			for(VehicleEvent event : events) {
				map.put(event.getMeanTimestamp(), 1d);
				map.put(event.getMeanTimestamp()+Double.MIN_VALUE, 1d);
			}
			return map;
		}
		TreeMap<Double, Double> map = new TreeMap<Double, Double>();
		TreeMap<Double, VehicleEvent> timeMap = new TreeMap<Double, VehicleEvent>();
		HashSet<VehicleEvent> activityMap = new HashSet<VehicleEvent>();
		int init = 0;
		int post = 0;
		for(int i = 0; i < events.length; i++) {
			if(events[i].getMeanTimestamp() - timeInterval < min) {
				activityMap.add(events[i]);
				init++;
			} else
				timeMap.put(events[i].getMeanTimestamp() - timeInterval, events[i]);
			if(events[i].getMeanTimestamp() + timeInterval > max)
				post++;
			else
				timeMap.put(events[i].getMeanTimestamp() + timeInterval, events[i]);
		}
		map.put(min, (double)init);
		Counter counter = new Counter(init);
		timeMap.forEach((k, v) -> {
			if(activityMap.contains(v)) {
				activityMap.remove(v);
				counter.decrement();
			} else {
				activityMap.add(v);
				counter.increment();
			}
			map.put(k, (double)counter.getCount());
		});
		map.put(max, (double)post);
		return map;
	}
}
