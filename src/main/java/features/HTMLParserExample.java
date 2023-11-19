package features;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLParserExample {
    public static void main(String[] args) {
//        String url = "https://example.com"; // Replace with the URL of the web page you want to parse
        String url = "https://www.ford.ca/?searchid=700000001050422&campaignid=71700000010155675&gclid=CjwKCAiA3aeqBhBzEiwAxFiOBhm5bRPepI0Nyp3LEF5qjUmeW5EQeDHBQqXrjLhS7h-tGo9KE4o_fxoCbYQQAvD_BwE&gclsrc=aw.ds";

        try {
            // Connect to the URL and fetch the HTML content
            Document doc = Jsoup.connect(url).get();

            // Example 1: Extract and print the title of the web page
            String pageTitle = doc.title();
            System.out.println("Page Title: " + pageTitle);

            // Example 2: Extract and print all the links on the web page
            Elements links = doc.select("a");
            System.out.println("Links on the Page:");
            for (Element link : links) {
                String linkText = link.text();
                String linkHref = link.attr("href");
                System.out.println(linkText + " : " + linkHref);
            }

            // Example 3: Extract and print a specific element by its CSS selector
            Element specificElement = doc.select("h2").first();
            if (specificElement != null) {
                String elementText = specificElement.text();
                System.out.println("Specific Element Text: " + elementText);
            } else {
                System.out.println("Specific Element not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
