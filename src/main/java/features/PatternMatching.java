package features;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatching {
    public static void main(String[] args) {
        // Directory containing files
        String pathToDir = "src/main/resources";

        // Define regex patterns for phone numbers, emails and prices
        String regexOfrPhone = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
        String regexForEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        String regexForPrice = "\\$\\d+,\\d+";

        // Create Pattern objects
        Pattern patternOfPhone = Pattern.compile(regexOfrPhone);
        Pattern patternOfEmail = Pattern.compile(regexForEmail);
        Pattern patternOfPrice = Pattern.compile(regexForPrice);

        // Process each file in the directory
        File directory = new File(pathToDir);
        File[] files = pathToDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("Searching in file: " + file.getName());
                    searchInFile(file, patternOfPhone, patternOfEmail, patternOfPrice);
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }
    }

    public static void searchInFile(File file, Pattern patternOfPhone, Pattern patternOfEmail, Pattern patternOfPrice) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matchForPhone = patternOfPhone.matcher(line);
                while (matchForPhone.find()) {
                    System.out.println("Phone number found: " + matchForPhone.group());
                }

                Matcher matchForEmail = patternOfEmail.matcher(line);
                while (matchForEmail.find()) {
                    System.out.println("Email address found: " + matchForEmail.group());
                }
                
                Matcher matchForPrice = patternOfPrice.matcher(line);
                while (matchForPrice.find()) {
                    System.out.println("Prices found: " + matchForPrice.group());
                }
                
                
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + file.getName());
            e.printStackTrace();
        }
    }
}






