package image;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import analysis.process.Explore;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import com.github.softwarevisualinterface.visual.composite.popout.PopoutAlert;

public class ConvertVisual  {
	
	private final static int DPI_INCREASE = 1;
	
	private static String imagePath;
	private static String sourcePath;
	private static String settingsPath;

	private static Logger logger = LogManager.getLogger();

	public static void assignPaths(String img, String src, String sett) {
		imagePath = img;
		sourcePath = src;
		settingsPath = sett;
	}
	
	public static String generateUMLDiagram(String path, List<String> ignore, String name, boolean inst, boolean func, boolean priv, boolean consta) {
		File f = new File(path);
		Explore.setParameters(inst, func, priv, consta);
		logger.info("Beginning Explore operations");
		Explore e = new Explore(f);
		for(String s : ignore) {
			e.ignorePackage(s);
		}
		e.run();
		DotProcess.setProject(e);
		logger.info("Ending Explore operations");
		logger.info("Beginning draw operations");
		return draw(DotProcess.generateDot(), name, Format.PNG).getAbsolutePath();
	}
	
	public static File draw(String dotData, String name, String type) {
		return draw(dotData, name, Format.valueOf(type));
	}

	public static File draw(String dotData, String name, Format format) {
		File folder = new File(imagePath);
		folder.mkdir();
		System.out.println( settingsPath + "/config.txt");
		Graphviz graphviz = Graphviz.fromString(dotData);
		
		File out = new File(imagePath + File.separator + name + FilenameUtils.EXTENSION_SEPARATOR + format.fileExtension);

		try {
			graphviz.render(format).toFile(out);
		} catch (Exception e) {
			logger.error("Failure to draw UML via GraphViz.", e);
			PopoutAlert pa = new PopoutAlert(300, 250, "Failure to draw UML via GraphViz.");
		}
		logger.info("Done");
		return out;
	}
}

