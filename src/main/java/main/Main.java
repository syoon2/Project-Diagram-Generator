package main;

import java.util.Arrays;
import java.util.ArrayList;

import image.ConvertVisual;
import ui.Display;

/**
 * 
 * 
 * Hierarchy:
 *  - Explore
 *  - Class
 *  
 * TODO: A UI, embed GraphViz? c
 * 
 * @author Ada Clevinger
 *
 */

public class Main <T> {

	public final static String ADDRESS_SETTINGS = "./Diagram/settings/";
	public final static String ADDRESS_IMAGES = "./Diagram/images/";
	public final static String ADDRESS_SOURCES = "./Diagram/sources/";
	public final static String ADDRESS_CONFIG = ADDRESS_SETTINGS + "/config.txt";
	
	public static void main(String[] args) throws Exception{
		if (args.length == 0) runReal();
		else {
			ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
			if (argList.contains("-h")) {
				printHelp();
				System.exit(0);
			}
			String root = findArgData(argList, "-root=");
			String saveName = findArgData(argList, "-savename=");
			boolean inst = containsArg(argList, "-i");
			boolean func = containsArg(argList, "-f");
			boolean priv = containsArg(argList, "-p");
			boolean consta = containsArg(argList, "-c");
			runLoose(root, saveName, inst, func, priv, consta, argList.toArray(new String[0]));
		}
	}

	private static void printHelp() {
		System.out.println("========== List of Available Arguments ==========");
		System.out.println("-root=<path-to-root-of-project (required)");
		System.out.println("    This argument specifies the path to project root.");
		System.out.println("-savename=<image-name> (required)");
		System.out.println("    This argument specifies the filename of the generated diagram.");
		System.out.println("-i (optional)");
		System.out.println("    If this argument is present, the generated diagram will show instance variables.");
		System.out.println("-p (optional)");
		System.out.println("    If this argument is present, the generated diagram will show private entities.");
		System.out.println("-f (optional)");
		System.out.println("    If this argument is present, the generated diagram will show functions.");
		System.out.println("-c (optional)");
		System.out.println("    If this argument is present, the generated diagram will show constants.");
		System.out.println("-h (optional)");
		System.out.println("    Displays this help message then exits.");
	}

	private static String findArgData(ArrayList<String> args, String argPrefix) {
		for (int i = 0; i < args.size(); i++) {
			if (args.get(i).startsWith(argPrefix)) {
				String arg = args.remove(i);
				return arg.substring(argPrefix.length());
			}
		}
		throw new IllegalArgumentException(argPrefix + " not specified");
	}

	private static boolean containsArg(ArrayList<String> args, String arg) {
		return args.remove(arg);
	}
	
	private static <T> void runLoose(String path, String name, boolean inst, boolean func, boolean priv, boolean consta, String ... rem) {
		ConvertVisual.assignPaths(ADDRESS_IMAGES, ADDRESS_SOURCES, ADDRESS_SETTINGS);
		ArrayList<String> ignore = new ArrayList<String>();
		for(String s : rem) {
			ignore.add(s);
		}
		ConvertVisual.generateUMLDiagram(path, ignore, name, inst, func, priv, consta);
	}
	
	private static void runReal() {
		Display disp = new Display();
	}

}
