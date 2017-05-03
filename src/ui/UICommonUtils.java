package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Classe utilitaire contenant des méthodes communes aux différents composants graphiques.
 *
 * @author Philippe Rivest
 */
public final class UICommonUtils {

	public static final String PATH_IMG_AMNAM_LOGO_URL = "/img/AMNAM-LOGO-S.png";
	public static final String PATH_IMG_VEHICULE = "/img/auto/";

	public static final String PATH_IMG_EVENT_BASE = "/img/events/analysis/event.png";
	public static final String PATH_IMG_OBSTACLE = "/img/events/obstacles/obstacle_white.png";
	public static final String PATH_IMG_EVENT_VEHICLES = "/img/events/vehicles/";


	public static final String PATH_IMG_HOURGLASS_END = "/img/icons/hourglass_end.png";
	public static final String PATH_IMG_HOURGLASS_START = "/img/icons/hourglass_start.png";
	public static final String PATH_IMG_MAP_MARKER = "/img/icons/map_marker.png";


	private static final double BUTTON_ICON_SIZE_RATIO = 0.4;
	public static final String PATH_BUTTON_ICON_PLAY = "/img/button_icons/play.png";
	public static final String PATH_BUTTON_ICON_PAUSE = "/img/button_icons/pause.png";
	public static final String PATH_BUTTON_ICON_ZOOM_IN = "/img/button_icons/zoom_in.png";
	public static final String PATH_BUTTON_ICON_ZOOM_OUT = "/img/button_icons/zoom_out.png";
	public static final String PATH_BUTTON_ICON_SUPERPOSITIONS = "/img/button_icons/superpositions.png";
	public static final String PATH_BUTTON_ICON_NEW_ROUTE = "/img/button_icons/route.png";
	public static final String PATH_BUTTON_ICON_STATS = "/img/button_icons/stats.png";
	public static final String PATH_BUTTON_ICON_CENTER = "/img/button_icons/center.png";
	public static final String PATH_BUTTON_ICON_X = "/img/button_icons/x.png";
	public static final String PATH_BUTTON_ICON_UP = "/img/button_icons/up.png";
	public static final String PATH_BUTTON_ICON_DOWN = "/img/button_icons/down.png";
	public static final String PATH_BUTTON_ICON_COGS = "/img/button_icons/cogs.png";

	public static final String PATH_MARIACHI_MUSIC = "/audio/mariachi.wav";

	public static final String PATH_HTML_HOME = "http://lrima.cmaisonneuve.qc.ca/amnam/index.html";
	public static final String PATH_HTML_A_PROPOS = "http://lrima.cmaisonneuve.qc.ca/amnam/index.html";
	public static final String PATH_HTML_GUIDE = "http://lrima.cmaisonneuve.qc.ca/amnam/guide.html";
	public static final String PATH_HTML_SCI_CONCEPTS = "http://lrima.cmaisonneuve.qc.ca/amnam/concepts_sci.html";

	public static File lastFileChooserDirectory = new File(System.getProperty("user.dir")); //Cette initialisation sauve notre santé mentale

	/**
	 * Fait la transition entre fenêtres en chachant la première et en faisant apparaitre la deuxième.
	 *
	 * @param frameCourrant Le fenêtre à faire disparaitre
	 * @param prochainFrame La fenêtre à faire apparaitre
	 */
	public static void transfererEtape(Frame frameCourrant, Frame prochainFrame){
		prochainFrame.setVisible(true);
		frameCourrant.dispose();
	}


	/**
	 * Remplace le texte d'un {@link JButton} par une icône.
	 *
	 * @param button le bouton
	 * @param img    le chemin d'accès vers l'icône
	 */
	public static void addIconToButton(JButton button, String img) {

		if (button.getHeight() != 0) {
			Image loadedImage = null;
			URL urlImage = UICommonUtils.class.getResource(img);

			if (urlImage != null) {

				try {
					loadedImage = ImageIO.read(urlImage);
				} catch (IOException e) {
					System.out.println("Image not found: " + img);
				}

				double loadedImageWidth = loadedImage.getWidth(null);
				double loadedImageHeight = loadedImage.getHeight(null);

				double ratioWH = loadedImageWidth / loadedImageHeight;

				double resizedHeight = button.getHeight() * BUTTON_ICON_SIZE_RATIO;
				double resizedWidth = resizedHeight * ratioWH;

				Image imgRedim = loadedImage.getScaledInstance(((int) Math.round(resizedWidth)), ((int) Math.round(resizedHeight)), Image.SCALE_SMOOTH);

				button.setIcon(new ImageIcon(imgRedim));
				button.setText("");

				loadedImage.flush();
				imgRedim.flush();
			}else
				System.out.println("Image not found: " + img);
		}
	}

