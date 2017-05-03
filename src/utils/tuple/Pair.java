package utils.tuple;

/**
 * Classe générique représentant une structure de données formant une pair d'objet de type personalisable.
 *
 * @param <T> Le type du premier objet
 * @param <U> Le type du deuxième objet
 * @author Pascal Dally-Bélanger
 */
public class Pair<T, U> {

	private final T first;
	private final U second;

	/**
	 * Instancies un nouveau Triplet.
	 *
	 * @param first  Le premier objet
	 * @param second Le deuxième objet
	 */
	public Pair(T first, U second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Retroune le premier objet.
	 *
	 * @return le premier objet
	 */
	public T getFirst() { return first; }

	/**
	 * Retroune le deuxième objet.
	 *
	 * @return le deuxième objet
	 */
	public U getSecond() { return second; }

}
