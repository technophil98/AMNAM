import io.amnam.AMNAMInfo;
import io.amnam.FichierAMNAM;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Classe de test utilitaire permettant de convertir les {@link AMNAMInfo} de version 1 des {@link FichierAMNAM} en version 2.
 *
 * @author Philippe Rivest
 */
public class ConverterV1ToV2 {

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 * @throws IOException Erreur lors de la conversion du/des fichier(s)
	 */
	public static void main(String[] args) throws IOException {

		FileDialog fileDialog = new FileDialog((Frame) null, "Fichiers a convertir", FileDialog.LOAD);

		fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".amnam"));
		fileDialog.setMultipleMode(true);
		fileDialog.setVisible(true);
		fileDialog.dispose();

		final File[] fichiers = fileDialog.getFiles();

		if (fichiers != null && fichiers.length != 0){

			for (File fichier : fichiers) {

				ZipFile zipFile = new ZipFile(fichier);

				final ZipEntry entreeInit = zipFile.getEntry("csv_data/init.csv");
				final ZipEntry entreeEvent = zipFile.getEntry("csv_data/event.csv");
				final List<? extends ZipEntry> periodEntries = zipFile.stream().filter(e -> e.getName().contains("period_")).collect(Collectors.toList());

				//Init
				final InputStream entreeInitStream = zipFile.getInputStream(entreeInit);
				final File tmpInit = new File(System.getProperty("java.io.tmpdir") + "/init.csv");
				tmpInit.delete();
				final File initFile = streamToFile(entreeInitStream, tmpInit);

				//Event
				final InputStream entreeEventStream = zipFile.getInputStream(entreeEvent);
				final File tmpEvent = new File(System.getProperty("java.io.tmpdir") + "/event.csv");
				tmpEvent.delete();
				final File eventFile = streamToFile(entreeEventStream, tmpEvent);

				//Period
				File[] periodFiles = new File[periodEntries.size()];

				for (ZipEntry periodEntry : periodEntries) {

					final InputStream periodStream = zipFile.getInputStream(periodEntry);

					final String periodEntryName = periodEntry.getName().substring(periodEntry.getName().lastIndexOf('/') + 1);

					final File tmpPeriod = new File(System.getProperty("java.io.tmpdir") + "/" + periodEntryName);
					tmpPeriod.delete();

					periodFiles[periodEntries.indexOf(periodEntry)] = streamToFile(periodStream, tmpPeriod);
				}

				File newFile = new File(fichier.getAbsolutePath().replace(".amnam", "_v2.amnam"));

				FichierAMNAM.creerFicherAmnam(newFile.toPath(), initFile, eventFile, periodFiles, null, null);

				zipFile.close();
			}

		}
	}

	/**
	 * Transforme une {@link InputStream} en {@link File}.
	 *
	 * @param initialStream La {@link InputStream} à copier
	 * @param file 		Le fichier de destination
	 * @return Le fichier contenant les données de initialStream
	 * @throws IOException Erreur lors de l'écriture
	 */
	private static File streamToFile(InputStream initialStream, File file) throws IOException {
		byte[] buffer = new byte[2048];
		int bytesLus;

		OutputStream outStream = new FileOutputStream(file);

		while ((bytesLus = initialStream.read(buffer)) > 0){
			outStream.write(buffer, 0, bytesLus);
		}

		outStream.close();

		return file;
	}

}
