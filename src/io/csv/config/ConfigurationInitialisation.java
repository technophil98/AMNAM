package io.csv.config;

import io.csv.ConfigurationCSV;
import io.csv.Entete;
import io.csv.EnteteException;
import io.csv.Ligne;
import io.csv.TypesCSV;
import io.csv.config.ConfigurationInitialisation.LigneInitialisation;



/**
 * Configuration facilitant la lecture d'un fichier d'initialisation.
 * @author Pascal Dally-Bélanger
 *
 */
public class ConfigurationInitialisation implements ConfigurationCSV<LigneInitialisation> {


	@Override
	public Entete getEntete() {
		try {
			return new Entete(new String[] {
					"#@Id_vehicle", "Type_vehicle", "Capability_DRSC"
			}, new TypesCSV[] {
					TypesCSV.INTEGER, TypesCSV.STRING, TypesCSV.BOOLEAN
			});
		} catch (EnteteException e) {
			throw new RuntimeException("Mauvaise configuration");
		}
	}

	@Override
	public LigneInitialisation convertirLigne(Ligne ligne) {
		return new LigneInitialisation(ligne);
	}

	@Override
	public String getRegex() {
		return ";";
	}

	/**
	 * Ligne d'un fichier d'initialisation.
	 * @author Pascal Dally-Bélanger
	 *
	 */
	public static class LigneInitialisation extends Ligne {

		/**
		 * Initialise une nouvelle LigneInitialisation.
		 * @param ligne La ligne originale.
		 */
		public LigneInitialisation(Ligne ligne) {
			super(ligne.getObjets());
		}

		/*
		 * Code et documentation autogénérés
		 */
		/**
		 * Retourne la valeur de l'entrée "Id_vehicle" dans le document .csv.
		 * @return La valeur de l'entrée "Id_vehicle".
		 */
		public int getIdVehicle() {
			 return super.<Integer>get(0);
		}

		/**
		 * Retourne la valeur de l'entrée "Type_vehicle" dans le document .csv.
		 * @return La valeur de l'entrée "Type_vehicle".
		 */
		public String getTypeVehicle() {
			 return super.<String>get(1);
		}

		/**
		 * Retourne la valeur de l'entrée "Capability_DRSC" dans le document .csv.
		 * L'entrée s'appele "Capability_DRSC" à cause d'une faute de frappe du côté de virage.
		 * @return La valeur de l'entrée "Capability_DRSC".
		 */
		public boolean getCapabilityDSRC() {
			 return super.<Boolean>get(2);
		}




	}
}
