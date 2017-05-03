package kmoyenne;

import math.MathUtils;
import math.MathUtils.KMean;
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
public class KMoyennePanel extends JPanel {

	private static final long serialVersionUID = -7072259667424377034L;
	private static final int NB_POINT_DEFAULT = 1000;
	private static final int NB_CENTROIDES_DEFAULT = 5;
	private static final double SIGMA_COEF = 10;
	private static final int CLUSTER = 5;
	private static final double SIGMA_MIN = 14;
	private static final double c = 2;

	private KMean kMean;
	
	private boolean isInit = false;
	private boolean animation = false;
	private Vecteur[] clusters = new Vecteur[0];
	private double[] sigmas = new double[0];
	
	
	private ArrayList<ArrayList<Vecteur>> optics;
	
	/**
	 * Initialise un nouveau KMoyennePanel.
	 */
	public KMoyennePanel() {
		
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
		Vecteur[] centroides = KMean.genererCentroide(NB_CENTROIDES_DEFAULT, points);
		isInit = true;
		kMean = new KMean(points, centroides);
		optics = new OPTICS(100, 50, points).execute();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		if(!isInit)
			init();
		
		Vecteur[] data = kMean.getData();
		Vecteur[] centroids = kMean.getCentroids();
		int[] deadCentroids = kMean.getDeadCentroidIndices();
		if(deadCentroids == null)
			deadCentroids = new int[0];
		
		g2d.setColor(Color.black);
		for(int i = 0; i < data.length; i++)
			g2d.draw(new Ellipse2D.Double(data[i].getX()-2, data[i].getY()-2, 4, 4));
		
		g2d.setColor(Color.green.darker());
		g2d.setStroke(new BasicStroke(2f));
		for(int i = 0; i < clusters.length; i++)
			if(clusters[i] != null)
				g2d.draw(new Ellipse2D.Double(clusters[i].getX()-sigmas[i]*c, clusters[i].getY()-sigmas[i]*c, sigmas[i]*c*2, sigmas[i]*c*2));
			else
				System.out.printf("%s is null%n", i);
		
		for(int i = 0, j = 0; i < centroids.length; i++) {
			if(j < deadCentroids.length && deadCentroids[j] == i) {
				g2d.setColor(Color.red);
				j++;
			}
			else
				g2d.setColor(Color.blue);
			g2d.fill(new Ellipse2D.Double(centroids[i].getX()-3, centroids[i].getY()-3, 6, 6));
		}
		
		if(optics != null)
			for(ArrayList<Vecteur> list : optics) {
				g2d.setColor(new Color((int) (Math.random() * 0xffffff)));
				for(Vecteur v : list) {
					g2d.draw(new Ellipse2D.Double(v.getX()-2, v.getY()-2, 4, 4));
				}
			}
		
	}
	
	/**
	 * Effectue une itération de l'algorithme de K-moyenne.
	 * Si l'algorithme a convergé, ré-initialise les données et les centroïdes.
	 */
	public void prochain() {
		if(kMean.isDone(0.001))
			init();
		else
			kMean.next();
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
