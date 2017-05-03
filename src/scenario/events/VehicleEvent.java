package scenario.events;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import math.Vecteur;
import ui.UICommonUtils;
import ui.dessin.Clickable;
import ui.panneaux.inspection.InspectionHeaderPanel;
import utils.inspection.Inspectable;
import utils.inspection.InspectionElement;

/**
 * Un VehicleEvent est un regrouppement d'évènement semblable levé par un véhicule durant la simulation.
 * @author Pascal Dally-Bélanger
 */
@Inspectable(
		name = "Évènement Véhiculaire",
		groups = {"Informations g\u00e9n\u00e9rales", "Position", "Temps de détection", "Informations invisibles"},
		header = InspectionHeaderPanel.class,
		args = {
				"Informations invisibles", "Image",
				"Informations g\u00e9n\u00e9rales", "Type",
				"Informations invisibles", "Source",
		}
)
public class VehicleEvent implements Clickable {

	private static final double DURATION = Double.MAX_VALUE;
	private ArrayList<Vecteur> positions = new ArrayList<Vecteur>();
	private EventType eventType;
	private ArrayList<Double> timestamps = new ArrayList<Double>();
	private transient AffineTransform transform;
	private int source;
	private transient double time;
	

	private static final double SCALE_DEFAUT = 1/200d;
	private static final HashMap<EventType, HashMap<Integer, Image>> IMAGES = new HashMap<EventType, HashMap<Integer, Image>>();
	private static final String[] NAMES = {
			"blockage.png",
			"pothole.png",
			"variator.png"
	};
	
	static {
		for(int i = 0; i < NAMES.length; i++) {
			Image img = null;
			String file = UICommonUtils.PATH_IMG_EVENT_VEHICLES + NAMES[i];
			try {
				img = ImageIO.read(VehicleEvent.class.getResource(file));
			} catch (IOException | IllegalArgumentException e) {
				e.printStackTrace();
			}
			EventType type = EventType.valueOf(new File(file).getName().replaceAll("\\..*", "").toUpperCase());
			IMAGES.put(type, new HashMap<Integer, Image>());
			IMAGES.get(type).put(null, img);
		}
	}
	
	/**
	 * Crée un nouveau VehicleEvent.
	 *
	 * @param source l'ID du véhicule qui lève l'évènement.
	 * @param position La position de l'évènement.
	 * @param eventType Le type de l'évènement.
	 * @param timestamp le temps auquel l'évènement est levé.
	 */
	public VehicleEvent(int source, Vecteur position, EventType eventType, double timestamp) {
		positions.add(position);
		this.eventType = eventType;
		this.timestamps.add(timestamp);
		this.source = source;
		new Line2D.Double();
	}
	
	/**
	 * Retourne la position moynene de l'évènement.
	 * @return la position moynene de l'évènement.
	 */
	@InspectionElement(name = "Moyenne", group = "Position")
	public Vecteur getPosition() {
		Vecteur temp = new Vecteur(0, 0);
		for(Vecteur v : positions)
			temp = temp.plus(v);
		return temp.div(positions.size());
	}

	/**
	 * Retourne la dernière position de l'évènement.
	 * @return La dernière position de l'évènement.
	 */
	@InspectionElement(name = "Dernière", group = "Position")
	public Vecteur getLastPosition() {
		return positions.get(positions.size() - 1);
	}

	/**
	 * Retourne le type de l'évènement.
	 * @return Le type de l'évènement.
	 */
	@InspectionElement(name = "Type", group = "Informations g\u00e9n\u00e9rales")
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * Retourne le temps de détection moyenne de l'évènement en secondes.
	 * @return Le temps de détection moyenne de l'évènement en secondes.
	 */
	public double getMeanTimestamp() {
		double temp = 0;
		for(Double t : timestamps)
			temp += t;
		return temp/timestamps.size();
	}
	
	/**
	 * Retourne l'image du véhicule.
	 * @return l'image du véhicule
	 */
	@InspectionElement(group = "Informations invisibles", name = "Image", displayed = false)
	public Image getImage() {
		return IMAGES.get(eventType).get(source);
	}


	/**
	 * Retourne le temps de détection moyenne de l'évènement en millisecondes.
	 * @return Le temps de détection moyenne de l'évènement en millisecondes.
	 */
	@InspectionElement(name = "Moyenne", group = "Temps de détection", format = "%tM:%<tS:%<tL")
	public long getMeanTimestampMillis(){
		return (long) (getMeanTimestamp() * 1000);
	}

