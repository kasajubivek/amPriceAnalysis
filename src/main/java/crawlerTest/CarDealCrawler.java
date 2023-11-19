package crawlerTest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CarDealCrawler {
    public static void main(String[] args) {
        String[] websites = {
                "https://www.worldometers.info/geography/alphabetical-list-of-countries/"
//                "https://www.ford.ca/mustang/",
//                "https://example.com/deals/ford",
//                "https://example.com/deals/chrysler"
        };

        for (String website : websites) {
            crawlCarDeals(website);
        }
    }

    public static void crawlCarDeals(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if(con.response().statusCode() == 200){

                for(Element list: doc.select("table.table-hover.table-condensed tr")){
                    String countries = list.select("td:nth-of-type(2)").text();
                    String population = list.select("td:nth-of-type(3)").text();
                    System.out.println(countries+" "+population.replace(",",""));
                }


                Elements carElements = doc.select(".car-deal");

                for (Element carElement : carElements) {
                    String carMake = carElement.select(".make").text();
                    String carModel = carElement.select(".model").text();
                    String carPrice = carElement.select(".price").text();
                    String fuelEfficiency = carElement.select(".fuel-efficiency").text();
                    String seatingCapacity = carElement.select(".seating-capacity").text();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
