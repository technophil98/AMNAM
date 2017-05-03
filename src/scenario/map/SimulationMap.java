package scenario.map;

import math.Vecteur;
import math.graph.Graph;
import scenario.events.Event;
import utils.tuple.Pair;
import utils.tuple.Triplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La carte de la simulation.
 * @author Pascal Dally-Bélanger
 */
public class SimulationMap {

	private HashMap<Integer, MapEdge> edges;
	private HashMap<Integer, MapEdge> sidewalks;
	private MapNode[] nodes;
	
	/**
	 * Initialise une carte en lisant le fichier spécifié.
	 * @param file Le chemain d'accès du fichier.
	 * @throws IOException Si une IOException est levé durant la lecture de la carte.
	 */
	public SimulationMap(String file) throws IOException {

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(SimulationMap.class.getResource(file).openStream()))) {
			MapElement map = parseMap(reader);
			Pair<HashMap<Integer, MapEdge>, HashMap<Integer, MapEdge>> pair = extractEdges(map);
			edges = pair.getFirst();
			sidewalks = pair.getSecond();
			nodes = extractNodes(map);
		}
	}
	
	/**
	 * Retourne le graph associé à la carte.
	 * @return Le graph.
	 */
	public Graph getGraph() {
		return new Graph(nodes, edges.values().toArray(new MapEdge[0]));
	}
	
	/**
	 * Détermine le point sur la route le plus proche d'un point quelconque sur la carte.
	 *
	 * @param point le point quelconque sur la carte.
	 * @return Un triplet contenant: <br>
	 * 1. La {@link MapEdge} sur laquelle se retrouve le point. <br>
	 * 2. le {@link Vecteur} position du point sur la route. <br>
	 * 3. Un {@link Double} représentant la "progression" vers le prochain points.
	 */
	public Triplet<MapEdge, Vecteur, Double> getClosestPosition(Vecteur point) {
		int edge = 0;
		MapEdge[] edges = this.edges.values().toArray(new MapEdge[0]);
		Vecteur position = edges[edge].getPoints()[0];
		double progress = 0;
		double dist2Min = point.moins(position).norme2();
		
		for(int i = 0; i < edges.length; i++) {
			Vecteur[] points = edges[i].getPoints();
			for(int j = 1; j < points.length; j++) {
				Vecteur a = points[j-1];
				Vecteur b = points[j];
				Vecteur ap = point.moins(points[j-1]);
				Vecteur ab = points[j].moins(points[j-1]);
				double coef = ap.dot(ab)/ab.norme2();
				
				Vecteur proxim;
				if(coef < 0) {
					proxim = a;
				} else if(coef > 1){
					proxim = b;
				} else {
					proxim = a.plus(ab.mul(coef));
				}
				double dist2 = proxim.moins(point).norme2();
				if(dist2 < dist2Min) {
					edge = i;
					position = proxim;
					progress = coef;
					dist2Min = dist2;
				}
			}
		}
		return new Triplet<>(edges[edge], position, progress);
	}
	
	/**
	 * Retourne la liste de sommet dans la carte.
	 * @param map La carte.
	 * @return La liste de sommet dans la carte.
	 */
	private static MapNode[] extractNodes(MapElement map) {
		for(MapElement element : map.subElements)
			if(element.type == MapElement.MapElementType.ROAD_NODES) {
				ArrayList<MapNode> nodes = new ArrayList<MapNode>();
				int count = Integer.parseInt(element.postDeclaration[0]);
				for(int i = 0; i < count; i++) {
					int id = Integer.parseInt(element.subElements[i].preDeclaration[0]);
					double x = Double.parseDouble(element.subElements[i].postDeclaration[0]);
					double y = Double.parseDouble(element.subElements[i].postDeclaration[1]);
					double z = Double.parseDouble(element.subElements[i].postDeclaration[2]);
					nodes.add(new MapNode(id, x, y, z));
				}
				return nodes.toArray(new MapNode[0]);
			}
		throw new RuntimeException("ROAD_NODES parameter not found in element " + map.type);
	}
	
	/**
	 * Retourne la liste d'arrête dans la carte.
	 * @param map La carte.
	 * @return Une pair contenant la liste d'arrête dans la carte suivie de la liste de trottoir de la carte.
	 */
	private static Pair<HashMap<Integer, MapEdge>, HashMap<Integer, MapEdge>> extractEdges(MapElement map) {
		for(MapElement roads : map.subElements)
			if(roads.type == MapElement.MapElementType.ROADS) {
				HashMap<Integer, MapEdge> edges = new HashMap<Integer, MapEdge>();
				HashMap<Integer, MapEdge> sidewalks = new HashMap<Integer, MapEdge>();
				for(MapElement road : roads.subElements) {
					boolean isSidewalk = true;
					lanes:
					for(MapElement lanes : road.subElements)
						if(lanes.type == MapElement.MapElementType.LANES)
							lane: 
							for(MapElement lane : lanes.subElements) {
								for(String info : lane.subDeclaration)
									if(info.contains("PEDESTRIAN"))
										continue lane;
								isSidewalk = false;
								break lanes;
							}
					if(isSidewalk)
						sidewalks.putAll(parseEdge(road));
					else 
						edges.putAll(parseEdge(road));
				}
				return new Pair<HashMap<Integer, MapEdge>, HashMap<Integer, MapEdge>>(edges, sidewalks);
			}
		throw new RuntimeException("ROADS parameter not found in element " + map.type);
	}
	
	/**
	 * Extrait l'arrête de la route dans l'élément passé en paramètre.
	 * @param road La route à extraire.
	 * @return La route extrait.
	 */
	private static HashMap<Integer, MapEdge> parseEdge(MapElement road) {
		double dist = 0;
		double speed = 0;
		int id = Integer.parseInt(road.preDeclaration[0]);
		int source = Integer.parseInt(road.postDeclaration[0]);
		int destination = Integer.parseInt(road.postDeclaration[1]);
		byte directions = 0;
		Vecteur[] points = null;
		for(MapElement subElements : road.subElements)
			if(subElements.type == MapElement.MapElementType.POINTS) {
				Vecteur[] v = new Vecteur[Integer.parseInt(subElements.postDeclaration[0])];
				for(int i = 0; i < v.length; i++)
					v[i] = new Vecteur(Double.parseDouble(subElements.subElements[i].postDeclaration[0]), Double.parseDouble(subElements.subElements[i].postDeclaration[1]));
				points = v;
				for(int i = 1; i < v.length; i++)
					dist += Math.sqrt(v[i-1].moins(v[i]).norme2());
			} else if(subElements.type == MapElement.MapElementType.LANES) {
				for(MapElement lane : subElements.subElements) {
					directions |= lane.postDeclaration[0].equals("-1") ? 0b10 : 0b01;
					speed = Double.parseDouble(lane.subDeclaration[0].replaceAll("\\D", ""));
				}
			}
		HashMap<Integer, MapEdge> edges = new HashMap<Integer, MapEdge>();
		if((directions & 0b01) != 0) {
			MapEdge edge = new MapEdge(id, source, destination, dist / speed);
			edge.setPoints(points);
			edges.put(edge.getId(), edge);
		}
		if((directions & 0b10) != 0) {
			MapEdge edge = new MapEdge(-id, destination, source, dist / speed);
			List<Vecteur> list = Arrays.asList(Arrays.copyOf(points, points.length));
			Collections.reverse(list);
			edge.setPoints(list.toArray(new Vecteur[0]));
			edges.put(edge.getId(), edge);
		}
		return edges;
	}
	
	/**
	 * Retourne la carte tel décrit dans le fichier passé en paramètre.
	 * @param reader Le lecteur de fichier.
	 * @return Retourne la carte tel décrit dans le fichier passé en paramètre.
	 * @throws IOException Si une exception est levée durant la lecture de la carte.
	 */
	private static MapElement parseMap(BufferedReader reader) throws IOException {
		return MapElement.parse("", MapElement.MapElementType.MAP, reader).getFirst();
	}
	
	/**
	 * Retourne la liste d'arrête de la carte.
	 * @return La liste d'arrête de la carte.
	 */
	public MapEdge[] getEdges() {
		return edges.values().toArray(new MapEdge[0]);
	}

	/**
	 * Retourne la liste des trottoirs de la carte.
	 * @return La liste des trottoirs de la carte.
	 */
	public MapEdge[] getSidewalks() {
		return sidewalks.values().toArray(new MapEdge[0]);
	}
	
	/**
	 * Retourne la liste des sommets de la carte.
	 * @return La liste des sommets de la carte.
	 */
	public MapNode[] getNodes() {
		return nodes;
	}
	
	/**
	 * Ajoute les évènements aux arrêtes.
	 * @param events Les évènements à ajouter.
	 */
	public void addEvents(Event[] events) {
		for(Event event : events) {
			int id = getClosestPosition(event.getPosition()).getFirst().getId();
			if(id == -1)
			edges.get(id).addEvent(event);
			edges.get(-id).addEvent(event);
		}
	}
	
	/**
	 * Met à jour les poids des arrêtes de la carte.
	 * @param t le temps auquel le poids doit être ajusté.
	 */
	public void updateWeights(double t) {
		for(MapEdge edge : edges.values())
			edge.setTime(t);
	}

	/**
	 * Rajoute un sommet médian séparant une arête en deux parties.
	 *
	 * @param originalEdge 	L'arête que le nouveau sommet séparera
	 * @param graphToModify 	Le graphe contenant l'arête
	 * @param medianNodePos 	La position du sommet médian
	 * @param completionInEdge Nombre entre [0,1] représentant le ratio chemin déjà parcouru
	 * @return le sommet médian
	 */
	public static MapNode addMedianNodeInGraph(MapEdge originalEdge, Graph graphToModify, Vecteur medianNodePos, double completionInEdge) {

		final MapNode medianNode = new MapNode(graphToModify.getNextAvailableNodeId(), medianNodePos.getX(), medianNodePos.getY(), 0);
		graphToModify.addNode(medianNode);

		int previousPointIndex = getPreviousClosestPoint(originalEdge, medianNodePos);

		final MapEdge edgeFromSource = new MapEdge(graphToModify.getNextAvailableEdgeId(), originalEdge.getMapSource(), medianNode.getId(), completionInEdge * originalEdge.getWeight());
		edgeFromSource.setPoints(Arrays.copyOfRange(originalEdge.getPoints(), 0, previousPointIndex));
		graphToModify.addEdge(edgeFromSource);


		final MapEdge edgeToDestination = new MapEdge(graphToModify.getNextAvailableEdgeId(), medianNode.getId(), originalEdge.getDestination(), 1 - completionInEdge * originalEdge.getWeight());
		edgeToDestination.setPoints(Arrays.copyOfRange(originalEdge.getPoints(), previousPointIndex + 1, originalEdge.getPoints().length));
		graphToModify.addEdge(edgeToDestination);

		if (graphToModify.getEdge(-originalEdge.getId()) != null){
			//Bi-dir!

			final MapEdge reversedEdge = ((MapEdge) graphToModify.getEdge(-originalEdge.getId()));

			int rPreviousPointIndex = getPreviousClosestPoint(reversedEdge, medianNodePos);

			final MapEdge rEdgeFromDestination = new MapEdge(graphToModify.getNextAvailableEdgeId(), reversedEdge.getMapSource(), medianNode.getId(), 1 - completionInEdge * originalEdge.getWeight());
			rEdgeFromDestination.setPoints(Arrays.copyOfRange(reversedEdge.getPoints(), 0, rPreviousPointIndex + 1));
			graphToModify.addEdge(rEdgeFromDestination);


			final MapEdge redgeToSource = new MapEdge(graphToModify.getNextAvailableEdgeId(), medianNode.getId(), reversedEdge.getDestination(), completionInEdge * originalEdge.getWeight());
			redgeToSource.setPoints(Arrays.copyOfRange(reversedEdge.getPoints(), rPreviousPointIndex, reversedEdge.getPoints().length));
			graphToModify.addEdge(redgeToSource);
		}

		graphToModify.removeEdge(originalEdge.getId());

		return medianNode;
	}

	/**
	 * Retourne l'index du point précédant à un point donné dans une arête de route.
	 *
	 * @param originalEdge L'arête contenant les points à comparer
	 * @param selectedPoint Le point à comparer
	 * @return L'index du point précédant
	 * @see MapEdge#points
	 */
	public static int getPreviousClosestPoint(MapEdge originalEdge, Vecteur selectedPoint) {
		double minDistance = Double.MAX_VALUE;
		int previousPointIndex = -1;

		for (int i = 0; i < originalEdge.getPoints().length - 1; i++) {
			final Vecteur meanPos = originalEdge.getPoints()[i+1].moins(originalEdge.getPoints()[i]).div(2.0).plus(originalEdge.getPoints()[i]);
			final double distFromMeanPos = selectedPoint.moins(meanPos).norme();
			if (minDistance > distFromMeanPos){
				minDistance = distFromMeanPos;
				previousPointIndex = i;
			}
		}

		return previousPointIndex;
	}
	
	/**
	 * Un élément dans le fichier de la carte.
	 * @author Pascal Dally-Bélanger
	 */
	private static class MapElement {
		
		private static final Pattern NEXT_DECLARATION = Pattern.compile(".*<(.*)>");
		private static final String[] EMPTY_ARRAY = new String[0];
		private String[] preDeclaration = EMPTY_ARRAY;
		private String[] postDeclaration = EMPTY_ARRAY;
		private String[] subDeclaration = EMPTY_ARRAY;
		private MapElement[] subElements;
		private MapElementType type;
		
		/**
		 * Crée le prochain élément de la carte.
		 * @param declaration La ligne sur laquelle est déclaré l'élément.
		 * @param type Le type de l'élément.
		 * @param reader Le lecteur de fichier.
		 * @return Une pair contenant l'élément créé et la ligne suivante.
		 * @throws IOException Si une exception est levée durant la lecture de la carte.
		 */
		private static Pair<MapElement, String> parse(String declaration, MapElementType type, BufferedReader reader) throws IOException {
			MapElement temp = new MapElement();
			ArrayList<String> preDeclaration = new ArrayList<String>();
			ArrayList<String> postDeclaration = new ArrayList<String>();
			ArrayList<String> subDeclaration = new ArrayList<String>();
			ArrayList<MapElement> subElements = new ArrayList<MapElement>();
			String[] list = declaration.split("\\s+");
			boolean isPredeclaration = true;
			for(int i = 0; i < list.length; i++) {
				if(list[i].equals("<" + type.name() + ">")) {
					isPredeclaration = false;
					continue;
				}
				if(isPredeclaration)
					preDeclaration.add(list[i]);
				else
					postDeclaration.add(list[i]);
			}
			String line = reader.readLine();
			w: while(line != null) {
				Matcher matcher = NEXT_DECLARATION.matcher(line);
				if(matcher.find()) {
					String tag = matcher.group(1);
					for(MapElementType child : type.children)
						if(child.name().equals(tag)) {
							Pair<MapElement, String> subElement = parse(line, child, reader);
							subElements.add(subElement.getFirst());
							line = subElement.getSecond();
							continue w;
						}
					break;
				} else
					subDeclaration.add(line);
				line = reader.readLine();
			}
			temp.preDeclaration = preDeclaration.toArray(new String[0]);
			temp.postDeclaration = postDeclaration.toArray(new String[0]);
			temp.subDeclaration = subDeclaration.toArray(new String[0]);
			temp.subElements = subElements.toArray(new MapElement[0]);
			temp.type = type;
			
			return new Pair<MapElement, String>(temp, line);
		}
	
		@Override
		public String toString() {
			return type + "=" + Arrays.toString(subElements);
		}
		
		/**
		 * La liste des types présents dans le fichier de la carte.
		 * @author Pascal Dally-Bélanger.
		 *
		 */
		private static enum MapElementType {
			ROAD_NODE(),
			ROAD_NODES(ROAD_NODE),
			STATE(),
			STATES(STATE),
			CROSSROAD_LIGHT(STATES),
			CROSSROAD_LIGHTS(CROSSROAD_LIGHT),
			SIGN(),
			SIGNS(SIGN),
			LANE(SIGNS),
			LANES(LANE),
			POINT(),
			POINTS(POINT),
			ROAD(POINTS, LANES),
			ROADS(ROAD),
			VERSION(),
			TRAFFIC_TYPE(),
			MAP(ROAD_NODES, ROADS, CROSSROAD_LIGHTS, VERSION, TRAFFIC_TYPE);
			
			private MapElementType[] children;
			
			/**
			 * Crée un type.
			 * @param children La liste des types des sous-éléments du type.
			 */
			private MapElementType(MapElementType... children) {
				this.children = children;
			}
		}
		
	}
}
