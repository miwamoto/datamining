

import java.io.FileNotFoundException;
import java.util.List;

public class Classify {

	private TreeNode root;
	private List<char[]> trainingData;
	private List<char[]> testData;
	private int indexOfClassLabel;

	public Classify(String trainingData, String testData, int indexOfClassLabel) throws FileNotFoundException {
		this.indexOfClassLabel = indexOfClassLabel;
	    this.trainingData = c45Tree.readData(trainingData);
	    this.testData = c45Tree.readData(testData);
	    
	    root = new TreeNode();
	    root.setTrainingDataInitial(this.trainingData, this.indexOfClassLabel);
	    root.setType(TreeNode.Type.root);
	    learn(root);
	}
	
	private void learn(TreeNode node) {
        if (node.getType() != TreeNode.Type.leaf) {
            int indexOfPartitioningAttribute = node.findPartitioningAttribute();
            node.partitionData(indexOfPartitioningAttribute);
            for (TreeNode child : node.getChildren()) {
                learn(child);
            }
        }
    }
	
	public static void printAccuracy(int numCorrect, int numIncorrect, int total) {
		double accuracy = (double) numCorrect / total;
	    System.out.println("Number correct: " + numCorrect + "\n" + "Number incorrect: " + numIncorrect + "\n" +
	                		"Percent Correct: " + accuracy * 100 + "%");
	}

	public void testTree() {
		int numberCorrect = 0;
		int numberIncorrect = 0;
		for (char[] tuple : testData) {
			TreeNode node = root;
	        char trueClassification = tuple[indexOfClassLabel];
	        char testClassification = classify(tuple, node);
	        	System.out.println("Classification Label: " + testClassification); 	//prints classification label of tuple 
	        if (testClassification == trueClassification) {
	        	numberCorrect++;
	        } else {
	        	numberIncorrect++;
	        }
	    }
		printAccuracy(numberCorrect, numberIncorrect, testData.size());
	}

	private char classify(char[] tuple, TreeNode node) {
		while (node.getType() != TreeNode.Type.leaf) {
	    	List<TreeNode> children = node.getChildren();
	        int index = node.getSplittingAttribute();
	        for (TreeNode child : children) {
	        	if (tuple[index] == child.getBranch()) {
	        		node = child;
	                break;
	            }
	        }
	    }
	    return node.getLabel();				//returns test classification
	}  
}
