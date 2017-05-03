package ui.dessin.superposition;

import scenario.Obstacle;
import scenario.events.Event;
import scenario.events.EventType;
import ui.UICommonUtils;
import ui.dessin.Clickable;
import ui.dessin.Superposition;
import ui.panneaux.side_panels.SidePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.HashMap;

/**
 * Une superposition contenant les différents obstacles de la superposition.
 * 
 * @author Pascal Dally-Bélanger
 */
public class ObstacleSuperposition implements Superposition {
	
	private Obstacle[] obstacles;
	private boolean isEnabled = true;
	private AffineTransform transform;
	private static final double SCALE_DEFAUT = 1/50d;
	
	private static final HashMap<EventType, Image> IMAGES = new HashMap<EventType, Image>();
	
	static {
		try {
			IMAGES.put(null, ImageIO.read(Event.class.getResource(UICommonUtils.PATH_IMG_OBSTACLE)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instancie une nouvelle superposition d'obstacle.
	 * @param obstacles La liste des obstacles.
	 */
	public ObstacleSuperposition(Obstacle[] obstacles) {
		this.obstacles = obstacles;
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		for(int i = 0; i < obstacles.length; i++) {
			if(obstacles[i].isOmitted() || obstacles[i].getRadius() == 0d) {
				continue;
			}
			Image img = IMAGES.get(obstacles[i].getObstacleType());
			if(img == null) {
				IMAGES.put(obstacles[i].getObstacleType(), Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(IMAGES.get(null).getSource(), new UICommonUtils.ColorOverloadImageFilter(obstacles[i].getObstacleType().getEventColor()))));
				img = IMAGES.get(obstacles[i].getObstacleType());
			}
			AffineTransform backup = g2d.getTransform();
			g2d.transform(transform);
			g2d.translate(obstacles[i].getPosition().getX(), obstacles[i].getPosition().getY());
			g2d.scale(SCALE_DEFAUT, -SCALE_DEFAUT);
			g2d.drawImage(img, -img.getWidth(null)/2, -img.getHeight(null)/2, img.getWidth(null), img.getHeight(null), null);
			g2d.setTransform(backup);
		}
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public SidePanel getPanneauConfiguration() {
		return null;
	}

	@Override
	public void setTemps(double t) {
		
	}

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
		return "Obstacles";
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

}
