package scenario.events;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import io.amnam.EtatsFichiersAMNAM;
import math.Vecteur;
import ui.UICommonUtils;
import ui.dessin.Clickable;
import ui.panneaux.inspection.InspectionHeaderPanel;
import utils.inspection.Inspectable;
import utils.inspection.InspectionElement;

/**
 * Un Event est créé durant l'étape de l'analyse.
 * C'est le fruit des algorithmes de détection.
 *
 * @see EtatsFichiersAMNAM#ANALYSE
 * @author Philippe Rivest
 */
@Inspectable(
		name = "Évènement",
		groups = {"Informations", "Informations invisibles"},
		header = InspectionHeaderPanel.class,
		args = {
				"Informations invisibles", "Image",
				"Informations", "Type",
				"Informations", "Type",
		}
)
public class Event implements Clickable {

	private transient static final double SCALE_DEFAUT = 1/50d;

	private Vecteur position;
	private EventType eventType;

	private double certainty = 1;
	private double detectionTime;
	private double expirationTime = Double.MAX_VALUE;

	private transient AffineTransform transform;
	
	private static final HashMap<EventType, Image> IMAGES = new HashMap<EventType, Image>();
	
	static {
		try {
			IMAGES.put(null, ImageIO.read(Event.class.getResource(UICommonUtils.PATH_IMG_EVENT_BASE)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instancies un nouvel Event.
	 *
	 * @param position      La position de l'évènement
	 * @param eventType     le type d'évènement
	 * @param detectionTime le temps où l'évènement a été détecté
	 */
	public Event(Vecteur position, EventType eventType, double detectionTime) {
		this.position = position;
		this.eventType = eventType;
		this.detectionTime = detectionTime;
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		AffineTransform backup = g2d.getTransform();

		g2d.transform(transform);
		g2d.translate(getPosition().getX(), getPosition().getY());
		g2d.scale(SCALE_DEFAUT, -SCALE_DEFAUT);
		Image img = IMAGES.get(eventType);
		if(img == null) {
			IMAGES.put(eventType, Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(IMAGES.get(null).getSource(), new UICommonUtils.ColorOverloadImageFilter(eventType.getEventColor()))));
			img = IMAGES.get(eventType);
		}
		g2d.translate(-img.getWidth(null)/2, -img.getHeight(null)/2);
		g2d.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		g2d.setTransform(backup);
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}

	@Override
	public Shape getShape() {
		AffineTransform transform = new AffineTransform(this.transform);
		transform.translate(getPosition().getX(), getPosition().getY());
		transform.scale(SCALE_DEFAUT, SCALE_DEFAUT);
		Image img = IMAGES.get(eventType);
		transform.translate(-img.getWidth(null)/2, -img.getHeight(null)/2);
		return transform.createTransformedShape(new Ellipse2D.Double(0, 0, img.getWidth(null), img.getHeight(null)));
	}

	/**
	 * Retourne la position de l'évènement.
	 *
	 * @return la position de l'évènement
	 */
	@InspectionElement(name = "Position", group = "Informations")
	public Vecteur getPosition() {
		return position;
	}

	/**
	 * Retourne l'image de l'évènement.
	 *
	 * @return l'image de l'évènement
	 */
	@InspectionElement(name = "Image", group = "Informations invisibles", displayed = false)
	public Image getImage() {
		return IMAGES.get(eventType);
	}
	
	/**
	 * Mets à jour la position de l'évènement.
	 *
	 * @param position la position de l'évènement
	 */
	public void setPosition(Vecteur position) {
		this.position = position;
	}

	/**
	 * Retourne le type d'évènement.
	 *
	 * @return le type d'évènement
	 */
	@InspectionElement(name = "Type", group = "Informations")
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * Retourne le temps de détection de l'évènement.
	 *
	 * @return le temps de détection de l'évènement
	 */
	public double getDetectionTime() {
		return detectionTime;
	}

	/**
	 * Retourne le temps de détection de l'évènement en millisecondes.
	 *
	 * @return le temps de détection de l'évènement en millisecondes
	 */
	@InspectionElement(name = "Temps de détection", group = "Informations", format = "%tM:%<tS:%<tL")
	public long getDetectionTimeMillis() {
		return (long) (detectionTime*1000);
	}
	
	/**
	 * Retourne le temps de d'expiration de l'évènement.
	 *
	 * @return le temps de d'expiration de l'évènement
	 */
	public double getExpirationTime() {
		return expirationTime;
	}

	/**
	 * Retourne le temps de d'expiration de l'évènement en millisecondes.
	 *
	 * @return le temps de d'expiration de l'évènement en millisecondes
	 */
	@InspectionElement(name = "Temps d'expiration", group = "Informations", format = "%tM:%<tS:%<tL")
	public long getExpirationTimeMillis() {
		return (long) (expirationTime*1000);
	}

	/**
	 * Mets à jour le temps de d'expiration de l'évènement.
	 *
	 * @param expirationTime le temps de d'expiration de l'évènement
	 */
	public void setExpirationTime(double expirationTime) {
		this.expirationTime = expirationTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Event))
			return false;

		Event event = (Event) o;

		if (Double.compare(event.getDetectionTime(), getDetectionTime()) != 0)
			return false;
		if (Double.compare(event.getExpirationTime(), getExpirationTime()) != 0)
			return false;
		if (!getPosition().equals(event.getPosition()))
			return false;
		return getEventType() == event.getEventType();
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = getPosition().hashCode();
		result = 31 * result + getEventType().hashCode();
		temp = Double.doubleToLongBits(getDetectionTime());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(getExpirationTime());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	/**
	 * Retourne le poids de l'évènement, pondéré en fonction de la certitude.
	 * @return Le poids de l'évènement, pondéré en fonction de la certitude.
	 */
	public double getWeight() {
		return certainty * eventType.getWeight();
	}
	
	/**
	 * Retourne vrai si l'évènement est actif au temps t.
	 * @param t le temps à évaluer.
	 * @return Vrai si l'évènement est actif.
	 */
	public boolean isActive(double t) {
		return detectionTime < t && t < expirationTime;
	}
}
