
public class ConditionalP {
	private String attrName;
	private int attrNum;
	private String attrVal;
	private String label;
	private double probability;
	
	public void setAttributeName(String name){
		this.attrName = name;
	}
	
	public String getAttributeName(){
		return attrName;
	
	}
	
	public void setLabel(String classLabel){
		this.label = classLabel;
	}

	public String getLabel() {
		return label;
	}
	
	public void setAttrNum(int n){
		this.attrNum = n;
	}

	public int getAttrNum() {
		return attrNum;
	}

	public void setProbability(double p){
		this.probability = p;
	}
	
	public double getProbability() {
		return probability;
	}
	public void setValue(String s){
		this.attrVal = s;
	}
	public String getValue(){
		return attrVal;
	}

}
