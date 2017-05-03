package io.csv;


/**
 * Cette exception signifie qu'il y a eut un erreur lors de la création de l'entête.
 * @author Pascal Dally-Bélanger
 *
 */
public class EnteteException extends RuntimeException {

	private static final long serialVersionUID = -5847000996253158792L;
	
	private String message;
	
	/**
	 * Création d'une EnteteException vide.
	 */
	public EnteteException() {
		
	}
	
	/**
	 * Création d'une EnteteException avec message.
	 * @param message le message de l'EnteteExcepion
	 */
	public EnteteException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
