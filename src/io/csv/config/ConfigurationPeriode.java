package io.csv.config;

import java.util.Iterator;

import io.csv.ConfigurationCSV;
import io.csv.Entete;
import io.csv.EnteteException;
import io.csv.Ligne;
import io.csv.TypesCSV;
import io.csv.config.ConfigurationPeriode.LignePeriode;
import utils.inspection.Inspectable;
import utils.inspection.InspectionElement;

/**
 * Configuration facilitant la lecture de fichier périodique.
 * @author Pascal Dally-Bélanger
 *
 */
public class ConfigurationPeriode implements ConfigurationCSV<LignePeriode> {

	public static final int LONGUEUR_LIGNE = 18;
	
	@Override
	public Entete getEntete() {
		try {
			return new Entete(new String[] {
					"#@Id_vehicle", "Duration",
					"PositionX", "PositionY", "PositionZ",
					"Speed_x", "Speed_y", "Speed_z",
					"Speed", "OrientationZ",
					"Acceleration_x", "Acceleration_y", "Acceleration_z", "Acceleration",
					"Coef_friction", "Id_Road",
					"speed_limit", "Road condition"
			}, new TypesCSV[] {
					TypesCSV.INTEGER, TypesCSV.DOUBLE, //ID, timestamp
					TypesCSV.DOUBLE, TypesCSV.DOUBLE, TypesCSV.DOUBLE, //pos x, y, z
					TypesCSV.DOUBLE, TypesCSV.DOUBLE, TypesCSV.DOUBLE, //vit x, y, z
					TypesCSV.DOUBLE, TypesCSV.DOUBLE, //||vit||, angle vit
					TypesCSV.DOUBLE, TypesCSV.DOUBLE, TypesCSV.DOUBLE, TypesCSV.DOUBLE, //accel x, y, z, norme
					TypesCSV.DOUBLE, //coef friction
					TypesCSV.INTEGER, TypesCSV.DOUBLE, //id route, limite vitesse
					TypesCSV.DOUBLE //condition route
			});
		} catch (EnteteException e) {
			throw new RuntimeException("Mauvaise configuration");
		}
	}

	@Override
	public LignePeriode convertirLigne(Ligne ligne) {
		return new LignePeriode(ligne);
	}

	@Override
	public String getRegex() {
		return ";";
	}

	/**
	 * Une ligne d'un fichier périodique.
	 * @author Pascal Dally-Bélanger
	 */
	@Inspectable(groups = {"Informations g\u00e9n\u00e9rales", "Route actuelle", "Position", "Vitesse", "Acc\u00e9l\u00e9ration"})
	public static class LignePeriode extends Ligne {

		/**
		 * Initialise une nouvelle LignePeriode.
		 * @param ligne La ligne originale.
		 */
		public LignePeriode(Ligne ligne) {
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
		 * Retourne la valeur de l'entrée "Duration" dans le document .csv.
		 * @return La valeur de l'entrée "Duration".
		 */
		public double getDuration() {
			 return super.<Double>get(1);
		}

		/**
		 * Retourne la valeur de l'entrée "PositionX" dans le document .csv.
		 * @return La valeur de l'entrée "PositionX".
		 */
		@InspectionElement(name = "x", group = "Position", format = "%.4f m")
		public double getPositionX() {
			 return super.<Double>get(2);
		}

		/**
		 * Retourne la valeur de l'entrée "PositionY" dans le document .csv.
		 * @return La valeur de l'entrée "PositionY".
		 */
		@InspectionElement(name = "y", group = "Position", format = "%.4f m")
		public double getPositionY() {
			 return super.<Double>get(3);
		}

		/**
		 * Retourne la valeur de l'entrée "PositionZ" dans le document .csv.
		 * @return La valeur de l'entrée "PositionZ".
		 */
		@InspectionElement(name = "z", group = "Position", format = "%.4f m")
		public double getPositionZ() {
			 return super.<Double>get(4);
		}

		/**
		 * Retourne la valeur de l'entrée "Speed_x" dans le document .csv.
		 * @return La valeur de l'entrée "Speed_x".
		 */
		@InspectionElement(name = "x", group = "Vitesse", format = "%.4f m/s")
		public double getSpeedX() {
			 return super.<Double>get(5);
		}

		/**
		 * Retourne la valeur de l'entrée "Speed_y" dans le document .csv.
		 * @return La valeur de l'entrée "Speed_y".
		 */
		@InspectionElement(name = "y", group = "Vitesse", format = "%.4f m/s")
		public double getSpeedY() {
			 return super.<Double>get(6);
		}

		/**
		 * Retourne la valeur de l'entrée "Speed_z" dans le document .csv.
		 * @return La valeur de l'entrée "Speed_z".
		 */
		@InspectionElement(name = "z", group = "Vitesse", format = "%.4f m/s")
		public double getSpeedZ() {
			 return super.<Double>get(7);
		}

		/**
		 * Retourne la valeur de l'entrée "Speed" dans le document .csv.
		 * @return La valeur de l'entrée "Speed".
		 */
		@InspectionElement(name = "Norme", group = "Vitesse", format = "%.4f m/s")
		public double getSpeed() {
			 return super.<Double>get(8);
		}

		/**
		 * Retourne la valeur de l'entrée "OrientationZ" dans le document .csv, modifiée pour avoir des valeurs positives seulement.
		 * @return La valeur de l'entrée "OrientationZ".
		 */
		@InspectionElement(name = "Orientation", group = "Vitesse", format = "%.4f rad")
		public double getOrientationZ() {
			 return super.<Double>get(9) + Math.PI/2;
		}

		/**
		 * Retourne la valeur de l'entrée "Acceleration_x" dans le document .csv.
		 * @return La valeur de l'entrée "Acceleration_x".
		 */
		@InspectionElement(name = "x", group = "Acc\u00e9l\u00e9ration", format = "%.4f m/s\u00B2")
		public double getAccelerationX() {
			 return super.<Double>get(10);
		}

		/**
		 * Retourne la valeur de l'entrée "Acceleration_y" dans le document .csv.
		 * @return La valeur de l'entrée "Acceleration_y".
		 */
		@InspectionElement(name = "y", group = "Acc\u00e9l\u00e9ration", format = "%.4f m/s\u00B2")
		public double getAccelerationY() {
			 return super.<Double>get(11);
		}

		/**
		 * Retourne la valeur de l'entrée "Acceleration_z" dans le document .csv.
		 * @return La valeur de l'entrée "Acceleration_z".
		 */
		@InspectionElement(name = "z", group = "Acc\u00e9l\u00e9ration", format = "%.4f m/s\u00B2")
		public double getAccelerationZ() {
			 return super.<Double>get(12);
		}

		/**
		 * Retourne la valeur de l'entrée "Acceleration" dans le document .csv.
		 * @return La valeur de l'entrée "Acceleration".
		 */
		@InspectionElement(name = "Norme", group = "Acc\u00e9l\u00e9ration", format = "%.4f m/s\u00B2")
		public double getAcceleration() {
			 return super.<Double>get(13);
		}

		/**
		 * Retourne la valeur de l'entrée "Coef_friction" dans le document .csv.
		 * @return La valeur de l'entrée "Coef_friction".
		 */
		@InspectionElement(name = "<html>&mu;<sub>C</sub></html>", group = "Informations g\u00e9n\u00e9rales", format = "%.1f")
		public double getCoefFriction() {
			 return super.<Double>get(14);
		}

		/**
		 * Retourne la valeur de l'entrée "Id_Road" dans le document .csv.
		 * @return La valeur de l'entrée "Id_Road".
		 */
		@InspectionElement(name = "ID", group = "Route actuelle", description = "L'ID de la route sur laquelle ce retrouve le v\u00e9hicule")
		public int getIdRoad() {
			 return super.<Integer>get(15);
		}

		/**
		 * Retourne la valeur de l'entrée "speed_limit" dans le document .csv.
		 * @return La valeur de l'entrée "speed_limit".
		 */
		@InspectionElement(name = "Limite de vitesse", group = "Route actuelle", format = "%.0f km/h")
		public double getSpeedLimit() {
			 return super.<Double>get(16);
		}

		/**
		 * Retourne la valeur de l'entrée "Road condition" dans le document .csv.
		 * @return La valeur de l'entrée "Road condition".
		 */
		@InspectionElement(name = "Condition routi\u00e9re", group = "Route actuelle")
		public double getRoadCondition() {
			 return super.<Double>get(17);
		}



	}

