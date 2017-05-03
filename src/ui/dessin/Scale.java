package ui.dessin;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * L'échelle d'une carte.
 * @author Pascal Dally-Bélanger
 *
 */
public class Scale implements Dessinable {

	private static final ScaleInfo[] INFO;
	
	static {
		ScaleInfo[] temp = new ScaleInfo[] {
				new ScaleInfo("2m", 2),
				new ScaleInfo("5m", 5),
				new ScaleInfo("10m", 10),
				new ScaleInfo("20m", 20),
				new ScaleInfo("50m", 50),
				new ScaleInfo("100m", 100),
				new ScaleInfo("200m", 200),
				new ScaleInfo("500m", 500),
				new ScaleInfo("1km", 1000),
		};
		INFO = temp;
	}
	
	private AffineTransform transform;
	private int indice = INFO.length/2;
	private double length = 100;
	private double min = 0;
	private double max = Double.POSITIVE_INFINITY;
	private Point2D p1;
	private Point2D p2;
	
	/**
	 * Initialise une échelle avec une longueur minimale et maximale.
	 * @param min La longueur minimale de l'échelle
	 * @param max La longueur maximale de l'échelle
	 */
	public Scale(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Ajuste l'échelle pour respecter le minimum et le maximum établi.
	 */
	private void adjust() {
		p1 = transform.transform(new Point2D.Double(0, 0), null);
		p2 = transform.transform(new Point2D.Double(INFO[indice].length, 0), null);
		length = p2.getX() - p1.getX();
		while(min > length || max < length) {
			if(min > length)
				indice++;
			else
				indice--;
			p2 = transform.transform(new Point2D.Double(INFO[indice].length, 0), null);
			length = p2.getX() - p1.getX();
		}
	}
	
	/**
	 * Un ScaleInfo est une valeur que peut prendre une échelle.
	 * @author Pascal Dally-Bélanger
	 */
	private static class ScaleInfo {
		private String name;
		private double length;
		
		/**
		 * Initialise un nouveau ScaleInfo.
		 *
		 * @param name Le nom de l'info.
		 * @param length La longueur de l'info.
		 */
		public ScaleInfo(String name, double length) {
			this.name = name;
			this.length = length;
		}
	}
	
	/**
	 * Dessine l'objet sur le contexte graphique à la position spécifiée.
	 * @param g2d Le contexte graphique.
	 * @param x La position en x.
	 * @param y La position en y.
	 */
	public void dessiner(Graphics2D g2d, int x, int y) {
		try {
			adjust();
		} catch(Exception e) {
			indice = 0;
		}
		g2d.setStroke(new BasicStroke(3f));
		g2d.drawLine(x, y, (int) (x + length), y);
		g2d.setStroke(new BasicStroke(1f));
		try {
			g2d.drawString(INFO[indice].name, x, y + 15);
		} catch(Exception e) {
			g2d.drawString("out of bounds", x, y + 15);
		}
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		dessiner(g2d, 0, 0);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}
	
}
