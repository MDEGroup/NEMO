package it.univaq.disim.seagroup.FOCUS;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;


class ValueComparator implements Comparator<String> {

    Map<String, Float> base;
    public ValueComparator(Map<String, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}



public class Runner {
	
	private String srcDir;	
	private String subFolder;
	private int numOfProjects;
	private int numOfNeighbours;
	private int numOfFolds;
	private int conf;
	
	public Runner(){
		
	}
	
	public void loadConfigurations(){		
		Properties prop = new Properties();				
		try {
			prop.load(new FileInputStream("evaluation.properties"));		
			this.srcDir=prop.getProperty("sourceDirectory");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return;
	}
			
	
	public void run(){		
		System.out.println("LUPE: Recommending API invocations with Deep Learning!");
		loadConfigurations();
		
		int startingVal = 192740; 	/*Round1*/ 
		startingVal = 192498; 		/*Round2*/
		startingVal = 192566; 		/*Round3*/
		startingVal = 191402; 		/*Round4*/
		startingVal = 191622; 		/*Round5*/
		startingVal = 192689; 		/*Round6*/
		startingVal = 191622; 		/*Round7*/
		startingVal = 191535; 		/*Round8*/
		startingVal = 192995; 		/*Round9*/
		startingVal = 191983; 		/*Round10*/
		
		startingVal = 32000;
				
		computePILOTAccuracy("./",startingVal);			
//		LevenshteinDistance("/home/phuong/CROSSMINER/Evaluation/PILOT/Dataset1200_2/Round10/",startingVal);		
		
	}
			
	/*compute accuracy for sequence to sequence learning*/
	
	public void computePILOTAccuracy(String path, int startingVal) {
		DataReader reader = new DataReader();
		String inputFile = "", resultFile = "";
		inputFile = "big_step_2TOPS.csv";
		resultFile = "resultsTOPS.csv";
		reader.PILOTEvaluationMetrics(path, inputFile, resultFile, startingVal);		
		return;		
	}
	
	public void LevenshteinDistance(String path, int startingVal) {
		DataReader reader = new DataReader();			
		String inputFile = "input_data.csv";		
		String resultFile = "results.csv";		
		reader.computeLevenshteinDistance(path,inputFile,resultFile,startingVal);		
		return;
	}
	
	
	public static void main(String[] args) {	
		Runner runner = new Runner();			
		runner.run();				    		    
		return;
	}	
	
}
