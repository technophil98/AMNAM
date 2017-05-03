package io.csv;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 * Un entête est une liste de paire de noms et de types, décrivant
 * le format d'un fichier .csv.
 * @author Pascal Dally-Bélanger
 */
public class Entete {

	
	private final String[] noms;
	private final TypesCSV[] types;
	
	/**
	 * Crée une nouvelle entête.
	 * @param noms le nom de chaque élément.
	 * @param types le type de chaque élément.
	 * @throws EnteteException Si les noms ne sont pas conforment au types.
	 */
	public Entete(String[] noms, TypesCSV[] types) throws EnteteException {
		if(noms.length != types.length)
			throw new EnteteException(Arrays.toString(noms) + ", " + Arrays.toString(types));
		this.noms = noms;
		this.types = types;
	}
	
	@Override
	public String toString() {
		String string = "[";
		for(int i = 0; i < noms.length; i++)
			string += String.format("[%s, %s], ", noms[i], getTypes()[i]);
		return string.substring(0, string.length() - 2) + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Entete))
			return false;
		Entete t2 = (Entete) o;
		if(t2.noms.length != noms.length)
			return false;
		for(int i = 0; i < noms.length; i++)
			if(!noms[i].equals(t2.noms[i]) || !(getTypes()[i] == t2.getTypes()[i]))
				return false;
		return true;
	}

	/**
	 * Retourne la liste des types de chaque entrée de l'entête.
	 * @return la liste des types de chaque entrée de l'entête.
	 */
	public TypesCSV[] getTypes() {
		return types;
	}
	
	/**
	 * Retourne la liste des noms de chaque entrée de l'entête.
	 * @return la liste des noms de chaque entrée de l'entête.
	 */
	public String[] getNoms() {
		return noms;
	}
	
	/**
	 * Méthode utilitaire permettant d'autogénération des méthodes d'accès d'une ligne corespondant à cette entête.
	 * @return les méthodes d'accès autogénérées.
	 */
	public String genererCodeLigne() {
		String string = "";
		string += "/*\n";
		string += " * Code et documentation autog\u00e9n\u00e9r\u00e9s\n";
		string += " */\n";
		
		for(int i = 0; i < noms.length; i++) {
			String nomMethode = noms[i];
			String nomDocumentation = noms[i].replaceAll("^#@", "");
			String typeGenerique = types[i].name().toLowerCase();
			String typeRetour = typeGenerique;
			switch(typeRetour) {
				case "string": 
					typeRetour = "String";
					break;
				case "integer":
					typeRetour = "int";
					break;
			}
			typeGenerique = StringUtils.capitalize(typeGenerique);
			Matcher match = Pattern.compile("[a-zA-Z0-9]*").matcher(nomMethode);
			nomMethode = "";
			
			while(match.find())
				nomMethode += StringUtils.capitalize(match.group());
			string += String.format("/**%n");
			string += String.format(" * Retourne la valeur de l'entr\u00e9e \"%s\" dans le document .csv.%n", nomDocumentation);
			string += String.format(" * @return La valeur de l'entr\u00e9e \"%s\".%n", nomDocumentation);
			string += String.format(" */%n");
			
			string += String.format("public %s get%s() {%n", typeRetour, nomMethode);
			string += String.format("\t return super.<%s>get(%s);%n", typeGenerique, i);
			string += String.format("}%n%n");
		}
		return string;
	}
}
