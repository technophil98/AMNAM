package scenario;

import io.amnam.FichierAMNAM;
import io.csv.config.ConfigurationInitialisation.LigneInitialisation;
import io.csv.config.ConfigurationPeriode.InfoPeriode;
import io.csv.config.ConfigurationPeriode.LignePeriode;
import math.MathUtils;
import math.Vecteur;
import scenario.events.Event;
import scenario.events.event_manipulations.EventGenerator;
import scenario.map.SimulationMap;
import ui.dessin.Vehicule;
import ui.dessin.superposition.*;
import utils.tuple.Pair;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Un scénario est l'objet qui s'occupe de l'interpretation d'un fichier .amnam pour en extraire les composants graphiques
 * qui doivent être rendu sur la carte ainsi que leur analyse.
 * @author Pascal Dally-Bélanger
 *
 */
public class Scenario {


	private static final String PATH_RNS_SIMTON = "/simton.rns";

	private FichierAMNAM fichier;
	
	private Set<Integer> IDs;
	private HashMap<Integer, InfoPeriode> infos = new HashMap<Integer, InfoPeriode>();
	private HashMap<Integer, LigneInitialisation> init = new HashMap<Integer, LigneInitialisation>();
	private SimulationMap map;
	private Vehicule[] vehicules;
	
	/**
	 * Initialise un nouveau scénario avec un fichier .amnam.
	 * @param fichier Le FichierAMNAM à interpreter.
	 * @throws IOException Si une exception est levée durant la lecture de la carte.
	 */
	public Scenario(FichierAMNAM fichier) throws IOException {
		this.fichier = fichier;

		for(InfoPeriode info :  fichier.getInformationsPeriodiques())
			infos.put(info.getFirstLine().getIdVehicle(), info);
		for(LigneInitialisation ligne :  fichier.getLignesInitCSV())
			init.put(ligne.getIdVehicle(), ligne);
		
		IDs = infos.keySet();
		map = new SimulationMap(PATH_RNS_SIMTON);

		vehicules = new Vehicule[fichier.getInfo().getNbVehicules()];
		int index = 0;
		for(Integer i : IDs)
			vehicules[index++] = new Vehicule(init.get(i).getTypeVehicle(), infos.get(i), init.get(i).getCapabilityDSRC(), init.get(i).getIdVehicle());
	}
	
	/**
	 * Retourne la liste des véhicules de la simulation.
	 * @return la liste des véhicules de la simulation.
	 */
	public Vehicule[] getVehicules() {
		return vehicules;
	}
	
	/**
	 * Retourne la valeur minimale de temps interpolable basé sur le FichierAMNAM passé en paramètre à l'initialisation.
	 * @return la valeur minimale de temps interpolable.
	 */
	public double getTempsMin() {
		return infos.get(IDs.toArray()[0]).iterator().next().getDuration();
	}
	
	/**
	 * Retourne la valeur maximale de temps interpolable basé sur le FichierAMNAM passé en paramètre à l'initialisation.
	 * @return la valeur maximale de temps interpolable.
	 */
	public double getTempsMax() {
		double t = 0;
		for(LignePeriode ligne : infos.get(IDs.toArray()[0]))
			t = ligne.getDuration();
		return t;
	}
	
	
	/**
	 * Retourne la superposition des véhicules de la simulation.
	 * @return la superposition des véhicules de la simulation.
	 */
	public VehiculeSuperposition getSuperpositionVehicule() {
		return new VehiculeSuperposition(getVehicules());
	}
	
	/**
	 * Retourne la superposition contenant la carte.
	 * @return La superposition contenant la carte.
	 */
	public CarteSuperposition getCarteSuperposition() {
		return new CarteSuperposition(map);
	}
	
	/**
	 * Retourne la superposition contenant la carte.
	 * @return La superposition contenant la carte.
	 */
	public VehicleEventSuperposition getVehicleEventSuperposition() {
		return new VehicleEventSuperposition(fichier.getVehiculeEvents());
	}
	
	/**
	 * Retourne la superposition contenant la carte.
	 * @param deltaTMax La valeur maximale d'écart de temps pour le regrouppement des données.
	 * @param deltaP2Max La valeur maximale d'écart de position pour le regrouppement des données.
	 * @return La superposition contenant la carte.
	 */
	public VehicleEventSuperposition getVehicleEventSuperposition(double deltaTMax, double deltaP2Max) {
		EventGenerator generator = new EventGenerator(fichier, deltaTMax, deltaP2Max, this);
		generator.start();
		while(!generator.isDone())
			try {
				synchronized(this) {
					wait();
				}
			} catch (InterruptedException e) {e.printStackTrace();}
		return new VehicleEventSuperposition(generator.getResult());
		
	}
	
