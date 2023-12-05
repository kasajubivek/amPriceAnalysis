package features;


import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatching {
    public static void main(String[] args) {
        // Showing path of directory
        String pathToDir = "C:\\Users\\nikit\\OneDrive\\Desktop\\Advanced Computing Concepts\\Documents\\GitHub\\acc_project\\src\\main\\resources";
        // Define regex patterns for phone numbers and prices
        String regexOfrPhone = "\\b[0-9]{3}[-.]?[0-9]{3}[-.]?[0-9]{4}\\b";
      
        String regexForPrice = "\\$\\d+,\\d+";

        // Creating objects for Pattern
        Pattern patternOfPhone = Pattern.compile(regexOfrPhone);
        Pattern patternOfPrice = Pattern.compile(regexForPrice);

        // Processing all the files present in the  directory
        File dir = new File(pathToDir);
        File[] filesFound = dir.listFiles();
        if (filesFound != null) {
            for (File filesHere : filesFound) {
                if (filesHere.isFile()) {
                    System.out.println("Reading in file: " + filesHere.getName());
                    searchFile(filesHere, patternOfPhone, patternOfPrice);
                }
            }
        } else {
            System.out.println("Invalid directory path. Check or try a different path");
        }
    }

    public static void searchFile(File filesFound, Pattern patternOfPhone, Pattern patternOfPrice) {
        try (BufferedReader bufReaddd = new BufferedReader(new FileReader(filesFound))) {
            String data;
            while ((data = bufReaddd.readLine()) != null) {
                Matcher matchForPhone = patternOfPhone.matcher(data);
                while (matchForPhone.find()) {
                    System.out.println("Phone numbers in the file found: " + matchForPhone.group());
                }

                
                Matcher matchForPrice = patternOfPrice.matcher(data);
                while (matchForPrice.find()) {
                    System.out.println("Prices of car found: " + matchForPrice.group());
                }
                
                
            }
        } catch (IOException e) {System.out.println("Problem with readind files");}
          
    }
}






