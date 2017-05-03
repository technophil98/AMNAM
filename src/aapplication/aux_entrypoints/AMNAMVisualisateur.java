package aapplication.aux_entrypoints;

import com.bulenkov.darcula.DarculaLaf;
import io.amnam.FichierAMNAM;
import ui.fenetres.VisualisateurFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Point d'entrée du visualisateur à des fins de test.
 *
 * @author Philippe Rivest
 */
public class AMNAMVisualisateur {

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			try {
				UIManager.setLookAndFeel(new DarculaLaf());
			} catch (UnsupportedLookAndFeelException | ExceptionInInitializerError e) {
				e.printStackTrace();
				System.out.println("Unsupported Look and Feel. Falling back to default.");

				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {
					e1.printStackTrace();
				}

			} finally {

				VisualisateurFrame frame = null;

				try {
					frame = new VisualisateurFrame(FichierAMNAM.chargerUnFichier("Choisir un fichier", null));
				} catch (IOException e) {
					e.printStackTrace();
				}finally {

					if (frame != null) {
						frame.setVisible(true);
					}
				}
			}
		});
	}

}