	/**
	 * Un objet InfoPeriode est un objet qui permet de facilité l'accès au différentes données d'un fichier périodique.
	 * Cette classe consiste essentiellement d'une liste de LignePeriode.
	 * @author Pascal Dally-Bélanger.
	 *
	 */
	public static class InfoPeriode implements Iterable<LignePeriode> {

		private static final Entete ENTETE = new ConfigurationPeriode().getEntete();
		private LignePeriode[] lignes;
		
		/**
		 * Initialise un nouvel objet InfoPeriode.
		 * @param lignes Les lignes contenues dans le fichier periodique .csv.
		 */
		public InfoPeriode(LignePeriode[] lignes) {
			this.lignes = lignes;
		}
		
		/**
		 * Retourne la première ligne du fichier .csv associé.
		 * @return La première ligne.
		 */
		public LignePeriode getFirstLine() {
			return lignes[0];
		}

		/**
		 * Permet l'interpolation entre deux ligne du fichier .csv.
		 * Cette méthode retourne une ligne fictive qui représente le mieux les informations du véhicules au temps t.
		 * @param t Le temps auquel évaluter l'interpolation.
		 * @return Une ligne interpolée représantant le mieux la valeur au temps t.
		 */
		public LignePeriode interpolation(double t) {
			if(t <= lignes[0].getDuration())
				return lignes[0];
			if(t >= lignes[lignes.length - 1].getDuration())
				return lignes[lignes.length - 1];

			Object[] objets = new Object[ConfigurationPeriode.LONGUEUR_LIGNE];
			int indice = -1;
			for(int i = 0; i < lignes.length; i++) {
				if(t <= lignes[i].getDuration()) {
					indice = i - 1;
					break;
				}
			}
			t -= lignes[indice].getDuration();
			t /= lignes[indice + 1].getDuration() - lignes[indice].getDuration();
			double thetaf = lignes[indice + 1].getOrientationZ();
			double thetai = lignes[indice].getOrientationZ();
			if(Math.abs(thetaf - thetai) < Math.PI)
				objets[9] = thetai * (1 - t) + thetaf * t;
			else
				objets[9] = (lignes[indice].getOrientationZ() + 2*Math.PI);
			for(int i = 0; i < ENTETE.getTypes().length; i++) {
				if(objets[i] == null) {
					switch(ENTETE.getTypes()[i]) {
						case DOUBLE:
							objets[i] = lignes[indice].<Double>get(i) * (1 - t) + lignes[indice + 1].<Double>get(i) * t;
							break;
						default:
							objets[i] = lignes[indice].get(i);
							break;
					}
				}
			}
			return new LignePeriode(new Ligne(objets));
		}
		
		@Override
		public Iterator<LignePeriode> iterator() {
			return new Iterator<LignePeriode>() {
				
				private int i = 0;
				
				@Override
				public boolean hasNext() {
					return i < lignes.length;
				}

				@Override
				public LignePeriode next() {
					return lignes[i++];
				}
				
			};
		}
		
		
	}


}
