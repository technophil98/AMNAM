package math.chart;

import ui.panneaux.ChartPane.ChartViewportSettings;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Map;

/**
 * Implémentation du nuage de point en suivant les standards requis pour l'affichage dans un {@link ui.panneaux.ChartPane}.
 *
 * @author Philippe Rivest
 * @see ui.panneaux.ChartPane
 */
public class ChartScatterPlot extends AbstractChartContent {

	private final Map<Double, Double> points;
	private Path2D.Double brokenLine;

	/**
	 * Instantiates un nouveau ChartScatterPlot.
	 *
	 * @param name       Le nom du nuage de points
	 * @param points     Une {@link Map} contenant des couples (x,y) où les clés sont les valeurs en x et les valeurs sont les valeurs en y
	 * @param continuous Vrai si les points doivent être reliés par une droite brisée
	 */
	public ChartScatterPlot(String name, Map<Double , Double> points, boolean continuous) {

		this.points = points;

		double minX = Double.MAX_VALUE;
		for (Double aDouble : points.keySet())
			minX = Math.min(aDouble, minX);

		double maxX = Double.MIN_VALUE;
		for (Double aDouble : points.keySet())
			maxX = Math.max(aDouble, maxX);


		double minY = Double.MAX_VALUE;
		for (Double aDouble : points.values())
			minY = Math.min(aDouble, minY);

		double maxY = Double.MIN_VALUE;
		for (Double aDouble : points.values())
			maxY = Math.max(aDouble, maxY);

		setName(name);
		setContinuous(continuous);
		setChartViewportSettings(new ChartViewportSettings(Math.floor(minX), Math.ceil(maxX), Math.floor(minY), Math.ceil(maxY)));
	}

	@Override
	public void dessiner(Graphics2D g2d) {

		for (Map.Entry<Double, Double> entry : points.entrySet()) {
			Ellipse2D.Double point = new Ellipse2D.Double((entry.getKey() * getPixelPerX()) - 5, (entry.getValue() * getPixelPerY()) - 5, 10, 10);
			g2d.fill(point);
		}

		if (isContinuous()) {

			brokenLine = new Path2D.Double();

			double minX = Double.MAX_VALUE;
			for (Double aDouble : points.keySet())
				minX = Math.min(aDouble, minX);

			brokenLine.moveTo(minX * getPixelPerX(), points.get(minX) * getPixelPerY());
			points.forEach((key, value) -> brokenLine.lineTo(key * getPixelPerX(), value * getPixelPerY()));
			g2d.draw(brokenLine);
		}
	}
}
