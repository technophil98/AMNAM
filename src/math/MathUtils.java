package math;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Cette classe contient les différents algorithmes utiliser dans le traitement des données.
 * @author Pascal Dally-Bélanger
 */
public final class MathUtils {
	
	/**
	 * Constructeur privé qui empêche l'initialisation d'objet.
	 */
	private MathUtils() {
		
	}
	
	/**
	 * Génère nombre pseudo-aléatoire distribuer normalement avec une espérance mathématique de 0 et un écart-type de 1.
	 * @return Un nombre pseudo-aléatoire.
	 */
	public static double normalDistribution() {
		return Math.sqrt(-2*Math.log(Math.random()))*Math.cos(2*Math.PI*Math.random());
	}

	/**
	 * Génère nombre pseudo-aléatoire distribuer normalement.
	 * @param mu L'espérance mathématique.
	 * @param sigma L'écart-type.
	 * @return Un nombre pseudo-aléatoire.
	 */
	public static double normalDistribution(double mu, double sigma) {
		return sigma * normalDistribution() + mu;
	}
	
	/**
	 * Détermine l'écart-type d'une série de vecteur.
	 * @param vectors La liste de vecteur.
	 * @return L'écart-type.
	 */
	public static double standardDeviation(Vecteur[] vectors) {
		double sigma2 = 0;
		Vecteur avg = average(vectors);
		for(int i = 0; i < vectors.length; i++)
			sigma2 += vectors[i].moins(avg).norme2();
		sigma2 /= vectors.length;
		return Math.sqrt(sigma2);
		
	}
	
	/**
	 * Retourne la moyenne euclidienne des vecteurs.
	 * @param vectors La liste de vecteur.
	 * @return La moyenne de l'ensemble des vecteurs.
	 */
	public static Vecteur average(Vecteur[] vectors) {
		Vecteur avg = new Vecteur(0, 0);
		for(int i = 0; i < vectors.length; i++)
			avg = avg.plus(vectors[i]);
		avg = avg.div(vectors.length);
		return avg;
	}
	
	
	/**
	 * Détermine si deux listes de vecteurs sont suffisemment proche l'une de l'autres en comparent, 
	 * composante par composante, chaque vecteur.
	 * @param v1 La première liste de vecteurs.
	 * @param v2 La première liste de vecteurs.
	 * @param tolerance L'écart maximal permit.
	 * @return vrai, si les deux listes sont suffisemment proche l'une de l'autre.
	 */
	public static boolean comparerVecteurs(Vecteur[] v1, Vecteur[] v2, double tolerance) {
		if(v1 == null || v2 == null)
			return false;
		if(v1.length != v2.length)
			return false;
		for(int i = 0; i < v1.length; i++)
			if(!(comparerDouble(v1[i].getX(), v2[i].getX(), tolerance) && comparerDouble(v1[i].getY(), v2[i].getY(), tolerance)))
				return false;
		return true;
				
	}
	
	/**
	 * Détermine si deux double sont suffisemment proche l'un de l'autre.
	 * @param x Le premier nombre.
	 * @param y Le deuxième nombre.
	 * @param tolerance L'écart maximal permit.
	 * @return vrai, si les deux nombres sont suffisemment proche l'une de l'autre.
	 */
	public static boolean comparerDouble(double x, double y, double tolerance) {
		double diff = x - y;
		return -tolerance < diff && diff < tolerance; 
	}
	
	/**
	 * Génère un nombre aléatoire entre x et y distribuer uniformément.
	 * @param x La valeur minimale du nombre.
	 * @param y La valeur maximale du nombre.
	 * @return un nombre aléatoire entre x et y.
	 */
	public static double distributionUniforme(double x, double y) {
		return Math.random() * (y - x) + x;
	}


	/**
	 * Implémentation de l'algorithme KMeans.
	 *
	 * @author Pascal Dally-Bélanger
	 */
	public static class KMean {
		
		private Vecteur[] data;
		private Vecteur[] prev;
		private Vecteur[] centroids;
		private int[] deadCentroids;
		

		/**
		 * Prépare l'algorithme K-moyenne sur l'ensemble des points.
		 * @param data Les points à regroupper
		 * @param centroids Les centroïdes a utiliser.
		 */
		public KMean(Vecteur[] data, Vecteur[] centroids) {
			this.data = data;
			this.centroids = centroids;
		}
		
		/**
		 * Une itération de l'algorithme.
		 */
		public void next() {
			if(hasConverged(0.01))
				cleanCentroids();
			else
				kMean();
		}
		
