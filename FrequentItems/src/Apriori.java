
import java.io.*;
import java.util.*

/*
      THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
      CODE WRITTEN BY OTHER STUDENTS. Mona Iwamoto 
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
		•You can use any programming language that you are familiar with.
		•The program should be executable with 3 parameters: 
			the name of the input dataset file, the threshold of minimum support count, 
			and the name of the output file.  The minimum support count should be an integer.  
			An itemset is frequent if its support count is larger or equal to this threshold.  
		•The program should output a file that contains all the frequent itemsets together 
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
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputfile;
		String outputfile = "output.txt";
		int minsup = 0;
		List<int{}> listitems;
		
		
		inputfile = args[0];
		minsup = Integer.parseInt(args[1]);
		
		System.out.println (inputfile + " " + minsup);
		
		// Open the file
		FileInputStream fstream = new FileInputStream(inputfile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  System.out.println (strLine);
		}

		//Close the input stream
		br.close();
	}
		
}


