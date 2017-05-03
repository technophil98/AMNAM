package utils.inspection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.swing.JPanel;

/**
 * Cet annotation marque une classe comme étant inspectable par un {@link Inspector}.
 * @author Pascal Dally-Bélanger
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inspectable {
	
	/**
	 * Le nom de l'élément.
	 * @return Le nom de l'élément.
	 */
	public String name() default "";
	/**
	 * La liste des groupes que l'inspectable a.
	 * Dans le panneau d'inspection, chaque group sera dans séparé dans son propre panneau ayant une bordure et le nom du group comme titre.
	 * @return La liste des groupes.
	 */
	public String[] groups() default {"Default"};
	
	/**
	 * La classe qui agit comme entête au panneau d'inspection.
	 * @return La classe qui agit comme entête au panneau d'inspection.
	 */
	public Class<? extends JPanel> header() default JPanel.class;
	/**
	 * Un descriptif de la liste des paramètres à donner au constructeur du panneau retourner par {@link #header()}.
	 * Chaque paramètre est extrait de la liste des {@link InspectionElement} et vien en pair : 
	 * le premier élément est le nom du groupe auquel l'{@link InspectionElement} fait parti,
	 * le deuxième est le nom de l'{@link InspectionElement}.
	 * Chaque paramètre est passé sous forme d'objet au constructeur, sans passer par le formattage en {@link String}.
	 * @return La liste des paramètres au constructeur.
	 */
	public String[] args() default {};
}
