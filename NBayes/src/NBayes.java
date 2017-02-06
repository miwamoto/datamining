package NBC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NBayes {

	private static Map<String, LabelAttribute> predicted; 
	private static Set<String> labels;
	private static Map<String, Integer> data; 
	private static int transactions; 
	private static int attribute; 
	static List<String> label = new ArrayList<String>();
	static List<String> labelGuessed = new ArrayList<String>();
	static List<UpdatedClass> pred = new ArrayList<>();
	

    public static void main(String[] args) throws Exception {
		String trainPath = args[0]; 	
		String testPath = args[1]; 		
		String outputPath = args[2]; 	
        
		//long time = System.currentTimeMillis();	
		predicted = new HashMap<String, LabelAttribute>();
		data = new HashMap<String, Integer>();
		labels = new HashSet<String>();
		transactions = 0;
		attribute = 0;
		
		initialize(trainPath);				//initializes training data
		test(testPath, outputPath);			//tests test path
		FileWriter fr = new FileWriter(new File(outputPath));
		BufferedWriter bw = new BufferedWriter(fr);			// print results in output text file
		
		int i;
		int correct = 0;
		for( i = 0; i < label.size(); i++) {
			bw.write("Original: " + label.get(i) + " Guessed: " + labelGuessed.get(i));
			bw.newLine();
			if(label.get(i).equals(labelGuessed.get(i))) 
				correct++;
		}
		double percentCorrect = (double) correct / (double) i;
		bw.write("Percentage Correct: " + percentCorrect*100 + " % ");
		bw.flush();
		bw.close();
		//System.out.println("Time:" + ((double) (System.currentTimeMillis() - time)/1000) + " sec");
    }
    
    public static void initialize(String trainPath) throws IOException {
    	FileReader fr = new FileReader(new File(trainPath));
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] lineSplit;
		while((line = br.readLine()) != null){
			transactions++;
			lineSplit = line.split("\t");
			labels.add(lineSplit[0]);
			placeAttributes(lineSplit);
		}
		br.close();
	}
    
    
    public static void placeAttributes(String[] line) {
		int count = 0;
		//line[0] is the class label
		if(data.containsKey(line[0])) 
			count = data.get(line[0]);
		
		data.put(line[0], ++count); 
		for(int i = 1; i < line.length; i++) {
			attribute++;
			count = 0;
			if(data.containsKey(line[i])) 
				count = data.get(line[i]);
			data.put(line[i], ++count); 
			LabelAttribute b;
			if(predicted.containsKey(line[0]+line[i])) {
				b = predicted.get(line[0]+line[i]);
				b.add(1);
			}
			else {
				b = new LabelAttribute(line[0], line[i]);
				b.add(1);
				predicted.put(line[0]+line[i], b);
			}
		}
    }
		
	public static void test(String testPath, String outputPath) throws IOException{
		FileReader fr = new FileReader(new File(testPath));
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] lineSplit;

		while((line = br.readLine()) != null){
				lineSplit = line.split("\t");
				label.add(lineSplit[0]);
				String predicted = predict(lineSplit);
				labelGuessed.add(predicted);
		}
		br.close();		
	}
	
	private static String predict(String[] lineSplit) {
		String bestPredictClass = "";
		double max = Double.MIN_VALUE;
		
		for(String label: labels){
			double x = data.get(label)/ transactions;
			UpdatedClass upClass = new UpdatedClass(label, x);
			pred.add(upClass);
		}		
		double score;
		
		for(int i = 1; i < lineSplit.length; i++) {
			score =  data.get(lineSplit[i]) / attribute;			//score 
			LabelAttribute temp;
			
			for(UpdatedClass p : pred){
				temp = predicted.get(p.getLabel()+lineSplit[i]);	
				
				if(temp==null){
					p.addScore(.0005);
					continue;
				}
				score = temp.getCount() / (double) data.get(lineSplit[i]);
				p.addScore(score);									//add score to updated class	
			}	
		}
		for (UpdatedClass p : pred){					//get the prediction of best predicted class
			if(p.getScore() > max)	{					//if score is greater 
				max = p.getScore();
				bestPredictClass = p.getLabel();
			}
		}	
		return bestPredictClass;
	}
}
