/*
 * Created by JFormDesigner on Sun Mar 26 10:54:12 EDT 2017
 */

package ui.panneaux.side_panels;

import ui.UICommonUtils;
import ui.composants.SuperpositionsListCellRenderer;
import ui.dessin.Superposition;
import ui.event.SuperpositionSidePanelListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Un panneau latéral permettant le remaniement des superpositions.
 *
 * @see SidePanel
 * @author Philippe Rivest
 */
public class SuperpositionSidePanel extends SidePanel {

	private static final long serialVersionUID = -7729918905717800359L;

	private final ArrayList<Superposition> superpositions;
	private DefaultListModel<Superposition> listModel = new DefaultListModel<>();

	private final EventListenerList LISTENERS = new EventListenerList();

	/**
	 * Instancies un nouveau SuperpositionSidePanel.
	 *
	 * @param superpositions La liste des superpositions à gérer
	 */
	public SuperpositionSidePanel(ArrayList<Superposition> superpositions) {
		this.setTitle("Superpositions");
		this.superpositions = superpositions;
		initComponents();
	}

	/**
	 * Méthode exécutée lors d'un clic sur le bouton « haut ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonUpActionPerformed(ActionEvent e) {
		final int selectedIndex = listSuperpositions.getSelectedIndex();

		if (selectedIndex > 0) {
			synchronized (superpositions) {
				Collections.swap(superpositions, selectedIndex, selectedIndex - 1);
			}
			updateListModel();
			listSuperpositions.setSelectedIndex(selectedIndex - 1);
			triggerSuperpositionOrderChangedEvent();
		}
	}

	/**
	 * Méthode exécutée lors d'un clic sur le bouton « bas ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonDownActionPerformed(ActionEvent e) {
		final int selectedIndex = listSuperpositions.getSelectedIndex();

		if ((selectedIndex >= 0) && (selectedIndex < listModel.getSize())) {
			synchronized (superpositions) {
				Collections.swap(superpositions, selectedIndex, selectedIndex + 1);
			}
			updateListModel();
			listSuperpositions.setSelectedIndex(selectedIndex + 1);
			triggerSuperpositionOrderChangedEvent();
		}
	}

	/**
	 * Méthode exécutée lors d'un clic sur le bouton « configurer ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonConfigActionPerformed(ActionEvent e) {

		final Superposition superposition = listSuperpositions.getSelectedValue();

		if (superposition != null && superposition.isConfigurable())
			triggerSuperpositionConfigSidePanelRequestedEvent(superposition.getPanneauConfiguration());

	}

	/**
	 * Méthode exécutée lors d'un clic sur le bouton « Activer/Désactiver ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonToggleDrawActionPerformed(ActionEvent e) {
		if (listSuperpositions.getSelectedValue() != null) {
			listSuperpositions.getSelectedValue().setEnabled(!listSuperpositions.getSelectedValue().isEnabled());
			buttonToggleDraw.setText(listSuperpositions.getSelectedValue().isEnabled() ? "D\u00e9sactiver" : "Activer");
			triggerSuperpositionOrderChangedEvent();
		}
	}

	/**
	 * Méthode exécutée lors d'un changement de sélection dans la liste.
	 *
	 * @param e Évènement de changement
	 */
	private void listSuperpositionsValueChanged(ListSelectionEvent e) {
		if (listSuperpositions.getSelectedValue() != null) {
			buttonToggleDraw.setText((listSuperpositions.getSelectedValue().isEnabled() ? "D\u00e9sactiver" : "Activer"));
			buttonConfig.setEnabled(listSuperpositions.getSelectedValue().isConfigurable());
		}
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.labelLblSuperpositionOrder = new JLabel();
		this.scrollPaneListSuperpositions = new JScrollPane();
		this.listSuperpositions = new JList<>();
		this.buttonUp = new JButton();
		this.buttonDown = new JButton();
		this.buttonConfig = new JButton();
		this.buttonToggleDraw = new JButton();

		//======== this ========
		setLayout(null);

		//---- labelLblSuperpositionOrder ----
		this.labelLblSuperpositionOrder.setText("Ordre des superpositions"); //$NON-NLS-1$ //NON-NLS
		add(this.labelLblSuperpositionOrder);
		this.labelLblSuperpositionOrder.setBounds(new Rectangle(new Point(10, 35), this.labelLblSuperpositionOrder.getPreferredSize()));

		//======== scrollPaneListSuperpositions ========
		{
			this.scrollPaneListSuperpositions.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			//---- listSuperpositions ----
			this.listSuperpositions.addListSelectionListener(e -> listSuperpositionsValueChanged(e));
			this.scrollPaneListSuperpositions.setViewportView(this.listSuperpositions);
		}
		add(this.scrollPaneListSuperpositions);
		this.scrollPaneListSuperpositions.setBounds(10, 55, 325, 520);

		//---- buttonUp ----
		this.buttonUp.setText("U"); //$NON-NLS-1$ //NON-NLS
		this.buttonUp.setToolTipText("Monter la superposition dans la hi\u00e9rarchie"); //$NON-NLS-1$ //NON-NLS
		this.buttonUp.setFocusable(false);
		this.buttonUp.addActionListener(e -> buttonUpActionPerformed(e));
		add(this.buttonUp);
		this.buttonUp.setBounds(10, 585, 40, 40);

		//---- buttonDown ----
		this.buttonDown.setText("D"); //$NON-NLS-1$ //NON-NLS
		this.buttonDown.setToolTipText("Descendre la superposition dans la hi\u00e9rarchie"); //$NON-NLS-1$ //NON-NLS
		this.buttonDown.setFocusable(false);
		this.buttonDown.addActionListener(e -> buttonDownActionPerformed(e));
		add(this.buttonDown);
		this.buttonDown.setBounds(58, 585, 40, 40);

		//---- buttonConfig ----
		this.buttonConfig.setText("C"); //$NON-NLS-1$ //NON-NLS
		this.buttonConfig.setToolTipText("Configurer la superposition"); //$NON-NLS-1$ //NON-NLS
		this.buttonConfig.setFocusable(false);
		this.buttonConfig.addActionListener(e -> buttonConfigActionPerformed(e));
		add(this.buttonConfig);
		this.buttonConfig.setBounds(106, 585, 40, 40);

		//---- buttonToggleDraw ----
		this.buttonToggleDraw.setText("D\u00e9sactiver"); //$NON-NLS-1$ //NON-NLS
		this.buttonToggleDraw.setToolTipText("Activer/D\u00e9sactiver la superposition"); //$NON-NLS-1$ //NON-NLS
		this.buttonToggleDraw.setFocusable(false);
		this.buttonToggleDraw.addActionListener(e -> buttonToggleDrawActionPerformed(e));
		add(this.buttonToggleDraw);
		this.buttonToggleDraw.setBounds(154, 585, 180, 40);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < getComponentCount(); i++) {
				Rectangle bounds = getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			setMinimumSize(preferredSize);
			setPreferredSize(preferredSize);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		listSuperpositions.setCellRenderer(new SuperpositionsListCellRenderer());

		updateListModel();

		UICommonUtils.addIconToButton(buttonUp, UICommonUtils.PATH_BUTTON_ICON_UP);
		UICommonUtils.addIconToButton(buttonDown, UICommonUtils.PATH_BUTTON_ICON_DOWN);
		UICommonUtils.addIconToButton(buttonConfig, UICommonUtils.PATH_BUTTON_ICON_COGS);
	}

