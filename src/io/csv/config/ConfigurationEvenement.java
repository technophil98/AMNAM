package io.csv.config;

import io.csv.ConfigurationCSV;
import io.csv.Entete;
import io.csv.EnteteException;
import io.csv.Ligne;
import io.csv.TypesCSV;
import io.csv.config.ConfigurationEvenement.LigneEvenement;


/**
 * Configuration facilitant la lecture d'un fichier d'événement.
 * @author Pascal Dally-Bélanger
 *
 */
public class ConfigurationEvenement implements ConfigurationCSV<LigneEvenement> {


	@Override
	public Entete getEntete() {
		try {
			return new Entete(new String[] {
					"#@Id_vehicle", "Id_event", "Time",
					"PositionX", "PositionY", "PositionZ",
					"Id_Road"
			}, new TypesCSV[] {
					TypesCSV.INTEGER, TypesCSV.INTEGER, TypesCSV.DOUBLE, 
					TypesCSV.DOUBLE, TypesCSV.DOUBLE, TypesCSV.DOUBLE,
					TypesCSV.DOUBLE
			});
		} catch (EnteteException e) {
			throw new RuntimeException("Mauvaise configuration");
		}
	}

	@Override
	public LigneEvenement convertirLigne(Ligne ligne) {
		return new LigneEvenement(ligne);
	}

	@Override
	public String getRegex() {
		return ";";
	}

	/**
	 * Ligne d'un fichier d'événement.
	 * @author Pascal Dally-Bélanger
	 *
	 */
	public static class LigneEvenement extends Ligne {

		/**
		 * Instancie une nouvelle ligne.
		 * @param ligne la ligne originale.
		 */
		public LigneEvenement(Ligne ligne) {
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
		 * Retourne la valeur de l'entrée "Id_event" dans le document .csv.
		 * @return La valeur de l'entrée "Id_event".
		 */
		public int getIdEvent() {
			 return super.<Integer>get(1);
		}

		/**
		 * Retourne la valeur de l'entrée "Time" dans le document .csv.
		 * @return La valeur de l'entrée "Time".
		 */
		public double getTime() {
			 return super.<Double>get(2);
		}

		/**
		 * Retourne la valeur de l'entrée "PositionX" dans le document .csv.
		 * @return La valeur de l'entrée "PositionX".
		 */
		public double getPositionX() {
			 return super.<Double>get(3);
		}

		/**
		 * Retourne la valeur de l'entrée "PositionY" dans le document .csv.
		 * @return La valeur de l'entrée "PositionY".
		 */
		public double getPositionY() {
			 return super.<Double>get(4);
		}

		/**
		 * Retourne la valeur de l'entrée "PositionZ" dans le document .csv.
		 * @return La valeur de l'entrée "PositionZ".
		 */
		public double getPositionZ() {
			 return super.<Double>get(5);
		}

		/**
		 * Retourne la valeur de l'entrée "Id_Road" dans le document .csv.
		 * @return La valeur de l'entrée "Id_Road".
		 */
		public double getIdRoad() {
			 return super.<Double>get(6);
		}

	}
}
