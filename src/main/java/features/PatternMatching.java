package features;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PatternMatching {
    public static void main(String[] args) {
        // Directory containing files
        String pathOfDirectory = "src/main/resources";

        // Define regex patterns for phone numbers and email addresses
        String regexForPhone = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
        String regexForEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

        // Creating Pattern objects
        Pattern patternOfPhone = Pattern.compile(regexForPhone);
        Pattern patternOfEmail = Pattern.compile(regexForEmail);

        // Process each file in the directory
        File directory = new File(pathOfDirectory);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    System.out.println("Searching in file: " + file.getName());
                    searchInFile(file, patternOfPhone, patternOfEmail);
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }
    }

    public static void searchInFile(File file, Pattern patternOfPhone, Pattern patternOfEmail) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // Traverse the document
            traverseDoc(document, patternOfPhone, patternOfEmail);
        } catch (Exception e) {
            System.err.println("Error parsing the XML file: " + file.getName());
            e.printStackTrace();
        }
    }

    public static void traverseDoc(Node node, Pattern patternOfPhone, Pattern patternOfEmail) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                traverseDoc(childNode, patternOfPhone, patternOfEmail);
            }
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            String allText = node.getTextContent();
            Matcher matchForPhone = patternOfPhone.matcher(allText);
            while (matchForPhone.find()) {
                System.out.println("Phone number found: " + matchForPhone.group());
            }

            Matcher matchForEmail = patternOfEmail.matcher(allText);
            while (matchForEmail.find()) {
                System.out.println("Email address found: " + matchForEmail.group());
            }
        }
    }
}






