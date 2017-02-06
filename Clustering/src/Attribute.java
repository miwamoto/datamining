// This project was completed by Mona Iwamoto and Melissa Mendoza, without consulting code
// from other students.

public class Attribute {


	private String name;
	private String  cValue;
	private double num;
		
	public Attribute(String name, String cValue) {
			this.name = name;
			this.cValue = cValue;
		}
	public Attribute(String name, String cValue, double n) {
		this.name = name;
		this.cValue = cValue;
		this.num = n;
	}
			
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
		
	public void setValue(String cValue) {
			this.cValue = cValue;
		}

	public String getValue() {
		return cValue;
	}

	public void setNum(double n){
		this.num = n;
	}
	
	public double getNum() {
		return num;
	}

}
