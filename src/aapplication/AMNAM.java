package aapplication;

import com.apple.eawt.Application;
import com.bulenkov.darcula.DarculaLaf;
import ui.UICommonUtils;
import ui.fenetres.EcranAccueilFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée de la suite complète d'AMNAM.
 *
 * @author Philippe Rivest
 */
public class AMNAM {

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {

		/*
		Ceci est le message servant de modification pour le commit final de notre projet.
		La longue période de développement est enfin finie...

		« Hello World, at last! »
		 */

		//Préférences MacOS
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {

			Application application = Application.getApplication();

			//Change l'icône de l'application dans le Dock
			application.setDockIconImage(new ImageIcon(AMNAM.class.getResource(UICommonUtils.PATH_IMG_AMNAM_LOGO_URL)).getImage());
		}

		//Lance un frame EcranAccueilFrame
		EventQueue.invokeLater(() -> {

			//Configuration du LaF pour Darcula
			try {

				UIManager.setLookAndFeel(new DarculaLaf());

			} catch (UnsupportedLookAndFeelException | ExceptionInInitializerError e) {

				System.out.println("Look and feel non support\u00e9!");

				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {

					System.out.println("Erreur dans le retrait de Darcula et le r\u00e9glage du LaF syst\u00e8me!");

					e1.printStackTrace();
					System.exit(-1);
				}

			} finally {
				EcranAccueilFrame frame = new EcranAccueilFrame();
				frame.setVisible(true);
			}

		});
	}

}
