package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Classe utilitaire permettant la gestion de fichiers compressés.
 *
 * @author Philippe Rivest
 * @see com.sun.nio.zipfs.ZipFileSystem
 */
public final class ZipUtil {

	private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Contructeur privé afin d'empêcher toute instanciation.
	 */
	private ZipUtil(){}

	/**
	 * Sérialise un objet en JSON puis l'ajoute à un fichier zip.
	 *
	 * @param fs               Un système de fichier ( « FileSystem » ) ouvert sur le fichier ZIP
	 * @param objetToSerialize L'objet à sérialiser
	 * @param zipFSPath        Une chaîne représentant le répertoire dans le zip où se situera le fichier .json de sérialisation
	 * @throws IOException Une erreur lors de l'écriture du fichier
	 */
	public static void serializeJSONToZipFileSystem(FileSystem fs, Object objetToSerialize, String zipFSPath) throws IOException {

		final Path zipElementPath = fs.getPath(zipFSPath);
		final String jsonContent = GSON.toJson(objetToSerialize);

		if (!Files.exists(zipElementPath)) Files.createDirectories(zipElementPath);

		Files.copy(new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8)), zipElementPath, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Prends un fichier et l'ajoute à un fichier zip.
	 *
	 * @param fs        Un système de fichier ( « FileSystem » ) ouvert sur le fichier ZIP
	 * @param file      Le fichier à ajouter
	 * @param zipFSPath Une chaîne représentant le répertoire dans le zip où se situera le fichier
	 * @throws IOException Une erreur lors de l'écriture du fichier
	 */
	public static void addFileToZipFileSystem(FileSystem fs, File file, String zipFSPath) throws IOException {
		Path filePath = fs.getPath(zipFSPath);
		if (!Files.exists(filePath)) Files.createDirectories(filePath);

		Files.copy(file.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Charge un objet sérialisé en JSON stocké dans un fichier ZIP.
	 *
	 * @see Gson
	 *
	 * @param <T>             Le type de l'objet à charger
	 * @param zipFile         Le répertoire du fichier ZIP
	 * @param pathOfJSONinZip Une chaîne représentant le répertoire dans le zip où se situe le fichier
	 * @param typeToParse     Le type de l'objet à charger
	 * @return L'objet chargé
	 * @throws IOException Une erreur lors de la lecture du fichier ou du chargement de l'objet
	 */
	public static <T> T loadFromJSONinZip(Path zipFile, String pathOfJSONinZip, Class<T> typeToParse) throws IOException {
		try(FileSystem fs = FileSystems.newFileSystem(zipFile, null)) {

			JsonReader reader = new JsonReader(new InputStreamReader(Files.newInputStream(fs.getPath(pathOfJSONinZip))));
			final T parsedObject = GSON.fromJson(reader, typeToParse);

			if (parsedObject == null)
				throw new IOException("Le fichier " + pathOfJSONinZip + " n'est pas valide.");

			return parsedObject;

		}catch (JsonIOException | JsonSyntaxException e) {
			throw new IOException("Le fichier " + pathOfJSONinZip + " n'est pas valide.", e);
		}
	}
}
