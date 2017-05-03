package scenario.map;

import java.util.HashSet;

import math.Vecteur;
import math.graph.Edge;
import math.graph.Node;
import scenario.events.Event;

/**
 * Modification de l'arc généralisé pour une représentation routière de celui-ci.
 * @see math.graph.Edge
 * @author Pascal Dally-Bélanger
 */
public class MapEdge extends Edge {
	
	private int source;
	private int destination;
	private Vecteur[] points;
	private HashSet<Event> temporaryWeight = new HashSet<Event>();
	private double weight;
	
	/**
	 * Crée une nouvelle arrête.
	 * @param id L'ID de l'arrête.
	 * @param source L'ID du sommet source de l'arrête.
	 * @param dest L'ID du sommet de destination de l'arrête.
	 * @param weight Le poid de l'arrête. Plus le poids est petit, plus il est facile de la traverser.
	 */
	public MapEdge(int id, int source, int dest, double weight) {
		super(id, new Node(source), new Node(dest), weight);
		this.source = source;
		this.destination = dest;
	}
	
	
	@Override
	public synchronized double getWeight() {
		return weight + super.getWeight();
	}

	/**
	 * Crée une nouvelle arrête.
	 * @param id L'ID de l'arrête.
	 * @param source Le sommet source de l'arrête.
	 * @param dest Le sommet de destination de l'arrête.
	 * @param weight Le poid de l'arrête. Plus le poids est petit, plus il est facile de la traverser.
	 */
	public MapEdge(int id, MapNode source, MapNode dest, double weight) {
		super(id, source, dest, weight);
		this.source = source.getId();
		this.destination = dest.getId();
	}

	/**
	 * La liste de point de l'arrête.
	 * @return La liste de point.
	 */
	public Vecteur[] getPoints() {
		return points;
	}
	
	/**
	 * Assigne la liste de point passée en paramètre à l'arrête.
	 * @param points la nouvelle liste de point.
	 */
	public void setPoints(Vecteur[] points) {
		this.points = points;
	}
	
	/**
	 * Retourne l'ID du sommet de la source.
	 * @return l'ID du sommet de la source.
	 */
	public int getMapSource() {
		return source;
	}
	
	/**
	 * Retourne l'ID du sommet de la destination.
	 * @return l'ID du sommet de la destination.
	 */
	public int getDestination() {
		return destination;
	}
	
	/**
	 * Met à jour le poids de l'arrête.
	 * @param t Le temps auquel le poids doit être ajusté.
	 */
	public synchronized void setTime(double t) {
		weight = 0;
		for(Event e : temporaryWeight)
			if(e.isActive(t))
				weight += e.getWeight();
	}
	
	/**
	 * Ajoute un évènement à l'arrête.
	 *
	 * @param e l'évènement à ajouter.
	 */
	public void addEvent(Event e) {
		temporaryWeight.add(e);
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s, %s]", getId(), source, destination);
	}

}