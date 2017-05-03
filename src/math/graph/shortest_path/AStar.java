package math.graph.shortest_path;

import math.graph.Graph;
import math.graph.Node;

import java.util.*;
import java.util.Map.Entry;

/**
 * Modélisation en Java de l'algorithme de recherche du plus petit chemin A*.
 *
 * @author Philippe Rivest
 */
public class AStar {

	private Graph graph;
	private Map<Node, AStarNode> aStarNodes  = new HashMap<>();

	/**
	 * Instancies un nouveau processus AStar.
	 *
	 * @param graph Le graphe à utiliser pour la recherche.
	 */
	public AStar(Graph graph) {
		this.graph = graph;

		Arrays.stream(graph.getNodes()).forEach(node -> aStarNodes.put(node, new AStarNode(node)));
		Arrays.stream(graph.getEdges()).forEach(edge -> aStarNodes.get(edge.getSource()).addNeighbourWithWeight(aStarNodes.get(edge.getDest()), edge.getWeight()));

	}

	/**
	 * Méthode déterminant le chemin le plus court entre deux sommets.
	 *
	 * @param start Le sommet de départ
	 * @param end   Le sommet d'arrivée
	 * @return Une liste de sommets représentant le chemin parcouru
	 */
	public List<Node> findShortestPath(Node start, Node end){

		if (graph.getNode(start.getId()) != null && graph.getNode(end.getId()) != null){

			aStarNodes.values().forEach(aStarNode -> aStarNode.sethScore(aStarNode.getNode().heuristicToNode(end)));

			final PriorityQueue<AStarNode> openSet = new PriorityQueue<>((n1, n2) -> ((int) Math.signum(n1.getfScore() - n2.getfScore())));
			final HashSet<AStarNode> closedSet = new HashSet<>();
			final Map<AStarNode, AStarNode> path = new HashMap<>();

			AStarNode startNode = aStarNodes.get(start);
			startNode.setgScore(0);
			openSet.add(startNode);

			while (!openSet.isEmpty()){

				final AStarNode n = openSet.poll();

				if (n.getNode().equals(end))
					return createPath(path, n);

				closedSet.add(n);

				for (Entry<AStarNode, Double> neighborEntry : n.getNeighbours().entrySet()) {

					AStarNode neighbour = neighborEntry.getKey();

					if (closedSet.contains(neighbour))
						continue;

					double distance = neighborEntry.getValue();
					double tentativeGScore = distance + n.getgScore();

					if (tentativeGScore >= neighbour.getgScore())
						continue;

					neighbour.setgScore(tentativeGScore);
					path.put(neighbour, n);

					openSet.remove(neighbour);
					openSet.add(neighbour);
				}

			}
		}

		return null; //Aucun chemin possible
	}

	/**
	 * Recontruit les données accumulées par {@link #findShortestPath(Node, Node)} en une liste de sommets représentant le chemin le plus court.
	 *
	 * @param path Les données accumulées par {@link #findShortestPath(Node, Node)}
	 * @param dest La destination de la recherche
	 * @return une liste de sommets représentant le chemin le plus court
	 */
	private List<Node> createPath(Map<AStarNode, AStarNode> path, AStarNode dest){
		final List<Node> pathList = new ArrayList<>();

		pathList.add(dest.getNode());

		while (path.containsKey(dest)){
			dest = path.get(dest);
			pathList.add(dest.getNode());
		}

		Collections.reverse(pathList);
		return pathList;
	}

	/**
	 * Classe contenant les informations de sommets nécessaires à l'exécution de {@link #findShortestPath(Node, Node)}.
	 */
	private class AStarNode {

		private Node n;
		private Map<AStarNode, Double> neighbours = new HashMap<>();
		private double gScore = Double.MAX_VALUE, hScore;

		/**
		 * Instancies une nouvelle AStarNode.
		 *
		 * @param n Le sommet du graphe à tranformer en AStarNode
		 */
		AStarNode(Node n) {
			this.n = n;
		}

		/**
		 * Ajoute un voisin à la AStarNode avec un poids entre ceux-ci (poids de l'arête).
		 *
		 * @param n Le sommet du graphe à tranformer en AStarNode
		 * @param weight Le poids de l'arête menant au voisin
		 */
		void addNeighbourWithWeight(AStarNode n, double weight){
			neighbours.put(n, weight);
		}

		/**
		 * Retourne les voisins.
		 *
		 * @return les voisins
		 */
		Map<AStarNode, Double> getNeighbours() {
			return neighbours;
		}

		/**
		 * Retourne le sommet du graphe.
		 *
		 * @return le sommet du graphe
		 */
		Node getNode() {
			return n;
		}

		/**
		 * Retourne le score de déplacement à partir du départ de la AStarNode.
		 *
		 * @return le score de déplacement à partir du départ de la AStarNode
		 */
		double getgScore() {
			return gScore;
		}

		/**
		 * Mets à jour le score de déplacement à partir du départ de la AStarNode.
		 *
		 * @param gScore le score de déplacement à partir du départ de la AStarNode
		 */
		void setgScore(double gScore) {
			this.gScore = gScore;
		}

		/**
		 * Retourne le score heuristique entre cette AStarNode et l'arrivée.
		 *
		 * @return le score heuristique entre cette AStarNode et l'arrivée
		 */
		double gethScore() {
			return hScore;
		}

		/**
		 * Mets à jour le score heuristique entre cette AStarNode et l'arrivée.
		 *
		 * @param hScore le score heuristique entre cette AStarNode et l'arrivée
		 */
		void sethScore(double hScore) {
			this.hScore = hScore;
		}

		/**
		 * Retourne le score total de la AStarNode pour le calcul de chemin.
		 *
		 * @return le score total de la AStarNode
		 */
		double getfScore() {
			return getgScore() + gethScore();
		}

		@Override
		public String toString() {
			return String.format("%s[G: %s, H: %s, F: %s]", n.toString(), getgScore(), gethScore(), getfScore());
		}
	}

}
