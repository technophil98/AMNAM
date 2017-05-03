package ui.panneaux;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import io.amnam.FichierAMNAM;
import math.Vecteur;
import scenario.Scenario;
import ui.dessin.Clickable;
import ui.dessin.Scale;
import ui.dessin.Selectable;
import ui.dessin.Superposition;
import ui.dessin.superposition.CarteSuperposition;
import ui.dessin.superposition.RouteSuperposition;
import ui.event.PanelChangeRequestListener;
import ui.panneaux.side_panels.SidePanel;
import utils.ModelePhysique;
import utils.inspection.Inspectable;
import utils.inspection.Inspector;

/**
 * Une carte est un composant permettant le rendu des différentes superpositions
 * dessinées afin de recréé une représentation d'un espace routier.
 * @author Pascal Dally-Bélanger
 */
public class Carte extends JPanel {

	private static final long serialVersionUID = -2149592302809304593L;
	
	private static final double DEFAULT_SCALE = 1000;
	private static final double SPEED_SCALE = 1.4;
	private static final int CROSSHAIR_LENGTH = 20;
	private static final int TEXT_OFFSET_Y = 10;
	private static final int TEXT_OFFSET_X = 10;
	private static final int SCALE_OFFSET_X = 10;
	private static final int SCALE_OFFSET_Y = 40;
	private static final double SCALE_MAX = 15000;
	private static final double SCALE_MIN = 10; 
	
	private final EventListenerList EVENT_LISTENERS = new EventListenerList();
	
	private Scale scale = new Scale(75, 200);
	private ArrayList<Superposition> superpositions = new ArrayList<Superposition>();
	private final RouteSuperposition routeSuperposition = new RouteSuperposition();
	private Scenario scenario;
	private AffineTransform transform;
	private AffineTransform inverse;
	private double xPos = 0;
	private double yPos = 0;
	private double xClick = 0;
	private double yClick = 0;
	private double dx = 0;
	private double dy = 0;
	private double zoom = DEFAULT_SCALE;
	private boolean drag = false;
	private boolean transformUpdateRequested = true;
	
	private Selectable selection;
	private Object inspected;
	private Inspector inspector;
	

	/**
	 * Crée une carte vide.
	 */
	public Carte() {}

	/**
	 * Crée une carte avec les superpositions passées en paramètres.
	 * @param fichier Un {@link FichierAMNAM} contenant les informations sur la simulation
	 */
	public Carte(FichierAMNAM fichier) {
		scenario = fichier.getScenario();
		
		superpositions.add(scenario.getSuperpositionVehicule());

		if (fichier.isVehiculeEventsLoaded())
			superpositions.add(scenario.getVehicleEventSuperposition());

		if (fichier.isAnalysedEventsLoaded()) {
			if(fichier.getInfo().getObstacles() != null)
				scenario.bindEventsToMap();
			superpositions.add(scenario.getEventSuperposition());
		}
		superpositions.add(routeSuperposition);
		superpositions.add(scenario.getObstacleSuperposition());
		
		CarteSuperposition carteSuperposition = scenario.getCarteSuperposition();
		carteSuperposition.addUpdateRequestHandler(() -> repaint());
		superpositions.add(carteSuperposition);

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent event) {
				xClick = event.getX();
				yClick = event.getY();
			}
			
