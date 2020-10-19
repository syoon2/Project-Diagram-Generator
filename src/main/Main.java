package main;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import explore.Explore;
import graphviz.ConvertVisual;
import ui.Display;

/**
 * 
 * 
 * Hierarchy:
 *  - Explore
 *  - Class
 *  
 * TODO: Options for 'splines = ortho' and 'nodesep = 1' separately
 * TODO: JavaClass needs to specify inheritance/directed association/etc. in associates
 * TODO: Show package hierarchy for easy means of filtering what packages are examined (already possible, just remember to make it look nice for the user)
 * TODO: A UI, embed GraphViz? c
 * 
 * @author Ada Clevinger
 *
 */

public class Main {

	private final static String PATH = "C:/Users/Borinor/git/Finite-State-Machine-Model/src/";
	private final static String PATH2 = "C:/Users/Borinor/eclipse-workspace/Project Diagram Generator/src/";
	private final static String PATH3 = "C:/Users/Borinor/git/SoftwareVisualInterface/src";
	private final static String PATH4 = "C:/Users/Borinor/git/ZombieApocalypse/src"; 
	private final static String PATH5 = "C:/Users/Borinor/git/Mr.Jack/src"; 
	private final static String PATH6 = "C:/Users/Borinor/eclipse-workspace/OOExample1/src/"; 

	public final static String ADDRESS_SETTINGS = "./Diagram/settings/";
	public final static String ADDRESS_IMAGES = "./Diagram/images/";
	public final static String ADDRESS_SOURCES = "./Diagram/sources/";
	public final static String ADDRESS_CONFIG = ADDRESS_SETTINGS + "/config.txt";
	
	private final static String NAME = "Self";
	
	public static void main(String[] args) {
		//runReal();
		runLoose();
	}
	
	private static void runLoose() {
		ConvertVisual.assignPaths(ADDRESS_IMAGES, ADDRESS_SOURCES, ADDRESS_SETTINGS);
		File f = new File(PATH2);
		Explore.setParameters(true,  true,  true);
		Explore expl = new Explore(f, "");
		String dot = ConvertVisual.convertClassStructure(expl.getClassStructure(), expl.getClusters());
		ConvertVisual.draw(dot, NAME, "jpg");
	}
	
	private static void runReal() {
		Display disp = new Display();
	}

}
