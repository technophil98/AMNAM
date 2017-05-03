package io.amnam;

import java.util.Date;

import com.sun.istack.internal.Nullable;

/**
 * Classe contenant les informations sur les étapes qu'a subit un {@link FichierAMNAM}.
 *
 * @author Philippe Rivest
 */
public class EtapeAMNAM {

	private boolean isExecute;
	private transient String nom;
	private Date dateExecution;

	/**
	 * Instancies une nouvelle EtapeAMNAM.
	 *
	 * @param nom           Le nom de l'étape
	 * @param isExecute     Si l'étape à été exécutée
	 * @param dateExecution La date d'éxécution de l'étape
	 */
	public EtapeAMNAM(String nom, boolean isExecute, @Nullable Date dateExecution) {
		this.isExecute = isExecute;
		this.nom = nom;
		this.dateExecution = dateExecution;
	}

	/**
	 * Retourne le nom de l'étape.
	 *
	 * @return le nom de l'étape
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Retourne vrai si l'étape a été exécuté.
	 *
	 * @return vrai si l'étape a été exécuté
	 */
	public boolean isExecute() {
		return isExecute;
	}

	/**
	 * Détermine si l'étape a été exécuté.
	 *
	 * @param execute si l'étape a été exécuté
	 */
	public void setExecute(boolean execute) {
		isExecute = execute;
	}

	/**
	 * Retourne la date d'éxécution de l'étape.
	 *
	 * @return la date d'éxécution de l'étape
	 */
	public Date getDateExecution() {
		return dateExecution;
	}

	/**
	 * Détermine la date d'éxécution de l'étape.
	 *
	 * @param dateExecution la date d'éxécution de l'étape
	 */
	public void setDateExecution(Date dateExecution) {
		this.dateExecution = dateExecution;
	}
}