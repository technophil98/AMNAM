package math.graph;

/**
 * Modélisation d'un sommet de graphe.
 *
 * @author Philippe Rivest
 */
public class Node {

	private int id;

	/**
	 * Instancies une nouvelle Node (sommet).
	 *
	 * @param id l'identifiant du sommet
	 */
	public Node(int id) {
		this.id = id;
	}

	/**
	 * Retourne l'identifiant du sommet.
	 *
	 * @return l'identifiant du sommet
	 */
	public int getId() {
		return id;
	}

	/**
	 * Calcule le facteur heuristique entre ce sommet et le sommet « n ».
	 *
	 * @param n le sommet avec lequel calculer l'heuristique
	 * @return Le facteur heuristique entre ce sommet et le sommet « n »
	 */
	public double heuristicToNode(Node n){
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Node))
			return false;

		Node node = (Node) o;

		return getId() == node.getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public String toString() {
		return String.format("Node{%s}", id);
	}
}
