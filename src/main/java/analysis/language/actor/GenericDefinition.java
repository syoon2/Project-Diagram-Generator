package analysis.language.actor;

import java.util.ArrayList;

import analysis.language.component.Constructor;
import analysis.language.component.Function;

public abstract class GenericDefinition implements Comparable<GenericDefinition>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private String name;
	private String context;
	private ArrayList<GenericDefinition> associates;
	private ArrayList<GenericDefinition> realizations;	//dotted line, empty arrowhead
	private ArrayList<Function> functions;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public GenericDefinition(String inName, String inContext) {
		name = inName;
		context = inContext;
		associates = new ArrayList<GenericDefinition>();
		functions = new ArrayList<Function>();
		realizations = new ArrayList<GenericDefinition>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addAssociation(GenericDefinition ref) {
		if(ref == null) {
			return;
		}
		associates.add(ref);
	}
	
	public void addFunction(Function in) {
		functions.add(in);
	}
	
	public void addRealization(GenericDefinition in) {
		realizations.add(in);
	}
	
	public void addFunction(String vis, String nom, String ret, ArrayList<String> argNom, ArrayList<String> argTyp, boolean statStatic, boolean statAbstract, boolean isFinal) {
		Function in = new Function(vis, nom, ret, argNom, argTyp);
		in.setAbstract(statAbstract);
		in.setStatic(statStatic);
		in.setFinal(isFinal);
		addFunction(in);
	}
	
	public void addConstructor(String vis, String nom, ArrayList<String> argNom, ArrayList<String> argTyp) {
		Constructor in = new Constructor(vis, nom, argNom, argTyp);
		addFunction(in);
	}

//---  Getter Methods   -----------------------------------------------------------------------

	//-- Functions  -------------------------------------------
	
	public ArrayList<Function> getFunctions() {
		return functions;
	}
	
	public Function getFunctionAt(int index) {
		return getFunctions().get(index);
	}
	
	public int getNumberFunctions() {
		return getFunctions().size();
	}
	
	public String[] getFunctionArgumentNamesAt(int index) {
		Function func = getFunctionAt(index);
		String[] argNom = new String[func.getNumberArguments()];
		for(int i = 0; i < func.getNumberArguments(); i++) {
			argNom[i] = func.getArgumentNameAt(i);
		}
		return argNom;
	}
	
	public String[] getFunctionArgumentTypesAt(int index) {
		Function func = getFunctionAt(index);
		String[] argNom = new String[func.getNumberArguments()];
		for(int i = 0; i < func.getNumberArguments(); i++) {
			argNom[i] = func.getArgumentTypeAt(i);
		}
		return argNom;
	}
	
	public String getFunctionTypeAt(int index) {
		return getFunctionAt(index).getType();
	}
	
	public String getFunctionVisibilityAt(int index) {
		return getFunctionAt(index).getVisibility();
	}
	
	public String getFunctionNameAt(int index) {
		return getFunctionAt(index).getName();
	}

	public boolean getFunctionStaticAt(int index) {
		return getFunctionAt(index).getStatic();
	}
	
	public boolean getFunctionAbstractAt(int index) {
		return getFunctionAt(index).getAbstract();
	}
	
	public boolean getFunctionFinalAt(int index) {
		return getFunctionAt(index).getFinal();
	}
	
	//-- GenericDefinition  -----------------------------------
	
	public String getName() {
		return name;
	}
	
	public ArrayList<GenericDefinition> getClassAssociates(){
		return associates;
	}
	
	public ArrayList<GenericDefinition> getRealizations(){
		return realizations;
	}

	public String getContext() {
		return context;
	}
	
	public String[] getContextArray() {
		return context.split("\\.");
	}
	
	public String getFullName() {
		return getContext() + "/" + getName();
	}
	
	public boolean hasAssociate(GenericDefinition gc) {
		for(GenericDefinition c : associates) {
			if(c.compareTo(gc) == 0) {
				return true;
			}
		}
		return false;
	}

//---  Mechanics   ----------------------------------------------------------------------------

	@Override
	public int compareTo(GenericDefinition o) {
		return getFullName().compareTo(o.getFullName());
	}
	
	public boolean equals(GenericDefinition gd) {
		if(gd == null)
			return false;
		return getFullName().equals(gd.getFullName());
	}
	
}
