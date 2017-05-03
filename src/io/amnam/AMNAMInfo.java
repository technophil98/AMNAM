package io.amnam;

import com.google.gson.Gson;
import scenario.Obstacle;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe contenant les informations sur le {@link FichierAMNAM}. Cette classe est sérialisée et désérialisée par {@link Gson} sous « amnam_info.json ».
 *
 * @author Philippe Rivest
 */
public class AMNAMInfo {

	private String comments;
	private EtatsFichiersAMNAM etat;
	private int nbVehicules;
	private Obstacle[] obstacles;
	private Map<EtatsFichiersAMNAM, EtapeAMNAM> etapesAmnam;

	/**
	 * Instancies un nouveau AMNAMInfo avec les informations de base.
	 */
	public AMNAMInfo() {

		this.etat = EtatsFichiersAMNAM.VIDE;

		this.etapesAmnam = new HashMap<>(4);
		this.etapesAmnam.put(EtatsFichiersAMNAM.CSV_DATA, new EtapeAMNAM("CSV", false, null));
		this.etapesAmnam.put(EtatsFichiersAMNAM.EVENEMENTS, new EtapeAMNAM("evenement", false, null));
		this.etapesAmnam.put(EtatsFichiersAMNAM.OBFUSQUATION_EVENEMENTS, new EtapeAMNAM("obfuscation", false, null));
		this.etapesAmnam.put(EtatsFichiersAMNAM.ANALYSE, new EtapeAMNAM("analyse", false, null));
	}

	/**
	 * Retourne l'état du FichierAMNAM dans le fichier « amnam_info.json ».
	 *
	 * @return l 'état du fichier
	 * @see EtatsFichiersAMNAM
	 */
	public EtatsFichiersAMNAM getEtat() {
		return etat;
	}

	/**
	 * Mets l'état du {@link FichierAMNAM} à jour.
	 *
	 * @param etat L'état
	 * @see EtatsFichiersAMNAM
	 */
	public void setEtat(EtatsFichiersAMNAM etat) {
		this.etat = etat;
	}

	/**
	 * Retourne le nombre de véhicules contenus dans le FichierAMNAM
	 * spécifié dans le fichier « amnam_info.json ».
	 *
	 * @return le nombre de véhicules
	 */
	public int getNbVehicules() {
		return nbVehicules;
	}

	/**
	 * Mets à jour le nombre de véhicules du {@link FichierAMNAM} spécifié
	 * dans le fichier « amnam_info.json ».
	 *
	 * @param nbVehicules le nombre de véhicules
	 */
	public void setNbVehicules(int nbVehicules) {
		this.nbVehicules = nbVehicules;
	}

	/**
	 * Retourne l'information sur une étape précise.
	 *
	 * @param etape L'étape que nous voulons ses informations
	 * @return L 'étape et ses informations
	 * @see EtapeAMNAM
	 */
	public EtapeAMNAM getEtape(EtatsFichiersAMNAM etape) {
		return etapesAmnam.get(etape);
	}

	/**
	 * Retourne les commentaires du fichier.
	 *
	 * @return les commentaires du fichier
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Mets à jour les commentaires du fichier.
	 *
	 * @param comments les commentaires du fichier
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Mets à jour les obstacles de la simulation.
	 *
	 * @param obstacles les obstacles de la simulation
	 */
	public void setObstacles(Obstacle[] obstacles) {
		this.obstacles = obstacles;
	}

	/**
	 * Retourne les obstacles de la simulation.
	 *
	 * @return les obstacles de la simulation
	 */
	public Obstacle[] getObstacles() {
		return obstacles;
	}
}