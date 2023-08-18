package analysis.language.component;

public class Argument {

	private String name;
	private String type;
	
	public Argument(String nom, String typ) {
		name = nom;
		type = typ;
	}
		
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
}
