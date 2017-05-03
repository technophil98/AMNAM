package utils.tuple;

/**
 * Classe générique représentant une structure de données formant un triplet d'objet de type personalisable.
 *
 * @param <T> Le type du premier objet du triplet
 * @param <U> Le type du deuxième objet du triplet
 * @param <V> Le type du troisième objet du triplet
 * @author Philippe Rivest
 */
public class Triplet<T, U, V> {

	private final T first;
	private final U second;
	private final V third;

	/**
	 * Instancies un nouveau Triplet.
	 *
	 * @param first  Le premier objet
	 * @param second Le deuxième objet
	 * @param third  Le troisième objet
	 */
	public Triplet(T first, U second, V third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s, %s]", first, second, third);
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

	/**
	 * Retroune le troisième objet.
	 *
	 * @return le troisième objet
	 */
	public V getThird() { return third; }
}
