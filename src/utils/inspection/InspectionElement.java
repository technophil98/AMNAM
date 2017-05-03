package utils.inspection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marque une méthode ou une propriété comme étant inspectable, c'est-à-dire, qu'un {@link Inspector}
 * utilisera pour la création du panneau d'inspection.
 * @author Pascal Dally-Bélanger
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InspectionElement {

	/**
	 * Le nom de l'élément.
	 * @return Le nom de l'élément.
	 */
	public String name() default "";
	
	/**
	 * Le nom du groupe auquel l'élément appartient.
	 * @return Le nom du groupe auquel l'élément appartient.
	 */
	public String group() default "Default";
	
	/**
	 * La description de l'élément qui apparaîtera dans un infobulles.
	 * @return La description de l'élément.
	 */
	public String description() default "";
	
	/**
	 * Le format à utiliser pour la traduction de l'objet que l'élément donne à l'{@link Inspector} en {@linkplain String}.
	 * @return Le format de l'élément.
	 */
	public String format() default "%s";
	
	/**
	 * Détermine si l'élément apparait sur le panneau d'inspection.
	 * @return Vrai, si l'élément apparait sur le panneau d'inspection.
	 */
	public boolean displayed() default true;
	
}
