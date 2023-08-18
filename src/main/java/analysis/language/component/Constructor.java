package analysis.language.component;

import java.util.List;

public class Constructor extends Function{

	public Constructor(String vis, String nom, List<Argument> arg) {
		super(vis, nom, arg, null);
	}
	
	public Constructor(String vis, String nom, List<String> argNom, List<String> argTyp) {
		super(vis, nom, null, argNom, argTyp);
	}
	
	public String getReturnType() {
		return "";
	}
	
}
