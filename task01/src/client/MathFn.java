package client;

import java.util.ArrayList;

public class MathFn {

    public Double mean(String input) {
        // mean = sum of all numbers / number of numbers
        Double sum = add(input);
        Integer numNumbers = getNumbersFromString(input).size();

        return sum / Double.valueOf(numNumbers);
    }
    
    public Double stdev(String input) {
        // stdev = sqrt(sum((number - mean)^2)/number of numbers)
        
        Double stdev;
        Double sumDev = 0d;
        ArrayList<Double> numbers = getNumbersFromString(input);
        Integer popSize = numbers.size();
        Double mean = mean(input);

        for(Double n : numbers) {
            sumDev += (Math.pow(n - mean,2));
        }

        stdev = Math.pow(sumDev / Double.valueOf(popSize), 0.5);
        
        return stdev;
    }
    
    private Double add(String input) {
        // sum of numbers
        ArrayList<Double> numbers = getNumbersFromString(input);
        Double result = 0d;

        for(Double d : numbers) {
            result += d;
        }

        return result;
    }

    private ArrayList<Double> getNumbersFromString(String input) {
        // split string into numbers; skips non-numerical input

        String[] array = input.split(",");
        ArrayList<Double> result = new ArrayList<>();

        for(Integer i = 0; i < array.length; i++) {
            try {
                result.add(Double.parseDouble(array[i]));
            } catch (NumberFormatException ex) {
                System.out.println("Non-numerical input detected and skipped.");
            }
        }
        
        return result;
    }
}
