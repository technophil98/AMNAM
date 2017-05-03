import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import io.amnam.AMNAMInfo;
import scenario.Obstacle;
import scenario.events.EventType;
import utils.ZipUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static scenario.Obstacle.OMITTED_POS;

/**
 * Outil servant à ajouter des obstacles à un FichierAMNAM.
 *
 * @author Philippe Rivest
 */
public class AddObstacles {

//	NDP: -228.57 , 321.01
//	NDP: -37.07 , 554.45
//	BLOCK: -326.07 , 388.92
//	VARIATEUR: -174.72 ,  628.46
//	VARIATEUR: 374.26,  456.30
//	BLOCK: 283.49, -7.46


	public static final int RADIUS = 15;
	public final static Obstacle[] OBSTACLES_TO_ADD = {
			new Obstacle(1, EventType.POTHOLE, new Point2D.Double( -228.57 , 321.01), RADIUS),
			new Obstacle(2, EventType.POTHOLE, new Point2D.Double(-37.07 , 554.45),RADIUS),
			new Obstacle(3, EventType.BLOCKAGE, new Point2D.Double(-326.07 , 388.92),RADIUS),
			new Obstacle(4, EventType.BLOCKAGE, new Point2D.Double(283.49, -7.46),RADIUS),
			new Obstacle(5, EventType.VARIATOR, new Point2D.Double(-174.72 ,  628.46),RADIUS),
			new Obstacle(6, EventType.VARIATOR, new Point2D.Double( 374.26,  456.30),RADIUS),
			new Obstacle(7, EventType.SNOW, OMITTED_POS,0),
			new Obstacle(8, EventType.ICE, OMITTED_POS,0),
			new Obstacle(9, EventType.RAIN, OMITTED_POS,0)
	};
	public static final String AMNAM_INFO_JSON = "amnam_info.json";

	/**
	 * Point d'entrée de la classe.
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {

		Gson GSON = new GsonBuilder().setPrettyPrinting().create();

		FileDialog fileDialog = new FileDialog((Frame) null, "Fichiers a convertir", FileDialog.LOAD);

		fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".amnam"));
		fileDialog.setMultipleMode(false);
		fileDialog.setVisible(true);
		fileDialog.dispose();
		final File[] fichiers = fileDialog.getFiles();

		if (fichiers != null && fichiers.length != 0) {

			Path fichierAMANM = fichiers[0].toPath();

			try(FileSystem fs = FileSystems.newFileSystem(fichierAMANM, null)) {

				JsonReader reader = new JsonReader(new InputStreamReader(Files.newInputStream(fs.getPath(AMNAM_INFO_JSON))));
				AMNAMInfo info = GSON.fromJson(reader, AMNAMInfo.class);

				info.setObstacles(OBSTACLES_TO_ADD);

				ZipUtil.serializeJSONToZipFileSystem(fs, info, AMNAM_INFO_JSON);

			}catch (JsonIOException | JsonSyntaxException | IOException e) {
				System.out.println("Le fichier info " + AMNAM_INFO_JSON + " n'est pas lisible.");
				e.printStackTrace();
			}
		}
	}

}
