package ui.dessin;

import io.csv.config.ConfigurationPeriode.InfoPeriode;
import io.csv.config.ConfigurationPeriode.LignePeriode;
import math.Vecteur;
import ui.UICommonUtils;
import ui.panneaux.inspection.InspectionHeaderPanel;
import utils.inspection.Inspectable;
import utils.inspection.InspectionElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * 
 * Un véhicule est un composant graphique représentant un véhicule de la simulation.
 * @author Pascal Dally-Bélanger
 */
@Inspectable(
		name = "V\u00e9hicule",
		groups = {"Informations g\u00e9n\u00e9rales", "Information invisible"},
		header = InspectionHeaderPanel.class,
		args = {
				"Information invisible", "Image",
				"Informations g\u00e9n\u00e9rales", "ID",
				"Informations g\u00e9n\u00e9rales", "Type"
				}
		)
public class Vehicule implements Selectable {
	
	private AffineTransform transform;
	
	private InfoPeriode info;
	private LignePeriode ligne;
	private String type;
	private int id;
	private boolean dsrc;
	private static final double SCALE_DEFAUT = 1/500d;
	
	public static final HashMap<String, Image> IMAGES = new HashMap<String, Image>();
	private static final String[] NAMES = {
			"audi_a5_2007_blue.png",
			"c_audi_a3.png",
			"c_caravelle.png",
			"c_golf.png",
			"h100r_1a.png",
			"interactive.png",
			"motorbike.png",
			"volvo_v70_2007_blue.png",
			"volvo_v70_2007_sand.png"
	};
	
	static {
		for(int i = 0; i < NAMES.length; i++) {
			String file = UICommonUtils.PATH_IMG_VEHICULE + NAMES[i];
			Image img = null;
			try {
				img = ImageIO.read(Vehicule.class.getResource(file));
			} catch (IOException | IllegalArgumentException e) {
				e.printStackTrace();
			}
			IMAGES.put(new File(file).getName().replaceAll("\\..*", ""), img);
		}
	}
	
	/**
	 * Initialise un nouveau véhicule.
	 *
	 * @param type Le type du véhicule
	 * @param info Les informations contenus dans le fichier périodique du véhicule (.csv).
	 * @param dsrc Vrai, si le véhicule est dsrc, faux sinon.
	 * @param id L'ID du véhicule, spécifier dans le fichier d'initialisation (.csv) et dans son fichier périodique (.csv).
	 */
	public Vehicule(String type, InfoPeriode info, boolean dsrc, int id) {
		this.type = type;
		this.info = info;
		this.dsrc = dsrc;
		this.id = id;
	}
	
	@Override
	public void dessiner(Graphics2D g2d) {
		Image img = IMAGES.get(type);
		AffineTransform backup = g2d.getTransform();
		g2d.transform(transform);
		g2d.translate(ligne.getPositionX(), ligne.getPositionY());
		g2d.rotate(ligne.getOrientationZ());
		g2d.scale(SCALE_DEFAUT, SCALE_DEFAUT);
		g2d.drawImage(img, -img.getWidth(null)/2, -img.getHeight(null)/2, img.getWidth(null), img.getHeight(null), null);
		g2d.setTransform(backup);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s", type, id, dsrc);
	}
	
	/**
	 * Retourne la position du véhicule comme vecteur.
	 * @param t Le temps à laquelle la position est évalué.
	 * @return la position du véhicule.
	 */
	public Vecteur getPosition(double t) {
		return new Vecteur(ligne.getPositionX(), ligne.getPositionY());
	}
	
	/**
	 * Retourne l'orientation du véhicule comme vecteur.
	 * @param t Le temps à laquelle l'orientation est évalué.
	 * @return l'orientation du véhicule.
	 */
	public double getOrientation(double t) {
		return ligne.getOrientationZ();
	}
	
	/**
	 * Modifie le temps auquel le véhicule est rendu (position et orientation).
	 * @param t le temps auquel le véhicule est rendu.
	 */
	public void setTemps(double t) {
		ligne = info.interpolation(t);
	}

	/**
	 * Retourne vrai si le véhicule est DSRC.
	 * @return Vrai si le véhicule est DSRC.
	 */
	public boolean isDSRC() {
		return dsrc;
	}

	/**
	 * Retourne "Vrai" ou "Faux" si le véhicule est DSRC.
	 * @return "Vrai" si le véhicule est DSRC, "Faux" sinon.
	 */
	@InspectionElement(name = "Capacité DSRC", group = "Informations g\u00e9n\u00e9rales")
	public String isDSRCString() {
		return dsrc ? "Vrai" : "Faux";
	}

	/**
	 * Retourne l'ID du véhicule.
	 * @return L'ID du véhicule.
	 */
	@InspectionElement(name = "ID", group = "Informations g\u00e9n\u00e9rales")
	public int getID() {
		return id;
	}

	/**
	 * Retourne la ligne actuelle, c'est à dire l'ensemble des différentes informations du véhicules.
	 * @return La ligne actuelle.
	 */
	@InspectionElement(group = "Informations g\u00e9n\u00e9rales")
	public LignePeriode getLigne() {
		return ligne;
	}

	/**
	 * Retourne le type du véhicule.
	 * @return Le type du véhicule.
	 */
	@InspectionElement(name = "Type", group = "Informations g\u00e9n\u00e9rales")
	public String getType() {
		return type;
	}

	/**
	 * Retourne l'image du véhicule.
	 *
	 * @return l'image du véhicule
	 */
	@InspectionElement(name = "Image", group = "Information invisible", displayed = false)
	public Image getImage() {
		return IMAGES.get(type);
	}
	

	@Override
	public Shape getShape() {
		AffineTransform transform = new AffineTransform(this.transform);
		transform.translate(ligne.getPositionX(), ligne.getPositionY());
		transform.rotate(ligne.getOrientationZ());
		transform.scale(SCALE_DEFAUT, SCALE_DEFAUT);
		Image img = IMAGES.get(type);
		return transform.createTransformedShape(new Rectangle2D.Double(-img.getWidth(null)/2, -img.getHeight(null)/2, img.getWidth(null), img.getHeight(null)));
	}

	@Override
	public Vecteur getPosition() {
		return new Vecteur(ligne.getPositionX(), ligne.getPositionY());
	}
}