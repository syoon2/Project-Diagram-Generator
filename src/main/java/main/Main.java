package main;

import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.*;

import image.ConvertVisual;
import ui.Display;

/**
 * 
 * 
 * Hierarchy:
 *  - Explore
 *  - Class
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

		Options cliOptions = new Options();

		Option root = Option.builder("root").hasArg(true).numberOfArgs(1).argName("path-to-root-of-project").desc("Specifies the path to project root.").type(File.class).build();
		Option savename = Option.builder("savename").hasArg(true).argName("image-name").desc("Specifies the filename of the generated diagram.").type(File.class).build();
		Option instanceVariable = new Option("i", false, "If this argument is present, the generated diagram will show instance variables.");
		Option privateEntities = new Option("p", false, "If this argument is present, the generated diagram will show private entities.");
		Option functions = new Option("f", false, "If this argument is present, the generated diagram will show functions.");
		Option constants = new Option("c", false, "If this argument is present, the generated diagram will show constants.");
		Option help = new Option("h", "help", false, "Displays this help message then exits.");

		cliOptions.addOption(root);
		cliOptions.addOption(savename);
		cliOptions.addOption(instanceVariable);
		cliOptions.addOption(privateEntities);
		cliOptions.addOption(functions);
		cliOptions.addOption(constants);
		cliOptions.addOption(help);
		
		CommandLineParser parser = new DefaultParser();

		CommandLine line = parser.parse(cliOptions, args, true);
		if (line.hasOption(help)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar Project-Diagram-Generator.jar", cliOptions, true);
			System.exit(0);
		}
		List<Option> missingOptions = new ArrayList<>();
		if (!line.hasOption(root)) {
			missingOptions.add(root);
		}
		if (!line.hasOption(savename)) {
			missingOptions.add(savename);
		}
		if (!missingOptions.isEmpty()) {
			throw new MissingOptionException(missingOptions);
		}
		boolean inst = line.hasOption(instanceVariable);
		boolean func = line.hasOption(functions);
		boolean priv = line.hasOption(privateEntities);
		boolean consta = line.hasOption(constants);
		runLoose(line.getOptionValue(root), line.getOptionValue(savename), inst, func, priv, consta, line.getArgs());
		
	}

	private static <T> void runLoose(String path, String name, boolean inst, boolean func, boolean priv, boolean consta, String ... rem) {
		ConvertVisual.assignPaths(ADDRESS_IMAGES, ADDRESS_SOURCES, ADDRESS_SETTINGS);
		List<String> ignore = List.of(rem);
		ConvertVisual.generateUMLDiagram(path, ignore, name, inst, func, priv, consta);
	}
	
	private static void runReal() {
		Display disp = new Display();
	}

}
