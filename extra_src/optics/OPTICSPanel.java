package optics;

import math.MathUtils;
import math.MathUtils.OPTICS;
import math.Vecteur;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;


/**
 * Panneau de rendu de l'algorithme de K-moyenne.
 *
 * @author Pascal Dally-Bélanger
 */
public class OPTICSPanel extends JPanel {

	private static final long serialVersionUID = -7072259667424377034L;
	private static final int NB_POINT_DEFAULT = 1000;
	private static final double SIGMA_COEF = 10;
	private static final int CLUSTER = 5;
	private static final double SIGMA_MIN = 14;
	private static final double c = 2;
	private static final double RADIUS_DATA = 4;
	private static final double EPSILON = 20;
	private static final int MIN_PTS = 10;
	private static Color[] COLORS = {
			Color.blue,
			Color.green,
			Color.yellow.darker(),
			Color.magenta.darker(),
			Color.gray,
			Color.cyan.darker(),
			Color.green.darker()
	};

	private boolean isInit = false;
	private boolean animation = false;
	private Vecteur[] clusters = new Vecteur[0];
	private double[] sigmas = new double[0];
	
	
	private ArrayList<ArrayList<Vecteur>> optics;
	
	/**
	 * Initialise un nouveau KMoyennePanel.
	 */
	public OPTICSPanel() {
		
	}
	
	/**
	 * Initialise les données et les centroïdes.
	 */
	private void init() {
		Vecteur[] points = new Vecteur[NB_POINT_DEFAULT];
		double x = Math.random() * getWidth();
		double y = Math.random() * getHeight();
		double sigma = Math.random() * SIGMA_COEF + SIGMA_MIN;
		clusters = new Vecteur[CLUSTER];
		sigmas = new double[CLUSTER];
		clusters[0] = new Vecteur(x, y);
		sigmas[0] = sigma;
		for(int i = 0, j = 0; i < NB_POINT_DEFAULT; i++) {
			if(i*CLUSTER/NB_POINT_DEFAULT != j) {
				x = Math.random() * getWidth();
				y = Math.random() * getHeight();
				sigma = Math.random() * SIGMA_COEF + SIGMA_MIN;
				clusters[++j] = new Vecteur(x, y);
				sigmas[j] = sigma;
			}
			points[i] = new Vecteur(
					MathUtils.normalDistribution(x, sigma),
					MathUtils.normalDistribution(y, sigma)
				);
		}
		isInit = true;
		optics = new OPTICS(EPSILON*EPSILON, MIN_PTS, points).execute();
		System.out.printf("%s clusters%n", optics.size());
		int count = 0;
		for(ArrayList<Vecteur> list : optics)
			if(list.size() == 1)
				count++;
		System.out.printf("%s non-singleton clusters%n", optics.size() - count);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		if(!isInit)
			init();
		
		g2d.setColor(Color.green.darker());
		g2d.setStroke(new BasicStroke(2f));
		for(int i = 0; i < clusters.length; i++)
			if(clusters[i] != null)
				g2d.draw(new Ellipse2D.Double(clusters[i].getX()-sigmas[i]*c, clusters[i].getY()-sigmas[i]*c, sigmas[i]*c*2, sigmas[i]*c*2));
			else
				System.out.printf("%s is null%n", i);
		
		g2d.setStroke(new BasicStroke(1f));
		if(optics != null)
			for(int i = 0; i < optics.size(); i++) {
				if(optics.get(i).size() == 1)
					g2d.setColor(Color.red);
				else if(i < COLORS.length)
					g2d.setColor(COLORS[i]);
				else
					g2d.setColor(new Color((int)(Math.random() * 0xffffff)));
				for(Vecteur v : optics.get(i))
					g2d.draw(new Ellipse2D.Double(v.getX()-RADIUS_DATA, v.getY()-RADIUS_DATA, RADIUS_DATA*2, RADIUS_DATA*2));
			}
		
	}
	
	/**
	 * Effectue une itération de l'algorithme de K-moyenne.
	 * Si l'algorithme a convergé, ré-initialise les données et les centroïdes.
	 */
	public void prochain() {
		init();
		repaint();
	}
	
	/**
	 * Ré-initialise les données et les centroïdes.
	 */
	public void reinitialiser() {
		init();
		repaint();
	}
	
	/**
	 * Anime le panel, en itérant avec K-moyenne à chaque 100 ms.
	 */
	public void animer() {
		if(!animation)
			new Thread(() -> {
				animation = true;
				while(animation) {
					prochain();
					try {
						Thread.sleep(100L);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
	}
	
	/**
	 * Arrête l'animation.
	 */
	public void arreter() {
		animation = false;
	}
}
