package image;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import analysis.process.Explore;
import com.github.softwarevisualinterface.visual.composite.popout.PopoutAlert;

public class ConvertVisual  {
	
	private final static int DPI_INCREASE = 1;
	
	private static String imagePath;
	private static String sourcePath;
	private static String settingsPath;
	
	public static void assignPaths(String img, String src, String sett) {
		imagePath = img;
		sourcePath = src;
		settingsPath = sett;
	}
	
	public static String generateUMLDiagram(String path, ArrayList<String> ignore, String name, boolean inst, boolean func, boolean priv, boolean consta) {
		File f = new File(path);
		Explore.setParameters(inst, func, priv, consta);
		System.out.println("Beginning Explore operations");
		Explore e = new Explore(f);
		for(String s : ignore) {
			e.ignorePackage(s);
		}
		e.run();
		DotProcess.setProject(e);
		System.out.println("Ending Explore operations\nBeginning draw operations");
		return draw(DotProcess.generateDot(), name, "jpg").getAbsolutePath();
	}
	
	public static File draw(String dotData, String name, String type) {
		File folder = new File(imagePath);
		folder.mkdir();
		System.out.println( settingsPath + "/config.txt");
		GraphViz gv = new GraphViz(imagePath, settingsPath + "/config.txt");
		gv.add(dotData);
		for(int i = 0; i < DPI_INCREASE; i++) {
			gv.increaseDpi();
		}
		
		File ref = new File(sourcePath + "//" + name + ".txt");
		ref.delete();
		try {
			RandomAccessFile raf = new RandomAccessFile(ref, "rw");
			raf.writeBytes(dotData);
			raf.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		File out = new File(imagePath + "//" + name + "." + type);

		int result = gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
		if(result == -1) {
			PopoutAlert pa = new PopoutAlert(300, 250, "Failure to draw UML via GraphViz.");
		}
		System.out.println("Done");
		return out;
	}
}

