package ui.event;

import ui.composants.SpinnerSliderInput;

import javax.swing.event.ChangeEvent;

/**
 * Évènement personnalisé pour les composants de type SpinnerSliderInput.
 *
 * @see SpinnerSliderInputChangeListener
 * @see SpinnerSliderInput
 * @see ChangeEvent
 * @author Philippe Rivest
 */
public class SpinnerSliderInputChangeEvent extends ChangeEvent {

	private static final long serialVersionUID = -4095929253065822977L;

	/**
     * Constructeur complet du SpinnerSliderInputChangeEvent.
     * Instancies un nouveau SpinnerSliderInputChangeEvent.
     *
     * @param source le SpinnerSliderInput source
     */
    public SpinnerSliderInputChangeEvent(SpinnerSliderInput source) {
        super(source);
    }

    /**
     * Retoune la valeur courante du SpinnerSliderInput source.
     *
     * @return la valeur courante du SpinnerSliderInput source
     */
    public double getValeur() {
        return ((SpinnerSliderInput) source).getValeur();
    }
}
