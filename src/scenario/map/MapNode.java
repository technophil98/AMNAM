package scenario.map;

import math.graph.Node;

/**
 * Modification du sommet généralisé pour une représentation routière de celui-ci.
 * @see math.graph.Node
 * @author Pascal Dally-Bélanger
 */
public class MapNode extends Node {

	private int id;
	private double x, y, z;

	/**
	 * Crée un nouveau sommet.
	 *
	 * @param id L'ID du sommet.
	 * @param x La position en x.
	 * @param y La position en y.
	 * @param z La position en z.
	 */
	public MapNode(int id, double x, double y, double z) {
		super(id);
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Retourne la coordonée en x du sommet.
	 *
	 * @return la coordonée en x du sommet
	 */
	public double getX() {
		return x;
	}

	/**
	 * Retourne la coordonée en y du sommet.
	 *
	 * @return la coordonée en y du sommet
	 */
	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return String.format("Node{%s}[%s, %s, %s]", id, x, y, z);
	}
}