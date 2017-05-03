package ui.panneaux;

import math.chart.AbstractChartContent;
import ui.event.ChartViewportChangeListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Classe de dessin d'un graphique cartésien d'une fonction mathématique donnée, d'un nuage de points ou autre.
 *
 * @author Philippe Rivest
 */
public class ChartPane extends JPanel {

	private static final long serialVersionUID = -9212403670250295143L;

	private static final int AXIS_WIDTH = 3;
	private static final int UNITS_PIXEL_MOVE_PARALLEL = 2;
	private static final int UNITS_PIXEL_MOVE_PERPENDICULAR = 5;
	private final EventListenerList LISTENERS = new EventListenerList();

	private AbstractChartContent chartContent;

	private double xMin = -10.0;
	private double xMax = 10.0;
	private double yMin = -10.0;
	private double yMax = 10.0;

	private double stepX = 1.0;
	private double stepY = 1.0;

	private double pixelPerX, pixelPerY;

	private Path2D.Double grid;
	private Color colorGrid = Color.LIGHT_GRAY;

	private Path2D.Double axisX;
	private Color colorAxisX = Color.WHITE;
	private Path2D.Double axisY;
	private Color colorAxisY = Color.WHITE;

	private Color colorContent = Color.BLUE;

	private boolean gridEnabled = true;
	private boolean axisXEnabled = true;
	private boolean axisYEnabled = true;

	/**
	 * Retourne le minimum en x affiché.
	 *
	 * @return le minimum en x affiché
	 */
	public double getxMin() {
		return xMin;
	}

	/**
	 * Mets à jour le minimum en x affiché.
	 *
	 * @param xMin le minimum en x affiché
	 */
	public void setxMin(double xMin) {
		this.xMin = xMin;
		repaint();
	}

	/**
	 * Retourne le maximum en x affiché.
	 *
	 * @return le maximum en x affiché
	 */
	public double getxMax() {
		return xMax;
	}

	/**
	 * Mets à jour le maximum en x affiché.
	 *
	 * @param xMax le maximum en x affiché
	 */
	public void setxMax(double xMax) {
		this.xMax = xMax;
		repaint();
	}

	/**
	 * Retourne le miniumum en y affiché.
	 *
	 * @return le miniumum en y affiché
	 */
	public double getyMin() {
		return yMin;
	}

	/**
	 * Mets à jour le miniumum en y affiché.
	 *
	 * @param yMin le miniumum en y affiché
	 */
	public void setyMin(double yMin) {
		this.yMin = yMin;
		repaint();
	}

	/**
	 * Retourne le maximum en y affiché.
	 *
	 * @return le maximum en y affiché
	 */
	public double getyMax() {
		return yMax;
	}

	/**
	 * Mets à jour le maximum en y affiché.
	 *
	 * @param yMax le maximum en y affiché
	 */
	public void setyMax(double yMax) {
		this.yMax = yMax;
		repaint();
	}

	/**
	 * Retourne le pas de l'axe X.
	 *
	 * @return le pas de l'axe X
	 */
	public double getStepX() {
		return stepX;
	}

	/**
	 * Mets à jour le pas de l'axe X.
	 *
	 * @param stepX le pas de l'axe X
	 */
	public void setStepX(double stepX) {
		this.stepX = stepX;
		repaint();
	}

	/**
	 * Retourne le pas de l'axe Y.
	 *
	 * @return le pas de l'axe Y
	 */
	public double getStepY() {
		return stepY;
	}

	/**
	 * Mets à jour le pas de l'axe Y.
	 *
	 * @param stepY le pas de l'axe Y
	 */
	public void setStepY(double stepY) {
		this.stepY = stepY;
		repaint();
	}

	/**
	 * Retourne la couleur de la grille.
	 *
	 * @return la couleur de la grille
	 */
	public Color getColorGrid() {
		return colorGrid;
	}

	/**
	 * Mets à jour la couleur de la grille.
	 *
	 * @param colorGrid la couleur de la grille
	 */
	public void setColorGrid(Color colorGrid) {
		this.colorGrid = colorGrid;
		repaint();
	}

	/**
	 * Retourne la couleur de l'axe x.
	 *
	 * @return la couleur de l'axe x
	 */
	public Color getColorAxisX() {
		return colorAxisX;
	}

	/**
	 * Mets à jour la couleur de l'axe x.
	 *
	 * @param colorAxisX la couleur de l'axe x
	 */
	public void setColorAxisX(Color colorAxisX) {
		this.colorAxisX = colorAxisX;
		repaint();
	}

	/**
	 * Retourne la couleur de l'axe y.
	 *
	 * @return la couleur de l'axe y
	 */
	public Color getColorAxisY() {
		return colorAxisY;
	}

