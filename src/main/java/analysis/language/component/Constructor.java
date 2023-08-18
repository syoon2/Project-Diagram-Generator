package analysis.language.component;

import java.util.ArrayList;

public class Constructor extends Function{

	public Constructor(String vis, String nom, ArrayList<Argument> arg) {
		super(vis, nom, arg, null);
	}
	
	public Constructor(String vis, String nom, ArrayList<String> argNom, ArrayList<String> argTyp) {
		super(vis, nom, null, argNom, argTyp);
	}
	
	public String getReturnType() {
		return "";
	}
	
}
