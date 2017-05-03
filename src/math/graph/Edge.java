package math.graph;

/**
 * Modélisation d'une arête de graphe.
 *
 * @author Philippe Rivest
 */
public class Edge {

	private int id;
	private Node source;
	private Node dest;

	private double weight;

	/**
	 * Instancies un nouveau Edge (arête).
	 *
	 * @param id     l'identifiant de l'arête
	 * @param source la source de l'arête
	 * @param dest   la destination de l'arête
	 * @param weight le poids de l'arête
	 */
	public Edge(int id, Node source, Node dest, double weight) {
		this.id = id;
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}

	/**
	 * Instancies un nouveau Edge (arête) avec un poids de 0.
	 *
	 * @param id     l'identifiant de l'arête
	 * @param source la source de l'arête
	 * @param dest   la destination de l'arête
	 */
	public Edge(int id, Node source, Node dest) {
		this(id,source,dest, 0);
	}

	/**
	 * Retourne l'identifiant de l'arête.
	 *
	 * @return l'identifiant de l'arête
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retourne la source de l'arête.
	 *
	 * @return la source de l'arête
	 */
	public Node getSource() {
		return source;
	}

	/**
	 * Retourne la destination de l'arête.
	 *
	 * @return la destination de l'arête
	 */
	public Node getDest() {
		return dest;
	}

	/**
	 * Retourne le poids de l'arête.
	 *
	 * @return le poids de l'arête
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Met à jour le poids de l'arête.
	 *
	 * @param weight le poids de l'arête
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Edge))
			return false;

		Edge edge = (Edge) o;

		return getId() == edge.getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public String toString() {
		return String.format("Edge{%d} : %s --(%s)--> %s", id, source, weight, dest);
	}
}
