

import java.io.*;
import java.util.*;


/*
      This project was completed by Melissa Mendoza and Mona Iwamoto. It was written without
      consulting code by other students.  Information on Apiori algorithms was obtained from
      original work by Rakesh Agrawal Ramakrishnan Srikant.
      */

/*
 * Your task for this assignment is to implement and evaluate the Apriori-based algorithm
 * for frequent itemsets mining.  

1.      Implement the Apriori algorithm that is originally proposed by Agrawal et al. 
		[AS94b] for frequent itemsets mining. You can also find the pseudocode and its related 
		procedures from the textbook.  You are not required but encouraged to use existing or 
		your own optimization techniques for the Apriori algorithm.  If you do, explain and 
		discuss the techniques you have used and/or provide the appropriate references in the 
		report.  
		You can use any programming language that you are familiar with.
		The program should be executable with 3 parameters: 
			the name of the input dataset file, the threshold of minimum support count, 
			and the name of the output file.  The minimum support count should be an integer.  
			An itemset is frequent if its support count is larger or equal to this threshold.  
		The program should output a file that contains all the frequent itemsets together 
			with their support.  The output file (sample output) should have the following format:
			each line contains a single frequent itemset as a list of items separated by 
			whitespace.  At the end of the line, its support is printed between a pair of 
			parenthesis.  For example: 1 2 3 (5) represents an itemset containing items 1, 2 
			and 3 with a support count of 5.

2.      Test your implementation on the dataset T10I4D100K (.dat, .gz) and measure execution time as well as number of frequent itemsets with various minimum support values.  The test dataset is a synthetic dataset that contains 100,000 transactions with an average size of 10 items from a set of 1000 distinct items.  Detailed descriptions about the dataset can be found in [AS94b].  You can also try your program with various other frequent itemset mining datasets.

3.      Write a brief report in PDF presenting your results on the test dataset and other datasets if you have tried.  Explain and discuss, if any, the algorithmic optimizations you have used in your implementation.  Discuss the experiences and lessons you have learned from the implementation.

4.      You can work as a team of up to two.  If you work on your own, you get 5 bonus points.  If you work as a team of two, please explain the contribution of each team member in your report.   

5.      Your submission should be a zip or tar file that contains the PDF report as well as the program deliverables including your source files, the executable, a readme file explaining how to compile/run your program, the output file for the test dataset with minimum support count 500 (corresponds to relative support 0.5%), and the PDF report.

 

Note: Please start early and be warned that an implementation without careful planning or 
efficient data structures could run for days!  There are a few online repositories for 
frequent pattern mining implementations, most notably FIMI repository.  You can study them 
but you are asked not to copy their implementations for this assignment.  

 

 */
public class FreqItems {

	int numItems;  // given in the problem
	int numTrans;
	String inputfile;
	String outputfile;
	int minsup;
	boolean checkData = true;		

	private List<int[]> lItems;
	