			@Override
			public void mouseClicked(MouseEvent event) {
				synchronized(superpositions) {
					ListIterator<Superposition> iterator = superpositions.listIterator(superpositions.size());
					w: while(iterator.hasPrevious()) {
						Superposition superposition = iterator.previous();
						if(superposition.isClickable() && superposition.isEnabled())
							for(Clickable clickable : superposition.getClickables())
								if(clickable.getShape().contains(event.getPoint()))
									if(event.isConsumed())
										break w;
									else if(clickable instanceof Selectable && event.getClickCount() == 2) {
										selection = (Selectable)clickable;
										transformUpdateRequested = true;
									} else if(clickable.getClass().isAnnotationPresent(Inspectable.class))
										inspect(clickable);
					}
				}
				repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent event) {
				xPos -= dx;
				yPos -= dy;
				dx = 0;
				dy = 0;
				drag = false;
				repaint();
			}
			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent event) {
				selection = null;
				drag = true;
				transformUpdateRequested = true;
				dx = (event.getX() - xClick) * zoom / DEFAULT_SCALE;
				dy = (event.getY() - yClick) * zoom / DEFAULT_SCALE;
				repaint();
			}

		});
		
		addMouseWheelListener(event -> {
			transformUpdateRequested = true;
			zoom *= Math.pow(SPEED_SCALE, event.getPreciseWheelRotation());
			fixZoom();
			repaint();
		});
		setFocusable(false);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(transformUpdateRequested || selection != null) {
			transformUpdateRequested = false;
			ModelePhysique modele = new ModelePhysique(getWidth(), getHeight(), 0, 0, zoom);
			if(selection != null) {
				Vecteur pos = selection.getPosition();
				xPos = pos.getX();
				yPos = -pos.getY();
			}
			AffineTransform temp = new ModelePhysique(getWidth(), getHeight(),
					modele.getLargUnitesReelles()/2 - xPos + dx,
					modele.getHautUnitesReelles()/2 - yPos + dy,
					zoom).getMatMC();
			temp.scale(1, -1);
			setTransform(temp);
			scale.setTransform(transform);
		}

		synchronized(superpositions) {
			ListIterator<Superposition> iterator = superpositions.listIterator(superpositions.size());
			while(iterator.hasPrevious()) {
				Superposition superposition = iterator.previous();
				if(superposition.isEnabled())
					superposition.dessiner(g2d);
			}
		}
		
		g2d.setStroke(new BasicStroke(2f));
		g2d.setColor(new Color(255, 255, 255, 128));
		if(drag) {
			g2d.drawLine(getWidth()/2 - CROSSHAIR_LENGTH, getHeight()/2, getWidth()/2 + CROSSHAIR_LENGTH, getHeight()/2);
			g2d.drawLine(getWidth()/2, getHeight()/2 - CROSSHAIR_LENGTH, getWidth()/2, getHeight()/2 + CROSSHAIR_LENGTH);
		}
		g2d.setColor(Color.white);

		//Replace la string de position à une distance constante de la droite de la carte
		String posXString = String.format("x : %.2fm", xPos - dx);
		String posYString = String.format("y : %.2fm", dy - yPos);

		g2d.drawString(posXString, getWidth() - g2d.getFontMetrics().stringWidth(posXString) - TEXT_OFFSET_X, TEXT_OFFSET_Y);
		g2d.drawString(posYString, getWidth() - g2d.getFontMetrics().stringWidth(posYString) - TEXT_OFFSET_X, TEXT_OFFSET_Y * 3);

		g2d.setColor(Color.black);
		scale.dessiner(g2d, SCALE_OFFSET_X, getHeight() - SCALE_OFFSET_Y);
	}
	
	/**
	 * Modifie le temps de simulation de la superposition.
	 * @param t Le nouveau temps.
	 */
	public void setTemps(double t) {
		synchronized(superpositions) {
			for(Superposition superposition : superpositions)
				superposition.setTemps(t);
		}
		scenario.getMap().updateWeights(t);
		if(inspector != null)
			inspector.inspect(inspected);
		repaint();
	}
	
	/**
	 * Retourne la valeur minimale de temps interpolable basé sur le FichierAMNAM passé en paramètre à l'initialisation.
	 * @return la valeur minimale de temps interpolable.
	 */
	public double getTempsMin() {
		return scenario.getTempsMin();
	}
	
	/**
	 * Retourne la valeur maximale de temps interpolable basé sur le FichierAMNAM passé en paramètre à l'initialisation.
	 * @return la valeur maximale de temps interpolable.
	 */
	public double getTempsMax() {
		return scenario.getTempsMax();
	}
	
	/**
	 * Retourne la matrice de transformation appliquée aux différentes superpositions de la carte.
	 * @return la matrice de transformation.
	 */
	public AffineTransform getTransform() {
		return transform;
	}
	
	/**
	 * Modifie la matrice de transformation appliquée sur la carte.
	 * @param transform la nouvelle matrice de transformation.
	 */
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
		try {
			inverse = transform.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		for(Superposition superposition : superpositions)
			superposition.setTransform(transform);
	}
	
	/**
	 * Augmente le zoom de la carte.
	 */
	public void zoomIn() {
		zoom /= SPEED_SCALE;
		transformUpdateRequested = true;
		repaint();
	}
	
	/**
	 * Diminue le zoom de la carte.
	 */
	public void zoomOut() {
		zoom *= SPEED_SCALE;
		transformUpdateRequested = true;
		repaint();
	}
	
	/**
	 * Corrige le zoom de la carte s'il est trop grand ou trop petit.
	 */
	private void fixZoom() {
		if(zoom > SCALE_MAX)
			zoom = SCALE_MAX;
		else if(zoom < SCALE_MIN)
			zoom = SCALE_MIN;
	}

	/**
	 * Transforme un point des unités du monde réel en pixel.
	 * @param point le point à transformer
	 * @return le point transformé
	 */
	public Point2D transformPoint(Point2D point) {
		return inverse.transform(point, null);
	}
	
	/**
	 * Recentre la carte et réinitalise le zoom.
	 */
	public void recenter() {
		selection = null;
		xPos = 0;
		yPos = 0;
		zoom = DEFAULT_SCALE;
		transformUpdateRequested = true;
		repaint();
	}
	
	/**
	 * Ajoute un écouteur qui répond à la requette de changement de panneau latéral.
	 * @param listener l'écouteur à ajouter.
	 */
	public void addPanelChangeRequestListener(PanelChangeRequestListener listener) {
		EVENT_LISTENERS.add(PanelChangeRequestListener.class, listener);
	}
	
	/**
	 * Lève un événement {@link PanelChangeRequestListener#requestChange(SidePanel)}.
	 * @param panel Le panneau latéral à afficher
	 */
	private void firePanelChangeRequestEvent(SidePanel panel) {
		for(PanelChangeRequestListener listener : EVENT_LISTENERS.getListeners(PanelChangeRequestListener.class))
			listener.requestChange(panel);
	}
	
	/**
	 * Retourne la liste des superpositions.
	 * @return la liste des superpositions.
	 */
	public ArrayList<Superposition> getSuperpositions() {
		return superpositions;
	}
	
	/**
	 * Inspecte un objet et lève un évènement qui demande le changement du panneau latéral.
	 * @param o l'objet à inspecter.
	 */
	private void inspect(Object o) {
		inspector = Inspector.generateInspector(o.getClass());
		inspected = o;
		inspector.inspect(inspected);
		
		
		SidePanel panel = new SidePanel();
		panel.setTitle("Inspection : " + inspector.getName());
		panel.add(inspector.getPanel());
		firePanelChangeRequestEvent(panel);
	}

	/**
	 * Retourne la superposition de trajet.
	 *
	 * @return la superposition de trajet
	 */
	public RouteSuperposition getRouteSuperposition() {
		return routeSuperposition;
	}
}
