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
        // Directory containing your XML files
        String directoryPath = "path/to/your/xml/files";

        // Define regex patterns for phone numbers and email addresses
        String phoneRegex = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

        // Create Pattern objects
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Pattern emailPattern = Pattern.compile(emailRegex);

        // Process each XML file in the directory
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    System.out.println("Searching in file: " + file.getName());
                    searchInXMLFile(file, phonePattern, emailPattern);
                }
            }
        } else {
            System.out.println("Invalid directory path.");
        }
    }

    public static void searchInXMLFile(File file, Pattern phonePattern, Pattern emailPattern) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // Traverse the XML document
            traverseXML(document, phonePattern, emailPattern);
        } catch (Exception e) {
            System.err.println("Error parsing the XML file: " + file.getName());
            e.printStackTrace();
        }
    }

    public static void traverseXML(Node node, Pattern phonePattern, Pattern emailPattern) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                traverseXML(childNode, phonePattern, emailPattern);
            }
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            String textContent = node.getTextContent();
            Matcher phoneMatcher = phonePattern.matcher(textContent);
            while (phoneMatcher.find()) {
                System.out.println("Phone number found: " + phoneMatcher.group());
            }

            Matcher emailMatcher = emailPattern.matcher(textContent);
            while (emailMatcher.find()) {
                System.out.println("Email address found: " + emailMatcher.group());
            }
        }
    }
}






