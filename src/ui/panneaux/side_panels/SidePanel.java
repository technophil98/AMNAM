/*
 * Created by JFormDesigner on Fri Feb 24 14:41:31 EST 2017
 */

package ui.panneaux.side_panels;

import ui.UICommonUtils;
import ui.composants.AdaptableTitledBorder;
import ui.event.SidePanelListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;

import static ui.UICommonUtils.PATH_BUTTON_ICON_X;

/**
 * Panneau d'information de base qui s'affiche par dessus la carte.
 *
 * @author Philippe Rivest
 */
public class SidePanel extends JPanel {
	
	private static final long serialVersionUID = 3639040859859750897L;
	public static final Dimension DEFAULT_SIZE = new Dimension(350, 645);

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();

	/**
	 * Instancies un nouveau SidePanel.
	 */
	public SidePanel() {
		initComponents();
	}

	/**
	 * Méthode exécutée lors d'un clic sur le bouton fermer du panneau.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonFermerPanneauActionPerformed(ActionEvent e) {
		this.closePanel();
	}

	/**
	 * Ferme le panneau et envoye un évènement de fermeture.
	 */
	public void closePanel() {

		this.setVisible(false);

		for (SidePanelListener sidePanelListener : OBJETS_ENREGISTRES.getListeners(SidePanelListener.class))
			sidePanelListener.panelClosed(this);

	}

	/**
	 * Initialise tous les composants graphiques du panneau.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.buttonFermerPanneau = new JButton();

		//======== this ========
		setBorder(new AdaptableTitledBorder());
		setLayout(null);

		//---- buttonFermerPanneau ----
		this.buttonFermerPanneau.setText("x"); //$NON-NLS-1$ //NON-NLS
		this.buttonFermerPanneau.setFont(new Font(".SF NS Text", Font.PLAIN, 10)); //$NON-NLS-1$ //NON-NLS
		this.buttonFermerPanneau.addActionListener(e -> buttonFermerPanneauActionPerformed(e));
		add(this.buttonFermerPanneau);
		this.buttonFermerPanneau.setBounds(310, 0, 35, 35);

		setPreferredSize(new Dimension(350, 645));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		UICommonUtils.addIconToButton(buttonFermerPanneau, PATH_BUTTON_ICON_X);
	}

	/**
	 * Retourne le texte de l'étiquette de titre.
	 *
	 * @return Le titre
	 */
	public String getTitle() {
		return ((AdaptableTitledBorder) this.getBorder()).getTitle();
	}

	/**
	 * Modifies le texte de l'étiquette de title.
	 *
	 * @param title Le title à afficher
	 */
	public void setTitle(String title) {
		((AdaptableTitledBorder) this.getBorder()).setTitle(title);
	}

	/**
	 * Ajoute un SidePanelListener à la liste des écouteurs.
	 *
	 * @param sidePanelListener Le SidePanelListener à ajouter
	 */
	public void addSidePanelListener(SidePanelListener sidePanelListener) {
		OBJETS_ENREGISTRES.add(SidePanelListener.class, sidePanelListener);
	}

	/**
	 * Retire le listener de la liste des écouteurs.
	 *
	 * @param sidePanelListener Le SidePanelListener à retirer
	 */
	public void removeSidePanelListener(SidePanelListener sidePanelListener) {
		OBJETS_ENREGISTRES.remove(SidePanelListener.class, sidePanelListener);
	}

	@Override
	public String toString() {
		return "PanneauLateral (" + this.getClass().getSimpleName() + "): \"" + ((TitledBorder) this.getBorder()).getTitle() + "\"";
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag)
			((AdaptableTitledBorder) getBorder()).setParent(this);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JButton buttonFermerPanneau;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
