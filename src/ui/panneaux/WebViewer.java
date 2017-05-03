package ui.panneaux;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

/**
 * Classe graphique affichant une page web et/ou HTML.
 *
 * @author Philippe Rivest
 */
public class WebViewer {

	public static final int WIDTH = 720;
	public static final int HEIGHT = 800;
	private String url, title;
    private WebView webView;
    private JFrame frame;

	/**
	 * Instancies un nouveau WebViewer.
	 *
	 * @param url   L'url de la page à afficher
	 * @param title Le titre de la fenêtre affichant la page web
	 */
	public WebViewer(String url, String title) {
        this.url = url;
        this.title = title;

        SwingUtilities.invokeLater(this::initGUI);
    }

	/**
	 * Initialise tous les composants graphiques Swing du composant.
	 */
    private void initGUI() {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        Platform.runLater(() -> initFX(fxPanel));
    }

	/**
	 * Initialise tous les composants graphiques JavaFX du composant.
	 * @param fxPanel le panneau à initialiser
	 */
    private void initFX(JFXPanel fxPanel) {
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

	/**
	 * Crée la scène affichée sur le panneau contenant le fureteur web.
	 * @return La scène affichée sur le panneau
	 */
    private Scene createScene() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        webView = new WebView();
        webView.getEngine().load(url);
        root.getChildren().add(webView);
        return scene;
    }

	/**
	 * Navigue vers la page spécifiée par le URL.
	 *
	 * @param url la page à afficher sur le panneau
	 */
	public void loadURL(String url) {
        Platform.runLater(() -> {
			webView.getEngine().load(url);
			frame.setVisible(true);
		});
    }
}