	/**
	 * Retourne la superposition d'évènement.
	 * @return La superposition d'évènement.
	 */
	public EventSuperposition getEventSuperposition() {
		return new EventSuperposition(fichier.getAnalysisResults().getEvents());
	}
	
	/**
	 * Retourne la superposition d'obstacle.
	 * @return La superposition d'obstacle.
	 */
	public ObstacleSuperposition getObstacleSuperposition() {
		return new ObstacleSuperposition(fichier.getInfo().getObstacles());
	}
	
	/**
	 * Retourne la carte de la simulation.
	 * @return La carte de la simulation.
	 */
	public SimulationMap getMap() {
		return map;
	}
	
	/**
	 * Associe les évènements initiaux aux évènements analysés.
	 * @return Une liste de pairs contenant chaque association.
	 */
	private Pair<Obstacle, ArrayList<Event>>[] bindObstaclesToEvents() {
		Obstacle[] obstacles = fichier.getInfo().getObstacles();
		Event[] events = fichier.getAnalysisResults().getEvents();
		Pair<Obstacle, ArrayList<Event>>[] pairs = (Pair<Obstacle, ArrayList<Event>>[]) Array.newInstance(Pair.class, obstacles.length);
		for(int i = 0; i < pairs.length; i++)
			pairs[i] = new Pair<Obstacle, ArrayList<Event>>(obstacles[i], new ArrayList<Event>());
		for(int i = 0; i < events.length; i++) {
			Vecteur pos = events[i].getPosition();
			double dist2min = Double.MAX_VALUE;
			int index = -1;
			for(int j = 1; j < obstacles.length; j++) {
				if(obstacles[j].getObstacleType() == events[i].getEventType() && pos.moins(Vecteur.parseFromPoint2D(obstacles[j].getPosition())).norme2() < dist2min) {
					dist2min = pos.moins(Vecteur.parseFromPoint2D(obstacles[j].getPosition())).norme2();
					index = j;
				}
			}
			if(index != -1)
				pairs[index].getSecond().add(events[i]);
		}
		return pairs;
	}
	
	/**
	 * Retourne le nombre d'évènement après l'analyse.
	 * @return le nombre d'évènement
	 */
	public int getEventCount() {
		return fichier.getVehiculeEvents().length;
	}
	
	/**
	 * Retourne le ratio d'évènement éliminé par l'analyse.
	 * @return  le ratio d'évènement éliminé.
	 */
	public double getDiscardedEventsRatio() {
		return fichier.getAnalysisResults().getDiscardedEvents() / (double)getEventCount();
	}
	
	/**
	 * Retourne la distance moyenne entre les obstacles et la position des évènements après l'analyse.
	 * @return La distance moyenne entre les obstacles et la position des évènements après l'analyse.
	 */
	public double getAccuracy() {
		Event[] events = fichier.getAnalysisResults().getEvents();
		if(events == null || events.length == 0)
			return Double.NaN;
		Pair<Obstacle, ArrayList<Event>>[] pairs = bindObstaclesToEvents();
		ArrayList<Vecteur> vecs = new ArrayList<Vecteur>();
		for(int i = 0; i < pairs.length; i++)
			for(int j = 0; j < pairs[i].getSecond().size(); j++)
				vecs.add(Vecteur.parseFromPoint2D(pairs[i].getFirst().getPosition()).moins(pairs[i].getSecond().get(j).getPosition()));
		double sum = 0;
		for(Vecteur v : vecs) {
			sum += Math.sqrt(v.norme2());
		}
		return vecs.size() <= 3 ? Double.NaN : sum / vecs.size();
	}
	
	/**
	 * L'écart-type des différences de positionentre les obstacles et la position des évènements après l'analyse.
	 * @return L'écart-type.
	 */
	public double getPrecision() {
		Event[] events = fichier.getAnalysisResults().getEvents();
		if(events == null || events.length == 0)
			return Double.NaN;
		Pair<Obstacle, ArrayList<Event>>[] pairs = bindObstaclesToEvents();
		ArrayList<Vecteur> vecs = new ArrayList<Vecteur>();
		for(int i = 0; i < pairs.length; i++)
			for(int j = 0; j < pairs[i].getSecond().size(); j++)
				vecs.add(Vecteur.parseFromPoint2D(pairs[i].getFirst().getPosition()).moins(pairs[i].getSecond().get(j).getPosition()));
		return MathUtils.standardDeviation(vecs.toArray(new Vecteur[0]));
	}
	
	/**
	 * Associe à chaque évènement une arrête de la carte.
	 */
	public void bindEventsToMap() {
		map.addEvents(fichier.getAnalysisResults().getEvents());
	}
}
