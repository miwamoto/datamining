
// This project was completed by Mona Iwamoto and Melissa Mendoza, without consulting code
// from other students.

import java.util.ArrayList;

public class Cluster {
	

	private ArrayList<Record> records;
	
	public Cluster(){
		this.records = new ArrayList<Record>();
		
	}
	public ArrayList<Record> getAttributes() {
		return this.records;
		}
	
	public void setAttributes(ArrayList<Record> records){
		this.records = records;
	}
}