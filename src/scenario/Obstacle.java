package scenario;

import scenario.events.EventType;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Classe représentant un obstacle présent dans la simulation initiale.
 * Elle permetera la comapraison avec les résultats générés.
 *
 * @author Philippe Rivest
 */
public class Obstacle {

	public static final int OMITTED_VALUE = 5000;
	public static final Point2D.Double OMITTED_POS = new Point2D.Double(OMITTED_VALUE,OMITTED_VALUE);

	public transient final static Obstacle[] DEFAULT_OBSTACLES = {
			new Obstacle(1, EventType.POTHOLE, OMITTED_POS, 0),
			new Obstacle(2, EventType.POTHOLE, OMITTED_POS,0),
			new Obstacle(3, EventType.BLOCKAGE, OMITTED_POS,0),
			new Obstacle(4, EventType.BLOCKAGE, OMITTED_POS,0),
			new Obstacle(5, EventType.VARIATOR, OMITTED_POS,0),
			new Obstacle(6, EventType.VARIATOR, OMITTED_POS,0),
			new Obstacle(7, EventType.SNOW, OMITTED_POS,0),
			new Obstacle(8, EventType.ICE, OMITTED_POS,0),
			new Obstacle(9, EventType.RAIN, OMITTED_POS,0)
	};

	private int id;
	private EventType obstacleType;

	private Point.Double position;
	private double radius;

	private transient boolean omitted;

	/**
	 * Instancies un nouvel Obstacle.
	 *
	 * @param id           l'identifiant de l'obstacle
	 * @param obstacleType le type de l'obstacle
	 * @param position     la position de l'obstacle
	 * @param radius       le rayon de détection de l'obstacle
	 */
	public Obstacle(int id, EventType obstacleType, Point.Double position, double radius) {
		this.id = id;
		this.obstacleType = obstacleType;
		this.position = position;
		this.radius = radius;

		omitted = this.position.equals(new Point2D.Double(OMITTED_VALUE, OMITTED_VALUE)) && radius == 0;
	}


	/**
	 * Retourne l'identifiant de l'obstacle.
	 *
	 * @return l'identifiant de l'obstacle
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retourne le type de l'obstacle.
	 *
	 * @return le type de l'obstacle
	 */
	public EventType getObstacleType() {
		return obstacleType;
	}

	/**
	 * Retourne la position de l'obstacle.
	 *
	 * @return la position de l'obstacle
	 */
	public Point.Double getPosition() {
		return position;
	}

	/**
	 * Mets à jour la position de l'obstacle.
	 *
	 * @param position la position de l'obstacle
	 */
	public void setPosition(Point.Double position) {
		this.position = position;
	}

	/**
	 * Retourne le rayon de détection de l'obstacle.
	 *
	 * @return le rayon de détection de l'obstacle
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Mets à jour le rayon de détection de l'obstacle.
	 *
	 * @param radius le rayon de détection de l'obstacle
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Retourne vrai si cet obstacle a été omit.
	 *
	 * @return vrai si cet obstacle a été omit
	 */
	public boolean isOmitted() {
		return omitted;
	}

	/**
	 * Omet ou non l'obstacle.
	 *
	 * @param omitted l'état d'ommission
	 */
	public void setOmitted(boolean omitted) {
		this.omitted = omitted;
	}

	@Override
	public String toString() {
		return "Obstacle{" +
			   "id=" + id +
			   ", obstacleType=" + obstacleType +
			   ", position=" + position +
			   ", radius=" + radius +
			   '}';
	}
}
