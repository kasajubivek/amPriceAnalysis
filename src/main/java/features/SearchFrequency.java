package features;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class SearchFrequency {
    public static void main(String[] args) {
        // Create a HashMap to store words and their frequencies
        Map<String, Integer> wordFreq = new HashMap<>();

        // Create a Scanner to read input
        Scanner scanner = new Scanner(System.in);

        // Loop to continuously receive input
        while (true) {
            System.out.print("Enter a word (type 'exit' to stop): ");
            String word = scanner.next();

            // Check if the user wants to exit
            if (word.equalsIgnoreCase("exit")) {
                break;
            }

            // Update the frequency of the word
            int currentFreq = wordFreq.getOrDefault(word, 0);
            wordFreq.put(word, currentFreq + 1);

            // Display the current word frequency
            System.out.println("Word: " + word + ", Frequency: " + wordFreq.get(word));
        }

        // Display the final word frequencies
        System.out.println("The Frequencies:");
        for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " times");
        }

        // Close the scanner
        scanner.close();
    }
}









