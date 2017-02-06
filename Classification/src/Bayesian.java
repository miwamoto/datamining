
import java.util.*;

public class Bayesian {
	
	private int numClass1;
	private int numClass2;
	private double pC1;
	private double pC2;
	private ArrayList<Mushroom> trainingSet;
	private ArrayList<Mushroom> testSet;
	private ArrayList<ConditionalP> cProbabilities;
	
	public  Bayesian(ArrayList<Mushroom> trainingSet, ArrayList<Mushroom> testSet){
		this.trainingSet = trainingSet;
		this.testSet = testSet;
	}
	
	public int countClass(ArrayList<Mushroom> mushroomSet, String cLabel){
		int count = 0;
		String sVal = "";
		for (Mushroom m: mushroomSet){
			sVal = m.getAttributes().get(0).getValue();
			if(sVal.equals(cLabel)){
				count++;
			}
		
		}
		return count;
	}

	public void evaluateSet(){
		double p = 0.0;
		String attrName = "";
		int attrNum = 0;
		String attrVal = "";
		String attrLab = "";
		String[] attrData;
		ArrayList<ConditionalP> cPs = new ArrayList<ConditionalP>();
				
		numClass1 = countClass(trainingSet,"p");
		numClass2 = countClass(trainingSet,"e");
		pC1 = (double)numClass1/trainingSet.size();
		pC2 = (double)numClass2/trainingSet.size();
		
		for( int i = 1; i<Classification.attrData.size(); i++){
			attrData = Classification.attrData.get(i);
			attrName = attrData[0];
			
			for (int j = 1; j< attrData.length; j++){

				attrVal = attrData[j];
				for (int x = 0; x<2; x++){
					if (x==0){
						attrLab = "p";
					}else{
						attrLab = "e";
					}
					ConditionalP cP = new ConditionalP();
					p = laPlaceProb(attrName,  attrVal, attrLab, i);
					cP.setAttributeName(attrName);
					cP.setAttrNum(i);
					cP.setLabel(attrLab);
					cP.setValue(attrVal);
					cP.setProbability(p);
					cPs.add(cP);
				}
				
				
			}
		}
		this.cProbabilities = cPs;
				
	}
	
	public double laPlaceProb(String attrName, String attrVal, String attrLab, int i){
		String sLabel;
		String sVal;
		int count = 0;
		for (Mushroom m : trainingSet){
			sLabel = m.getAttributes().get(0).getValue();
			sVal = m.getAttributes().get(i).getValue();
			if(sLabel.equals(attrLab) && sVal.equals(attrVal))
				count++;
			
		}
		if (attrLab.equals("p")){
			return (double)(count+1)/(numClass1+1);
		}else{
			return (double)(count+1)/(numClass2+1);
		}
	
	}
	
	public int classify(Mushroom m ){
		double p=0;
		double pPoisonous = 1.0;
		double pEdible = 1.0;
		String attrVal = "";
		String actualClass = "";		
		String predictedClass = "";
		
		ArrayList<Attribute> attributes;
		attributes = m.getAttributes();
		
		for (int i=1; i<attributes.size(); i++){
			attrVal = attributes.get(i).getValue();
			for (ConditionalP cp : cProbabilities){
				if ((cp.getAttrNum()==i && cp.getValue().equals(attrVal))&& cp.getLabel().equals("p")){
					pPoisonous = pPoisonous* cp.getProbability();
					break;
				}
			}
			for (ConditionalP cp : cProbabilities){
				if ((cp.getAttrNum()==i && cp.getValue().equals(attrVal))&& cp.getLabel().equals("e")){
					pEdible = pEdible* cp.getProbability();
					break;
				}
			}
			
		}
		if (pPoisonous > pEdible){
			predictedClass = "p";
			System.out.print("NBayes predicts --> Poisonous ");
		}else{
			predictedClass = "e";
			System.out.print("NBayes predicts --> Edible    ");
		}
		actualClass = attributes.get(0).getValue();
		if (actualClass.equals(predictedClass)){
			System.out.println("Correct");
			return 1;
		}else{
			System.out.println("Incorrect");
			return 0;
		}
	}
		
	public void runModel()	{
		int correct = 0;
		int incorrect; 
		double accuracy = 0.0;
		for(Mushroom m : testSet){
			correct = correct + classify(m);
		}
		incorrect = testSet.size() - correct;
		accuracy = (double)correct/testSet.size()*100.0;

		System.out.println("\n");
		System.out.println("--------------------------------------------");
		System.out.println("	Number correct:   " + correct);
		System.out.println("	Number incorrect: " + incorrect);
		System.out.println("	Accuracy:         " + accuracy + "%");
		System.out.println("--------------------------------------------");

	}
			
	
	
	
}
