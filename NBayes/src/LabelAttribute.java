package NBC;

public class LabelAttribute {
	String label;
	String attribute;
	double countTotal;
	double score;
	
	public LabelAttribute(String label, String attribute) {
		this.label = label;
		this.attribute = attribute;
		countTotal = 0;
		score = 0;
	}
	
	public void add(int count) {
		countTotal += count;
	}
	
	public double getCount(){
		return countTotal;
	}
}
