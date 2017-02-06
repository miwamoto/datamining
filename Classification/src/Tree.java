import java.io.*;
import java.util.*;

public class Tree {
	
	
	public Node buildTree(ArrayList<Mushroom> mushroomSet, Node root) {
		int bestAttribute = -1;
		double bestRatio= 0.0;
		double gain = 0.0;
		double gainRatio = 0.0;
		double infoP = 0.0;
		
		root.setInfo(C4dot5.calculateInfo(root.getData()));
		
		if(root.getInfo() == 0) {
			root.setType("Leaf");
			root.setClassLabel(findMajorityClass(root));
			return root;
		}
		
		for(int i = 1; i < Classification.numAttributes; i++) {
			if(!Classification.isAttributeUsed(i)) {
				ArrayList<Integer> setSizes = new ArrayList<Integer>();
				ArrayList<Double> infoPs = new ArrayList<Double>();
				
		
				ArrayList<String> sValues = findValues( i);
				ArrayList<Mushroom> partition;
				
				for (String s : sValues) {
					partition =  partition(root, i, s);
					if (partition.size() >0) {
						setSizes.add(partition.size());

						infoP = C4dot5.calculateInfo(partition);
						infoPs.add(infoP);
					
					}
			
				}
				
				gainRatio = C4dot5.calculateGainRatio(root.getInfo(), infoPs, setSizes, root.getData().size());  //TODO

				if (gainRatio > bestRatio) {
					bestAttribute = i;
					bestRatio = gainRatio;
				}
				
				
			}
		}

		
		if(bestAttribute != -1) {
			
			int setSize = Classification.setSize((bestAttribute));
			String[] sAttr = Classification.attrData.get(bestAttribute);
			String sAttrName = sAttr[0];
			root.setTestAttribute(new Attribute(sAttrName,"",bestAttribute));
			
			root.children = new Node[setSize];
			root.setUsed(true);
			Classification.usedAttributes.add(bestAttribute);
			
			for (int j = 0; j< sAttr.length-1; j++) {
				root.children[j] = new Node();
				root.children[j].setBranch(sAttr[j+1]);
				root.children[j].setParent(root);
				root.children[j].setData(partition(root, bestAttribute, sAttr[j+1] ));
			}

			for (int j = 0; j < setSize; j++) {
				buildTree(root.children[j].getData(), root.children[j]);
			}

			root.setData(null);
		}
		else {
			
			root.setType("Leaf");
			root.setClassLabel(findMajorityClass(root));
			return root;
		}
		
		return root;
	}
	
	public String findMajorityClass(Node node){
		
		String label = "";
		String classLabel = "";
		int countP = 0;
		int countE = 0;
		
		ArrayList<Mushroom> mushroomSet = node.getData();
		for (Mushroom m : mushroomSet){
			
			classLabel = m.getAttributes().get(0).getValue();
			if (classLabel.equals("p")){
				countP++;
			}else{
				countE++;
			}
		}
		if (countP > countE){
			return "p";
		}else{
			return "e";
		}
		
		
	}
	
	public ArrayList <String> findValues(int attr){
		ArrayList<String> sValues = new ArrayList<String>();
		String sArray[] = Classification.attrData.get(attr);
		for (int i=1; i<sArray.length; i++) {
			
			String s = sArray[i];
			sValues.add(s);					
		}
		return sValues;
	}
	
	public ArrayList<Mushroom> partition(Node root, int attr, String sValue) {
		ArrayList<Mushroom> partition = new ArrayList<Mushroom>();
		String temp;
		for(int i = 0; i < root.getData().size(); i++) {
			Mushroom mushroom = root.getData().get(i);
			
			temp = mushroom.getAttributes().get(attr).getValue();
			if(temp.equals(sValue)) {
				partition.add(mushroom);
			}
		}
		return partition;
	}
	
	public void testClassifier(ArrayList<Mushroom> testSet, Node root){
		
		double accuracy = 0.0;
		int correct = 0;
		int incorrect = 0;
		String pClass = "";
		String tClass = "";
		Attribute splitAttr;
	
		for (Mushroom m: testSet) {
			tClass = m.getAttributes().get(0).getValue();
			pClass = findPredictedClass(m, root);
			System.out.println("Predicted Class: " + pClass );
			if (pClass.equals(tClass)){
				correct++;
			}else{
				incorrect++;
			}
		}
		accuracy = (double)correct/testSet.size()*100.0;

		System.out.println("\n");
		System.out.println("--------------------------------------------");
		System.out.println("	Number correct:   " + correct);
		System.out.println("	Number incorrect: " + incorrect);
		System.out.println("	Accuracy:         " + accuracy + "%");
		System.out.println("--------------------------------------------");

	}

	
	public String findPredictedClass(Mushroom m, Node node){
		String label = "";
		Attribute splitAttribute = new Attribute("","");
		
		while (!(node.getType()=="Leaf")){
			Node[] children = node.getChildren();
			splitAttribute = node.getTestAttribute();
			int index = splitAttribute.getNum();
			String sBranch = m.getAttributes().get(index).getValue();
			for (Node child : children) {
				if (sBranch.equals(child.getBranch())){
					node = child;
					
					break;
				}
			}
				
		}
		label = node.getClassLabel();
		return label;
	}
	
	public int printTree(Node node, int l){
		String testAttr = "";
		String label = "";
		String branch = "";
		String bString = "";
		boolean moreLevels = true;
		int level = l;

		for (int i = 1; i< level; i++){
			bString = bString + "|     ";
		}

		while (!(node.getType()=="Leaf")){
			
			level++;
			if(level >1){
				testAttr = node.getParent().getTestAttribute().getName();
				branch = node.getBranch();
				bString =bString + testAttr + " = " + branch;
				System.out.println(bString);
			}
			Node [] children = node.getChildren();
			for (Node child : children) {
				node = child;
	//			level++;
				printTree(node, level);
			}
			level--;
			return level;
		}
		if(!(node.getParent().equals(null))) {
			testAttr = node.getParent().getTestAttribute().getName();
			branch = node.getBranch();
			label = node.getClassLabel();
			bString =bString + testAttr + " = " + branch + "--> Class " + label;
			System.out.println(bString);
			return level;
		}
		return level;

	}
	
}
