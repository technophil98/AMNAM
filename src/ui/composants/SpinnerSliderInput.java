/*
 * Created by JFormDesigner on Fri Nov 25 08:49:25 EST 2016
 */

package ui.composants;

import ui.event.SpinnerSliderInputChangeEvent;
import ui.event.SpinnerSliderInputChangeListener;
import utils.UtilStrings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * Composant graphique personalisé (Java Bean) de prise de valeur numérique continue.
 * Comprenant un JSlider et un JSpinner conjointement dans le même composant, il permet un choix de valeur rapide et précis.
 *
 * @author Philippe Rivest
 */
public class SpinnerSliderInput extends JPanel implements Serializable {

	public static final long serialVersionUID = 1L;


	private static final String NOM_DEFAUT = "Valeur";
	private static final int TAILLE_POLICE_MAX = 11;
	private static final int NB_LIGNES_OVERFLOW = 3;

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();

	private String nomValeur;
	private double valeur = 0;

	private double valeurParDefaut = 0;

	private double minimum = 0;
	private double maximum = 100;
	private double increment = 1.0;
	private double precisionSlider = 1 / increment;
	private static final double NB_TICKS = 5;

	/**
	 * Constructeur vide.
	 * Normalisé pour les Java Beans.
	 * Donne le nom « Valeur » par défaut et référencie le constructeur avec un nom en paramètre.
	 */
	public SpinnerSliderInput() {
		this(NOM_DEFAUT);
	}

	/**
	 * Constructeur avec le nom de valeur.
	 * Donne la plage de données par défaut et référencie le constructeur complet.
	 * @param nomValeur Le nom de la valeur à afficher
	 */
	public SpinnerSliderInput(String nomValeur) {
		this(nomValeur,0.0,100.0,1.0);
	}

	/**
	 * Constructeur complet sans valeur initiale.
	 * Ajuste la valeur par défaut au minimum de la plage de données et référencie le constructeur complet.
	 *
	 * @param nomValeur        Le nom de la valeur à afficher
	 * @param valeurMinSpinner Valeur maximale de la plage de donnée
	 * @param valeurMaxSpinner Valeur minimale de la plage de donnée
	 * @param incrementSpinner L'incrément de valeur affiché sur le JSpinner
	 */
	public SpinnerSliderInput(String nomValeur, double valeurMinSpinner,  double valeurMaxSpinner, double incrementSpinner) {
		this(nomValeur,valeurMinSpinner,valeurMinSpinner,valeurMaxSpinner,incrementSpinner);
	}

