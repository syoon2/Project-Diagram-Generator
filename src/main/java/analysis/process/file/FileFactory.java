package analysis.process.file;

import java.io.File;
import java.util.ArrayList;

public class FileFactory {

	public static ArrayList<GenericFile> generateFile(File f, String root) {
		String name = f.getName();
		String type = name.substring(name.lastIndexOf(".")+1);
		switch(type) {
			case "java":
				JavaFile jf = new JavaFile(f, root);
				ArrayList<GenericFile> out = new ArrayList<GenericFile>();
				if(jf.detectInternalClasses()) {
					for(GenericFile gf : jf.extractInternalClasses()) {
						out.add(gf);
					}
				}
				else {
					out.add(jf);
				}
				return out;
			default: 
				return null;
		}
	}
	
}
