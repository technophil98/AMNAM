package io.csv;

import java.util.function.Function;

/**
 * Énumération des différents types pouvant exister dans un fichier .csv.
 * @author Pascal Dally-Bélanger
 */
public enum TypesCSV {
	STRING(x -> x),
	INTEGER(x -> (int)Double.parseDouble(x)),
	DOUBLE(Double::parseDouble),
	BOOLEAN(x -> x.equals("1"));
	
	private Function<String, ?> fonction;
	
	/**
	 * Instancie un type utilisé par les fichier .csv.
	 * @param fonction Une fonction qui permet de transformer une chaîne de charactère en objet.
	 */
	private TypesCSV(Function<String, ?> fonction) {
		this.fonction = fonction;
	}
	
	/**
	 * Conversion d'une chaine de charactère provenant d'une ligne d'un fichier .csv en objet Java.
	 * Par exemple : la lecture de la chaine "123" de type INTEGER dans un fichier .csv ce traduirait en objet Integer java 123.
	 * @param s La chaine de charactère.
	 * @return Un objet représentant le charactère.
	 * @throws CSVException Si la chaine n'est pas conforme avec l'entête.
	 */
	public Object parse(String s) throws CSVException {
		try {
			return fonction.apply(s);
		} catch(Exception e) {
			throw new CSVException();
		}
	}
}
