package io.csv;

import java.util.Arrays;

/**
 * Représente une ligne dans un fichier .csv.
 * @author Pascal Dally-Bélanger
 */
public class Ligne {

	private final Object[] objets;
	
	/**
	 * Crée une nouvelle ligne à partir des informations passées en paramètre.
	 * @param objets les informations contenue dans la ligne.
	 */
	public Ligne(Object[] objets) {
		this.objets = objets;
	}
	
	/**
	 * Retourne la valeur à l'indice <em>indice</em>, en tant que <em>T</em>.
	 * @param indice La position de l'objet dans la ligne.
	 * @param <T> Le type de l'objet.
	 * @return l'objet, en temps que <em>T</em>.
	 */
	public <T> T get(int indice) {
		return (T) getObjets()[indice];
	}
	
	public String toString() {
		return Arrays.toString(getObjets());
	}

	/**
	 * Retourne la liste des objets dans cette ligne, en ordre.
	 * @return la liste des objets dans cette ligne, en ordre.
	 */
	public Object[] getObjets() {
		return objets;
	}
}
