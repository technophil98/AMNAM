package io.csv;

import utils.Utils;

import java.io.*;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Un LecteurCSV est un objet qui permet la lecture, ligne par ligne, d'un fichier .csv.
 * @author Pascal Dally-Bélanger
 *
 * @param <T> Le type de ligne que le lecteur lit.
 */
public class LecteurCSV<T extends Ligne> implements Iterable<T> {
	
	private final Entete entete;
	
	private String regex;
	private BufferedReader lecteur;
	private ConfigurationCSV<T> config;

	/**
	 * Initialise un nouveau lecteur avec les paramètres fournies.
	 * @param lecteur Le lecteur de fichier.
	 * @param regex L'expression rationelle qui délimite chaque entrée d'une ligne.
	 * @param types La liste des types des entrées de chaque ligne.
	 * @throws IOException S'il y a une exception lors de la création du lecteur ou de la lecture de l'entête.
	 * @throws EnteteException Si l'entête n'est pas compatible avec la liste de types.
	 */
	public LecteurCSV(BufferedReader lecteur, String regex, TypesCSV[] types) throws IOException, EnteteException {
		this.regex = regex;
		this.lecteur = lecteur;
		entete = new Entete(this.lecteur.readLine().split(regex), types);
	}
	
	/**
	 * Initialise un nouveau lecteur avec les paramètres fournies.
	 * @param fichier Le fichier .csv à lire.
	 * @param regex L'expression rationelle qui délimite chaque entrée d'une ligne.
	 * @param types La liste des types des entrées de chaque ligne.
	 * @throws IOException S'il y a une exception lors de la création du lecteur ou de la lecture de l'entête.
	 * @throws EnteteException Si l'entête n'est pas compatible avec la liste de types.
	 */
	public LecteurCSV(File fichier, String regex, TypesCSV[] types) throws IOException, EnteteException {
		this(new BufferedReader(new FileReader(fichier)), regex, types);
	}
	
	/**
	 * Initialise un nouveau lecteur avec les paramètres fournies.
	 * @param lecteur Le lecteur de fichier.
	 * @param config La configuration à utiliser lors de la lecture.
	 * @throws IOException S'il y a une exception lors de la création du lecteur ou de la lecture de l'entête.
	 * @throws EnteteException Si l'entête n'est pas compatible avec la liste de types.
	 */
	public LecteurCSV(InputStream lecteur, ConfigurationCSV<T> config) throws IOException, EnteteException {
		this(new BufferedReader(new InputStreamReader(lecteur, "UTF-8")), config);
	}
	
	/**
	 * Initialise un nouveau lecteur avec les paramètres fournies.
	 * @param fichier Le fichier .csv à lire.
	 * @param config La configuration à utiliser lors de la lecture.
	 * @throws IOException S'il y a une exception lors de la création du lecteur ou de la lecture de l'entête.
	 * @throws EnteteException Si l'entête n'est pas compatible avec la liste de types.
	 */
	public LecteurCSV(File fichier, ConfigurationCSV<T> config) throws IOException, EnteteException {
		this(new BufferedReader(new FileReader(fichier)), config);
	}
	
	/**
	 * Initialise un nouveau lecteur avec les paramètres fournies.
	 * @param lecteur Le lecteur de fichier.
	 * @param config La configuration à utiliser lors de la lecture.
	 * @throws IOException S'il y a une exception lors de la création du lecteur ou de la lecture de l'entête.
	 * @throws EnteteException Si l'entête n'est pas compatible avec la liste de types.
	 */
	public LecteurCSV(BufferedReader lecteur, ConfigurationCSV<T> config) throws IOException, EnteteException {
		regex = config.getRegex();
		this.lecteur = lecteur;
		this.config = config;
		entete = config.getEntete();
		new Entete(lecteur.readLine().split(regex), config.getEntete().getTypes());
	}
	
	/**
	 * Retourne une ligne du fichier .csv.
	 * @return La ligne lue.
	 * @throws IOException Si une IOException est lancée durant la lecture.
	 */
	public T lireLigne() throws IOException {
		if(config != null)
			return config.convertirLigne(genererLigne(lecteur.readLine()));
		return (T) genererLigne(lecteur.readLine());
	}
	
	/**
	 * Génere un objet Ligne en fonction de la chaîne passée en paramêtre.
	 * @param line La ligne sur laquelle ce base la génération de l'objet Ligne.
	 * @return L'objet Ligne généré.
	 */
	private Ligne genererLigne(String line) {
		String[] lignes = line.split(regex);
		Object[] objets = new Object[lignes.length];
		for(int i = 0; i < lignes.length; i++)
			objets[i] = entete.getTypes()[i].parse(lignes[i]);
		return new Ligne(objets);
	}

	/**
	 * Ferme le BufferedReader du lecteur.
	 * @throws IOException S'il y a une erreur de fermeture du {@link InputStream}
	 */
	public void fermer() throws IOException {
		lecteur.close();
	}
	
	/**
	 * Retourne l'entête du fichier .csv.
	 * @return l'entête du fichier .csv.
	 */
	public Entete getEntete() {
		return entete;
	}
	
	@Override
	public Iterator<T> iterator() {
		return stream().iterator();
	}
	
	/**
	 * Retourne un Stream de ligne qui contients chaque ligne du fichier .csv.
	 * @return Un Stream de ligne qui contients chaque ligne du fichier .csv.
	 */
	public Stream<T> stream() {
		if(config != null)
			return lecteur.lines()
					.filter(s -> !s.equals(""))
					.map(ligne -> config.convertirLigne(genererLigne(ligne)))
					.onClose(Utils.ignorerException(this::fermer));
		return (Stream<T>) lecteur.lines()
				.filter(s -> !s.equals(""))
				.map(ligne -> (genererLigne(ligne)))
				.onClose(Utils.ignorerException(this::fermer));
	}
}
