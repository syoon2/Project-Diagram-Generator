package analysis.language.component;

import java.util.ArrayList;

public class Function extends ClassComponent{

//---  Instance Variables   -------------------------------------------------------------------
	
	private ArrayList<Argument> arguments;
	private boolean isAbstract;	//italics
	private boolean isStatic;	//underline
	private boolean isFinal;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Function(String vis, String nom, ArrayList<Argument> arg, String ret) {
		super(ret, nom, vis);
		arguments = arg;
	}
	
	public Function(String vis, String nom, String ret, ArrayList<String> argNom, ArrayList<String> argTyp) {
		super(ret, nom, vis);
		arguments = new ArrayList<Argument>();
		for(int i = 0; i < argNom.size(); i++) {
			arguments.add(new Argument(argNom.get(i), argTyp.get(i)));
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setAbstract(boolean in) {
		isAbstract = in;
	}
	
	public void setStatic(boolean in) {
		isStatic = in;
	}
	
	public void setFinal(boolean in) {
		isFinal = in;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public ArrayList<Argument> getArguments(){
		return arguments;
	}
	
	public int getNumberArguments() {
		return getArguments().size();
	}

	public Argument getArgumentAt(int index) {
		return arguments.get(index);
	}
	
	public String getArgumentNameAt(int index) {
		return getArgumentAt(index).getName();
	}
	
	public String getArgumentTypeAt(int index) {
		return getArgumentAt(index).getType();
	}
	
	public boolean getAbstract() {
		return isAbstract;
	}
	
	public boolean getStatic() {
		return isStatic;
	}
	
	public boolean getFinal() {
		return isFinal;
	}
	
}
