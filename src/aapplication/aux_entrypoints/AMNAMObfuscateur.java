package aapplication.aux_entrypoints;

import com.bulenkov.darcula.DarculaLaf;
import ui.fenetres.ObfuscatorConfigFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée de l'Obfuscateur à des fins de test.
 *
 * @author Philippe Rivest
 */
public class AMNAMObfuscateur {

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
				ObfuscatorConfigFrame frame = new ObfuscatorConfigFrame();
				frame.setVisible(true);
			}

		});
	}

}
