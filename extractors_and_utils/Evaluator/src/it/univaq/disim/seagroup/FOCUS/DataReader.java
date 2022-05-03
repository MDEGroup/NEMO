package it.univaq.disim.seagroup.FOCUS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;



public class DataReader {

	
	
	public DataReader() {		
		
	}
	
		
	
	public Map<String,Integer> count(String path, String filename, int val) {
		Map<String,Integer> map = new HashMap<String,Integer>();		
			
		
		filename = path + filename;
		String line = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));	
			
			int id = 0;
						
			while ((line = reader.readLine()) != null) {											
				
				String key = "";
				line = line.trim();
				int count = 0;
				
				String [] parts = line.split("\t");	
				
				key = parts[0];
									
				if(map.containsKey(key)) {
					count = map.get(key) + 1;
				} else count = 1;
				
				map.put(key,count);
				id++;
				
				if(id>val)break;
			}
			
			System.out.println("Total number of line:" + id);
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return map;
	}
	
		
	
	public void PILOTEvaluationMetrics(String path, String inputFile, String resultFile, int startingVal) {				
		String line = "";		
		
		Map<String,Set<String>> map = extractGroundTruthData4PILOT(path, inputFile, startingVal);		
		String features = "", results = "";				
		int match = 0, count = 0;
		
		Map<String,Integer> statistics = count(path,inputFile,startingVal);
		
		try {		
			
			BufferedReader reader = new BufferedReader(new FileReader(path + resultFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path + "PrecisionRecall.csv"));
			
			String[] parts = null;
			
			Set<String> prediction = new HashSet<String>();						
												
			while (((line = reader.readLine()) != null)) {									
				parts = line.split("\t");				
				features = parts[0];
				results = parts[1];
				String[] str = results.split(" ");
				int len = str.length;										
				
				prediction = new HashSet<String>();
				
				for(int i=0;i<len;i++) {
					prediction.add(str[i]);					
				}											
				
				Set<String> temp = map.get(features);						
					
				Set<String> groundtruth = new HashSet<String>();
				Set<String> common = new HashSet<String>();
								
				int max = 0;
											
				String selectedGT = "";
							
												
				for(String s:temp) {								
					str = s.split(" ");
					len = str.length;
					
					groundtruth = new HashSet<String>();
					
					for(int i=0;i<len;i++) {
						groundtruth.add(str[i]);					
					}
					
					common = Sets.intersection(prediction,groundtruth);
					
					if(common.size()>max) {
						selectedGT = s;
						max = common.size();
					}					
				}
								
				if(max>0)match++;
				
				
				/*compute precision, recall*/				
				str = selectedGT.split(" ");
				len = str.length;				
				groundtruth = new HashSet<String>();				
				for(int i=0;i<len;i++) {
					groundtruth.add(str[i]);					
				}				
				common = Sets.intersection(prediction,groundtruth);
				
				int val0 = common.size();
				int val1 = prediction.size();
				int val2 = groundtruth.size();
				
				float precision = (float) val0/val1;
				float recall = (float) val0/val2;							
				
				System.out.println(precision + "," + recall);
				
				int freq = 0;
				
				if(statistics.containsKey(features)) freq = statistics.get(features);
				else freq = 0;
								
				String content = precision + "," + recall + "," + freq;
				writer.append(content);							
				writer.newLine();
				writer.flush();					
				count++;				
			}	
			
			float sr = (float)match/count;
			
			System.out.println("Number of testing declarations " + count);
			System.out.println("Number of matches " + match);
			
			System.out.println("Accuracy is: " + sr);
			
			writer.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	public void computeLevenshteinDistance(String path, String inputFile, String resultFile, int startingVal) {				
		String line = "";		
		
		Map<String,Set<String>> GT = extractGroundTruthData4PILOT(path, inputFile, startingVal);

		String features = "", results = "";
		
		int match = 0, count = 0;
		
		try {		
			
			BufferedReader reader = new BufferedReader(new FileReader(path + resultFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path + "LevenshteinDistance.csv"));
			
			String[] parts = null;

			while (((line = reader.readLine()) != null)) {		
				
				parts = line.split("\t");
				
				features = parts[0].trim();
				results = parts[1].trim();
				
				System.out.println("feature: " + features);
				
				Set<String> groundTruth = GT.get(features);		
				
				System.out.println("Size is: " + groundTruth.size());
			
				int min = 1000;
				int lev = 0;
								
				String selectedGT = "";
								
				for(String gt:groundTruth) {					
					lev = LevenshteinDistance(results,gt);				
					if(lev<min) {
						selectedGT = gt;
						min = lev;
					}				
				}
								
				String content = selectedGT + "," + results + "," + Integer.toString(min); 
				writer.append(content);							
				writer.newLine();
				writer.flush();							
							
			}	
			
			System.out.println("Number of testing declarations " + count);
			System.out.println("Number of matches " + match);
			
			writer.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
		
	
	
	public int LevenshteinDistance(String string1, String string2) {                          
	    int len0 = string1.length() + 1;                                                     
	    int len1 = string2.length() + 1;                                                     
	                                                                                    
	    // the array of distances                                                       
	    int[] cost = new int[len0];                                                     
	    int[] newcost = new int[len0];                                                  
	                                                                                    
	    // initial cost of skipping prefix in String s0                                 
	    for (int i = 0; i < len0; i++) cost[i] = i;                                     
	                                                                                    
	    // dynamically computing the array of distances                                  
	                                                                                    
	    // transformation cost for each letter in s1                                    
	    for (int j = 1; j < len1; j++) {                                                
	        // initial cost of skipping prefix in String s1                             
	        newcost[0] = j;                                                             
	                                                                                    
	        // transformation cost for each letter in s0                                
	        for(int i = 1; i < len0; i++) {                                             
	            // matching current letters in both strings                             
	            int match = (string1.charAt(i - 1) == string2.charAt(j - 1)) ? 0 : 1;
	            // computing cost for each transformation                               
	            int cost_replace = cost[i - 1] + match;                                 
	            int cost_insert  = cost[i] + 1;                                         
	            int cost_delete  = newcost[i - 1] + 1;                             
	            // keep minimum cost                                                    
	            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
	        }                                                                           
	        // swap cost/newcost arrays                                                 
	        int[] swap = cost; cost = newcost; newcost = swap;                          
	    }                                                                               
	    // the distance is the cost for transforming all letters in both strings        
	    return cost[len0 - 1];                                                          
	}	
	
	/*Extract ground truth data for sequence to sequence learning*/
	
	public Map<String,Set<String>> extractGroundTruthData4PILOT(String path, String inputFile, int startingVal) {
		
		String line = "";		
		String inFile = path + inputFile;		
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();			
						
		try {					
			
			BufferedReader reader = new BufferedReader(new FileReader(inFile));		
						
			String[] parts = null;
			int index = 0;
						
			while (((line = reader.readLine()) != null)) {
				if(index<startingVal)index++;
				else {	
					parts = line.split("\t");								
					String features = parts[0];
					String APIs = parts[1].trim();	
														
					Set<String> list = new HashSet<String>();				
					if(map.containsKey(features))list = map.get(features);
					else list = new HashSet<String>();
					list.add(APIs);
					map.put(features,list);			
				}
			}
			reader.close();						

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
		
	
	public Map<String,String> readDictionary(String path, String filename) {							
		Map<String,String> ret = new HashMap<String,String>();	
		String line = null;		
		String[] vals = null;		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path + filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String invocation = vals[0].trim();
				String ID = vals[1].trim();
				ret.put(invocation, ID);		
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return ret;		
	}
	
	
	
	
	public Map<Integer, String> readProjectList(String filename, int startPos, int endPos){		
		Map<Integer,String> ret = new HashMap<Integer,String>();		
		String line="",repo="";
		int count=1;
		int id=startPos;
		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));			
			while (count< startPos) {
				line = reader.readLine();
				count++;
			}			
			while (((line = reader.readLine()) != null)) {
				repo = line.trim();			
				ret.put(id,repo);				
				id++;
				count++;
				if(count>endPos)break;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;				
	}
	
	
	
	
}