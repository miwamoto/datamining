

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class c45Tree {
    public static String trainingData;
    public static String testData;
    public static String outputPath;
    public static int indexOfClassLabel = 0;

    public static void main(String[] args) throws IOException {
    	trainingData = args[0];
        testData = args[1];
        outputPath = args[2];
        indexOfClassLabel = 0;
       
        Classify classifier = new Classify(trainingData, testData, indexOfClassLabel);
 
        File file = new File(outputPath);
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
       
        System.out.println("Testing " + testData);
        classifier.testTree();
    }

    public static List<char[]> readData(String fileName) throws FileNotFoundException {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(new File(fileName));
        StringTokenizer line;
        List<char[]> data;

        data = new ArrayList<char[]>();
        do {
            try {
                line = new StringTokenizer(scanner.nextLine());
            } catch (NoSuchElementException e) {
                break;
            }
            int numberOfAttributes = line.countTokens();

            char[] tuple = new char[numberOfAttributes];
            for (int i = 0; i < numberOfAttributes; i++) {
                String value = line.nextToken();
                assert value.length() == 1;
                tuple[i] = value.charAt(0);
            }
            data.add(tuple);
        } while (true);


        return data;
    }
    
}
