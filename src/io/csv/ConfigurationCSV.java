package io.csv;


/**
 * Une configuration est un moyen de spécifier au {@link LecteurCSV}
 * de quelle sorte de ligne il devrait s'attendre à lire, et de quelle façon.
 * @author Pascal Dally-Bélanger
 *
 * @param <T> Le type de ligne CSV
 * @see Ligne
 */
public interface ConfigurationCSV<T extends Ligne> {

	/**
	 * Retourne l'entête du fichier .csv que le lecteur devrait lire.
	 * @return l'entête du fichier .csv
	 */
	public Entete getEntete();
	
	/**
	 * Méthode permettant la traduction d'une ligne en T.
	 * @param ligne La ligne a traduire.
	 * @return La ligne traduite.
	 */
	public T convertirLigne(Ligne ligne);
	
	/**
	 * Retourne L'expression rationelle qui délimite chaque entrée d'une ligne.
	 * @return L'expression rationelle qui délimite chaque entrée d'une ligne.
	 */
	public String getRegex();
}
