package io.amnam;

/**
 * Énumération des états possibles d'un {@link FichierAMNAM}.
 *
 * @author Philippe Rivest
 */
public enum EtatsFichiersAMNAM {

	/**
	 * Le fichier n'est qu'un squelette.
	 */
	VIDE,
	/**
	 * Le fichier contient les fichier CSV provenant de la simulation.
	 */
	CSV_DATA,
	/**
	 * Le fichier contient les fichier CSV provenant de la simulation et les
	 * évènement issus de ceux-ci.
	 */
	EVENEMENTS,
	/**
	 * Le fichier contient les fichier CSV provenant de la simulation et les
	 * évènement issus de ceux-ci. Ces évènements sont obfusqués au moins une
	 * fois et son stockés dans ce fichier.
	 */
	OBFUSQUATION_EVENEMENTS,
	/**
	 * Le fichier contient les fichier CSV provenant de la simulation et les
	 * évènement issus de ceux-ci. Ces évènements sont obfusqués au moins une
	 * fois et son stockés dans ce fichier. Le fichier à été traité au moins une
	 * fois et est prêt pour être représenté visuellement.
	 */
	ANALYSE;

	/**
	 * Retourne une chaîne de charactères décrivant un état de {@link FichierAMNAM}.
	 *
	 * @param e l'état à décrire
	 * @return la description de l'état
	 */
	public static String etatToString(EtatsFichiersAMNAM e) {

		switch (e) {
			case VIDE:
				return "Le fichier est vide";
			case CSV_DATA:
				return "N'inclue que les CSV de simulation";
			case EVENEMENTS:
				return "Informations sous forme d'\u00e9v\u00e8nements";
			case OBFUSQUATION_EVENEMENTS:
				return "\u00c9v\u00e8nements obfusqu\u00e9s";
			case ANALYSE:
				return "R\u00e9sultats de l'analyse des donn\u00e9es";
			default:
				return "";
		}
	}

}
