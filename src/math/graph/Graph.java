package math.graph;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Modélisation d'une multigraphe pondéré et orienté
 *
 * @author Philippe Rivest
 */
public class Graph {

	private HashMap<Integer, Node> nodes = new HashMap<>();
	private HashMap<Integer, Edge> edges = new HashMap<>();

	/**
	 * Instancies un nouveau graphe sans arêtes ni sommets.
	 */
	public Graph() {}

	/**
	 * Instancies un nouveau graphe avec les sommets et arêtes spécifiées.
	 *
	 * @param nodes les sommets à ajouter
	 * @param edges les arêtes à ajouter
	 */
	public Graph(Node[] nodes, Edge[] edges) {
		Arrays.stream(nodes).forEach(node -> this.nodes.put(node.getId(), node));
		Arrays.stream(edges).forEach(edge -> this.edges.put(edge.getId(), edge));
	}


	/**
	 * Instancies un nouveau graphe en copiant les données d'un autre graphe.
	 *
	 * @param g Le graphe à copier
	 */
	public Graph(Graph g){
		this(Arrays.copyOf(g.getNodes(),g.getNodes().length), Arrays.copyOf(g.getEdges(),g.getEdges().length));
	}

	/**
	 * Retourne les sommets du graphe.
	 *
	 * @return les sommets du graphe
	 */
	public Node[] getNodes() {
		return nodes.values().toArray(new Node[0]);
	}

	/**
	 * Retourne les arêtes du graphe.
	 *
	 * @return les arêtes du graphe
	 */
	public Edge[] getEdges() {
		return edges.values().toArray(new Edge[0]);
	}

	/**
	 * Retourne l'arête entre un sommet et un autre.
	 * Si aucune arête existe, cette méthode retourne null.
	 *
	 * @param src  	Le sommet source de l'arête
	 * @param desc 	Le sommet de destination de l'arête
	 * @return 		l'arête entre un sommet et un autre ou null si elle n'existe pas
	 */
	public Edge getEdgeBetweenNodes(Node src, Node desc){

		for (Edge edge : edges.values())
			if (edge.getSource().equals(src) && edge.getDest().equals(desc))
				return edge;

		return null;
	}


	/**
	 * Retourne le prochain identifiant d'arête disponible.
	 *
	 * @return le prochain identifiant d'arête disponible
	 */
	public int getNextAvailableEdgeId(){
		int max = 0;

		for (Integer integer : edges.keySet()) {
			max = Math.max(integer, max);
		}

		return max + 1;
	}

	/**
	 * Retourne le prochain identifiant de sommet disponible.
	 *
	 * @return le prochain identifiant de sommet disponible
	 */
	public int getNextAvailableNodeId(){
		int max = 0;

		for (Integer integer : nodes.keySet()) {
			max = Math.max(integer, max);
		}

		return max + 1;
	}

	/**
	 * Retourne vrai si le graphe contient le sommet n.
	 *
	 * @param n le sommet à vérifier
	 * @return vrai si le graphe contient le sommet n
	 */
	public boolean containsNode(Node n){
		return nodes.get(n.getId()) != null;
	}

	/**
	 * Retourne vrai si le graphe contient l'arête e.
	 *
	 * @param e l'arête à vérifier
	 * @return vrai si le graphe contient l'arête e
	 */
	public boolean containsEdge(Edge e){
		return edges.get(e.getId()) != null;
	}

	/**
	 * Ajoute le sommet n au graphe.
	 *
	 * @param n le sommet à ajouter
	 * @return retourne vrai si le sommet n'était pas déjà dans le graphe ; faux s'il y était déjà
	 */
	public boolean addNode(Node n) {
		return nodes.put(n.getId(), n) != n;
	}

	/**
	 * Retourne un sommet dans le graphe qui a un identifiant « id ».
	 *
	 * @param id l'identifiant du sommet à retourner
	 * @return le sommet avec l'identifiant « id »
	 */
	public Node getNode(int id) {
		return nodes.get(id);
	}

	/**
	 * Ajoute l'arête e au graphe.
	 *
	 * @param e l'arête à ajouter
	 * @return retourne vrai si l'arête n'était pas déjà dans le graphe ; faux si elle y était déjà
	 */
	public boolean addEdge(Edge e){
		return edges.put(e.getId(), e) != e;
	}

	/**
	 * Retourne une arête dans le graphe qui a un identifiant « id ».
	 *
	 * @param id l'identifiant de l'arête à retourner
	 * @return l'arête avec l'identifiant « id »
	 */
	public Edge getEdge(int id){
		return edges.get(id);
	}

	/**
	 * Supprime une arête du graphe.
	 *
	 * @param id l'identifiant de l'arête à supprimer
	 * @return retourne vrai si l'arête était dans le graphe ; faux si elle n'y était pas
	 */
	public boolean removeEdge(int id){
		return edges.remove(id) != null;
	}

	@Override
	public String toString() {

		StringBuilder sBuilder = new StringBuilder("sommets : ");

		for (Node node : this.getNodes()) {
			sBuilder.append(node).append(" ");
		}

		sBuilder.append("\nArêtes :\n");

		for (Edge edge : this.getEdges()) {
			sBuilder.append(edge).append("\n");
		}

		return sBuilder.toString();
	}
}
