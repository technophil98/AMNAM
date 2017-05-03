import io.amnam.EtatsFichiersAMNAM;
import io.amnam.FichierAMNAM;

import java.awt.*;
import java.io.File;

/**
 * Code utilitaire pour forcer un état à plusieurs FichierAMNAM.
 *
 * @author Philippe Rivest
 */
public class ForceState {

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {
		FileDialog fileDialog = new FileDialog((Frame) null, "Fichiers a convertir", FileDialog.LOAD);

		fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".amnam"));
		fileDialog.setMultipleMode(true);
		fileDialog.setVisible(true);

		final File[] fichiers = fileDialog.getFiles();

		if (fichiers != null && fichiers.length != 0) {

			for (File fichier : fichiers) {
				System.out.println("Processing : " + fichier);
				FichierAMNAM.forceState(fichier.toPath(), EtatsFichiersAMNAM.CSV_DATA);
			}
		}
		fileDialog.dispose();
	}
}
