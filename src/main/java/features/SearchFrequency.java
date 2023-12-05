package features;

import java.util.HashMap;
import java.util.Map;
import java.util.*;


public class SearchFrequency {
    public static void main(String[] args) {
        // Creating a HashMap to store input words and their frequencies
        Map<String, Integer> wordFreq = new HashMap<>();

        // A Scanner is created to take input of the users
        Scanner inputData = new Scanner(System.in);

        // A loop is created to take input till the user stops
        while (true) {
            System.out.print("Input a word you want to search (to stop, type 'exit'): ");
            String typedWord = inputData.next();

            // This is to check if the user wants to exit
            if (typedWord.equalsIgnoreCase("exit")) {
                break;
            }

            // the frequency is updated each time an input is taken
            int currentFreq = wordFreq.getOrDefault(typedWord, 0);
            wordFreq.put(typedWord, currentFreq + 1);

            
        }

        // The words and its frequency is displayed
        System.out.println("The Words and their Frequencies:");
        for (Map.Entry<String, Integer> enteredWord : wordFreq.entrySet()) {
            System.out.println(enteredWord.getKey() + ": " + enteredWord.getValue() + " time(s)");
        }

        // To stop scanning input
        inputData.close();
    }
}



