	/**
	 * Mets à jour la couleur de l'axe y.
	 *
	 * @param colorAxisY la couleur de l'axe y
	 */
	public void setColorAxisY(Color colorAxisY) {
		this.colorAxisY = colorAxisY;
		repaint();
	}

	/**
	 * Retourne la couleur du contenu du graphique.
	 *
	 * @return la couleur du contenu du graphique
	 */
	public Color getColorContent() {
		return colorContent;
	}

	/**
	 * Mets à jour la couleur du contenu du graphique.
	 *
	 * @param colorContent la couleur du contenu du graphique
	 */
	public void setColorContent(Color colorContent) {
		this.colorContent = colorContent;
		repaint();
	}

	/**
	 * Retourne le contenu affiché présentement.
	 * @return le contenu affiché
	 */
	public AbstractChartContent getChartContent() {
		return chartContent;
	}

	/**
	 * Mets à jour le contenu a afficher.
	 * @param chartContent le contenu a afficher
	 */
	public void setChartContent(AbstractChartContent chartContent) {
		this.chartContent = chartContent;

		ChartViewportSettings chartViewportSettings = chartContent.getChartViewportSettings();

		setStepX(chartViewportSettings.stepX);
		setStepY(chartViewportSettings.stepY);
		setxMin(chartViewportSettings.xMin - stepX);
		setxMax(chartViewportSettings.xMax + stepX);
		setyMin(chartViewportSettings.yMin - stepY);
		setyMax(chartViewportSettings.yMax + stepY);

		repaint();
		fireChartViewportWasChangedEvent();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//Transformations de mise en échelle
		g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
		g2d.scale(1, -1);

		this.pixelPerX = this.getWidth() / (xMax-xMin);
		this.pixelPerY = this.getHeight() / (yMax-yMin);

		//Translation de centrage
		g2d.translate(-pixelPerX * (xMax + xMin) / 2.0, -pixelPerY * (yMax + yMin) / 2.0);

		Color colorBkp = g2d.getColor();

		if (gridEnabled) {
			this.generateGrid();
			g2d.setColor(colorGrid);
			g2d.draw(grid);
		}

		Stroke g2dStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(AXIS_WIDTH));

		if (axisXEnabled) {
			this.generateAxisX();
			g2d.setColor(colorAxisX);
			g2d.draw(axisX);
		}

		if (axisYEnabled) {
			this.generateAxisY();
			g2d.setColor(colorAxisY);
			g2d.draw(axisY);
		}

		g2d.setStroke(g2dStroke);

		drawUnits(g2d);

		if (chartContent != null) {
			g2d.setColor(colorContent);
			chartContent.setTransform(g2d.getTransform());
			chartContent.setPixelPerX(pixelPerX);
			chartContent.setPixelPerY(pixelPerY);
			chartContent.dessiner(g2d);
		}

