package aapplication.aux_entrypoints;

import io.amnam.FichierAMNAM;
import ui.fenetres.GenerateurEvenementFrame;

import java.awt.*;

/**
 * Point d'entrée du générateur d'évènements à des fins de test.
 *
 * @author Philippe Rivest
 */
public class AMNAMGenerateurEvenement {

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				GenerateurEvenementFrame frame = new GenerateurEvenementFrame(FichierAMNAM.chargerUnFichier("Choisir un fichier pour la translation en \u00e9v\u00e8nements", null));
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
