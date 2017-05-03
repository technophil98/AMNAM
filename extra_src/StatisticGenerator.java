import io.amnam.FichierAMNAM;
import obfuscation.ObfuscatedEventGenerator;
import obfuscation.ObfuscationParameters;
import obfuscation.parameters.EventTypeObfuscationParameters;
import obfuscation.parameters.PacketLossObfuscationParameters;
import obfuscation.parameters.PositionObfuscationParameters;
import scenario.events.ReportedEvent;
import scenario.events.VehicleEvent;
import scenario.events.event_manipulations.AnalysisResult;
import scenario.events.event_manipulations.EventAnalyser;
import scenario.events.event_manipulations.EventGenerator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;

/**
 * Classe utilitaire qui génère des données d'analyse puis les exportent en .csv
 *
 * @author Philippe Rivest
 */
public class StatisticGenerator {


	private final static ObfuscationParameters[] OBFUSCATION_PARAMETERS = {
		new PositionObfuscationParameters(0),
		new PacketLossObfuscationParameters(0),
		new EventTypeObfuscationParameters(0)
	};
	private static final double MAX_SIGMA_OBF_POS = 10.0;
	private static final double OBF_POS_STEP = 0.01;
	private static final String CSV_SEPARATOR = ";";


	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {

		FileDialog fileDialog = new FileDialog((Frame) null, "Fichiers a convertir", FileDialog.LOAD);

		fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".amnam"));
		fileDialog.setMultipleMode(false);
		fileDialog.setVisible(true);

		final File[] fichiers = fileDialog.getFiles();
		fileDialog.dispose();

		if (fichiers != null && fichiers.length != 0) {

			try {

				StringBuilder sb = new StringBuilder("Ecart_type_obf" + CSV_SEPARATOR + "Accuracy\n");

				FichierAMNAM f = new FichierAMNAM(fichiers[0].toPath());

				EventGenerator eventGenerator = new EventGenerator(f, 1, 1000);
				final VehicleEvent[] v_events = eventGenerator.execute();
				f.setVehiculeEvents(v_events);
				f.setReportedEvents(Arrays.stream(v_events).map(ReportedEvent::new).toArray(ReportedEvent[]::new));

				for (double i = 0; i < MAX_SIGMA_OBF_POS; i += OBF_POS_STEP) {

					((PositionObfuscationParameters) OBFUSCATION_PARAMETERS[0]).setStandardDeviation(i);

					ObfuscatedEventGenerator obfuscatedEventGenerator = new ObfuscatedEventGenerator(f, OBFUSCATION_PARAMETERS);
					ReportedEvent[] obfuscatedEvents = obfuscatedEventGenerator.execute();
					f.setObfuscatedEvents(obfuscatedEvents);

					EventAnalyser eventAnalyser = new EventAnalyser(f, null);
					AnalysisResult analysisResult = eventAnalyser.execute();
					f.setAnalysisResult(analysisResult);

					double accuracy = f.getScenario().getAccuracy();

					if (!Double.isNaN(accuracy)) {
						sb.append(i);
						sb.append(CSV_SEPARATOR);
						sb.append(accuracy);
						sb.append('\n');
					}
				}

				System.out.println(sb.toString());

				try(  PrintWriter out = new PrintWriter(String.format("data%tH%<tM%<tS.csv", new Date()) )  ){
					out.println( sb.toString() );
				}

			} catch (IOException e) {
				System.out.println("Impossible de charger le fichier au répertoire: " + fichiers[0].getAbsolutePath());
				e.printStackTrace();
			}
		}

	}

}
