package analysis.language.component;

public class InstanceVariable extends ClassComponent{
	
	private boolean isStatic;
	
	private boolean isFinal;
	
	public InstanceVariable(String vis, String nom, String typ) {
		super(typ, nom, vis);
	}
	
	public void setStatic(boolean in) {
		isStatic = in;
	}
	
	public boolean getStatic() {
		return isStatic;
	}
	
	public void setFinal(boolean in) {
		isFinal = in;
	}
	
	public boolean getFinal() {
		return isFinal;
	}
	
}
