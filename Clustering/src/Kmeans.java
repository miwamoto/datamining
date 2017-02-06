// This project was completed by Mona Iwamoto and Melissa Mendoza, without consulting code
// from other students.


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;

public class Kmeans {
	static String dataFile;
	static int k;
	static String outputFile;
	static int numRecords= 0;
	static int numAttributes = 0;

	public static String[] attrNames;


	public static void main(String[] args) throws Exception {

		dataFile = args[0];
		k = Integer.parseInt(args[1]);
		outputFile = args[2];
		boolean success = false;
		
		
		File file = new File(outputFile);
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);

		
		ArrayList<Record> dataset;

		dataset = buildDataSet(dataFile);
		success = cluster(dataset);
		
		ps.close();
		fos.close();
		
		
		
	}
	
	
	
	

	
	
	public static ArrayList<Record> buildDataSet(String filename) throws IOException, Exception {
		String strLine;
		String sValue;
		double dValue;
		numRecords = 0;
		numAttributes = 0;
		ArrayList<Record> dataset = new ArrayList<Record>();
		// Open the file
	
		ArrayList<Attribute> attributes;
		BufferedReader data_in = null;
		Record r;
		String attrLabel = "";
		

			File f = new File(filename);
        
			FileInputStream fis = new FileInputStream(f); 

			data_in = new BufferedReader(new InputStreamReader(fis));
			
			//first line in the data file are the attribute names
			
			if((strLine = data_in.readLine()) != null){
				attrNames = strLine.split("\t");
			}
	   	
			while ((strLine = data_in.readLine()) != null) {    
				
				StringTokenizer st = new StringTokenizer(strLine);
	    		attributes = new ArrayList<Attribute>();		
	    		r = new Record();
	    		numAttributes= st.countTokens();
	    		for(int i=0; i< numAttributes; i++) {
	    			sValue = "";
	    			dValue = 0.0;
	    			attrLabel = attrNames[i];
	    				if(i==numAttributes - 1){
	    					sValue = st.nextToken();	
	    				}else{
	    					dValue = Double.parseDouble(st.nextToken());
	    				}		  
		    			attributes.add(new Attribute(attrLabel,sValue,dValue));
	    		}
	    		r.setAttributes(attributes);
	    		dataset.add(r);
	    		numRecords++;
    		   		
			}
//			System.out.println(numRecords);
			System.out.print("K-means Clustering for data file: " + filename + "\t");
			System.out.println(numRecords + " Records");
	
			fis.close();
			data_in.close();
			return dataset;
	}	


	public static boolean cluster(ArrayList<Record> dataset){
		
		ArrayList<Double[]> centroids;
		ArrayList<Cluster> clusters;
		ArrayList<Cluster> finalClusters;
		double[] sse;
		boolean[] go = new boolean[k];
		boolean keepGoing = true;
		int iterations = 0;
		
		centroids = new ArrayList<Double[]>();
		finalClusters =  new ArrayList<Cluster> ();
		sse = new double[k];
		
		getInitialCentroids(centroids, dataset);
		
		while (keepGoing) {
			iterations = iterations +1;
			clusters = new ArrayList<Cluster> ();
			for (int i = 0; i<k; i++){
				Cluster c = new Cluster();
				clusters.add(c);
			}

			// assign each record to a cluster
			for (Record r : dataset){
				
				sse = new double[k];
				for (int a = 0; a < numAttributes - 1; a++){
					double p = r.getAttributes().get(a).getNum();
					for (int x=0; x<k; x++){
						double dist = p - centroids.get(x)[a];
						sse[x] = sse[x] + (dist*dist);
						
					}
					
				}
				int closest = 0;
				for (int x = 0; x<k; x++){
					if (sse[closest] > sse[x]){
						closest = x;
					}
						
				}
				clusters.get(closest).getAttributes().add(r);
			}
			
			
			//update cluster means
			for (int x=0; x<k;x++){
				go[x] = updateClusterMeans(centroids.get(x), clusters.get(x).getAttributes());
			}
			keepGoing = false;
			for (int x=0; x<k; x++){
				if (go[x] == true){
					keepGoing = true;
				}
			}
			//if cluster means are changing keep going
			finalClusters = clusters;
		}	
		outputClusterInfo(centroids, finalClusters);
		
		System.out.println("Clusters complete in " + iterations + " iterations");
		
		return false;
		
	}
	
	public static void getInitialCentroids(ArrayList<Double[]> centroids, ArrayList<Record> dataset ){
		Double[] points;
		int x=0;
		double randomSeed = 0.0;
		Record r;
		
		
		//randomly choose k records  
		for (int i = 0; i< k; i++){
			
			points = new Double[numAttributes-1];

			randomSeed = Math.random();
			x = (int) (randomSeed * numRecords);
			r = dataset.get(x);
			for (int j = 0; j < numAttributes - 1; j++){
				
				points[j] = r.getAttributes().get(j).getNum();
			
			}
			centroids.add(points);
		}
	}
	
	public static boolean updateClusterMeans(Double[] centroids, ArrayList<Record> dataset){
		
		boolean keepGoing = true;
		double[] totals = new double[numAttributes - 1];
		double oldCentroid, newCentroid;
		double difference = 0.0;
		
		
		if(dataset.size()>0){
			for (Record r : dataset){
				for (int i = 0; i < numAttributes - 1; i++){
					totals[i] = totals[i] + r.getAttributes().get(i).getNum();			
				}
			}
			for (int i = 0; i<numAttributes - 1; i++){
				oldCentroid = centroids[i];
				newCentroid = totals[i]/dataset.size();
				difference = oldCentroid - newCentroid;
				
				keepGoing = !(difference == 0);
			
				centroids[i] = totals[i]/dataset.size();
			}
			
		}
		return keepGoing;
	}
	
	public static void outputClusterInfo(ArrayList<Double[]> centroids, 
										ArrayList<Cluster> clusters){
		String clusterClass = "";
		String trueClass = "";
		int correct = 0;
		int incorrect = 0;
		
		for (int i = 0; i<k ; i++) {
			System.out.println("\nCluster - " + i + " Centroid:" );
			System.out.println("------------------------------------------------");
			for (int j=0; j<numAttributes-1; j++){
				System.out.print(attrNames[j] + " \t" );
				System.out.println( centroids.get(i)[j]);
			}
			System.out.println("------------------------------------------------");

			ArrayList<Record> dataset = clusters.get(i).getAttributes();
			System.out.println(dataset.size() + " records");
			
			// calculate cluster label
			clusterClass = findClusterLabel(dataset);
			
			for (Record r : dataset){
				for(int x = 0; x< numAttributes-1; x++){
					System.out.print("\t" + r.getAttributes().get(x).getNum());
				}
				trueClass = r.getAttributes().get(numAttributes-1).getValue();
				System.out.println("\t" + r.getAttributes().get(numAttributes-1).getValue());
				if (trueClass.equals(clusterClass)){
					correct++;
				}else{
					incorrect++;
				}	
				
			}
		}
		// Show quality metrics
		System.out.println("Extrinsic Cluster Quality" );
		System.out.println(correct + "\trecords correctly clustered ");
		System.out.println(incorrect + "\trecords incorrectly clustered");
	
	}
	
	public static String findClusterLabel(ArrayList<Record> dataset){
		String label = "";
		ArrayList<String> labels;
		boolean found = false;
		
		int x = 0;
		int[] counts = new int[k];
		
		labels = new ArrayList<String>();
		
		for (Record r : dataset){
			label = r.getAttributes().get(numAttributes-1).getValue();
			if (!(labels.contains(label))){
				labels.add(label);
			}
			x =labels.indexOf(label);
			counts[x]= counts[x]+1;
		}
		
		x=0;
		for(int i=1; i<k; i++){
			if(counts[i]> counts[i-1]){
				x = i;
			}
		}
		label = labels.get(x);
		return label;
		
	}
	
}
