package features;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainClass {

    private static final WebDriver driver=new FirefoxDriver();
    //	private static final WebDriver driver=new ChromeDriver();

    private static final List<String[]> carDataRows = new ArrayList<>();
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));



    static Set<Double> uniquePricesNewCars = new HashSet<>();
    static Set<Double> uniquePricesUsedCars = new HashSet<>();
    static List<Car> bestDealsNewCars = new ArrayList<>();
    static List<Car> bestDealsUsedCars = new ArrayList<>();
    static List<Car> newCars = new ArrayList<>();
    static List<Car> usedCars = new ArrayList<>();



    static Set<String> _similar_words_ = new HashSet<>();
    static boolean _match_;



    public static void main(String[] args) {

        System.out.println("\nWelcome!\n");

        while(true){

            System.out.println("-------------------------------------------------");
            System.out.println("Please pick one of the following options to test. If you want to exit press '8'");
            System.out.println("1. Web crawler\n2. Best Car Deal\n3. Inverted Indexing\n4. Spell Checking\n5. Frequency Count & Page Ranking\n6. Search Frequency\n7. Pattern Matching\n8. Exit");
            System.out.println("-------------------------------------------------");

            Scanner _user_choice_scanner_ = new Scanner(System.in);
            int _user_choice_ = _user_choice_scanner_.nextInt();

            if (_user_choice_ == 8) {
                System.out.println("See ya. Have a nice day!\n");
                break;
            }

            switch (_user_choice_) {
                case 1:{
                    //Web Crawler
                    try{
                        driver.manage().window().maximize();
                        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                        WebCrawler.websitesToCrawl();

                        Thread.sleep(1000);
                        driver.quit();
                    }catch (InterruptedException e){
                        System.out.println(e);
                        e.printStackTrace();
                    }
                    break;
                }

                case 2:{
                    //best car deal

                    List<Car> _all_cars_data_ = new ArrayList<>();

                    String[] _cars_data_ = {"src/main/resources/driveaxis.ca.csv","src/main/resources/goauto.ca.csv","src/main/resources/cargurus.ca.csv"};

                    for(String _car_data_: _cars_data_){
                        List _data_ = BestCarDeal.readCarsFromCSV(_car_data_);
                        _all_cars_data_.addAll(_data_);
                    }


                    Car[] cars = _all_cars_data_.toArray(new Car[0]);


                    BestCarDeal.mergeSort(cars, 0, cars.length - 1, "fuelConsumption");
                    BestCarDeal.mergeSort(cars, 0, cars.length - 1, "price");


                    for (Car car : cars) {

                        if ("0".equals(car.getMileage())) {
                            newCars.add(car);
                        } else {
                            usedCars.add(car);
                        }
                    }

                    System.out.println("\nNew cars:\n");
                    for (Car car : newCars) {
                        System.out.println(car);
                    }


                    System.out.println("\nUsed cars:\n");
                    for (Car car : usedCars) {
                        System.out.println(car);
                    }



                    for (Car car : newCars) {
                        if (!uniquePricesNewCars.contains(car.getPrice())) {
                            uniquePricesNewCars.add(car.getPrice());
                            bestDealsNewCars.add(car);
                        }
                    }

                    for (Car car : usedCars) {
                        if (!uniquePricesUsedCars.contains(car.getPrice())) {
                            uniquePricesUsedCars.add(car.getPrice());
                            bestDealsUsedCars.add(car);
                        }
                    }


                    System.out.println("\nBest deals for New cars:\n");
                    for (Car car : bestDealsNewCars) {
                        System.out.println(car);
                    }

                    System.out.println("\nBest deals for Used cars:\n");
                    for (Car car : bestDealsUsedCars) {
                        System.out.println(car);
                    }

                    break;
                }

                case 3:{
                    //Inverted Indexing

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
                            if(InvertedIndexing.isValid(newUrl)){
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
                        InvertedIndexing.mergeSort(searchResultList, 0, searchResultList.size() - 1);


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

                    break;
                }

                case 4:
                    //Spell Checking

                    int count = 0;
                    String _usr_inp_ = "";

                    try{
                        Set<String> _manufacturers_ = new HashSet<>();
                        Scanner _text_scanner_ =  new Scanner(new File("src/main/resources/manufacturers.txt"));

                        while (_text_scanner_.hasNextLine()){
                            _manufacturers_.add(_text_scanner_.nextLine());
                        }

                        Scanner scanner = new Scanner(System.in);

                        while (true) {
                            System.out.println("Please enter your favourite car brand\nIf you want to exit the program then type 'quit': ");

                            _usr_inp_ = scanner.nextLine();

                            if(SpellChecking.isValid(_usr_inp_)){
                                if (_usr_inp_.equalsIgnoreCase("quit")) {
                                    System.out.println("See you!");
                                    break;
                                }


                                for(String manufacturer: _manufacturers_){
                                    _match_ = SpellChecking.editDistanceSimilarity(_usr_inp_, manufacturer);
                                    if(_match_){
                                        break;
                                    }
                                }


                                if(!_match_ && !_similar_words_.isEmpty()){
                                    System.out.println("Oh no! The entered word was not found in our dictionary. But here are some suggestions: ");
                                    for(String _wrd_: _similar_words_){
                                        System.out.println(_wrd_);
                                    }
                                    System.out.println();
                                    _similar_words_.clear();
                                }else if(!_match_ && _similar_words_.isEmpty()){
                                    System.out.println("Oh no! The entered word was not found in our dictionary.\n");
                                }
                            }else {
                                System.out.println("Error! Invalid Input.\n");
                            }

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    break;
                case 5:{

                    //Page Ranking and Frequency Count
                    try {
                        Path file1Path = Paths.get("C:\\Users\\Bivek\\Documents\\ACC\\ACC_Project\\src\\main\\resources\\cargurus.ca.csv");
                        Path file2Path = Paths.get("C:\\Users\\Bivek\\Documents\\ACC\\ACC_Project\\src\\main\\resources\\driveaxis.ca.csv");
                        Path file3Path = Paths.get("C:\\Users\\Bivek\\Documents\\ACC\\ACC_Project\\src\\main\\resources\\goauto.ca.csv");

                        try (Scanner scanner = new Scanner(System.in)) {
                            System.out.print("Enter a word for page ranking calculation: ");
                            String userInputWord = scanner.next().toLowerCase();

                            AVLTree avlTree = new AVLTree();

                            // Process each file
                            for (Path filePath : Arrays.asList(file1Path, file2Path, file3Path)) {
                                try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        String[] words = line.toLowerCase().split("\\s+");
                                        for (String word : words) {
                                            if (word.equals(userInputWord)) {
                                                avlTree.roots = avlTree.insert(avlTree.roots, filePath.getFileName().toString());
                                            }
                                        }
                                    }
                                }
                            }

                            // Calculate total frequency
                            List<Map.Entry<String, Integer>> resultList = new ArrayList<>();
                            avlTree.inOrderTraversal(avlTree.roots, resultList);
                            int totalFrequency = resultList.stream().mapToInt(Map.Entry::getValue).sum();
                            System.out.println("Total frequency of '" + userInputWord + "' across all files: " + totalFrequency);

                            // Print page ranking after Bubble Sort
                            PageRanking.bubbleSort(resultList);
                            System.out.println("\nPage ranking of '" + userInputWord + "' (sorted):");
                            for (int i = 0; i < resultList.size(); i++) {
                                System.out.println((i + 1) + ". " + resultList.get(i).getKey() + ": " + resultList.get(i).getValue() + " occurrences");
                            }
                        }

                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }


                    break;
                }

                case 6:{

                    // Create a HashMap to store words and their frequencies
                    Map<String, Integer> wordFreq = new HashMap<>();

                    // Create a Scanner to read input
                    Scanner scan = new Scanner(System.in);

                    // Loop to continuously receive input
                    while (true) {
                        System.out.print("Enter a word (type 'exit' to stop): ");
                        String findWord = scan.next();

                        // Check if the user wants to exit
                        if (findWord.equalsIgnoreCase("exit")) {
                            break;
                        }

                        // Update the word frequency
                        int currentFreq = wordFreq.getOrDefault(findWord, 0);
                        wordFreq.put(findWord, currentFreq + 1);

                        // Display the current word frequency
                        System.out.println("Word: " + findWord + ", Frequency: " + wordFreq.get(findWord));
                    }

                    // Display the final word frequencies
                    System.out.println("The searched word and its Frequency:");
                    for (Map.Entry<String, Integer> occur : wordFreq.entrySet()) {
                        System.out.println(occur.getKey() + ": " + occur.getValue() + " times");
                    }

                    break;

                }

                case 7: {
                    // Directory containing files
                    String pathOfDir = "src/main/resources";

                    // Define regex patterns for phone numbers and email addresses
                    String regexOfPhone = "\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b";
                    String regexOfEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
                    String regexOfPrice = "\\$\\d+,\\d+";

                    // Creating Pattern objects
                    Pattern patternOfPhone = Pattern.compile(regexOfPhone);
                    Pattern patternOfEmail = Pattern.compile(regexOfEmail);
                    Pattern patternOfPrice = Pattern.compile(regexOfPrice);

                    // Process each file in the directory
        File directory = new File(pathOfDir);
        File[] files = pathOfDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("Searching in file: " + file.getName());
                    searchInFile(file, phonePattern, emailPattern, pricepattern);
                }
            }
        } else {
                        System.out.println("Invalid directory path.");
                    }
                }
                default:
                    System.out.println("Invalid day of the week");
            }

        }


    }


}
