package math.chart;

import ui.dessin.Dessinable;

import java.awt.geom.AffineTransform;

import static ui.panneaux.ChartPane.ChartViewportSettings;

/**
 * Classe abstraite donnant le squelette de base pour tout contenu souhaitant s'afficher sur un {@link ui.panneaux.ChartPane}.
 *
 * @author Philippe Rivest
 * @see ui.panneaux.ChartPane
 */
public abstract class AbstractChartContent implements Dessinable {

	private String name;
	private boolean isContinuous;

	private ChartViewportSettings chartViewportSettings;

	private double pixelPerX, pixelPerY;
	private AffineTransform transform;


	/**
	 * Mets à jour les réglages de base de zoom sur le contenu.
	 *
	 * @param chartViewportSettings les réglages de base de zoom
	 */
	void setChartViewportSettings(ChartViewportSettings chartViewportSettings){
		this.chartViewportSettings = chartViewportSettings;
	}

	/**
	 * Retourne les réglages de base de zoom sur le contenu.
	 *
	 * @return les réglages de base de zoom
	 */
	public ChartViewportSettings getChartViewportSettings(){
		return chartViewportSettings;
	}

	/**
	 * Retourne vrai si le contenu est continu.
	 *
	 * @return vrai si le contenu est continu ; faux si discret
	 */
	public boolean isContinuous() {
		return isContinuous;
	}

	/**
	 * Détermine si le contenu est continu ou discret.
	 *
	 * @param continuous vrai si le contenu est continu ; faux si discret
	 */
	public void setContinuous(boolean continuous) {
		isContinuous = continuous;
	}

	/**
	 * Retourne le nom du contenu.
	 *
	 * @return le nom du contenu
	 */
	public String getName() {
		return name;
	}

	/**
	 * Mets à jour le nom du contenu.
	 *
	 * @param name le nom du contenu
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retourne le nombre de pixels par unité x.
	 *
	 * @return le nombre de pixels par unité x
	 */
	public double getPixelPerX() {
		return pixelPerX;
	}

	/**
	 * Mets à jour le nombre de pixels par unité x.
	 *
	 * @param pixelPerX le nombre de pixels par unité x
	 */
	public void setPixelPerX(double pixelPerX) {
		this.pixelPerX = pixelPerX;
	}

	/**
	 * Retourne le nombre de pixels par unité y.
	 *
	 * @return le nombre de pixels par unité y
	 */
	public double getPixelPerY() {
		return pixelPerY;
	}

	/**
	 * Mets à jour le nombre de pixels par unité y.
	 *
	 * @param pixelPerY le nombre de pixels par unité y
	 */
	public void setPixelPerY(double pixelPerY) {
		this.pixelPerY = pixelPerY;
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}

	/**
	 * Retourne la matrice de transformation.
	 * @return la matrice de transformation
	 */
	public AffineTransform getTransform() {
		return transform;
	}

	@Override
	public String toString() {
		return getName();
	}
}
