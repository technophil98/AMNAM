/*
 * Created by JFormDesigner on Fri Feb 24 13:46:38 EST 2017
 */

package ui.fenetres;

import io.amnam.FichierAMNAM;
import math.Statistics;
import math.chart.AbstractChartContent;
import math.chart.ChartScatterPlot;
import ui.composants.ColorChooserPanel;
import ui.panneaux.ChartPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Fenêtre affichant les diverses statistiques et graphiques générés par l'application.
 *
 * @author Philippe Rivest
 */
public class StatsFrame extends JFrame {

	private static final long serialVersionUID = 7572938093171670041L;
	private final FichierAMNAM fichierAMNAM;

	/**
	 * Instancies un nouveau StatsFrame.
	 * @param fichierAMNAM Un fichier contenant des données analysés
	 * @throws InstantiationException Exception lancée si le fichierAMNAM ne contient pas d'évènements analysés
	 */
	public StatsFrame(FichierAMNAM fichierAMNAM) throws InstantiationException {
		this.fichierAMNAM = fichierAMNAM;

		if (!fichierAMNAM.isAnalysedEventsLoaded())
			throw new InstantiationException("Fichier " + fichierAMNAM.getCheminDAcces() + " n'est pas analysé. Impossible d'afficher de statistiques.");

		initComponents();
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner de valeur minimale X.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerAxisXMinStateChanged(ChangeEvent e) {
		chartPane.setxMin(((double) spinnerAxisXMin.getValue()));
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner de valeur maximale X.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerAxisXMaxStateChanged(ChangeEvent e) {
		chartPane.setxMax(((double) spinnerAxisXMax.getValue()));
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner du pas de l'axe X.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerAxisXStepStateChanged(ChangeEvent e) {
		chartPane.setStepX(((double) spinnerAxisXStep.getValue()));
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner de valeur minimale Y.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerAxisYMinStateChanged(ChangeEvent e) {
		chartPane.setyMin(((double) spinnerAxisYMin.getValue()));
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner de valeur maximale Y.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerAxisYMaxStateChanged(ChangeEvent e) {
		chartPane.setyMax(((double) spinnerAxisYMax.getValue()));
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner du pas de l'axe Y.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerAxisYStepStateChanged(ChangeEvent e) {
		chartPane.setStepY((double) spinnerAxisYStep.getValue());
	}


	/**
	 * Méthode exécutée lors d'un changement de couleur du ColorChooser de contenu.
	 * @param actionEvent Évènement de changement du spinner
	 */
	private void colorChooserContentActionPerformed(ActionEvent actionEvent) {
		chartPane.setColorContent(colorChooserContent.getColor());
	}

	/**
	 * Méthode exécutée lors d'un changement de couleur du ColorChooser de l'axe X.
	 * @param actionEvent Évènement de changement du spinner
	 */
	private void colorChooserAxisXActionPerformed(ActionEvent actionEvent) {
		chartPane.setColorAxisX(colorChooserAxisX.getColor());
	}

	/**
	 * Méthode exécutée lors d'un changement de couleur du ColorChooser de l'axe Y.
	 * @param actionEvent Évènement de changement du spinner
	 */
	private void colorChooserAxisYActionPerformed(ActionEvent actionEvent) {
		chartPane.setColorAxisY(colorChooserAxisY.getColor());
	}

	/**
	 * Méthode exécutée lors d'un changement de la sélection de la liste déroulante.
	 * @param e Évènement de changement de la liste déroulante
	 */
	private void comboBoxGraphContentChoiceActionPerformed(ActionEvent e) {
		chartPane.setChartContent(((AbstractChartContent) comboBoxGraphContentChoice.getSelectedItem()));
	}

	/**
	 * Méthode exécutée lors du click sur l'une des trois cases à cocher.
	 * @param e L'évènement de clic
	 */
	private void checkBoxShowActionPerformed(ActionEvent e) {
		chartPane.setGridEnabled(checkBoxShowGrid.isSelected());
		chartPane.setAxisXEnabled(checkBoxShowAxisX.isSelected());
		chartPane.setAxisYEnabled(checkBoxShowAxisY.isSelected());
	}

	/**
	 * Méthode exécutée lorsque le {@link StatsFrame#chartPane} ajuste automatiquement son zoom.
	 */
	private void chartViewportWasChanged() {
		spinnerAxisXMin.setValue(chartPane.getxMin());
		spinnerAxisXMax.setValue(chartPane.getxMax());
		spinnerAxisXStep.setValue(chartPane.getStepX());

		spinnerAxisYMin.setValue(chartPane.getyMin());
		spinnerAxisYMax.setValue(chartPane.getyMax());
		spinnerAxisYStep.setValue(chartPane.getStepY());
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.panelGraph = new JPanel();
		this.panelGraphSettings = new JPanel();
		this.labelLblGraphChoice = new JLabel();
		this.comboBoxGraphContentChoice = new JComboBox<>();
		this.labelLblColorLine = new JLabel();
		this.checkBoxShowGrid = new JCheckBox();
		this.colorChooserContent = new ColorChooserPanel();
		this.panelAxisSettingsX = new JPanel();
		this.labelLblAxisXMin = new JLabel();
		this.labelLblAxisXMax = new JLabel();
		this.labelLblAxisXStep = new JLabel();
		this.checkBoxShowAxisX = new JCheckBox();
		this.labelLblAxisXColor = new JLabel();
		this.colorChooserAxisX = new ColorChooserPanel();
		this.spinnerAxisXMin = new JSpinner();
		this.spinnerAxisXMax = new JSpinner();
		this.spinnerAxisXStep = new JSpinner();
		this.panelAxisSettingsY = new JPanel();
		this.labelLblAxisYMin = new JLabel();
		this.labelLblAxisYMax = new JLabel();
		this.labelLblAxisYStep = new JLabel();
		this.checkBoxShowAxisY = new JCheckBox();
		this.labelLblAxisYColor = new JLabel();
		this.colorChooserAxisY = new ColorChooserPanel();
		this.spinnerAxisYMin = new JSpinner();
		this.spinnerAxisYMax = new JSpinner();
		this.spinnerAxisYStep = new JSpinner();
		this.chartPane = new ChartPane();
		this.scrollPaneStats = new JScrollPane();
		this.panelStats = new JPanel();
		this.textAreaLblDetectedEvents = new JTextArea();
		this.labelDetectedEvents = new JLabel();
		this.textAreaLblExpectedEvents = new JTextArea();
		this.labelExpectedEvents = new JLabel();
		this.textAreaLblArchivedEvents = new JTextArea();
		this.labelArchivedEvents = new JLabel();
		this.textAreaLblAccuracy = new JTextArea();
		this.labelAccuracy = new JLabel();
		this.textAreaLblPrecision = new JTextArea();
		this.labelPrecision = new JLabel();

		//======== this ========
		setTitle("Statistiques et graphiques | AMNAM"); //$NON-NLS-1$ //NON-NLS
		setMinimumSize(new Dimension(960, 480));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panelGraph ========
		{
			this.panelGraph.setBorder(new TitledBorder("Graphique")); //$NON-NLS-1$ //NON-NLS
			this.panelGraph.setMinimumSize(new Dimension(272, 425));
			this.panelGraph.setLayout(new BorderLayout());

			//======== panelGraphSettings ========
			{
				this.panelGraphSettings.setMinimumSize(new Dimension(250, 0));
				this.panelGraphSettings.setPreferredSize(new Dimension(250, 0));
				this.panelGraphSettings.setBorder(new TitledBorder("Param\u00e8tres du graphique")); //$NON-NLS-1$ //NON-NLS
				this.panelGraphSettings.setLayout(null);

				//---- labelLblGraphChoice ----
				this.labelLblGraphChoice.setText("Choix du graphique :"); //$NON-NLS-1$ //NON-NLS
				this.panelGraphSettings.add(this.labelLblGraphChoice);
				this.labelLblGraphChoice.setBounds(10, 20, 180, this.labelLblGraphChoice.getPreferredSize().height);

				//---- comboBoxGraphContentChoice ----
				this.comboBoxGraphContentChoice.addActionListener(e -> comboBoxGraphContentChoiceActionPerformed(e));
				this.panelGraphSettings.add(this.comboBoxGraphContentChoice);
				this.comboBoxGraphContentChoice.setBounds(10, 40, 230, this.comboBoxGraphContentChoice.getPreferredSize().height);

				//---- labelLblColorLine ----
				this.labelLblColorLine.setText("Couleur contenu :"); //$NON-NLS-1$ //NON-NLS
				this.panelGraphSettings.add(this.labelLblColorLine);
				this.labelLblColorLine.setBounds(10, 75, 135, 16);

				//---- checkBoxShowGrid ----
				this.checkBoxShowGrid.setText("Afficher la grille"); //$NON-NLS-1$ //NON-NLS
				this.checkBoxShowGrid.setSelected(true);
				this.checkBoxShowGrid.setHorizontalAlignment(SwingConstants.LEFT);
				this.checkBoxShowGrid.addActionListener(e -> checkBoxShowActionPerformed(e));
				this.panelGraphSettings.add(this.checkBoxShowGrid);
				this.checkBoxShowGrid.setBounds(10, 95, 225, 20);

				//---- colorChooserContent ----
				this.colorChooserContent.setColor(Color.white);
				this.colorChooserContent.setBackground(UIManager.getColor("List.selectionBackground")); //$NON-NLS-1$ //NON-NLS
				this.panelGraphSettings.add(this.colorChooserContent);
				this.colorChooserContent.setBounds(220, 75, 15, 15);

				//======== panelAxisSettingsX ========
				{
					this.panelAxisSettingsX.setBorder(new TitledBorder("Axe des x")); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsX.setLayout(null);

					//---- labelLblAxisXMin ----
					this.labelLblAxisXMin.setText("Minimum :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsX.add(this.labelLblAxisXMin);
					this.labelLblAxisXMin.setBounds(10, 20, 75, this.labelLblAxisXMin.getPreferredSize().height);

					//---- labelLblAxisXMax ----
					this.labelLblAxisXMax.setText("Maximum :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsX.add(this.labelLblAxisXMax);
					this.labelLblAxisXMax.setBounds(10, 51, 75, 16);

					//---- labelLblAxisXStep ----
					this.labelLblAxisXStep.setText("Pas :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsX.add(this.labelLblAxisXStep);
					this.labelLblAxisXStep.setBounds(10, 82, 75, 16);

					//---- checkBoxShowAxisX ----
					this.checkBoxShowAxisX.setText("Afficher l'axe"); //$NON-NLS-1$ //NON-NLS
					this.checkBoxShowAxisX.setSelected(true);
					this.checkBoxShowAxisX.addActionListener(e -> checkBoxShowActionPerformed(e));
					this.panelAxisSettingsX.add(this.checkBoxShowAxisX);
					this.checkBoxShowAxisX.setBounds(5, 128, 215, this.checkBoxShowAxisX.getPreferredSize().height);

					//---- labelLblAxisXColor ----
					this.labelLblAxisXColor.setText("Couleur axe :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsX.add(this.labelLblAxisXColor);
					this.labelLblAxisXColor.setBounds(new Rectangle(new Point(10, 107), this.labelLblAxisXColor.getPreferredSize()));

					//---- colorChooserAxisX ----
					this.colorChooserAxisX.setColor(Color.white);
					this.colorChooserAxisX.setBackground(UIManager.getColor("TextComponent.selectionBackgroundInactive")); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsX.add(this.colorChooserAxisX);
					this.colorChooserAxisX.setBounds(205, 110, 15, 15);

					//---- spinnerAxisXMin ----
					this.spinnerAxisXMin.setModel(new SpinnerNumberModel(0.0, null, null, 1.0));
					this.spinnerAxisXMin.addChangeListener(e -> spinnerAxisXMinStateChanged(e));
					this.panelAxisSettingsX.add(this.spinnerAxisXMin);
					this.spinnerAxisXMin.setBounds(145, 15, 75, this.spinnerAxisXMin.getPreferredSize().height);

					//---- spinnerAxisXMax ----
					this.spinnerAxisXMax.setModel(new SpinnerNumberModel(10.0, null, null, 1.0));
					this.spinnerAxisXMax.addChangeListener(e -> spinnerAxisXMaxStateChanged(e));
					this.panelAxisSettingsX.add(this.spinnerAxisXMax);
					this.spinnerAxisXMax.setBounds(145, 45, 75, 26);

					//---- spinnerAxisXStep ----
					this.spinnerAxisXStep.setModel(new SpinnerNumberModel(1.0, 0.1, null, 0.5));
					this.spinnerAxisXStep.addChangeListener(e -> spinnerAxisXStepStateChanged(e));
					this.panelAxisSettingsX.add(this.spinnerAxisXStep);
					this.spinnerAxisXStep.setBounds(145, 80, 75, 26);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < this.panelAxisSettingsX.getComponentCount(); i++) {
							Rectangle bounds = this.panelAxisSettingsX.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = this.panelAxisSettingsX.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						this.panelAxisSettingsX.setMinimumSize(preferredSize);
						this.panelAxisSettingsX.setPreferredSize(preferredSize);
					}
				}
				this.panelGraphSettings.add(this.panelAxisSettingsX);
				this.panelAxisSettingsX.setBounds(10, 120, 230, 155);

				//======== panelAxisSettingsY ========
				{
					this.panelAxisSettingsY.setBorder(new TitledBorder("Axe des y")); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsY.setLayout(null);

					//---- labelLblAxisYMin ----
					this.labelLblAxisYMin.setText("Minimum :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsY.add(this.labelLblAxisYMin);
					this.labelLblAxisYMin.setBounds(10, 20, 75, this.labelLblAxisYMin.getPreferredSize().height);

					//---- labelLblAxisYMax ----
					this.labelLblAxisYMax.setText("Maximum :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsY.add(this.labelLblAxisYMax);
					this.labelLblAxisYMax.setBounds(10, 51, 75, 16);

					//---- labelLblAxisYStep ----
					this.labelLblAxisYStep.setText("Pas :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsY.add(this.labelLblAxisYStep);
					this.labelLblAxisYStep.setBounds(10, 82, 75, 16);

					//---- checkBoxShowAxisY ----
					this.checkBoxShowAxisY.setText("Afficher l'axe"); //$NON-NLS-1$ //NON-NLS
					this.checkBoxShowAxisY.setSelected(true);
					this.checkBoxShowAxisY.addActionListener(e -> checkBoxShowActionPerformed(e));
					this.panelAxisSettingsY.add(this.checkBoxShowAxisY);
					this.checkBoxShowAxisY.setBounds(5, 128, 215, this.checkBoxShowAxisY.getPreferredSize().height);

					//---- labelLblAxisYColor ----
					this.labelLblAxisYColor.setText("Couleur axe :"); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsY.add(this.labelLblAxisYColor);
					this.labelLblAxisYColor.setBounds(new Rectangle(new Point(10, 107), this.labelLblAxisYColor.getPreferredSize()));

					//---- colorChooserAxisY ----
					this.colorChooserAxisY.setColor(Color.white);
					this.colorChooserAxisY.setBackground(UIManager.getColor("TextComponent.selectionBackgroundInactive")); //$NON-NLS-1$ //NON-NLS
					this.panelAxisSettingsY.add(this.colorChooserAxisY);
					this.colorChooserAxisY.setBounds(205, 110, 15, 15);

					//---- spinnerAxisYMin ----
					this.spinnerAxisYMin.setModel(new SpinnerNumberModel(0.0, null, null, 1.0));
					this.spinnerAxisYMin.addChangeListener(e -> spinnerAxisYMinStateChanged(e));
					this.panelAxisSettingsY.add(this.spinnerAxisYMin);
					this.spinnerAxisYMin.setBounds(145, 15, 75, this.spinnerAxisYMin.getPreferredSize().height);

					//---- spinnerAxisYMax ----
					this.spinnerAxisYMax.setModel(new SpinnerNumberModel(10.0, null, null, 1.0));
					this.spinnerAxisYMax.addChangeListener(e -> spinnerAxisYMaxStateChanged(e));
					this.panelAxisSettingsY.add(this.spinnerAxisYMax);
					this.spinnerAxisYMax.setBounds(145, 45, 75, 26);

					//---- spinnerAxisYStep ----
					this.spinnerAxisYStep.setModel(new SpinnerNumberModel(1.0, 0.1, null, 0.5));
					this.spinnerAxisYStep.addChangeListener(e -> spinnerAxisYStepStateChanged(e));
					this.panelAxisSettingsY.add(this.spinnerAxisYStep);
					this.spinnerAxisYStep.setBounds(145, 80, 75, 26);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < this.panelAxisSettingsY.getComponentCount(); i++) {
							Rectangle bounds = this.panelAxisSettingsY.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = this.panelAxisSettingsY.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						this.panelAxisSettingsY.setMinimumSize(preferredSize);
						this.panelAxisSettingsY.setPreferredSize(preferredSize);
					}
				}
				this.panelGraphSettings.add(this.panelAxisSettingsY);
				this.panelAxisSettingsY.setBounds(10, 280, 230, 155);

				{ // compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < this.panelGraphSettings.getComponentCount(); i++) {
						Rectangle bounds = this.panelGraphSettings.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = this.panelGraphSettings.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					this.panelGraphSettings.setMinimumSize(preferredSize);
					this.panelGraphSettings.setPreferredSize(preferredSize);
				}
			}
			this.panelGraph.add(this.panelGraphSettings, BorderLayout.WEST);

			//---- chartPane ----
			this.chartPane.setBorder(null);
			this.chartPane.setxMin(-0.0);
			this.chartPane.setyMin(-0.0);
			this.panelGraph.add(this.chartPane, BorderLayout.CENTER);
		}
		contentPane.add(this.panelGraph, BorderLayout.CENTER);

		//======== scrollPaneStats ========
		{
			this.scrollPaneStats.setBorder(new TitledBorder("Statistiques")); //$NON-NLS-1$ //NON-NLS
			this.scrollPaneStats.setMinimumSize(new Dimension(200, 42));
			this.scrollPaneStats.setPreferredSize(new Dimension(200, 119));
			this.scrollPaneStats.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			//======== panelStats ========
			{
				this.panelStats.setLayout(new GridBagLayout());
				((GridBagLayout)this.panelStats.getLayout()).columnWidths = new int[] {130, 25, 0};
				((GridBagLayout)this.panelStats.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)this.panelStats.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
				((GridBagLayout)this.panelStats.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

				//---- textAreaLblDetectedEvents ----
				this.textAreaLblDetectedEvents.setText("Nb d'\u00e9v\u00e8nements d\u00e9tect\u00e9s"); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblDetectedEvents.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblDetectedEvents.setLineWrap(true);
				this.textAreaLblDetectedEvents.setWrapStyleWord(true);
				this.textAreaLblDetectedEvents.setEditable(false);
				this.textAreaLblDetectedEvents.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
				this.panelStats.add(this.textAreaLblDetectedEvents, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
					new Insets(0, 5, 5, 5), 0, 0));

				//---- labelDetectedEvents ----
				this.labelDetectedEvents.setText("0"); //$NON-NLS-1$ //NON-NLS
				this.labelDetectedEvents.setHorizontalAlignment(SwingConstants.TRAILING);
				this.panelStats.add(this.labelDetectedEvents, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- textAreaLblExpectedEvents ----
				this.textAreaLblExpectedEvents.setText("Nb d'\u00e9v\u00e8nements attendus"); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblExpectedEvents.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblExpectedEvents.setLineWrap(true);
				this.textAreaLblExpectedEvents.setWrapStyleWord(true);
				this.textAreaLblExpectedEvents.setEditable(false);
				this.textAreaLblExpectedEvents.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
				this.panelStats.add(this.textAreaLblExpectedEvents, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
					new Insets(0, 5, 5, 5), 0, 0));

				//---- labelExpectedEvents ----
				this.labelExpectedEvents.setText("0"); //$NON-NLS-1$ //NON-NLS
				this.labelExpectedEvents.setHorizontalAlignment(SwingConstants.TRAILING);
				this.panelStats.add(this.labelExpectedEvents, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- textAreaLblArchivedEvents ----
				this.textAreaLblArchivedEvents.setText("Nb d'\u00e9v\u00e8nements discart\u00e9s"); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblArchivedEvents.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblArchivedEvents.setLineWrap(true);
				this.textAreaLblArchivedEvents.setWrapStyleWord(true);
				this.textAreaLblArchivedEvents.setEditable(false);
				this.textAreaLblArchivedEvents.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
				this.panelStats.add(this.textAreaLblArchivedEvents, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
					new Insets(0, 5, 5, 5), 0, 0));

				//---- labelArchivedEvents ----
				this.labelArchivedEvents.setText("0"); //$NON-NLS-1$ //NON-NLS
				this.labelArchivedEvents.setHorizontalAlignment(SwingConstants.TRAILING);
				this.panelStats.add(this.labelArchivedEvents, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- textAreaLblAccuracy ----
				this.textAreaLblAccuracy.setText("Distance moyenne de l'obstacle"); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblAccuracy.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblAccuracy.setLineWrap(true);
				this.textAreaLblAccuracy.setWrapStyleWord(true);
				this.textAreaLblAccuracy.setEditable(false);
				this.textAreaLblAccuracy.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
				this.panelStats.add(this.textAreaLblAccuracy, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
					new Insets(0, 5, 5, 5), 0, 0));

				//---- labelAccuracy ----
				this.labelAccuracy.setText("00.00 m"); //$NON-NLS-1$ //NON-NLS
				this.labelAccuracy.setHorizontalAlignment(SwingConstants.TRAILING);
				this.panelStats.add(this.labelAccuracy, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- textAreaLblPrecision ----
				this.textAreaLblPrecision.setText("\u00c9cart de l'obstacle moyen"); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblPrecision.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
				this.textAreaLblPrecision.setLineWrap(true);
				this.textAreaLblPrecision.setWrapStyleWord(true);
				this.textAreaLblPrecision.setEditable(false);
				this.textAreaLblPrecision.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
				this.panelStats.add(this.textAreaLblPrecision, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
					new Insets(0, 5, 5, 5), 0, 0));

				//---- labelPrecision ----
				this.labelPrecision.setText("00.00 m"); //$NON-NLS-1$ //NON-NLS
				this.labelPrecision.setHorizontalAlignment(SwingConstants.TRAILING);
				this.panelStats.add(this.labelPrecision, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
					new Insets(0, 0, 5, 5), 0, 0));
			}
			this.scrollPaneStats.setViewportView(this.panelStats);
		}
		contentPane.add(this.scrollPaneStats, BorderLayout.EAST);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		panelStats.setMaximumSize(this.scrollPaneStats.getSize());

		chartPane.addChartViewportChangeListener(this::chartViewportWasChanged);

		comboBoxGraphContentChoice.addItem(new ChartScatterPlot("Évènements en fonction du temps", Statistics.getFunctionVehicleEventsTime(fichierAMNAM.getVehiculeEvents()), true));
		comboBoxGraphContentChoice.addItem(new ChartScatterPlot("Évènements par 10 secondes", Statistics.getFunctionVehicleEventsTimeInterval(fichierAMNAM.getVehiculeEvents(), 10, fichierAMNAM.getScenario().getTempsMin(), fichierAMNAM.getScenario().getTempsMax()), true));

		colorChooserAxisX.setColor(chartPane.getColorAxisX());
		this.colorChooserAxisX.addActionListener(this::colorChooserAxisXActionPerformed);

		colorChooserAxisY.setColor(chartPane.getColorAxisY());
		this.colorChooserAxisY.addActionListener(this::colorChooserAxisYActionPerformed);

		colorChooserContent.setColor(chartPane.getColorContent());
		this.colorChooserContent.addActionListener(this::colorChooserContentActionPerformed);

		labelDetectedEvents.setText(String.format("%d", fichierAMNAM.getScenario().getEventCount()));
		labelExpectedEvents.setText(String.format("%d", Arrays.stream(fichierAMNAM.getInfo().getObstacles()).filter(obstacle -> !obstacle.isOmitted()).count()));
		labelArchivedEvents.setText(String.format("%d", fichierAMNAM.getAnalysisResults().getDiscardedEvents()));
		labelAccuracy.setText(String.format("%.2f m", fichierAMNAM.getScenario().getAccuracy()));
		labelPrecision.setText(String.format("%.2f m", fichierAMNAM.getScenario().getPrecision()));
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panelGraph;
	private JPanel panelGraphSettings;
	private JLabel labelLblGraphChoice;
	private JComboBox<AbstractChartContent> comboBoxGraphContentChoice;
	private JLabel labelLblColorLine;
	private JCheckBox checkBoxShowGrid;
	private ColorChooserPanel colorChooserContent;
	private JPanel panelAxisSettingsX;
	private JLabel labelLblAxisXMin;
	private JLabel labelLblAxisXMax;
	private JLabel labelLblAxisXStep;
	private JCheckBox checkBoxShowAxisX;
	private JLabel labelLblAxisXColor;
	private ColorChooserPanel colorChooserAxisX;
	private JSpinner spinnerAxisXMin;
	private JSpinner spinnerAxisXMax;
	private JSpinner spinnerAxisXStep;
	private JPanel panelAxisSettingsY;
	private JLabel labelLblAxisYMin;
	private JLabel labelLblAxisYMax;
	private JLabel labelLblAxisYStep;
	private JCheckBox checkBoxShowAxisY;
	private JLabel labelLblAxisYColor;
	private ColorChooserPanel colorChooserAxisY;
	private JSpinner spinnerAxisYMin;
	private JSpinner spinnerAxisYMax;
	private JSpinner spinnerAxisYStep;
	private ChartPane chartPane;
	private JScrollPane scrollPaneStats;
	private JPanel panelStats;
	private JTextArea textAreaLblDetectedEvents;
	private JLabel labelDetectedEvents;
	private JTextArea textAreaLblExpectedEvents;
	private JLabel labelExpectedEvents;
	private JTextArea textAreaLblArchivedEvents;
	private JLabel labelArchivedEvents;
	private JTextArea textAreaLblAccuracy;
	private JLabel labelAccuracy;
	private JTextArea textAreaLblPrecision;
	private JLabel labelPrecision;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