	/**
	 * Rafraîchit le {@link ListModel} de la liste de superpositions.
	 */
	private void updateListModel() {
		listModel.clear();

		synchronized (superpositions) {
			superpositions.forEach(s -> listModel.add(superpositions.indexOf(s), s));
		}

		listSuperpositions.setModel(listModel);
	}

	/**
	 * Ajoute un SuperpositionSidePanelListener à la liste des écouteurs.
	 *
	 * @param listener Le SuperpositionSidePanelListener à ajouter
	 */
	public void addSuperpositionSidePanelListener(SuperpositionSidePanelListener listener){
		LISTENERS.add(SuperpositionSidePanelListener.class, listener);
	}

	/**
	 * Envoie un évènement aux écouteurs lors du changement d'ordre des superpositions.
	 */
	private void triggerSuperpositionOrderChangedEvent(){
		for(SuperpositionSidePanelListener listener : LISTENERS.getListeners(SuperpositionSidePanelListener.class))
			listener.superpositionOrderChanged();
	}

	/**
	 * Envoie un évènement aux écouteurs lors d'une demande d'affichage de panneau de configuration de superposition.
	 * @param configSidePanel le panneau de configuration de superposition à afficher
	 */
	private void triggerSuperpositionConfigSidePanelRequestedEvent(SidePanel configSidePanel){
		for(SuperpositionSidePanelListener listener : LISTENERS.getListeners(SuperpositionSidePanelListener.class))
			listener.superpositionConfigSidePanelRequested(configSidePanel);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel labelLblSuperpositionOrder;
	private JScrollPane scrollPaneListSuperpositions;
	private JList<Superposition> listSuperpositions;
	private JButton buttonUp;
	private JButton buttonDown;
	private JButton buttonConfig;
	private JButton buttonToggleDraw;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
