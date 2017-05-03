/*
 * Created by JFormDesigner on Wed Mar 29 15:35:15 EDT 2017
 */

package ui.panneaux.inspection;

import ui.UICommonUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Un panneau affiché à la tête d'un panneau d'inspection.
 *
 * @author Philippe Rivest
 */
public class InspectionHeaderPanel extends JPanel {

	private static final long serialVersionUID = 1066344617998903422L;
	private static final int LABEL_TITLE_MAX_FONT_SIZE = 36;
	private static final int LABEL_TYPE_MAX_FONT_SIZE = 26;
	private Image image;
	private String title;
	private String type;

	/**
	 * Instancies un nouveau InspectionHeaderPanel.
	 *
	 * @param elements Les éléments de l'objet à affichier dans le panneau. L'ordre est important:
	 *                    	1: {@link Image} 	L'icône de l'objet
	 *                  	2: {@link Integer} 	L'identifiant de l'objet
	 *                		3: {@link String} 	Le type de l'objet
	 */
	public InspectionHeaderPanel(Object[] elements) {
		image = (Image) elements[0];
		title = String.valueOf(elements[1]);
		type = String.valueOf(elements[2]);
		initComponents();
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.labelIcon = new JLabel();
		this.labelTitle = new JLabel();
		this.labelType = new JLabel();

		//======== this ========
		setBorder(new EtchedBorder());
		setPreferredSize(new Dimension(325, 125));
		setMinimumSize(new Dimension(325, 125));
		setLayout(null);

		//---- labelIcon ----
		this.labelIcon.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.labelIcon);
		this.labelIcon.setBounds(10, 5, 110, 110);

		//---- labelTitle ----
		this.labelTitle.setText("300002"); //$NON-NLS-1$ //NON-NLS
		this.labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelTitle.setFont(new Font(".SF NS Text", Font.PLAIN, 36)); //$NON-NLS-1$ //NON-NLS
		add(this.labelTitle);
		this.labelTitle.setBounds(125, 30, 200, 35);

		//---- labelType ----
		this.labelType.setFont(new Font(".SF NS Text", Font.PLAIN, 26)); //$NON-NLS-1$ //NON-NLS
		this.labelType.setForeground(UIManager.getColor("Button.disabledText")); //$NON-NLS-1$ //NON-NLS
		this.labelType.setHorizontalAlignment(SwingConstants.CENTER);
		this.labelType.setText("interactive"); //$NON-NLS-1$ //NON-NLS
		add(this.labelType);
		this.labelType.setBounds(125, 65, 200, 30);

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

		UICommonUtils.addIconToLabel(labelIcon, image);
		labelTitle.setText(title);
		labelType.setText(type);

		UICommonUtils.fitTextToLabel(labelTitle, LABEL_TITLE_MAX_FONT_SIZE);
		UICommonUtils.fitTextToLabel(labelType, LABEL_TYPE_MAX_FONT_SIZE);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel labelIcon;
	private JLabel labelTitle;
	private JLabel labelType;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
