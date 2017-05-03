package utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Classe utilitaire regroupant différentes méthodes portant sur les chaînes de charactères.
 *
 * @author Philippe Rivest
 */
public final class UtilStrings {

    /**
     * Contructeur privé afin d'empêcher toute instanciation.
     */
    private UtilStrings(){}

    /**
     * Divise une châine en nbLignes lignes.
     * La division se fait aux espaces entre les mots.
     * Méthode privée interne de division de chaîne.
     *
     * @param s        la chaîne à diviser
     * @param nbLignes le nombre de lignes à obtenir
     * @param toHTML   détermine si la chaîne divisée en lignes à l'aide de charactères de nouvelle ligne ou en HTML
     * @param center   Vrai si le texte doit être centré
     * @return la chaîne divisée en lignes à l'aide de charactères de nouvelle ligne ou en HTML
     */
    private static String diviserLignes(String s, int nbLignes, boolean toHTML, boolean center){

        if (s != null) {

            List<String> mots = Arrays.asList(s.split("\\s"));

            if (mots.size() == 1)
                return s;

            Iterator<String> iterator = mots.iterator();
            String[] lignes = new String[nbLignes];
            Arrays.fill(lignes, "");

            for (int i = 0; i < lignes.length; i++) {
                while (lignes[i].length() < s.length()/nbLignes && iterator.hasNext()){
                    lignes[i] += iterator.next() + " ";
                }
            }

            if (toHTML)
                if (center)
                    return "<html><center>" + String.join("<br/>", lignes) + "</center></html>";
                else
                    return "<html>" + String.join("<br/>", lignes) + "</html>";
            else
                return String.join("\n",lignes);

        }else
            return s;

    }

    /**
     * Divise une châine en nbLignes lignes.
     * La division se fait aux espaces entre les mots.
     *
     * @param s        la chaîne à diviser
     * @param nbLignes le nombre de lignes à obtenir
     * @return la chaîne divisée en lignes à l'aide de charactères de nouvelle ligne
     */
	public static String diviserLignes(String s, int nbLignes) {
        return UtilStrings.diviserLignes(s, nbLignes, false, false);
    }

    /**
     * Divise une châine en nbLignes lignes.
     * La division se fait aux espaces entre les mots.
     *
     * @param s        la chaîne à diviser
     * @param nbLignes le nombre de lignes à obtenir
     * @param center   Vrai si le texte doit être centré
     * @return la chaîne divisée en lignes en HTML
     */
	public static String diviserLignesHTML(String s, int nbLignes, boolean center){
        return UtilStrings.diviserLignes(s, nbLignes, true, center);
    }

}