	/**
	 * Remplace le texte d'une {@link JLabel} par une icône.
	 *
	 * @param label l'étiquette
	 * @param img   le chemin d'accès vers l'icône
	 */
	public static void addIconToLabel(JLabel label, String img){
		try {
			addIconToLabel(label, ImageIO.read(UICommonUtils.class.getResource(img)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remplace le texte d'une {@link JLabel} par une icône.
	 *
	 * @param label l'étiquette
	 * @param img   l'image à afficher
	 */
	public static void addIconToLabel(JLabel label, Image img){
		Image scaledInstance = img.getScaledInstance((int)(label.getWidth() * ((double) img.getWidth(null) / img.getHeight(null))), label.getHeight(), Image.SCALE_SMOOTH);
		label.setIcon(new ImageIcon(scaledInstance));
	}

	/**
	 * Ajuste la taille de police d'une étiquette pour maximiser l'espace disponible.
	 *
	 * @param label       L'étiquette à modifier
	 * @param maxFontSize La taille maximale de police permise
	 */
	public static void fitTextToLabel(JLabel label, int maxFontSize) {
		Font fontLabel = label.getFont();
		String labelText = label.getText();

		int textWidth = label.getFontMetrics(fontLabel).stringWidth(labelText);
		int labelWidth = label.getWidth();

		if (labelWidth != 0) {

			double widthRatio = (double)labelWidth / (double)textWidth;

			if (widthRatio <= 1) {
				float fittedFontSize = (float) (fontLabel.getSize() * widthRatio - 1);

				if (fittedFontSize <= maxFontSize)
					label.setFont(fontLabel.deriveFont(fittedFontSize));
			}
		}
	}

	/**
	 * Divise un {@link Path2D} en plusieurs autres petits {@link Path2D}.
	 *
	 * @see PathIterator
	 * @param path Le {@link Path2D} à séparer.
	 * @return Un tableau de petits {@link Path2D}
	 */
	public static Path2D[] splitPathInSegments(Path2D path) {

		Path2D[] paths;

		ArrayList<double[]> coordsList = new ArrayList<>();

		PathIterator pi = path.getPathIterator(new AffineTransform());

		while (!pi.isDone()) {

			double[] coords = new double[2];
			int type = pi.currentSegment(coords);

			if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO)
				coordsList.add(coords);

			pi.next();
		}

		paths = new Path2D[coordsList.size() - 1];

		for (int i = 0; i < coordsList.size() - 1; i++) {
			Path2D.Double pathSegment = new Path2D.Double();
			pathSegment.moveTo(coordsList.get(i)[0], coordsList.get(i)[1]);
			pathSegment.lineTo(coordsList.get(i + 1)[0], coordsList.get(i + 1)[1]);
			paths[i] = pathSegment;
		}

		return paths;

	}

	/**
	 * Une extension de {@link RGBImageFilter} écrasant la couleur originale tout en conservant la valeur d'alpha.
	 *
	 * @see java.awt.image.ImageFilter
	 * @see java.awt.image.RGBImageFilter
	 */
	public static class ColorOverloadImageFilter extends RGBImageFilter {

		Color c;

		/**
		 * Instancies un nouveau ColorOverloadImageFilter avec la couleur de remplacement.
		 *
		 * @param c la couleur de remplacement
		 */
		public ColorOverloadImageFilter(Color c) {
			this.c = c;
		}

		@Override
		public int filterRGB(int x, int y, int rgb) {
			return c.getRGB() & 0x00ffffff | rgb & 0xff000000;
		}
	}

}
