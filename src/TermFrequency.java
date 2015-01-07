

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TermFrequency {
	
	public static Hashtable<String, Integer> stopWords = new Hashtable<String, Integer>();

	public static void loadStopWords(File file){
		List<String> words = new ArrayList<String>();
		try{
			words = tokenizeFile(file);			
		}
		catch(IOException e){
			System.out.println("COULD NOT LOAD STOP WORDS");
			words = null;
		}
		for(String item : words){
			stopWords.put(item, 1);
		}
	}
	
	
//	public static void printToFile(List<Frequency> frequencies, String path){
//        File log = new File(path);
//        try{
//	        if(log.exists()==false){
//	            System.out.println("We had to make a new file.");
//	            log.createNewFile();
//	        }
//	        PrintWriter out = new PrintWriter(new FileWriter(log, true));
//	        for(Frequency item : frequencies){
//		        out.append(item.getText() + "     " + item.getFrequency()); 
//		        out.println();
//		       
//	        }
//	        out.close();
//        }catch(IOException e){
//            System.out.println("COULD NOT LOG!!");
//        }
//	}
	
	public static ArrayList<String> tokenizeFile(File input) throws IOException
	{		
		
		String stuff = readFile(input.getPath(), StandardCharsets.UTF_8);
		String newStuff = stuff.toLowerCase();
		
		//the stop list needs to include 's'. The solution splits possessives like elizabeth's 
		//to "elizabeth s" and counts elizabeth but not s
		String[] temp = newStuff.replaceAll("[^a-zA-Z]", " ").toLowerCase().split("\\s+");
		//String[] temp = newStuff.split("[\\p{Punct}\\s]+");
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(temp));

		return words;				
	}
	
	public static String readFile(String path, Charset encoding) throws IOException
	{
		//stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
		//by erickson
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	

	
	public static void process(File file) throws IOException {
		
		
		Map<String, Integer> comparisonWords = new HashMap<String, Integer>();	
		if(file.exists()){
			List<String> words = new ArrayList<String>();
			words = tokenizeFile(file);
			String str = "";
		
			for(int x = 0; x < words.size();x++)
			{
				str = words.get(x);

				if(!stopWords.containsKey(str)){
					//increment count if it is already there
					if(comparisonWords.get(str) != null)
					{
						
						comparisonWords.put(str, new Integer(comparisonWords.get(str) + 1));
						
					}
					//otherwise add it to the table of words
					else
					{
						comparisonWords.put(str, new Integer(1));
					}
				}
			}
			
		} 
	
		Map<String, Integer> map = sortByValues(comparisonWords); 
	      Set set = map.entrySet();
	      Iterator iterator = set.iterator();
	      int count = 25;
	      while(iterator.hasNext() && count > 0) {
	           Map.Entry me = (Map.Entry)iterator.next();
	           //the if statement below ignores the 's' that is considered part of the possessives that was
	           //not included in the stop list
	           if(!me.getKey().equals("s")){
	        	   System.out.print(me.getKey() + "  -  ");
	        	   System.out.println(me.getValue());
	           count--;
	           }
	      }
	}

	public static HashMap<String, Integer> sortByValues(Map<String, Integer> comparisonWords) { 
	       LinkedList linkedList = new LinkedList(comparisonWords.entrySet());
		List list = linkedList;
	       
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return -((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });
	       
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	
	
	

	public static void main(String[] args) throws IOException{
		
	
		//String theFile = args[0];
		
		//File currentDir = new File(theFile);
		//File stops = new File("pride-and-prejudice.txt");
		
		//System.out.println(stops.getParentFile().getName());
		
		//String workingDir = System.getProperty("user.dir");
		//
		String workingDir = new File(".").getCanonicalPath();
		//String workingDir = new File(".").getAbsolutePath();
		//File workingDir = new File(".").getCanonicalFile().getParentFile();
		//System.out.println(workingDir);
		loadStopWords(new File(workingDir, "stop_words.txt"));
		
		//process(new File(theFile));
		process(new File(workingDir, "pride-and-prejudice.txt"));

		
	}
}