	/**
	 * Constructeur complet du SpinnerSliderInput.
	 *
	 * @param nomValeur        Le nom de la valeur à afficher
	 * @param valeurInitiale   Valeur initiale du composant
	 * @param minimum          Valeur maximale de la plage de donnée
	 * @param maximum          Valeur minimale de la plage de donnée
	 * @param incrementSpinner L'incrément de valeur affiché sur le JSpinner
	 */
	public SpinnerSliderInput(String nomValeur, double valeurInitiale, double minimum, double maximum, double incrementSpinner) {

		if (nomValeur != null)
			this.nomValeur = nomValeur;
		else
			this.nomValeur = NOM_DEFAUT;

		this.valeur = valeurInitiale;
		this.minimum = minimum;
		this.maximum = maximum;

		if (incrementSpinner != 0)
			this.increment = incrementSpinner;
		else
			this.increment = 1.0;

		initComponents();
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du spinner.
	 * @param e Évènement de changement du spinner
	 */
	private void spinnerStateChanged(ChangeEvent e) {
		this.valeur = (double) this.spinner.getValue();
		this.slider.setValue((int)Math.round(valeur * precisionSlider));
		this.throwChangeEvent();
	}

	/**
	 * Méthode exécutée lors d'un changement de valeur du slider.
	 * @param e Évènement de changement du slider
	 */
	private void sliderStateChanged(ChangeEvent e) {
		this.valeur = (double)this.slider.getValue() / precisionSlider;
		this.spinner.setValue(valeur);
		this.throwChangeEvent();
	}

	/**
	 * Ajouter un SpinnerSliderInputChangeListener à l'instance.
	 * @param ecouteur L'écouteur
	 */
	public void addSpinnerSliderInputChangeListener(SpinnerSliderInputChangeListener ecouteur){
		OBJETS_ENREGISTRES.add(SpinnerSliderInputChangeListener.class, ecouteur);
	}

	/**
	 * Lance un évènement SpinnerSliderInputChangeEvent lors d'un changement du JSpinner ou du JSlider.
	 */
	private void throwChangeEvent() {
		for (SpinnerSliderInputChangeListener ecout: OBJETS_ENREGISTRES.getListeners( SpinnerSliderInputChangeListener.class) ) {
			ecout.stateChanged(new SpinnerSliderInputChangeEvent(this));
		}
	}

	/**
	 * Mets le Slider à jour. Il Ajuste:
	 * - Précision
	 * - Maximum
	 * - Minimum
	 * - Ticks
	 * - Labels valeur
	 */
	private void updateSlider() {

		precisionSlider = 1 / increment;

		this.slider.setMaximum((int) Math.round(maximum * precisionSlider));
		this.slider.setMinimum((int) Math.round(minimum * precisionSlider));
		this.slider.setValue((int)Math.round(valeur * precisionSlider));

		this.slider.setMajorTickSpacing((int)( (precisionSlider / NB_TICKS) * (this.maximum - this.minimum)));

		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put((int)(this.minimum * precisionSlider), new JLabel(this.minimum + ""));
		labelTable.put((int)(this.maximum * precisionSlider), new JLabel(this.maximum + ""));

		this.slider.setLabelTable( labelTable );
	}

	/**
	 * Méthode auto-générée de création des composants graphique.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.spinner = new JSpinner();
		this.slider = new JSlider();
		this.labelNomVariable = new JLabel(nomValeur);

		//======== this ========
		setBorder(new EtchedBorder());
		setMinimumSize(new Dimension(290, 90));
		setMaximumSize(null);
		setPreferredSize(null);
		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {104, 183, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		//---- spinner ----
		this.spinner.setModel(new SpinnerNumberModel(0.0, null, null, 1.0));
		this.spinner.addChangeListener(e -> spinnerStateChanged(e));
		add(this.spinner, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
												 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
												 new Insets(0, 0, 5, 0), 0, 0));

		//---- slider ----
		this.slider.setMajorTickSpacing(50);
		this.slider.setPaintLabels(true);
		this.slider.setPaintTicks(true);
		this.slider.setValue(70);
		this.slider.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
		this.slider.addChangeListener(e -> sliderStateChanged(e));
		add(this.slider, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
												GridBagConstraints.CENTER, GridBagConstraints.BOTH,
												new Insets(0, 0, 0, 0), 0, 0));

		//---- labelNomVariable ----
		this.labelNomVariable.setLabelFor(this.spinner);
		this.labelNomVariable.setIconTextGap(0);
		add(this.labelNomVariable, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
														  GridBagConstraints.CENTER, GridBagConstraints.BOTH,
														  new Insets(0, 0, 5, 5), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	/**
	 * Ajuste la taille de charactère de l'étiquette du composant afin que le texte y entre en entier.
	 * Elle retroune si la taille a été ajustée correctement
	 * @return TRUE si l'ajustement est superflu ou correctement effectué ou FALSE si la taille retrounée est trop petite, donc illisible.
	 */
	private boolean setGrosseurTextLabelValeur() {
		Font policeLabel = labelNomVariable.getFont();
		String labelText = labelNomVariable.getText();

		int largeurText = labelNomVariable.getFontMetrics(policeLabel).stringWidth(labelText);
		int largeurLabel = labelNomVariable.getWidth();

		if (largeurLabel != 0) {

			double ratioLargeurs = (double)largeurLabel / (double)largeurText;

			if (ratioLargeurs <= 1) {
				int taillePoliceAjustee = (int) Math.round(policeLabel.getSize() * ratioLargeurs);
				int hauteurLabel = labelNomVariable.getHeight();

				int tailleMaximale = Math.min(taillePoliceAjustee - 1, hauteurLabel);

				if (tailleMaximale >= TAILLE_POLICE_MAX)
					labelNomVariable.setFont(new Font(policeLabel.getName(), Font.PLAIN, tailleMaximale));
				else
					return false;
			}
		}

		return true;
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JSpinner spinner;
	private JSlider slider;
	private JLabel labelNomVariable;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * Retourne le nom de la valeur du SpinnerSliderInput.
	 *
	 * @return le nom de la valeur du SpinnerSliderInput
	 */
	public String getNomValeur() {
		return nomValeur;
	}

	/**
	 * Détermine le nom de la valeur du SpinnerSliderInput.
	 * Ajuste le texte si celui-ci est trop grand pour la JLabel.
	 * La taille de charactère est soit réduite ou la chaîne est divisée sur un certain nombre de lignes.
	 * Lance un évènement de changement du composant.
	 *
	 * @see SpinnerSliderInputChangeEvent
	 * @see UtilStrings#diviserLignesHTML(String, int, boolean)
	 * @param nomValeur le de nom de la valeur du SpinnerSliderInput à déterminer
	 */
	public void setNomValeur(String nomValeur) {
		this.nomValeur = nomValeur;
		this.labelNomVariable.setText(this.nomValeur);

		//Ajustements
		if (!this.setGrosseurTextLabelValeur()) {
			this.labelNomVariable.setText(UtilStrings.diviserLignesHTML(nomValeur, NB_LIGNES_OVERFLOW, false));
		}

		this.throwChangeEvent();
	}

	/**
	 * Retourne le minimum du SpinnerSliderInput.
	 *
	 * @return le minimum du SpinnerSliderInput
	 */
	public double getMinimum() {
		return minimum;
	}

	/**
	 * Détermine le minimum de la plage de valeurs du SpinnerSliderInput.
	 * Mets à jour le JSlider et le JSpinner.
	 * Lance un évènement de changement du composant.
	 *
	 * @see SpinnerSliderInputChangeEvent
	 * @param minimum le minimum de la plage de valeurs du SpinnerSliderInput
	 */
	public void setMinimum(double minimum) {
		this.minimum = minimum;

		((SpinnerNumberModel) this.spinner.getModel()).setMinimum(minimum);
		this.updateSlider();
		this.throwChangeEvent();
	}

	/**
	 * Retourne le maximum du SpinnerSliderInput.
	 *
	 * @return le maximum du SpinnerSliderInput
	 */
	public double getMaximum() {
		return maximum;
	}

	/**
	 * Détermine le maximum de la plage de valeurs du SpinnerSliderInput.
	 * Mets à jour le JSlider et le JSpinner.
	 * Lance un évènement de changement du composant.
	 *
	 * @see SpinnerSliderInputChangeEvent
	 * @param maximum le maximum de la plage de valeurs du SpinnerSliderInput
	 */
	public void setMaximum(double maximum) {
		this.maximum = maximum;

		((SpinnerNumberModel) this.spinner.getModel()).setMaximum(maximum);
		this.updateSlider();
		this.throwChangeEvent();
	}

	/**
	 * Retourne le pas d'incrément du SpinnerSliderInput.
	 *
	 * @return le pas d'incrément du SpinnerSliderInput.
	 */
	public double getIncrement() {
		return increment;
	}

	/**
	 * Détermine le pas d'incrément du SpinnerSliderInput.
	 * Mets à jour le JSlider et le JSpinner.
	 * Lance un évènement de changement du composant.
	 *
	 * @see SpinnerSliderInputChangeEvent
	 * @param increment le pas d'incrément du SpinnerSliderInput
	 */
	public void setIncrement(double increment) {
		this.increment = increment;

		((SpinnerNumberModel) this.spinner.getModel()).setStepSize(increment);
		this.updateSlider();
		this.throwChangeEvent();
	}

	/**
	 * Retourne la valeur entrée dans le SpinnerSliderInput.
	 *
	 * @return la valeur entrée dans le SpinnerSliderInput.
	 */
	public double getValeur() {
		return valeur;
	}

	/**
	 * Détermine la valeur courante du SpinnerSliderInput.
	 * Mets à jour le JSlider et le JSpinner.
	 * Lance un évènement de changement du composant.
	 *
	 * @see SpinnerSliderInputChangeEvent
	 * @param valeur le pas d'incrément du SpinnerSliderInput
	 */
	public void setValeur(double valeur) {
		this.valeur = valeur;
		this.valeurParDefaut = this.valeur;

		this.spinner.setValue(valeur);
		this.updateSlider();

		this.throwChangeEvent();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.slider.setEnabled(this.isEnabled());
		this.spinner.setEnabled(this.isEnabled());
		this.labelNomVariable.setEnabled(this.isEnabled());
	}

	/**
	 * Ajuste la valeur courante du composant à sa valeur par défaut.
	 */
	public void valeurParDefaut(){
		this.setValeur(this.valeurParDefaut);
	}
}
