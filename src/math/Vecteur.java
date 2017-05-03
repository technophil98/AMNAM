package math;

import java.awt.geom.Point2D;

/**
 * Classe représentant un vecteur en 2D.
 * Chaque Vecteur est immutable.
 * @author Pascal Dally-Bélanger
 *
 */
public class Vecteur {

	private final double x;
	private final double y;
	
	/**
	 * Initialisation d'un vecteur ayant comme composante x et y.
	 * @param x La composante en x du vecteur.
	 * @param y La composante en x du vecteur.
	 */
	public Vecteur(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**¸
	 * Retourne le carré de la norme du vecteur.
	 * @return le carré de la norme du vecteur.
	 */
	public double norme2() {
		return x*x + y*y;
	}

	/**¸
	 * Retourne la norme du vecteur.
	 * @return la norme du vecteur.
	 */
	public double norme(){
		return Math.sqrt(norme2());
	}
	
	/**
	 * Calcul la différence entre deux vecteurs.
	 * @param v2 Le vecteur a soustraire.
	 * @return Le résultat du calcul.
	 */
	public Vecteur moins(Vecteur v2) {
		return new Vecteur(x - v2.x, y - v2.y);
	}
	
	/**
	 * Calcul la somme entre deux vecteurs.
	 * @param v2 Le vecteur a additionner.
	 * @return la somme des deux vecteurs.
	 */
	public Vecteur plus(Vecteur v2) {
		return new Vecteur(x + v2.x, y + v2.y);
	}
	
	/**
	 * Multiplie le vecteur par un scalaire.
	 * @param k Le scalaire.
	 * @return Le résultat de la multiplication par un scalaire.
	 */
	public Vecteur mul(double k) {
		return new Vecteur(x * k, y * k);
	}
	
	/**
	 * Divise le vecteur par un scalaire.
	 * @param k Le scalaire.
	 * @return Le résultat de la division par un scalaire.
	 */
	public Vecteur div(double k) {
		return mul(1/k);
	}

	/**
	 * Retourne la composante en x du vecteur.
	 *
	 * @return la composante en x du vecteur
	 */
	public double getX() {
		return x;
	}

	/**
	 * Retourne la composante en y du vecteur.
	 *
	 * @return la composante en y du vecteur
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Retourne le produit scalaire de ce vecteur avec un autre.
	 * @param v2 Le vecteur à multiplier.
	 * @return le résultat du produit scalaire.
	 */
	public double dot(Vecteur v2) {
		return x*v2.x + y*v2.y;
	}
	
	/**
	 * Effectue une conversion de Vecteur à Point2D.
	 * @return un Point2D ayant comme position (x, y) les coordonées de ce vecteur.
	 */
	public Point2D convertToPoint2D() {
		return new Point2D.Double(x, y);
	}
	
	/**
	 * Effectue une conversion de Point2D à Vecteur.
	 * @param point Le point à transformer en Vecteur.
	 * @return un Vecteur ayant comme position (x, y) les coordonées du ce point.
	 */
	public static Vecteur parseFromPoint2D(Point2D point) {
		return new Vecteur(point.getX(), point.getY());
	}
	
	@Override
	public String toString() {
		return String.format("[%.3f, %.3f]", x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vecteur other = (Vecteur) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		return Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}
}