		/**
		 * Retourne la liste des centroïdes.
		 * @return la liste des centroïdes.
		 */
		public Vecteur[] getCentroids() {
			return centroids;
		}
		
		/**
		 * Filtre les centroïdes qui n'ont pas pas de donné associé.
		 */
		public void cleanCentroids() {
			Vecteur[] temp = new Vecteur[centroids.length - deadCentroids.length];
			for(int i = 0, j = 0, k = 0; i < centroids.length; i++)
				if(j < deadCentroids.length && deadCentroids[j] == i)
					j++;
				else
					temp[k++] = centroids[i];
			deadCentroids = new int[0];
			centroids = temp;
		}
		
		/**
		 * Retourne la liste des données.
		 * @return La liste des données.
		 */
		public Vecteur[] getData() {
			return data;
		}
		
		/**
		 * Retourne la liste des indices des centroïdes qui n'ont pas pas de donné associé.
		 * @return La liste des indices des centroïdes morts.
		 */
		public int[] getDeadCentroidIndices() {
			return deadCentroids;
		}
		
		/**
		 * Retourne vrai si l'algorithme est terminé.
		 * @param tolerance La tolérance à utiliser pour la comparaison.
		 * @return Vrai, si l'algorithme est terminé.
		 */
		public boolean isDone(double tolerance) {
			return hasConverged(tolerance) && deadCentroids.length == 0;
		}

		/**
		 * Retourne vrai si l'algorithme a convergé.
		 * @param tolerance La tolérance à utiliser pour la comparaison.
		 * @return Vrai, si l'algorithme a convergé.
		 */
		public boolean hasConverged(double tolerance) {
			return comparerVecteurs(prev, centroids, tolerance);
		}
		
		/**
		 * Execute l'algorithme K-moyenne.
		 */
		private void kMean() {
			Vecteur[] ans = new Vecteur[centroids.length];
			int[] indices = new int[data.length];
			int[] pointParCentroide = new int[centroids.length];
			ArrayList<Integer> deadCentroids = new ArrayList<Integer>();
			for(int i = 0; i < indices.length; i++) {
				indices[i] = 0;
				double distmin = data[i].moins(centroids[0]).norme2();
				for(int j = 1; j < centroids.length; j++) {
					double dist = data[i].moins(centroids[j]).norme2();
					if(distmin > dist) {
						distmin = dist;
						indices[i] = j;
					}
				}
				pointParCentroide[indices[i]]++;
			}
			for(int i = 0; i < ans.length; i++)
				ans[i] = new Vecteur(0, 0);
			for(int i = 0; i < indices.length; i++)
				ans[indices[i]] = ans[indices[i]].plus(data[i]);
			for(int i = 0; i < ans.length; i++)
				if(pointParCentroide[i] == 0) {
					ans[i] = centroids[i];
					deadCentroids.add(i);
				}
				else
					ans[i] = ans[i].div(pointParCentroide[i]);
			this.deadCentroids = new int[deadCentroids.size()];
			for(int i = 0; i < deadCentroids.size(); i++)
				this.deadCentroids[i] = deadCentroids.get(i);
			prev = centroids;
			centroids = ans;
		}
		
		/**
		 * Execute l'algorithme K-moyenne sur l'ensemble des points.
		 * @param points Les points à regroupper
		 * @param nbCentroid Le nombre de centroïdes a utiliser.
		 * @return Les centroïdes finals, après convergeance.
		 */
		public static Vecteur[] kMoyenne(Vecteur[] points, int nbCentroid) {
			double xMin = points[0].getX();
			double xMax = points[0].getX();
			double yMin = points[0].getY();
			double yMax = points[0].getY();
			for(int i = 1; i < points.length; i++) {
				xMin = Math.min(xMin, points[i].getX());
				xMax = Math.max(xMax, points[i].getX());
				yMin = Math.min(yMin, points[i].getY());
				yMax = Math.max(yMax, points[i].getY());
			}
			return kMoyenne(points, nbCentroid, xMin, xMax, yMin, yMax);
		}
		
