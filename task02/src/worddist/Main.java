package worddist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length <= 0) {
            System.out.println("Usage: java -d classes worddist.Main <directory_name>");
            System.exit(0);
        }

        // validate directory and load list of files
        File directory = new File(args[0]);
        validateDirectory(directory);
        File[] files = directory.listFiles();

        // generate next word distribution
        Map<String, Map<String, Integer>> nextWordDistribution = getNextWordDistribution(files);

        // calculate probability
        Map<String, Map<String, Double>> nextWordDistProbability = new HashMap<>();

        for(String w1 : nextWordDistribution.keySet()) {
            // find total number of next words (not unique)
            Integer numWords = nextWordDistribution.get(w1).values().stream().reduce(0, Integer::sum);

            for(String w2 : nextWordDistribution.get(w1).keySet()) {
                // iterate through the nextWord map
                
                // calculate probability 
                Double probability = nextWordDistribution.get(w1).get(w2) / Double.valueOf(numWords.toString());

                // put into nextWordDistProbability
                if(nextWordDistProbability.containsKey(w1)) {
                    // map has w1

                    if(nextWordDistProbability.get(w1).containsKey(w2)) {
                        // as nextWordDistribution is a unique set, logically w2 will not exist in nextWordDistProbability before we iterate through it
                        // but will check anyway and overwrite previous data, though the number should be the same.
                        nextWordDistProbability.get(w1).replace(w2, probability);

                    } else {
                        // map has w1 but submap does not have w2
                        nextWordDistProbability.get(w1).put(w2, probability);
                        
                    }
                } else {
                    // map does not have w1 --> create w2 map and put w1, w2map into the map
                    Map<String, Double> w2map = new HashMap<>();
                    w2map.put(w2, probability);

                    nextWordDistProbability.put(w1, w2map);
                }
            }
        }

        System.out.println(nextWordDistProbability);
    }

    private static Map<String, Map<String, Integer>> getNextWordDistribution(File[] files) throws IOException {
        Map<String,Map<String,Integer>> nextWordDistribution = new HashMap<>();

        String w1 = "", w2 = "";

        for(File f : files) {
            // load content in file
            // loading is done separately instead of combined as before, as words from separate books should not be compared to one another
            List<String> corpus = readFromFile(f);

            // process content into map 
            for(String line : corpus) {
                // split into individual words, ignoring whitespaces
                List<String> words = Arrays.asList(line.split("\\s+"));
                
                for(String word : words) {
                    // set first word
                    if(w1.equals("")) {
                        w1 = word;
                        continue;
                    } 
    
                    w2 = word;
    
                    if(nextWordDistribution.containsKey(w1)) {
                        // map has w1 --> check if submap has w2 --> update integer count if yes --> put w2 if no
    
                        if(nextWordDistribution.get(w1).containsKey(w2)) {
                            // submap has w2 --> update integer count
                            nextWordDistribution.get(w1).replace(w2, (nextWordDistribution.get(w1).get(w2) + 1));
    
                        } else {
                            // submap does not have w2 --> put
                            nextWordDistribution.get(w1).put(w2, 1);
    
                        }
    
                    } else {
                        // map does not have w1 --> put w1 with value w2, 1
                        Map<String, Integer> w2map = new HashMap<>();
                        w2map.put(w2, 1);
    
                        nextWordDistribution.put(w1, w2map);
                    }
    
                    w1 = w2;
                }

                // reset first word
                w1 = "";
            }
    
        }
        return nextWordDistribution;
    }

    private static void validateDirectory(File directory) {
        if(!directory.exists()) {
            System.out.println("Directory does not exist.");
            System.exit(0);
        } else if(!directory.isDirectory()) {
            System.out.println("Path is not a directory.");
            System.exit(0);
        } else if(directory.list().length == 0) {
            System.out.println("There are no files to analyse in this directory.");
            System.exit(0);
        }

    }

    private static List<String> readFromFile(File file) throws IOException {
        
        if(file.isDirectory()) {
            System.out.println(file.getName() + " is a directory.");
            return null;
        }

        FileReader fr = null;
        BufferedReader br = null;      
        List<String> result = new ArrayList<>();

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = "";
    
            while((line = br.readLine()) != null) {
                if(!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))) {
                    result.add(line.replaceAll("[^a-zA-Z ]", "").toLowerCase());    
                }
            }    
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } finally {
            br.close();
            fr.close();    
        }

        return result;
    }


}