	/**
	 * Retourne le premier temps de détection de l'évènement en secondes.
	 * @return Le premier temps de détection de l'évènement en secondes.
	 */
	public double getFirstTimestamp() {
		return timestamps.get(0);
	}

	/**
	 * Retourne le premier temps de détection de l'évènement en millisecondes.
	 * @return Le premier temps de détection de l'évènement en millisecondes.
	 */
	@InspectionElement(name = "Premier", group = "Temps de détection", format = "%tM:%<tS:%<tL")
	public long getFirstTimestampMillis(){
		return (long) (getFirstTimestamp() * 1000);
	}

	/**
	 * Retourne le premier temps de détection de l'évènement en secondes.
	 * @return Le premier temps de détection de l'évènement en secondes.
	 */
	public double getLastTimestamp() {
		return timestamps.get(timestamps.size() - 1);
	}

	/**
	 * Retourne le premier temps de détection de l'évènement en secondes en millisecondes.
	 * @return Le premier temps de détection de l'évènement en secondes en millisecondes.
	 */
	@InspectionElement(name = "Dernier", group = "Temps de détection", format = "%tM:%<tS:%<tL")
	public long getLastTimestampMillis(){
		return (long) (getLastTimestamp() * 1000);
	}


	/**
	 * Ajoute un évènement secondaire à ce groupe d'évènement.
	 * @param secPos La position de l'évènement secondaire.
	 * @param timestamp Le temps de l'évènement secondaire.
	 */
	public void addSecondaryEvent(Vecteur secPos, double timestamp) {
		positions.add(secPos);
		timestamps.add(timestamp);
	}

	/**
	 * Retourne l'ID du véhicule qui lève l'évènement en chaîne.
	 * @return L'ID du véhicule qui lève l'évènement en chaîne.
	 */
	@InspectionElement(name = "Source", group = "Informations invisibles", displayed = false)
	public String getSourceFormatted() {
		return "Source : " + source;
	}

	/**
	 * Retourne l'ID du véhicule qui lève l'évènement.
	 * @return L'ID du véhicule qui lève l'évènement.
	 */
	@InspectionElement(name = "Source", group = "Informations g\u00e9n\u00e9rales")
	public int getSource() {
		return source;
	}

	/**
	 * Retourne le nombre d'évènement secondaire levé par le simulateur.
	 * @return Le nombre d'évènement secondaire levé par le simulateur.
	 */
	@InspectionElement(name = "Événements secondaires", group = "Informations g\u00e9n\u00e9rales", description = "Nombre de fois que cet événement à été levé par l'automobile de la simulation")
	public int getSecondaryEvent() {
		return positions.size();
	}

	/**
	 * Modifie le temps auquel le véhicule est rendu (grosseur).
	 * @param time le temps auquel le véhicule est rendu.
	 */
	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VehicleEvent))
			return false;

		VehicleEvent event = (VehicleEvent) o;

		if (Double.compare(event.getMeanTimestamp(), getMeanTimestamp()) != 0)
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
		temp = Double.doubleToLongBits(getMeanTimestamp());
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public String toString() {
		return String.format("[%10s, %45s, %17s]", eventType, getPosition(), getMeanTimestamp());
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		if(!isActive())
			return;
		Image img = IMAGES.get(eventType).get(source);
		if(img == null) {
			IMAGES.get(eventType).put(source, Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(IMAGES.get(eventType).get(null).getSource(), new UICommonUtils.ColorOverloadImageFilter(new Color(new Random(source).nextInt(0xFFFFFF))))));
			img = IMAGES.get(eventType).get(source);
		}
		Vecteur pos = getPosition();
		
		AffineTransform backup = g2d.getTransform();
		g2d.transform(transform);
		g2d.translate(pos.getX(), pos.getY());
		g2d.scale(SCALE_DEFAUT, -SCALE_DEFAUT);
		g2d.drawImage(img, -img.getWidth(null)/2, -img.getHeight(null)/2, img.getWidth(null), img.getHeight(null), null);
		g2d.setTransform(backup);
	}
	
	/**
	 * Retourne vrai si l'évènement est actif, c'est-à-dire que le temps de la simulation est proche du temps de sa création.
	 * @return vrai si l'évènement est actif.
	 */
	public boolean isActive() {
		return getMeanTimestamp() < time && time < getMeanTimestamp() + DURATION;
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
		Image img = IMAGES.get(eventType).get(null);
		transform.translate(-img.getWidth(null)/2, -img.getHeight(null)/2);
		return transform.createTransformedShape(new Ellipse2D.Double(0, 0, img.getWidth(null), img.getHeight(null)));
	}

}
