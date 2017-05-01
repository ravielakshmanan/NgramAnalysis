import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author ravielakshmanan
 *
 */
public class NGramCounter {
	
	/**
	 * A method to convert the given binary file to its hexadecimal equivalent
	 * @param file - the input file
	 * @return - a hexadecimal representation of the binary file
	 * @throws IOException
	 */
	public static String convertToHex(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		int bytesCounter = 0;
		int value = 0;
		StringBuilder sbHex = new StringBuilder();
		StringBuilder sbResult = new StringBuilder();

		while ((value = is.read()) != -1) {
		    //convert to hex value with "X" formatter
	        sbHex.append(String.format("%02X", value));

		    //Read a byte
		    if(bytesCounter==0){
		      	sbResult.append(sbHex).append(" ");
		       	sbHex.setLength(0);
		       	bytesCounter=0;
		    }else{
		        bytesCounter++;
		    }
	    }
		
		//if there are more bytes to process
		if(bytesCounter!=0){
		    for(; bytesCounter<1; bytesCounter++){
		    	sbHex.append("   ");
		    }
		    sbResult.append(sbHex);
	    }
		is.close();
		//System.out.println(sbResult);
		return(sbResult.toString());
	}
	
	/**
	 * A method to construct the ngrams for the hexadecimal string sequence passed
	 * @param n - ngram length
	 * @param str - hexadecimal string
	 * @param slidingWindow - the sliding window
	 * @return - a list of ngrams of specified length
	 */
	public static List<String> ngrams(int n, String str, int slidingWindow) {
        List<String> ngrams = new ArrayList<String>();
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++){
        	if(i > 0)
        		i = i + slidingWindow;
        	ngrams.add(concat(words, i, i+n));
        }
        return ngrams;
    }

	/**
	 * A method to concatenate the words
	 * @param words - the individual hexadecimal words
	 * @param start - the starting index
	 * @param end - the closing index
	 * @return - the concatenated string
	 */
    public static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        String wordToAppend = "";
        for (int i = start; i < end; i++){
        	try{
        		wordToAppend = words[i];
        	}catch(IndexOutOfBoundsException e){
        		wordToAppend = "<EOL>";
        	}
        	sb.append((i > start ? " " : "") + wordToAppend);
        }
        return sb.toString();
    }
    
    /**
     * A method to compute the nGram counts
     * @param nGramsList
     * @return - A map containing the nGram counts
     */
    public static Map<String, Integer> nGramCounter(List<String> nGramsList){
    	Map<String,Integer> nGramCounts = new HashMap<>();
    	for(String nGram: nGramsList){
    		nGramCounts.put(nGram, nGramCounts.getOrDefault(nGram, 0) + 1);
    	}
		return nGramCounts;	
    }
	
    /**
     * Main function
     * @param args
     * @throws IOException
     */
	public static void main(String[] args) throws IOException {
		int byteSequence = Integer.parseInt(args[0]); //2
		int slidingWindow = Integer.parseInt(args[1]); //2
		
		File inputFile = new File(args[2]); //bin/prog5
		
		String outputFile = args[3]; //bin/prog5.out
				
		if(slidingWindow > byteSequence){
			System.out.println("Sliding window cannot exceed the value provided for byte sequence. Please retry.");
			System.exit(-1);
		}
		
		//Convert the binary file to hex format
		String hexString = convertToHex(inputFile);
		
		List<String> nGramsList = ngrams(byteSequence, hexString, slidingWindow-1 );
		Map<String, Integer> nGramCountsMap = nGramCounter(nGramsList);
        
		Set<Entry<String, Integer>> nGramCountsSet = nGramCountsMap.entrySet();
		Iterator<Entry<String, Integer>> hashMapIterator = nGramCountsSet.iterator();
		
		//Write the results to file
		File opFile = new File(outputFile);

		Files.deleteIfExists(opFile.toPath());
		
		FileWriter fileWriter = new FileWriter(outputFile,true);
  	  	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		PrintWriter writer = new PrintWriter(bufferedWriter);
		
		//writer.println("The NGram counts for the binary file \"" + inputFile + "\" are as follows:\n");
		
	    while(hashMapIterator.hasNext()) {
	    	Map.Entry<String, Integer> mEntry = (Map.Entry<String, Integer>)hashMapIterator.next();
	    	if((int)mEntry.getValue() != 1){
	    		writer.println(mEntry.getKey() + " " + mEntry.getValue());
	    	}
	    }
		writer.close();
	}

}