package ui.event;

import ui.composants.SpinnerSliderInput;

import java.util.EventListener;

/**
 * Écouteur personnalisé pour les composants de type SpinnerSliderInput.
 *
 * @see SpinnerSliderInputChangeEvent
 * @see SpinnerSliderInput
 * @see EventListener
 * @author Philippe Rivest
 */
public interface SpinnerSliderInputChangeListener extends EventListener {

    /**
     * Évènement survenant lors d'un changement dans un composant SpinnerSliderInput.
     *
     * @param e l'évènement de changement d'un composant SpinnerSliderInput
     */
    void stateChanged(SpinnerSliderInputChangeEvent e);

}
