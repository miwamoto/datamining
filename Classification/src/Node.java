
import java.util.*;

public class Node {
	private Node parent;
	public Node[] children;
	private ArrayList<Mushroom> data;
	private double info;
//	private Attribute label;
	private boolean used;
	private Attribute testAttribute;
	private String type;
	private String branch;
	private String classLabel;

	public Node() {
		this.data = new ArrayList<Mushroom>();
		this.info = 0.0;
		this.children = null;
		this.parent = null;
		this.used = false;
		this.testAttribute = new Attribute("","");
		this.branch = "";
		this.classLabel = "";
	

	}
	public void setBranch (String branch){
		this.branch = branch;
	}
	public String getBranch(){
		return branch;
	}
	public void setClassLabel (String label){
		this.classLabel = label;
	}
	public String getClassLabel (){
		return classLabel;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
		
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}

	public void setData(ArrayList<Mushroom> data) {
		this.data = data;
	}

	public ArrayList<Mushroom> getData() {
		return data;
	}

	public void setInfo(double info) {
		this.info = info;
		}

	public double getInfo() {
		return info;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Node[] getChildren() {
		return children;
	}

	public void setUsed(boolean isUsed) {
		this.used = isUsed;
		}

	public boolean isUsed() {
		return used;
		}

	public void setTestAttribute(Attribute testAttribute) {
		this.testAttribute = testAttribute;
	}

	public Attribute getTestAttribute() {
		return testAttribute;
	}
	

}

