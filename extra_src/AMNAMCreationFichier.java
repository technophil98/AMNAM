import aapplication.AMNAM;
import com.apple.eawt.Application;
import com.bulenkov.darcula.DarculaLaf;
import ui.fenetres.CreationFichierAMNAMFrame;

import javax.swing.*;
import java.awt.*;

import static ui.UICommonUtils.PATH_IMG_AMNAM_LOGO_URL;

/**
 * Point d'entrée du créateur de {@link io.amnam.FichierAMNAM}.
 * Cette classe est autonome et peut représenter une application à part entière (un uililitaire à installer sur les simulateurs).
 *
 * @author Philippe Rivest
 */
public class AMNAMCreationFichier {

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {


		//Préférences MacOS
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {

			Application application = Application.getApplication();

			//Change l'icône de l'application dans le Dock
			application.setDockIconImage(new ImageIcon(AMNAM.class.getResource(PATH_IMG_AMNAM_LOGO_URL)).getImage());
		}

		EventQueue.invokeLater(() -> {

			try {
				//Hello darkness my old friend
				UIManager.setLookAndFeel(new DarculaLaf());
			} catch (UnsupportedLookAndFeelException | ExceptionInInitializerError e) {
				e.printStackTrace();
				System.out.println("Unsupported Look and Feel. Falling back to default.");

				//Fallback
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {
					e1.printStackTrace();
				}

			} finally {

				CreationFichierAMNAMFrame frame = new CreationFichierAMNAMFrame(true);
				frame.setVisible(true);

			}
		});
	}

}
