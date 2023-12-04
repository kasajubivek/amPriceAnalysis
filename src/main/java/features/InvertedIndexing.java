package features;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndexing {
    public Map<String, Map<String, Integer>> _hm_ = new HashMap<>();;

//    public InvertedIndexing() {
//        this._hm_ = new HashMap<>();
//    }


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
        //removes unwanted characters
        content = content.replaceAll("[\\'\".,$£€\\[\\](){},:;\\-_]", "");
        String[] _terms_ = content.toLowerCase().split("\\s+");

        for (String _term_ : _terms_) {
            if (!_hm_.containsKey(_term_)) {
                _hm_.put(_term_, new HashMap<>());
            }

            if(_hm_.get(_term_).size() == 0){
                _hm_.get(_term_).put(_url_,1);
                continue;
            }

            //updates the frequency if the url is present, if not adds the new url with an initial frequency of 1
            if( _hm_.get(_term_).containsKey(_url_)){
                _hm_.get(_term_).put(_url_, _hm_.get(_term_).get(_url_)+1);
            }else {
                _hm_.get(_term_).put(_url_, 1);
            }

        }

    }

    public Map<String, Integer> search(String _user_input_) {
        boolean first = true;
        String[] _uinput_terms_ = _user_input_.toLowerCase().split("\\s+");
        List<String> remove_url_list = new ArrayList<>();

        Map<String, Integer> first_entry = new HashMap<>();
        Map<String, Integer> updated_map = new HashMap<>();

        if (_hm_.containsKey(_uinput_terms_[0])) {
            first_entry.putAll(_hm_.get(_uinput_terms_[0]));
        }

        //for multiple words
        for (int i = 1; i < _uinput_terms_.length; i++) {
            String _ui_term_ = _uinput_terms_[i];
            if (_hm_.containsKey(_ui_term_)) {
                Map<String, Integer> _next_term_urls_ = _hm_.get(_ui_term_);
                for (Map.Entry<String, Integer> entry : first ? first_entry.entrySet(): updated_map.entrySet()) {
                    String url = entry.getKey();
                    int existing_frequency = entry.getValue();

                    // If one of the existing urs is present in the _next_term_urls_ map, update the frequency
                    if (_next_term_urls_.containsKey(url)) {

                        int next_term_frequency = _next_term_urls_.get(url);
                        updated_map.put(url, existing_frequency + next_term_frequency);
                    }
                    else if(!first){
                        remove_url_list.add(url);
                    }
                }

                //to remove uncommon urls
                if(!remove_url_list.isEmpty()){
                    for(String _unwanted_url_: remove_url_list){
                        updated_map.remove(_unwanted_url_);
                    }
                    remove_url_list.clear();
                }


                first = false;

            } else {
                return Collections.emptyMap();
            }
        }


        return updated_map.isEmpty()?first_entry:updated_map;
    }


    public static void main(String[] args) {
        String _user_input_;
        InvertedIndexing obj = new InvertedIndexing();

        List<String> urls = new ArrayList<>();
        urls.addAll(Arrays.asList("https://www.goauto.ca/vehicles",
                "https://www.cargurus.ca/",
                "https://www.driveaxis.ca/vehicle-listing"
              ));



        System.out.println("These are the current List of URLs: \n" + urls);

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDo you want to add more URLs? (Y/N)");

        _user_input_ = scanner.nextLine().toLowerCase();

        if (_user_input_.equalsIgnoreCase("yes") || _user_input_.equalsIgnoreCase("y")) {

            System.out.print("Enter the URL\n");
            System.out.println("Type 'done' after you finish!");

            String newUrl = scanner.nextLine();
            while (!newUrl.equalsIgnoreCase("done")) {
                if(isValid(newUrl)){
                    urls.add(newUrl);
                    System.out.println("URL added successfully.\n");

                    System.out.print("Enter another URL (or type 'done' to finish): ");
                    newUrl = scanner.nextLine();
                }else {
                    System.out.println("Invalid URL. Please enter a valid URL (or type 'done' to finish):");
                    newUrl = scanner.nextLine();
                }



            }
        }

        System.out.println("Crawling.....");
        obj.crawlAndIndex(urls);

        while (true) {

            System.out.println("\nEnter something to search\nIf you want to exit the search then type '_quit': ");

            _user_input_ = scanner.nextLine();

            if (_user_input_.equalsIgnoreCase("_quit")) {
                System.out.println("Sure!\n");
                break;
            }



            Map<String, Integer> searchResult = obj.search(_user_input_);


            List<Map.Entry<String, Integer>> searchResultList = new ArrayList<>(searchResult.entrySet());

            // Sort the list using merge sort based on the values
            mergeSort(searchResultList, 0, searchResultList.size() - 1);


            if (!searchResultList.isEmpty()) {
                System.out.println("\nThe relevant urls containing the input:\n");

                for (Map.Entry<String, Integer> url : searchResultList) {

                    System.out.println("URL: " + url.getKey() + ", Total Frequency: " + url.getValue());
                }

            } else {
                System.out.println("Sorry! No such urls were found related to the input.");
            }



        }

        System.out.println("Do you want to view the entire Inverted Index Map? (Y/N)");

        _user_input_ = scanner.nextLine();

        if(_user_input_.equalsIgnoreCase("yes") || _user_input_.equalsIgnoreCase("y")){
            for(Map.Entry<String, Map<String, Integer>> entry: obj._hm_.entrySet()){
                System.out.println("Word: "+entry.getKey());
                for(Map.Entry<String, Integer> innerEntry: entry.getValue().entrySet()){
                    System.out.println("URL: "+innerEntry.getKey()+" Frequency: "+innerEntry.getValue());
                }
                System.out.println("");
            }

            System.out.println("See you!");

        }else{
            System.out.println("See you!");
        }

        scanner.close();

    }

    public static boolean isValid(String _usr_inp_) {
        String urlPattern = "^(https?|ftp):\\/\\/[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(\\/[^\\s]*)?$";

        Pattern pattern = Pattern.compile(urlPattern);

        Matcher matcher = pattern.matcher(_usr_inp_);

        return matcher.matches();
    }



    public static void mergeSort(List<Map.Entry<String, Integer>> searchResultList, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;

            mergeSort(searchResultList, low, mid);
            mergeSort(searchResultList, mid + 1, high);

            merge(searchResultList, low, mid, high);
        }
    }

    public static void merge(List<Map.Entry<String, Integer>> searchResultList, int low, int mid, int high) {

        int _i_ = low;
        int _j_ = mid + 1;

        List<Map.Entry<String, Integer>> sorted_list = new ArrayList<>();


        while(_i_ <= mid && _j_ <= high){

            if(searchResultList.get(_i_).getValue() >= searchResultList.get(_j_).getValue()){
                sorted_list.add(searchResultList.get(_i_));
                _i_++;
            }else {
                sorted_list.add(searchResultList.get(_j_));
                _j_++;
            }


        }


        if(_i_ > mid){
            while (_j_ <= high){
                sorted_list.add(searchResultList.get(_j_));
                _j_++;
            }
        }else {
            while (_i_ <= mid){
                sorted_list.add(searchResultList.get(_i_));
                _i_++;
            }
        }


        for (int it = low; it <= high; it++) {
            searchResultList.set(it, sorted_list.get(it - low));
        }

    }


}