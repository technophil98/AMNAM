package utils;

/**
 * Un compteur en or permettant d'incrémenter un entier à l'extérieur d'une expression lambda.
 * @author Pascal Dally-Bélanger
 */
public class Counter {

	private int count;

	/**
	 * Instancie un nouveau compteur.
	 */
	public Counter() {}

	/**
	 * Instancie un nouveau compteur.
	 * @param start La valeur initiale du compteur.
	 */
	public Counter(int start) {
		this.count = start;
	}

	/**
	 * Augmente la valeur du compteur de 1.
	 */
	public void increment() {
		count++;
	}

	/**
	 * Diminue la valeur du compteur de 1.
	 */
	public void decrement() {
		count--;
	}
	
	/**
	 * Retourne la valeur du compteur.
	 * @return La valeur du compteur.
	 */
	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Counter: " + count;
	}
}
