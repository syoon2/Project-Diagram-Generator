package analysis.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cluster {

//---  Instance Variables   -------------------------------------------------------------------
	
	private String[] address;
	private List<Cluster> children;
	private Set<String> composite;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Cluster(String[] givenAddress) {
		address = givenAddress;
		children = new ArrayList<Cluster>();
		composite = new HashSet<String>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addComponent(String[] path, String in) {
		if(path == null || path.length == 0) {
			composite.add(in);
		}
		else {
			Cluster use = findChild(path[0]);
			if(use == null) {
				addCluster(path);
				use = findChild(path[0]);
			}
			use.addComponent(tearArray(path), in);
		}
	}
	
	public void addCluster(String[] path) {
		if(path == null || path.length == 0) {
			return;
		}
		Cluster next = findChild(path[0]);
		if(next == null) {
			next = new Cluster(mergePath(path[0]));
			children.add(next);
		}
		next.addCluster(tearArray(path));
	}
	
	private Cluster findChild(String step) {
		for(Cluster c : children) {
			if(step.equals(c.getTernary())) {
				return c;
			}
		}
		return null;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Cluster getCluster(String[] path) {
		if(path == null || path.length == 0) {
			return this;
		}
		Cluster next = findChild(path[0]);
		if(next == null) {
			return null;
		}
		return next.getCluster(tearArray(path));
	}
	
	public List<Cluster> getChildren(){
		return children;
	}
	
	public Set<String> getComponents(){
		return composite;
	}
	
	public String[] getAddressArray() {
		return address;
	}
	
	public String getAddress() {
		if(address == null || address.length == 0) {
			return "";
		}
		String out = address[0];
		for(int i = 1; i < address.length; i++)
			out += "." + address[i];
		return out;
	}
	
	private String getTernary() {
		return address[address.length - 1];
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	private String[] mergePath(String tack) {
		String[] out = new String[address.length + 1];
		for(int i = 0; i < address.length; i++)
			out[i] = address[i];
		out[out.length-1] = tack;
		return out;
	}
	
	private String[] tearArray(String[] in) {
		if(in.length <= 1) {
			return new String[] {};
		}
		return Arrays.copyOfRange(in, 1, in.length);
	}

	public void debugPrintOut() {
		debugPrintOut(0);
	}
	
	private void debugPrintOut(int d) {
		System.out.println(tabBuffer(d) + getAddress());
		for(String gd : getComponents()) {
			System.out.println(tabBuffer(d) + "  " + gd);
		}
		for(Cluster c : getChildren()) {
			c.debugPrintOut(d + 1);
		}
	}
	
	private String tabBuffer(int d) {
		String out = "";
		for(int i = 0; i < d; i++) {
			out += "\t";
		}
		return out;
	}
	
	public String toString() {
		return Arrays.toString(address) + " -> (" + children + ")";
	}
	
}
