package analysis.process;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import analysis.language.actor.GenericDefinition;
import analysis.process.file.FileFactory;
import analysis.process.file.GenericFile;

public class Explore{
	
//---  Instance Variables   -------------------------------------------------------------------
		
	private ArrayList<GenericFile> files;
	private HashMap<String, GenericDefinition> classes;
	private HashMap<String, GenericDefinition> interfaces;
	private HashMap<String, GenericDefinition> enums;
	private Cluster parent;
	private String rootPath;
	
	private HashSet<String> ignore;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Explore(File root) {
		rootPath = root.getAbsolutePath();
		ignore = new HashSet<String>();
		files = new ArrayList<GenericFile>();
		classes = new HashMap<String, GenericDefinition>();
		interfaces = new HashMap<String, GenericDefinition>();
		enums = new HashMap<String, GenericDefinition>();
		parent = new Cluster(new String[] {});
		if(rootPath.charAt(rootPath.length() - 1) != '/') {
			rootPath += "/";
		}
	}

//---  Operations   ---------------------------------------------------------------------------
	
	public void ignorePackage(String path) {
		ignore.add(path);
	}
	
	public void run() {
		File use = new File(rootPath);
		System.out.println("Begun exploring files");
		explore(use);
		System.out.println("Finished exploring files");
		System.out.println("Begun processing files");
		for(GenericFile f : files) {
			f.process(getDefinitionMapping(), parent);
		}
		System.out.println("Finished processing files");
	}
	
	private void explore(File root) {
		for(String s : root.list()) {
			File look = new File(root.getAbsolutePath() + File.separator + s);
			if(!look.exists()) {
				continue;
			}
			if(look.isDirectory() && !ignore(look.getAbsolutePath())) {
				explore(look);
			}
			else if(look.isFile()){
				ArrayList<GenericFile> gfs = FileFactory.generateFile(look, rootPath);
				if(gfs == null) {
					continue;
				}
				for(GenericFile f : gfs) {
					if(f == null || f.getDefinition() == null) {
						continue;
					}
					GenericDefinition gd = f.getDefinition();
					
					boolean canAdd = false;
					if(f.isClassFile()) {
						classes.put(gd.getFullName(), gd);
						canAdd = true;
					}
					else if(f.isInterfaceFile()) {
						interfaces.put(gd.getFullName(), gd);
						canAdd = true;
					}
					else if(f.isEnumFile()) {
						enums.put(gd.getFullName(), gd);
						canAdd = true;
					}
					if(canAdd)
						parent.addComponent(gd.getContextArray(), gd.getFullName());
					files.add(f);
				}
			}
		}
	}
	
	private boolean ignore(String path) {
		return ignore.contains(formPackagePath(path));
	}
	
	private String formPackagePath(String path) {
		String out = path.substring(rootPath.length());
		out = out.replaceAll("\\\\", ".");
		return out;
	}
		
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Collection<GenericDefinition> getClasses(){
		return classes.values();
	}
	
	public Collection<GenericDefinition> getInterfaces(){
		return interfaces.values();
	}
	
	public Collection<GenericDefinition> getEnums(){
		return enums.values();
	}
	
	public ArrayList<GenericDefinition> getDefinitions(){
		ArrayList<GenericDefinition> out = new ArrayList<GenericDefinition>();
		for(String s : classes.keySet())
			out.add(classes.get(s));
		for(String s : interfaces.keySet())
			out.add(interfaces.get(s));
		for(String s : enums.keySet())
			out.add(enums.get(s));
		return out;
	}
	
	public HashMap<String, GenericDefinition> getDefinitionMapping(){
		HashMap<String, GenericDefinition> out = new HashMap<String, GenericDefinition>();
		for(String s : classes.keySet())
			out.put(s, classes.get(s));
		for(String s : interfaces.keySet())
			out.put(s, interfaces.get(s));
		for(String s : enums.keySet())
			out.put(s, enums.get(s));
		return out;
	}
	
	public Cluster getClusterRoot(){
		return parent;
	}
	
//--  Setter Methods   ------------------------------------------------------------------------
	
	public static void setParameters(boolean inst, boolean func, boolean priv, boolean consta) {
		GenericFile.assignProcessStates(inst, func, priv, consta);
	}
	
}
