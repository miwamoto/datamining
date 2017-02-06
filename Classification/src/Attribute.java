
public class Attribute {


	private String name;
	private String  cValue;
	private int num;
		
	public Attribute(String name, String cValue) {
			this.name = name;
			this.cValue = cValue;
		}
	public Attribute(String name, String cValue, int n) {
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

	public void setNum(int n){
		this.num = n;
	}
	
	public int getNum() {
		return num;
	}

}