		/**
		 * Execute l'algorithme K-moyenne sur l'ensemble des points.
		 * @param points Les points à regroupper
		 * @param nbCentroid Le nombre de centroïdes a utiliser.
		 * @param xMin la valeur minimale que peut prendre un centroïde initial en x.
		 * @param xMax la valeur maximale que peut prendre un centroïde initial en x.
		 * @param yMin la valeur minimale que peut prendre un centroïde initial en y.
		 * @param yMax la valeur maximale que peut prendre un centroïde initial en y.
		 * @return Les centroïdes finals, après convergeance.
		 */
		public static Vecteur[] kMoyenne(Vecteur[] points, int nbCentroid, double xMin, double xMax, double yMin, double yMax) {
			return kMoyenne(points, genererCentroide(nbCentroid, xMin, xMax, yMin, yMax));
		}
		/**
		 * Execute l'algorithme K-moyenne sur l'ensemble des points.
		 * @param points Les points à regroupper.
		 * @param centroides La position initiale des centroides.
		 * @return Les centroïdes finals, après convergeance.
		 */
		public static Vecteur[] kMoyenne(Vecteur[] points, Vecteur[] centroides) {
			Vecteur[] prev = centroides;
			Vecteur[] curr = iterationKMoyenne(points, prev);
			while(comparerVecteurs(prev, curr, 1/100000d)) {
				prev = curr;
				curr = iterationKMoyenne(points, centroides);
			}
			return curr;
		}
		/**
		 * Génère aléatoirement des centroïdes pour l'algorithme de K-moyenne.
		 * @param nbCentroid Le nombre de centroïdes a utiliser.
		 * @param xMin la valeur minimale que peut prendre un centroïde initial en x.
		 * @param xMax la valeur maximale que peut prendre un centroïde initial en x.
		 * @param yMin la valeur minimale que peut prendre un centroïde initial en y.
		 * @param yMax la valeur maximale que peut prendre un centroïde initial en y.
		 * @see KMean
		 * @return La position initiale des centroïdes.
		 */
		public static Vecteur[] genererCentroide(int nbCentroid, double xMin, double xMax, double yMin, double yMax) {
			Vecteur[] centroides = new Vecteur[nbCentroid];
			for(int i = 0; i < centroides.length; i++)
				centroides[i] = new Vecteur(distributionUniforme(xMin, xMax), distributionUniforme(yMin, yMax));
			return centroides;
		}
		/**
		 * Génère aléatoirement des centroïdes pour l'algorithme de K-moyenne.
		 * @param nbCentroid Le nombre de centroïdes a utiliser.
		 * @param points les différents points de données qui seront utiliser dans l'algorithme de K-moyenne.
		 * @return La position initiale des centroïdes.
		 */
		public static Vecteur[] genererCentroide(int nbCentroid, Vecteur[] points) {
			double xMin = points[0].getX();
			double xMax = points[0].getX();
			double yMin = points[0].getY();
			double yMax = points[0].getY();
			for(int i = 1; i < points.length; i++) {
				xMin = Math.min(xMin, points[i].getX());
				xMax = Math.max(xMax, points[i].getX());
				yMin = Math.min(yMin, points[i].getY());
				yMax = Math.max(yMax, points[i].getY());
			}
			return genererCentroide(nbCentroid, xMin, xMax, yMin, yMax);
		}
		
		/**
		 * Effectu une itération de l'algorithme de K-moyenne.
		 *
		 * @param points Les points à regroupper.
		 * @param centroides La position initiale des centroïdes.
		 * @return La position des centroïdes après une itération.
		 */
		public static Vecteur[] iterationKMoyenne(Vecteur[] points, Vecteur[] centroides) {
			Vecteur[] nouv = new Vecteur[centroides.length];
			int[] indices = new int[points.length];
			int[] pointParCentroide = new int[centroides.length];
			for(int i = 0; i < indices.length; i++) {
				indices[i] = 0;
				double distmin = points[i].moins(centroides[0]).norme2();
				for(int j = 1; j < centroides.length; j++) {
					double dist = points[i].moins(centroides[j]).norme2();
					if(distmin > dist) {
						distmin = dist;
						indices[i] = j;
					}
				}
				pointParCentroide[indices[i]]++;
			}
			for(int i = 0; i < nouv.length; i++)
				nouv[i] = new Vecteur(0, 0);
			for(int i = 0; i < indices.length; i++)
				nouv[indices[i]] = nouv[indices[i]].plus(points[i]);
			for(int i = 0; i < nouv.length; i++)
				if(pointParCentroide[i] == 0)
					nouv[i] = centroides[i];
				else
					nouv[i] = nouv[i].div(pointParCentroide[i]);
			return nouv;
			
		}
	}
	
	/**
	 * OPTICS est un acronyme anglophone pour "Ordering points to identify the clustering structure",
	 * un algorithme de regroupement.
	 * @author Pascal Dally-Bélanger
	 *
	 */
	public static class OPTICS {
		
		private double epsilon2;
		private int minPts;
		private Data[] data;


