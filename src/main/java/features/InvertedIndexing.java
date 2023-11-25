package features;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndexing {
    private Map<String, Map<String, Integer>> _hm_;

    public InvertedIndexing() {
        this._hm_ = new HashMap<>();
    }


    public void crawlAndIndex(List<String> _urls_) {
        for (String _url_ : _urls_) {
            try {
                Document document = Jsoup.connect(_url_).get();
                String content = document.text();
                addDocument(_url_, content);
            } catch (IOException exception) {
                System.err.println("Error occurred when crawling " + _url_ + ": " + exception.getMessage());
            }
        }
    }


    public void addDocument(String _url_, String content) {
        content = content.replaceAll("['\".,]", "");
        String[] _terms_ = content.toLowerCase().split("\\s+");

        for (String _term_ : _terms_) {
            if (!_hm_.containsKey(_term_)) {
                _hm_.put(_term_, new HashMap<>());
            }

            if(_hm_.get(_term_).size() == 0){
                _hm_.get(_term_).put(_url_,1);
                continue;
            }


            if( _hm_.get(_term_).containsKey(_url_)){
                _hm_.get(_term_).put(_url_, _hm_.get(_term_).get(_url_)+1);
            }else {
                _hm_.get(_term_).put(_url_, 1);
            }

        }

    }

    public Map<String, Integer> search(String _user_input_) {
        String[] _uinput_terms_ = _user_input_.toLowerCase().split("\\s+");

        Map<String, Integer> result = new HashMap<>();

        if(_user_input_.contains("OR")){
            for (String _ui_term_ : _uinput_terms_) {
                if (_hm_.containsKey(_ui_term_)) {
                    result.putAll(_hm_.get(_ui_term_));
                }
            }
        }else {

            if (_hm_.containsKey(_uinput_terms_[0])) {
                result.putAll(_hm_.get(_uinput_terms_[0]));
            }

            for (int i = 1; i < _uinput_terms_.length; i++) {
                String _ui_term_ = _uinput_terms_[i];
                if (_hm_.containsKey(_ui_term_)) {
                    result.keySet().retainAll(_hm_.get(_ui_term_).keySet());
                } else {
                    return Collections.emptyMap();
                }
            }
        }

        return result;
    }


    public static void main(String[] args) {
        InvertedIndexing obj = new InvertedIndexing();

        List<String> urls = new ArrayList<>();
        urls.addAll(Arrays.asList("https://www.goauto.ca/",
                "https://en.wikipedia.org/wiki/Premier_League",
                "https://en.wikipedia.org/wiki/Chelsea",
                "https://www.cargurus.ca/",
                "https://www.football.london/chelsea-fc/news/chelsea-relegation-man-city-points-28141827",
                "https://www.express.co.uk/sport/football/1836898/Chelsea-Man-City-Everton-Premier-League-news-FFP"));



        System.out.println("These are the current List of URLs: \n" + urls);

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDo you want to add more URLs? (Y/N)");

        String userInput = scanner.nextLine().toLowerCase();

        if (userInput.equals("yes") || userInput.equals("y")) {

            System.out.print("Enter the URL\n");
            System.out.println("Type 'submit' after you're done!");

            String newUrl = scanner.nextLine();
            while (!newUrl.equalsIgnoreCase("submit")) {
                urls.add(newUrl);
                System.out.print("Enter another URL (or type 'done' to finish): ");
                newUrl = scanner.nextLine();
            }
        }

        System.out.println("Crawling.....");
        obj.crawlAndIndex(urls);

        System.out.println("Enter something to search:");
        System.out.println("(Note: To search for results related to x or y, use 'OR', to search for results related to x and y, use 'AND'");

        String _user_input_ = scanner.nextLine();

        scanner.close();

        Map<String, Integer> searchResult = obj.search(_user_input_);

        searchResult = searchResult.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);

        if (!searchResult.isEmpty()) {
            System.out.println("\nThe urls containing the input:\n");

            for (Map.Entry<String, Integer> url : searchResult.entrySet()) {

                System.out.println("URL: " + url.getKey() + ", Total Frequency: " + url.getValue());
            }

        } else {
            System.out.println("Sorry! No such urls were found related to the input.");
        }
    }


}