    public FreqItems(String[] args) throws Exception {
    	numItems = 1000;
    	numTrans = 0;
    	inputfile = args[0];
    	outputfile =args[2];
		minsup = Integer.parseInt(args[1]);
		
		// probably should count the number of items and transactions
		if (checkData) {
			setDataInfo();
		}
		
    }
    

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FreqItems fi = new FreqItems(args);
		fi.go();
		
	}
	
	public void go() throws Exception{
	
		int k = 1;
		// Start Timer
        long start = System.currentTimeMillis();
		File file = new File(outputfile);
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);

        
        
		buildL1();     						//k=1                
		keepItemswithSupport();
        System.err.println(lItems.size()+" frequent itemsets of size k = " + k + " (with support "+ minsup );
        
		int numCandidates = lItems.size();
		k=2;  			
		while (numCandidates >0) { 
			
	        	System.err.println("Creating C - " + k );

			createCandidateList(k);
			
			    long lap = System.currentTimeMillis();
			   	System.err.println("Created candidate list in "+((double)(lap-start)/1000) + " seconds.");
				System.err.println("Scanning data for support");
				
			keepItemswithSupport();
			
				long lap2 = System.currentTimeMillis();
				System.err.println("Scanned data in "+((double)(lap2-lap)/1000) + " seconds.");
		        System.err.println(lItems.size()+" frequent itemsets of size k = " + k + " (with support "+ minsup );
	        
			numCandidates = lItems.size();
			k++;
			
		}
		        long end = System.currentTimeMillis();
				System.err.println("Done. In "+((double)(end-start)/1000) + " seconds.");
		
	}
	
	
	// create L1 (large 1-itemsets)  
	public void buildL1() {
		lItems = new ArrayList<int[]>();
        for(int i=0; i<numItems; i++){
        	int[] c = {i};
        	lItems.add(c);
        }
	}
	
	// get candidates
	public void keepItemswithSupport() throws Exception{
		
		String strLine;
		int support[] = new int[lItems.size()];
		List<int[]> temp = new ArrayList<int[]>();
		
		
		// Open the file

		BufferedReader fstream = new BufferedReader(new InputStreamReader(new FileInputStream(inputfile)));

		
		//Read File Line By Line
		for (int i = 0; i < numTrans; i++) {
			strLine = fstream.readLine();
			

			StringTokenizer t = new StringTokenizer(strLine," ");  //  space delimited (can change delimiter here
	   	  	boolean[] tdetail = new boolean[numItems];
   	  
	   	  	while (t.hasMoreTokens()) {
	   	  		int x = Integer.parseInt(t.nextToken());    			
	   	  		if (x+1>numItems) numItems=x+1;
	   	  		tdetail[x]=true;			//  for each transaction record which items match
    	  }
	   	  
	   	  findSupport(tdetail, support);
	   	  
		}

		//Close the input stream
		fstream.close();
		
		for (int i = 0; i < lItems.size(); i++) {

			if (support[i]  >= minsup) {
				temp.add(lItems.get(i));				// print results
				for (int x : lItems.get(i)){
					System.out.print(x + " ");
				}
				System.out.println(" ("+support[i]+')');
			}
		}

       lItems = temp;    // this is the new itemset with minimum support

	}
	
	//  create new Ck candidate list
	public void createCandidateList(int k){
		
		boolean duplicate = false;
		
		// try hashmap
    	HashMap<String, int[]> temp = new HashMap<String, int[]>();
    	
        
        for(int i=k-2; i<lItems.size(); i++)
        {
            for(int j=i+1; j<lItems.size(); j++)
            {
                int[] A = lItems.get(i);
                int[] B = lItems.get(j);
              

                int [] setK = new int[k];
                for(int n=0; n< k-1; n++) {     // create new candidate set and fill with k-1 items
                	setK[n] = A[n];				// from set A
                }
                    
                for(int b=0; b<B.length; b++)
                {
                	duplicate = false;
                	
                    for(int a=0; a<A.length; a++) {
                    	if (A[a]==B[b]) { 
                    		duplicate = true;
                    		break;				
                    	}
                	}
                	if (!duplicate){ 
                		
                		setK[k-1] = B[b];		// add new item to the set
                	}
            	
            	}
            
                if (!duplicate) {
               	Arrays.sort(setK);
                	temp.put(Arrays.toString(setK),setK);
                }
            }
        }
        
        lItems = new ArrayList<int[]>(temp.values());
 	
	}
	
	public void findSupport(boolean[] tdetail, int[] support){

	   	boolean match;  
		//Check candidates against transaction
	   	for (int i = 0; i < lItems.size(); i++) {
			match = true;
	   		int[] candidates  = lItems.get(i);
	   		
			// check each item in lItems to see if it is present in the
			// transaction  *  changed to break out if a single candidate fails
	   		
			for (int candidate : candidates) {
				
				if (tdetail[candidate] == false) {
					match = false;
					break;
				}
			}
			if (match) { // if at this point it is a match, increase the count
				support[i]= support[i]+1;

			}
	   	  }
	}
	
	
	public void setDataInfo() throws Exception{
		
		String strLine;
		numTrans = 0;
		
		// Open the file
	   	BufferedReader data_in = new BufferedReader(new FileReader(inputfile));
    	while (data_in.ready()) {    		
    		strLine=data_in.readLine();
    		if (strLine.matches("\\s*")) continue; // be friendly with empty lines
    		numTrans++;
    		StringTokenizer t = new StringTokenizer(strLine," ");
    		while (t.hasMoreTokens()) {
    			int x = Integer.parseInt(t.nextToken());
 
    			if (x+1>numItems) numItems=x+1;
    		}    		
    	}  
	   	  
	   	  
	
			
	}
}
	
