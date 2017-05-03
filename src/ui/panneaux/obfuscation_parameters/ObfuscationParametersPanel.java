package ui.panneaux.obfuscation_parameters;

import obfuscation.ObfuscationParameters;

/**
 * Interface décrivant un panneau acceuillant des composant modifiant un paramètre d'obfuscation.
 *
 * @author Philippe Rivest
 * @see ObfuscationParameters
 */
public interface ObfuscationParametersPanel {


	/**
	 * Retourne un ObfuscationParameters généré à partir de l'entrée de l'utilisateur.
	 *
	 * @return le ObfuscationParameters
	 */
	public ObfuscationParameters getParameters();

}