		/**
		 * Prépare l'algorithme OPTICS sur l'ensemble des points.
		 * @param data Les points à regroupper.
		 * @param epsilon2 Le paramètre epsilon de l'algorthime OPTICS, mis au carré.
		 * @param minPts Le paramètre minPts de l'algorithme OPTICS.
		 */
		public OPTICS(double epsilon2, int minPts, Vecteur[] data) {
			this.epsilon2 = epsilon2;
			this.minPts = minPts;
			this.data = new Data[data.length];
			for(int i = 0; i < data.length; i++)
				this.data[i] = new Data(data[i]);
			for(int i = 0; i < data.length; i++)
				this.data[i].index = i;
		}
		
		/**
		 * Exécute l'algorithme sur les points.
		 * @return Une liste de liste de vecteur. Chaque liste de vecteur contient une grappe. 
		 * la liste de liste de vecteur est la liste des grappes.
		 */
		public ArrayList<ArrayList<Vecteur>> execute() {
			ArrayList<ArrayList<Vecteur>> list = new ArrayList<ArrayList<Vecteur>>();
			for(int i = 0; i < data.length; i++) {
				if(!data[i].processed) {
					ArrayList<Integer> n = neighborhood(i);
					data[i].processed = true;
					list.add(0, new ArrayList<Vecteur>());
					list.get(0).add(data[i]);
					if(coreDist(i) != Double.NaN) {
						PriorityQueue<Data> seeds = new PriorityQueue<Data>((x, y) -> (int)Math.signum(x.reachabilityDist - y.reachabilityDist));
						update(n, i, seeds);
						
						while(!seeds.isEmpty()) {
							Data q = seeds.poll();
							ArrayList<Integer> n2 = neighborhood(q.index);
							q.processed = true;
							list.get(0).add(q);
							if(coreDist(q.index) != Double.NaN) {
								update(n2, q.index, seeds);
							}
						}
					}
				}
			}
			return list;
		}
		
		/**
		 * Met a jour la queue.
		 * @param n La liste des indices des données dans le voisinage.
		 * @param p Le point central.
		 * @param seeds La queue.
		 */
		private void update(ArrayList<Integer> n, int p, PriorityQueue<Data> seeds) {
			for(int i : n)
				if(!data[i].processed) {
					double reachabilityDist = reachabilityDist(i, p);
					if(data[i].reachabilityDist == 0) {
						data[i].reachabilityDist = reachabilityDist;
						seeds.add(data[i]);
					} else if(reachabilityDist < data[i].reachabilityDist) {
						seeds.remove(data[i]);
						data[i].reachabilityDist = reachabilityDist;
						seeds.add(data[i]);
					}
				}
					
		}
		
		/**
		 * Retourne la liste de tout les points dans le voisinage de la donné spécifié.
		 * @param index L'indice de la donné spécifié.
		 * @return la liste de tout les points dans le voisinage.
		 */
		private ArrayList<Integer> neighborhood(int index) {
			ArrayList<Integer> neighborhood = new ArrayList<Integer>();
			for(int i = 0; i < data.length; i++)
				if(i != index)
					if(data[i].moins(data[index]).norme2() <= epsilon2)
						neighborhood.add(i);
			return neighborhood;
		}
		
		/**
		 * Détermine la distance du minPts-ème point le plus proche de la donné, ou {@link Double#NaN} si celui-ci ne fait pas partie du voisinage.
		 * @param index L'indice de la donné spécifié.
		 * @return La distance du minPts-ème point le plus proche de la donné.
		 */
		private double coreDist(int index) {
			ArrayList<Integer> neighborhood = neighborhood(index);
			neighborhood.sort((x, y) -> (int)Math.signum(data[x].moins(data[index]).norme2() - data[y].moins(data[index]).norme2()));
			return neighborhood.size() < minPts ? Double.NaN : neighborhood.get(minPts - 1); 
		}
		
		/**
		 * Méthode utilisé pour l'algorithme d'OPTICS.
		 * @param o L'indice du premier point.
		 * @param p L'indice du deuxième point.
		 * @return La valeur la plus grande entre la distance des deux points et 
		 * la distance du minPts-ème point le plus proche du deuxième point.
		 */
		private double reachabilityDist(int o, int p) {
			return Math.max(data[o].moins(data[p]).norme2(), coreDist(p));
		}
		

		/**
		 * Modélisation des données requise pour l'utilisation de l'algorithme OPTICS.
		 * @author Pascal Dally-Bélanger
		 */
		private class Data extends Vecteur {
			
			private boolean processed;
			private double reachabilityDist;
			private int index;
			
			/**
			 * Instancie le point.
			 * @param v la position du point.
			 */
			public Data(Vecteur v) {
				super(v.getX(), v.getY());
			}
		}
	}
}
