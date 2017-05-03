package ui.dessin.superposition;

import math.Vecteur;
import math.graph.Graph;
import math.graph.Node;
import math.graph.shortest_path.AStar;
import scenario.map.MapEdge;
import scenario.map.MapNode;
import scenario.map.SimulationMap;
import ui.UICommonUtils;
import ui.dessin.Clickable;
import ui.dessin.Superposition;
import ui.panneaux.side_panels.SidePanel;
import utils.Counter;
import utils.tuple.Triplet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La superposition qui contient le trajet calculé par {@link math.graph.shortest_path.AStar#findShortestPath(Node, Node)}
 *
 * @author Philippe Rivest
 */
public class RouteSuperposition implements Superposition {


	private static final double SCALE_DEFAUT = 1 / 15d;
	private static final BufferedImage mapMarkImg;

	static {
		URL urlImage = UICommonUtils.class.getResource(UICommonUtils.PATH_IMG_MAP_MARKER);
		BufferedImage tmpImg = null;

		if (urlImage != null) {
			try {
				tmpImg = ImageIO.read(urlImage);
			} catch (IOException e) {
				System.out.println("Image not found: " + urlImage);
			}
		}
		mapMarkImg = tmpImg;
	}

	private Path2D path;

	private boolean isEnabled = true;
	private AffineTransform transform;
	private Vecteur startPoint, arrivalPoint;
	private Image startPointMapMarkImg = mapMarkImg, arrivalPointMapMarkImg = mapMarkImg;
	private Color startPointColor, arrivalPointColor;


	/**
	 * Instantiates a new Route superposition.
	 */
	public RouteSuperposition() {}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public SidePanel getPanneauConfiguration() {
		return null;
	}

	@Override
	public void setTemps(double t) {}

	@Override
	public Clickable[] getClickables() {
		return null;
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public String getName() {
		return "Trajets";
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	@Override
	public void dessiner(Graphics2D g2d) {

		if (startPoint != null){
			AffineTransform backup = g2d.getTransform();
			g2d.transform(transform);
			g2d.translate(startPoint.getX(), startPoint.getY());
			g2d.scale((1/g2d.getTransform().getScaleX()) * SCALE_DEFAUT, (1/g2d.getTransform().getScaleY()) * SCALE_DEFAUT);
			g2d.translate(-startPointMapMarkImg.getWidth(null) / 2.0, -startPointMapMarkImg.getHeight(null));
			g2d.drawImage(startPointMapMarkImg, 0, 0, startPointMapMarkImg.getWidth(null), startPointMapMarkImg.getHeight(null), null);
			g2d.setTransform(backup);
		}

		if (arrivalPoint != null){
			AffineTransform backup = g2d.getTransform();
			g2d.transform(transform);
			g2d.translate(arrivalPoint.getX(), arrivalPoint.getY());
			g2d.scale((1/g2d.getTransform().getScaleX()) * SCALE_DEFAUT, (1/g2d.getTransform().getScaleY()) * SCALE_DEFAUT);
			g2d.translate(-arrivalPointMapMarkImg.getWidth(null) / 2.0, -arrivalPointMapMarkImg.getHeight(null));
			g2d.drawImage(arrivalPointMapMarkImg, 0, 0, arrivalPointMapMarkImg.getWidth(null), arrivalPointMapMarkImg.getHeight(null), null);
			g2d.setTransform(backup);
		}

		if (path != null) {

			Color bkpColor = g2d.getColor();
			final Stroke bkpStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			Path2D[] pathSegments = UICommonUtils.splitPathInSegments(this.path);

			float[] destHSV = Color.RGBtoHSB(arrivalPointColor.getRed(), arrivalPointColor.getGreen(), arrivalPointColor.getBlue(), null);
			float[] srcHSV = Color.RGBtoHSB(startPointColor.getRed(), startPointColor.getGreen(), startPointColor.getBlue(), null);
			if (destHSV[0] - srcHSV[0] > 0.5f)
				srcHSV[0]++;
			else if (srcHSV[0] - destHSV[0] > 0.5f)
				destHSV[0]++;
			for (int i = 0; i < pathSegments.length; i++) {
				final float ratio = (float) i  / pathSegments.length;
				g2d.setColor(Color.getHSBColor(
						destHSV[0] * ratio + srcHSV[0] * (1-ratio),
						destHSV[1] * ratio + srcHSV[1] * (1-ratio),
						destHSV[2] * ratio + srcHSV[2] * (1-ratio)
						));
				g2d.draw(transform.createTransformedShape(pathSegments[i]));
			}

			g2d.setColor(bkpColor);
			g2d.setStroke(bkpStroke);
		}
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}


	/**
	 * Mets à jours le {@link Path2D} de route de la superposition à partir du résultat de l'algorithme A*.
	 *
	 * @param routeStartPointSelection   Un triplet contenant l'information sur le point de départ ({@link scenario.map.SimulationMap#getClosestPosition(Vecteur)})
	 * @param routeArrivalPointSelection Un triplet contenant l'information sur le point d'arrivée ({@link scenario.map.SimulationMap#getClosestPosition(Vecteur)})
	 * @param roadGraph                  Le graph de la carte
	 * @see AStar
	 * @see Graph
	 * @see Path2D
	 * @see MapEdge
	 * @see MapNode
	 */
	public void renderRoute(Triplet<MapEdge, Vecteur, Double> routeStartPointSelection, Triplet<MapEdge, Vecteur, Double> routeArrivalPointSelection, Graph roadGraph){

		if (routeStartPointSelection != null && routeArrivalPointSelection != null) {

			if (!routeStartPointSelection.getFirst().equals(routeArrivalPointSelection.getFirst())) {

				final Graph tmpGraph = new Graph(roadGraph);

				final MapNode startMapNode = SimulationMap.addMedianNodeInGraph(routeStartPointSelection.getFirst(), tmpGraph, routeStartPointSelection.getSecond(), routeStartPointSelection.getThird());
				final MapNode arrivalMapNode = SimulationMap.addMedianNodeInGraph(routeArrivalPointSelection.getFirst(), tmpGraph, routeArrivalPointSelection.getSecond(), routeArrivalPointSelection.getThird());

				//=============================================//

				final AStar aStar = new AStar(tmpGraph);
				List<Node> shortestPath = aStar.findShortestPath(startMapNode, arrivalMapNode);


				if (shortestPath != null) {
					List<MapNode> mapNodesInPath = shortestPath.stream().map(node -> ((MapNode) node)).collect(Collectors.toList());
					Path2D path = new Path2D.Double();
					Counter c = new Counter();

					path.moveTo(mapNodesInPath.get(0).getX(), mapNodesInPath.get(0).getY());

					mapNodesInPath.stream()
								  .filter(mapNode -> !mapNode.equals(mapNodesInPath.get(0)))
								  .forEach((MapNode mapNode) -> {

									  final int count = c.getCount();

									  final MapEdge edgeBetweenNodes = (MapEdge) tmpGraph.getEdgeBetweenNodes(mapNodesInPath.get(count), mapNodesInPath.get(count + 1));
									  final Vecteur[] pointsInEdge = edgeBetweenNodes.getPoints();

									  if (pointsInEdge != null && pointsInEdge.length != 0)
										  Arrays.stream(pointsInEdge).forEach(edgePoint -> path.lineTo(edgePoint.getX(), edgePoint.getY()));

									  c.increment();
								  });

					path.lineTo(mapNodesInPath.get(mapNodesInPath.size() - 1).getX(), mapNodesInPath.get(mapNodesInPath.size() - 1).getY());

					setPath(path);

				}else {
					JOptionPane.showMessageDialog(null, "Aucune route n'est possible entre les deux points!", "", JOptionPane.ERROR_MESSAGE);
					setStartPoint(null);
					setArrivalPoint(null);
					setPath(null);
				}

			}else {

				Path2D path = new Path2D.Double();
				path.moveTo(routeStartPointSelection.getSecond().getX(), routeStartPointSelection.getSecond().getY());
				path.lineTo(routeArrivalPointSelection.getSecond().getX(), routeArrivalPointSelection.getSecond().getY());

				setPath(path);
			}
		}
	}

	/**
	 * Retourne le chemin dessiné.
	 *
	 * @return le chemin dessiné
	 */
	public Path2D getPath() {
		return path;
	}

	/**
	 * Mets à jour le chemin dessiné.
	 *
	 * @param path le chemin dessiné
	 */
	public void setPath(Path2D path) {
		this.path = path;
	}

	/**
	 * Mets à jour le point de départ.
	 *
	 * @param startPoint le point de départ
	 */
	public void setStartPoint(Vecteur startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Retourne le point de départ.
	 *
	 * @return le point de départ
	 */
	public Vecteur getStartPoint() {
		return startPoint;
	}

	/**
	 * Retourne le point d'arrivée.
	 *
	 * @return le point d'arrivée
	 */
	public Vecteur getArrivalPoint() {
		return arrivalPoint;
	}

	/**
	 * Mets à jour le point d'arrivée.
	 *
	 * @param arrivalPoint le point d'arrivée
	 */
	public void setArrivalPoint(Vecteur arrivalPoint) {
		this.arrivalPoint = arrivalPoint;
	}

	/**
	 * Mets à jour la couleur du point de départ.
	 *
	 * @param c la couleur du point de départ
	 */
	public void setStartPointColor(Color c){
		this.startPointColor = c;
		startPointMapMarkImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(startPointMapMarkImg.getSource(), new UICommonUtils.ColorOverloadImageFilter(c)));
	}

	/**
	 * Mets à jour la couleur du point d'arrivée.
	 *
	 * @param c la couleur du point d'arrivée
	 */
	public void setArrivalPointColor(Color c){
		this.arrivalPointColor = c;
		arrivalPointMapMarkImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(arrivalPointMapMarkImg.getSource(), new UICommonUtils.ColorOverloadImageFilter(c)));
	}
}
