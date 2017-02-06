

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {
	private static int classLabelIndex;
    private static int numAttributes;
    private static boolean[] attribute;
    private static List<List<Character>> totalAttributes;
    private double infoOfData;
    private int numTuples;
    private int splittingAttribute;
    private char branch;
    private char label;
    private List<char[]> trainingData;
    private List<TreeNode> children;
    private Type type;

    public TreeNode() {
        numTuples = -1;
        infoOfData = -1;
        splittingAttribute = -1;
        trainingData = new ArrayList<char[]>();
        children = new ArrayList<TreeNode>();
    }

    public enum Type {
        inside, leaf, root
    }
    
    public char getLabel() {
        return label;
    }

    public int getSplittingAttribute() {
        return splittingAttribute;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public char getBranch() {
        return branch;
    }

    public void setBranch(char branch) {
        this.branch = branch;
    }


    public void setTrainingData(List<char[]> data) {
        this.trainingData = data;
        numAttributes = this.trainingData.get(0).length;
        numTuples = this.trainingData.size();
        infoOfData = calculateInfoOfSubset(this.trainingData, classLabelIndex);
        if (tuplesAreAllSameClass()) {				//convert to leaf node without majority voting
        	boolean majority = false; 
        	if (type != Type.leaf) {
        		type = Type.leaf;
        		if (majority) {
                     label = getMajorityClass();
        			// System.out.println(label);			//prints class label
        		}
                else{
                     label = trainingData.get(0)[classLabelIndex];
                    // System.out.println(label);
                }
        	}
        } else if (attributeListIsEmpty()) {		//convert to leaf node with majority voting
        	boolean majority = true; 		
        	if (type != Type.leaf) 
	            type = Type.leaf;
        		if (majority) {
        			label = getMajorityClass();
        			System.out.println(label);			//prints class label
        		}
        		else {
        			label = trainingData.get(0)[classLabelIndex];
        			System.out.println(label);
        		}
        	}
    }

    public boolean attributeListIsEmpty() {
        for (int i = 0; i < numAttributes; i++) {
            if (attribute[i]) 
                return false;
        }
        return true;
    }

    
    public boolean tuplesAreAllSameClass() {
        boolean sameClass = false;

        char value = trainingData.get(0)[classLabelIndex];
        for (int i = 1; i < trainingData.size(); i++) {
            if (trainingData.get(i)[classLabelIndex] == value) 
                sameClass = true;
            else {
                sameClass = false;
                break;
            }
        }
        return sameClass;
    }

    // sets the initial dataset, should only be called by root
    public void setTrainingDataInitial(List<char[]> data, int indexOfClassLabel) {
        this.trainingData = data;
        numTuples = this.trainingData.size();
        numAttributes = this.trainingData.get(0).length;

        totalAttributes = new ArrayList<List<Character>>();
        getAttributeList(indexOfClassLabel);
        createUniqueAttributes();
        infoOfData = calculateInfoOfSubset(this.trainingData, classLabelIndex);
    }

    
    private void getAttributeList(int indexOfClassLabel) {
    	classLabelIndex = indexOfClassLabel;
        attribute = new boolean[numAttributes];
        Arrays.fill(attribute, true);						// list of all attributes
        attribute[classLabelIndex] = false;
    }

    private void createUniqueAttributes() {
        for (int i = 0; i < numAttributes; i++) {
            List<Character> attributeSet = new ArrayList<Character>();
            totalAttributes.add(attributeSet);	
        }
        for (char[] record : trainingData) {
            for (int i = 0; i < numAttributes; i++) {
                List<Character> uniqueAttributes = totalAttributes.get(i);
                if (!uniqueAttributes.contains(record[i])) 
                    uniqueAttributes.add(record[i]);			//unique values for attribute
            }
        }
    }

    public int findPartitioningAttribute() {
        int indexOfAttribute = -1;
        double oldGainRatio = 0;

        for (int currentAttribute = 0; currentAttribute < numAttributes; currentAttribute++) {
            if (currentAttribute == classLabelIndex || !attribute[currentAttribute]) 
                continue;

            double infoOfAttribute = 0;
            List<Character> uniqueAttributes = totalAttributes.get(currentAttribute);
            List<char[]> subsetForSplitInfo = new ArrayList<char[]>();

            for (Character attribute : uniqueAttributes) {
                List<char[]> subsetForInfo = new ArrayList<char[]>();
                for (int i = 0; i < numTuples; i++) {
                    char[] tuple = trainingData.get(i);
                    if (tuple[currentAttribute] == attribute) {
                        subsetForInfo.add(tuple);
                        subsetForSplitInfo.add(tuple);
                    }
                }
                int subsetSize = subsetForInfo.size();
                double infoOfSubset = calculateInfoOfSubset(subsetForInfo, classLabelIndex);
                infoOfAttribute += (((double) subsetSize / numTuples) * infoOfSubset);
            }

            double splitInfo = calculateInfoOfSubset(subsetForSplitInfo, currentAttribute);
            double gain = infoOfData - infoOfAttribute;
            double gainRatio = gain / splitInfo;							//GAIN RATIO - C4.5

            if (gainRatio > oldGainRatio) {
                    oldGainRatio = gainRatio;
                    infoOfAttribute = 0;
                    indexOfAttribute = currentAttribute;
            }
      }

        assert indexOfAttribute > -1;
        splittingAttribute = indexOfAttribute;
        return indexOfAttribute;
    }

    public void partitionData(int indexOfPartitioningAttribute) {
        if (type == Type.leaf) 
            return;
        
        assert indexOfPartitioningAttribute == splittingAttribute;
        List<Character> uniqueAttributes = totalAttributes.get(indexOfPartitioningAttribute);
        attribute[indexOfPartitioningAttribute] = false;

        for (Character attribute : uniqueAttributes) {
            TreeNode child = new TreeNode();
            List<char[]> subset = new ArrayList<char[]>();

            for (char[] tuple : trainingData) {
                if (tuple[indexOfPartitioningAttribute] == attribute) 
                    subset.add(tuple);
            }

            children.add(child);
            child.setBranch(attribute);
            if (subset.size() == 0) {
                child.setType(Type.leaf);
                continue;
            }

            child.setType(Type.inside);
            child.setTrainingData(subset);
        }
    }

    private double calculateInfoOfSubset(List<char[]> subset, int indexOfAttribute) {
        int subsetSize = subset.size();
        if (subsetSize == 0)
            return 0;
        double totalInfo = 0;

        List<Character> uniqueAttributes = totalAttributes.get(indexOfAttribute);
        for (Character attribute : uniqueAttributes) {
            int countAttrSubset = 0; 					//count attributes of the subset
            for (char[] tuple : subset) {
                if (tuple[indexOfAttribute] == attribute)
                    countAttrSubset++;
            }
            
            double frequency = (double) countAttrSubset / subsetSize;
            double zero = 0; 
            double newFreq = 0; 
            if (frequency == zero)
            	newFreq = zero; 
            else 
            	newFreq = (Math.log10(frequency) / Math.log10(2));
            totalInfo -= (frequency * newFreq);
        }
        return totalInfo;
    }


    private char getMajorityClass() {
        List<Character> uniqueAttributes = totalAttributes.get(classLabelIndex);
        Map<Character, Integer> counts = new HashMap<Character, Integer>();

        for (Character attribute : uniqueAttributes) {
            int countAttrSubset = 0; 								//count attributes in subset of training data
            for (char[] tuple : trainingData) {
                if (tuple[classLabelIndex] == attribute) 
                    countAttrSubset++;
            }
            counts.put(attribute, countAttrSubset);				// count the majority class label
        }
        int max = -1;
        char majority = '\0';
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > max) {
                majority = entry.getKey();
                max = entry.getValue();
            }
        }
        assert majority != '\0';
        return majority;	
    }
}