		g2d.setColor(colorBkp);
	}

	/**
	 * Dessine les unités sur le graphique.
	 * @param g2d le contexte de dessin du graphique
	 */
	private void drawUnits(Graphics2D g2d) {

		AffineTransform transform = g2d.getTransform();
		g2d.scale(1, -1);

		
		//Unitées en X
		for (int i = 0; i < xMax + stepX; i+= stepX) {
			g2d.drawString("" + i, Math.round(i * pixelPerX) + UNITS_PIXEL_MOVE_PARALLEL, -UNITS_PIXEL_MOVE_PERPENDICULAR);
		}

		for (int i = ((int) Math.round(-stepX)); i > xMin - stepX; i -= stepX) {
			g2d.drawString("" + i, Math.round(i * pixelPerX) + UNITS_PIXEL_MOVE_PARALLEL, -UNITS_PIXEL_MOVE_PERPENDICULAR);
		}

		//Unitées en Y
		for (int i = ((int) Math.round(stepY)); i < yMax + stepY; i+= stepY) {
			g2d.drawString("" + i, UNITS_PIXEL_MOVE_PERPENDICULAR, -(Math.round(i * pixelPerY) + UNITS_PIXEL_MOVE_PARALLEL));
		}

		for (int i = ((int) Math.round(-stepY)); i > yMin - stepY; i -= stepY) {
			g2d.drawString("" + i, UNITS_PIXEL_MOVE_PERPENDICULAR, -(Math.round(i * pixelPerY) + UNITS_PIXEL_MOVE_PARALLEL));
		}

		g2d.setTransform(transform);
	}

	/**
	 * Crée le Path2D.Double du grillage
	 */
	private void generateGrid() {

		grid = new Path2D.Double();

		if (stepX != 0d) {

			grid.moveTo(0, yMin * pixelPerY);
	
			for (int i = 0; i < Math.abs(xMax / stepX); i++) {
				grid.lineTo(grid.getCurrentPoint().getX(), yMax * pixelPerY);
				grid.moveTo(grid.getCurrentPoint().getX() + stepX * pixelPerX, yMin * pixelPerY);
			}
	
			grid.moveTo(0, yMin * pixelPerY);
	
			for (int i = 0; i < Math.abs(xMin / stepX); i++) {
				grid.lineTo(grid.getCurrentPoint().getX(), yMax * pixelPerY);
				grid.moveTo(grid.getCurrentPoint().getX() - stepX * pixelPerX, yMin * pixelPerY);
			}

		}

		if (stepY != 0d) {

			grid.moveTo(xMin  * pixelPerX, 0);
	
			for (int i = 0; i < Math.abs(yMax / stepY); i++) {
				grid.lineTo(xMax * pixelPerX, grid.getCurrentPoint().getY());
				grid.moveTo(xMin * pixelPerX, grid.getCurrentPoint().getY() + stepY * pixelPerY);
			}
	
			grid.moveTo(xMin  * pixelPerX, 0);
	
			for (int i = 0; i < Math.abs(yMin / stepY); i++) {
				grid.lineTo(xMax * pixelPerX, grid.getCurrentPoint().getY());
				grid.moveTo(xMin * pixelPerX, grid.getCurrentPoint().getY() - stepY * pixelPerY);
			}

		}
	}

	/**
	 * Crée le Path2D.Double de l'axe X.
	 */
	private void generateAxisX() {
		axisX = new Path2D.Double();
		axisX.moveTo(xMin * pixelPerX, 0);
		axisX.lineTo(xMax * pixelPerX, 0);
	}

	/**
	 * Crée le Path2D.Double de l'axe Y.
	 */
	private void generateAxisY() {
		axisY = new Path2D.Double();
		axisY.moveTo(0, yMin * pixelPerY);
		axisY.lineTo(0, yMax * pixelPerY);
	}

	/**
	 * Détermine l'affichage de la grille.
	 * @param gridEnabled Vrai si affiché ; faux sinon
	 */
	public void setGridEnabled(boolean gridEnabled) {
		this.gridEnabled = gridEnabled;
		repaint();
	}

	/**
	 * Retourne l'état d'affichage de la grille.
	 * @return Vrai si affiché ; faux sinon
	 */
	public boolean isGridEnabled() {
		return gridEnabled;
	}

	/**
	 * Détermine l'affichage de l'axe X.
	 * @param axisXEnabled Vrai si affiché ; faux sinon
	 */
	public void setAxisXEnabled(boolean axisXEnabled) {
		this.axisXEnabled = axisXEnabled;
		repaint();
	}

	/**
	 * Retourne l'état d'affichage de l'axe X.
	 * @return Vrai si affiché ; faux sinon
	 */
	public boolean isAxisXEnabled() {
		return axisXEnabled;
	}

	/**
	 * Détermine l'affichage de l'axe Y.
	 * @param axisYEnabled Vrai si affiché ; faux sinon
	 */
	public void setAxisYEnabled(boolean axisYEnabled) {
		this.axisYEnabled = axisYEnabled;
		repaint();
	}

	/**
	 * Retourne l'état d'affichage de l'axe X.
	 * @return Vrai si affiché ; faux sinon
	 */
	public boolean isAxisYEnabled() {
		return axisYEnabled;
	}

	/**
	 * Structure de données contenant les différents réglages d'affichage du graphique.
	 */
	public static class ChartViewportSettings{

		private static final double DEFAULT_LINE_COUNT = 10;
		private double xMin, xMax, yMin, yMax, stepX, stepY;

		/**
		 * Instancies un nouveau ChartViewportSettings.
		 *
		 * @param xMin La valeur minimale de l'axe X
		 * @param xMax La valeur maximale de l'axe X
		 * @param yMin La valeur minimale de l'axe Y
		 * @param yMax La valeur maximale de l'axe Y
		 */
		public ChartViewportSettings(double xMin, double xMax, double yMin, double yMax) {
			this.xMin = xMin;
			this.xMax = xMax;
			this.yMin = yMin;
			this.yMax = yMax;
			this.stepX = Math.ceil((xMax - xMin) / DEFAULT_LINE_COUNT);
			this.stepY = Math.ceil((yMax - yMin) / DEFAULT_LINE_COUNT);
		}
	}

	/**
	 * Ajoute un écouteur qui répond aux chagement automatique de zoom du panneau.
	 * @param listener l'écouteur à ajouter.
	 */
	public void addChartViewportChangeListener(ChartViewportChangeListener listener){
		LISTENERS.add(ChartViewportChangeListener.class, listener);
	}

	/**
	 * Envoye un évènement de type {@link ChartViewportChangeListener#chartViewportWasChanged()} aux écouteurs.
	 */
	private void fireChartViewportWasChangedEvent(){
		for(ChartViewportChangeListener listener : LISTENERS.getListeners(ChartViewportChangeListener.class))
			listener.chartViewportWasChanged();
	}
}
