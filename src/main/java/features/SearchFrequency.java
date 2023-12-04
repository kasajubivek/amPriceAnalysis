package features;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class SearchFrequency {
    public static void main(String[] args) {
        // Create a HashMap to store words and their frequencies
        Map<String, Integer> wordFrequency = new HashMap<>();

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

            // Update the word frequency
            int currentFrequency = wordFrequency.getOrDefault(word, 0);
            wordFrequency.put(word, currentFrequency + 1);

            // Display the current word frequency
            System.out.println("Word: " + word + ", Frequency: " + wordFrequency.get(word));
        }

        // Display the final word frequencies
        System.out.println("Word Frequencies:");
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " times");
        }

        // Close the scanner
        scanner.close();
    }
}









