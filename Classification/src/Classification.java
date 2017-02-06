/* This project was completed Mona Iwamoto. It was written without
   consulting code by other students.  */

import java.util.*;
import java.io.*;
public class Classification {
	
	static String trainingFile;
	static String testFile;
	static String outputFile;
	static int numMushrooms = 0;
	static int numAttributes = 22;

	public static ArrayList<String[]> attrData;
	public static ArrayList<Integer> usedAttributes = new ArrayList<Integer>();


	public static void main(String[] args) throws Exception {

		trainingFile = args[0];
		testFile = args[1];
		outputFile = args[2];
		
	
		
		
		File file = new File(outputFile);
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);

		
		ArrayList<Mushroom> mushroomSet;
		ArrayList<Mushroom> testSet;

		setAttributeData();
		mushroomSet = buildMushroomSet(trainingFile);
		testSet = buildMushroomSet(testFile);

		// run Naive Bayesian classifier
		runNaiveBayesian(mushroomSet, testSet);
		// run c4.5 classifier
		runC4dot5(mushroomSet, testSet);
		
		ps.close();
		fos.close();
		
		
		
	}
	
	public static void runNaiveBayesian (ArrayList <Mushroom>mushroomSet, 
			ArrayList<Mushroom> testSet){
	
		Bayesian b = new Bayesian(mushroomSet, testSet);
		b.evaluateSet();
		
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("                  Testing Naive Bayesian Classifier : " + testFile);
		System.out.println("--------------------------------------------------------------------------------");

		b.runModel();

		
	}
	
	
	public static void runC4dot5(ArrayList<Mushroom> mushroomSet, ArrayList<Mushroom> testSet){

		int level = 0;
		Tree t = new Tree();

		Node root = new Node();
		for(Mushroom mushroom : mushroomSet){
			root.getData().add(mushroom);
		}
		t.buildTree(mushroomSet, root);
		System.out.println("C4.5 Decision tree complete...");
		System.out.println("\n");
		level = t.printTree(root,level);
		System.out.println("\n");
		
		System.out.println("----------------------------------------------------------------------------------");
		System.out.println("                    Testing C4.5 Classifier " + testFile);
		System.out.println("----------------------------------------------------------------------------------");

		t.testClassifier(testSet, root);
		
	
	}
	
	
	
	public static ArrayList<Mushroom> buildMushroomSet(String filename) throws IOException, Exception {
		String strLine;
		String sValue;
		numMushrooms = 0;
		ArrayList<Mushroom> mushroomSet = new ArrayList<Mushroom>();
		// Open the file
	
		ArrayList<Attribute> attributes;
		BufferedReader data_in = null;
		Mushroom mushroom;
		String attrLabel = "";
		

			File f = new File(filename);
        
			FileInputStream fis = new FileInputStream(f); 

			data_in = new BufferedReader(new InputStreamReader(fis));
	   	
			while ((strLine = data_in.readLine()) != null) {    
				
				StringTokenizer st = new StringTokenizer(strLine);
	    		attributes = new ArrayList<Attribute>();		
	    		mushroom = new Mushroom();
	    		
	    		for(int i=0; i< attrData.size(); i++) {
		    			sValue = st.nextToken();	  
		    			String [] attrDesc = attrData.get(i);
		    			attrLabel = attrDesc[0];	    			
		    			attributes.add(new Attribute(attrLabel,sValue,i));
	    		}
	    		mushroom.setAttributes(attributes);
	    		mushroomSet.add(mushroom);
	    		numMushrooms++;
    		   		
			}
//			System.out.println(numMushrooms);
			fis.close();
			data_in.close();
			return mushroomSet;
	}	

	public static int countC1(ArrayList<String[]> mushroomSet) {
		int c1 = 0;
		for(String[] mushroom:mushroomSet){
			System.out.println(mushroom[0]);
			if (mushroom[0].equals("p")) {
				c1++;
			}
		}
		return c1;
		
	}
	public static boolean isAttributeUsed(int attribute) {
		if(usedAttributes.contains(attribute)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static int setSize(int index){
		int size = 0;
		switch (index) {
			case 0: 
			case 4: 
			case 8: 
			case 10: 
			case 15: size = 2;
				break;		
			case 1:
			case 20: size = 6;
				break;
			case 2: 
			case 6:
			case 11: 
			case 12: 
			case 16: size = 4;
				break;
			case 3: size = 10;
				break; 
			case 5: 
			case 13: 
			case 14: 
			case 19: size = 9;
				break;
			case 7: 
			case 17: size = 3;
				break;
			case 9: size = 12;
			case 18: size = 8;
			case 21: size = 7;
				break;
			default:
				break;
		}
		return size;
					
		
	}
	
	public static void setAttributeData(){
		String s;
		String[] set;
		attrData = new ArrayList<String[]>();
		
		s="class,e,p";
		set = s.split(",");
		attrData.add(set);
		
		s = "cap-shape,b,c,f,k,s,x";
		set = s.split(",");
		attrData.add(set);
		
		s="cap-surface,f,g,s,y";
		set = s.split(",");
		attrData.add(set);
		
		s="cap-color,b,c,e,g,n,p,r,u,w,y";
		set = s.split(",");
		attrData.add(set);
		
		s="bruises?,f,t";
		set = s.split(",");
		attrData.add(set);
		
		s="odor,a,c,f,l,m,n,p,s,y";
		set = s.split(",");
		attrData.add(set);
		
		s="gill-attachment,a,d,f,n";
		set = s.split(",");
		attrData.add(set);
		
		s="gill-spacing,c,d,w";
		set = s.split(",");
		attrData.add(set);
		
		s="gill-size,b,n";
		set = s.split(",");
		attrData.add(set);
		
		s="gill-color,b,e,g,h,k,n,o,p,r,u,w,y";
		set = s.split(",");
		attrData.add(set);
		
		s="stalk-shape,e,t";
		set = s.split(",");
		attrData.add(set);
		
		s="stalk-surface-above-ring,f,k,s,y";
		set = s.split(",");
		attrData.add(set);
		
		s="stalk-surface-below-ring,f,k,s,y";
		set = s.split(",");
		attrData.add(set);
		
		s="stalk-color-above-ring,b,c,e,g,n,o,p,w,y";
		set = s.split(",");
		attrData.add(set);
		
		s="stalk-color-below-ring,b,c,e,g,n,o,p,w,y";
		set = s.split(",");
		attrData.add(set);
		
		s="veil-type,p,u";
		set = s.split(",");
		attrData.add(set);

		s="veil-color,n,o,w,y";
		set = s.split(",");
		attrData.add(set);
		
		s="ring-number,n,o,t";
		set = s.split(",");
		attrData.add(set);
		
		s="ring-type,c,e,f,l,n,p,s,z";
		set = s.split(",");
		attrData.add(set);
		
		s="spore-print-color,b,h,k,n,o,r,u,w,y";
		set = s.split(",");
		attrData.add(set);
		
		s="population,a,c,n,s,v,y";
		set = s.split(",");
		attrData.add(set);
		
		s="habitat,d,g,l,m,p,u,w";
		set = s.split(",");
		attrData.add(set);
	}


}
