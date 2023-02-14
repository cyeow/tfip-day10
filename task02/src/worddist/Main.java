package worddist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

        // load content in files
        List<String> corpus = loadFileContent(files);

        // process content into map 
        Map<String,Map<String,Integer>> worddist = new HashMap<>();

        String w1 = "", w2 = "";

        for(String line : corpus) {
            
        }
        
    }

    private static List<String> loadFileContent(File[] files) throws IOException {
        List<String> corpus = new ArrayList<>();
        
        for(File f : files) {
            if(f.isFile()) {
                try {
                    corpus.addAll(readFromFile(f));
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found!");
                }    
            } else {
                System.out.println(f.getName() + " skipped as it is a directory.");
            }
        }

        return corpus;
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

    private static List<String> readFromFile(File file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        List<String> result = new ArrayList<>();
        String line = "";

        while((line = br.readLine()) != null) {
            if(!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))) {
                result.add(line.replaceAll("[^a-zA-Z ]", "").toLowerCase());    
            }
        }

        br.close();
        fr.close();

        return result;
    }


}
