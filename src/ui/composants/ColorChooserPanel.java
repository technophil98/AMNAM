package ui.composants;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Composant graphique permettant à l'utilisateur de choisir une couleur en cliquant sur un carré coloré.
 *
 * @author Philippe Rivest
 */
public class ColorChooserPanel extends JPanel {

	private static final long serialVersionUID = 3523301420473569191L;

	private static final String TEXT_INFOBULLE = "Cliquer ici pour choisir une couleur.";

	private String titreColorChooser = "Choisissez une couleur";
	private Color color = Color.CYAN;

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();

	/**
	 * Instancies un nouveau ColorChooserPanel.
	 */
	public ColorChooserPanel() {

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				ColorChooserPanel.this.onClick(e);
			}
		});

		this.setOpaque(true);
		this.setBackground(color);

		this.setToolTipText(TEXT_INFOBULLE);
		this.setPreferredSize(new Dimension(30, 30));
	}

	/**
	 * Méthode exécutée lors du clic sur le composant.
	 *
	 * @param e Évènement de clic
	 */
	private void onClick(MouseEvent e) {

		Color returnedColor = JColorChooser.showDialog(null, titreColorChooser, this.color);

		if (returnedColor != null) {

			color = new Color(returnedColor.getRGB(), false);
			this.setBackground(this.color);
			for (ActionListener listener : OBJETS_ENREGISTRES.getListeners(ActionListener.class)) {
				listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,""));
			}
		}
	}

	/**
	 * Retourne la dernière couleur choisie.
	 *
	 * @return la dernière couleur choisie
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Détermine la couleur du composant.
	 *
	 * @param color la couleur du composant
	 */
	public void setColor(Color color) {
		this.color = color;
		this.setBackground(this.color);
	}

	/**
	 * Retourne le titre du JColorChooser à afficher lors du clic.
	 *
	 * @return le titre du JColorChoose
	 */
	public String getTitreColorChooser() {
		return titreColorChooser;
	}

	/**
	 * Détermine le titre du JColorChooser à afficher lors du clic.
	 *
	 * @param titreColorChooser le titre du JColorChooser
	 */
	public void setTitreColorChooser(String titreColorChooser) {
		this.titreColorChooser = titreColorChooser;
	}

	/**
	 * Ajoute un ActionListener à la liste des écouteurs.
	 *
	 * @param actionListener Le ActionListener à ajouter
	 */
	public void addActionListener(ActionListener actionListener) {
		OBJETS_ENREGISTRES.add(ActionListener.class, actionListener);
	}
}
