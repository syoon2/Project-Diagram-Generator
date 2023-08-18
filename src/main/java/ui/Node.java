package ui;

import java.util.ArrayList;

public class Node {

	private ArrayList<Node> children;
	private String label;
	private boolean enable;
	private int code;
	
	public Node(String lab, int inCode) {
		label = lab;
		code = inCode;
		children = new ArrayList<Node>();
		enable = true;
	}
	
	public Node findCode(int inCode) {
		if(code == inCode) {
			return this;
		}
		Node out = null;
		for(Node n : children) {
			out = n.findCode(inCode);
			if(out != null) {
				return out;
			}
		}
		return out;
	}
	
	public String getDisabled(String out) {
		for(Node n : children) {
			out = n.traverseDisabled(out, "");
		}
		return out;
	}
	
	public String traverseDisabled(String out, String path) {
		String address = path + (path.equals("") ? "" : ".") + label;
		if(!enable) {
			out += (out.equals("") ? "" : ";") + address;
			return out;
		}
		for(Node n : children) {
			out = n.traverseDisabled(out, address);
		}
		return out;
	}
	
	public int getCode() {
		return code;
	}
	
	public void toggle() {
		enable = !enable;
	}
	
	public boolean getStatus() {
		return enable;
	}
	
	public void addChild(Node in) {
		children.add(in);
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
	
	public Node findChild(String nom) {
		Node out = getChild(nom);
		if(out == null) {
			for(Node n : children) {
				out = n.getChild(nom);
				if(out != null)
					return out;
			}
		}
		return null;
	}
	
	public Node getChild(String nom) {
		for(Node n : children) {
			if(n.getName().equals(nom)) {
				return n;
			}
		}
		return null;
	}
	
	public String getName() {
		return label;
	}
	
}
