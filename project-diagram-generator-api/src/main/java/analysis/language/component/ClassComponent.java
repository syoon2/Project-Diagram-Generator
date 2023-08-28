package analysis.language.component;

public abstract class ClassComponent {
	
	private String name;
	private String type;
	private String visibility;
	
	public ClassComponent(String typ, String nom, String vis) {
		type = typ;
		name = nom;
		visibility = vis;
	}
	
	public String getVisibility() {
		return visibility;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}
