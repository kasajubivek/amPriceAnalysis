package features;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVWriter;

public class WebCrawler {
    private static final WebDriver driver=new FirefoxDriver();
    //	private static final WebDriver driver=new ChromeDriver();

    private static final List<String[]> carDataRows = new ArrayList<>();
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    public static void websitesToCrawl() throws InterruptedException {
        Scanner userInput = new Scanner(System.in);

        while (true) {
            System.out.println("Do you want to crawl any specific website?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter your choice: ");

            int userChoice = userInput.nextInt();
            userInput.nextLine();

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            switch (userChoice) {
                case 1:
                    System.out.println("Enter the website URL:");
                    System.out.println("The correct format for website is: https://www.abc.xyz");
                    String websiteUrl = userInput.nextLine();
                    userInput.nextLine();

                    if (ValidateUserInput.isValidWebsite(websiteUrl)) {
                        crawlWebsite(websiteUrl);
                        userInput.close();
                        return;  // Exit the method if valid input is provided
                    } else {
                        System.out.println("Invalid website URL. Please enter a valid URL.");
                    }
                    break;

                case 2:
                    System.out.println("Please Enter Your Postal Code!");
                    System.out.println("Accepted format is: A1B 2C3");
                    System.out.print("Enter your postal code: ");
                    String postalCodeChoice = userInput.nextLine();

                    if (ValidateUserInput.isValidPostalCode(postalCodeChoice)) {
                        System.out.println("how many pages you want to crawl?");
                        int pageLimit=userInput.nextInt();
                        userInput.nextLine();
//                        System.out.println(pageLimit);
                        crawlDefaultWebsite(postalCodeChoice,pageLimit);
                        userInput.close();
                        return;  // Exit the method if valid input is provided
                    } else {
                        System.out.println("Invalid Postal Code! Please try again.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please choose 1 or 2.");
            }
        }
    }

    private static void crawlWebsite(String websiteUrl) throws InterruptedException {
        System.out.println("Crawling website: " + websiteUrl);
        driver.get(websiteUrl);
        Thread.sleep(5000);
        driver.quit();
    }

    private static void crawlDefaultWebsite(String postalCode,int pagesToVisit) throws InterruptedException {
        System.out.println("Crawling " +pagesToVisit+ " pages of the selected websites.");

        crawlDriveAxis(postalCode,pagesToVisit);
        Thread.sleep(3000);
        crawlGoAuto(postalCode,pagesToVisit);
        Thread.sleep(3000);
        crawlCarGurus(postalCode,pagesToVisit);
        Thread.sleep(2000);

        driver.quit();
    }

    public static void crawlCarGurus(String postalCode,int pagesToVisit) throws InterruptedException {
        int currentPage=0;
        int totalPages=0;
        String phoneNumber;
        String fuelConsumption;
        try{
            driver.get("https://www.cargurus.ca/");
            String domain=extractDomain(driver.getCurrentUrl());
            driver.findElement(By.xpath("//span[normalize-space()='Buy']")).click();
            try {
                WebElement closeButtonForSurvey = driver.findElement(By.xpath("//button[@class='qr_GLx cg__survey__close']"));
                if (closeButtonForSurvey.isDisplayed()) {
                    closeButtonForSurvey.click();
                }
            }catch(Exception e){
                System.out.println("Survey not found! Proceeding with the rest of the code.");
            }
            driver.findElement(By.xpath("//a[@alt='Sedan Body Style']//div[@class='card noBorder center bodyoption clickable']")).click();
            String pages=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='tdyLOj']//span[@class='_A3m0_']"))).getText();
            int indexOfPage = pages.indexOf("Page ");
            int indexOfOf = pages.indexOf(" of ");
            if (indexOfPage != -1 && indexOfOf != -1) {
                currentPage = Integer.parseInt(pages.substring(indexOfPage + 5, indexOfOf).trim());
                totalPages = Integer.parseInt(pages.substring(indexOfOf + 4).trim());
            }
            for(int i=currentPage;i<=pagesToVisit;i++) {
                List<WebElement> carsInTheSite=driver.findElements(By.xpath("//div[@class='k4FSCT']"));
                int pgSize=carsInTheSite.size();
                for(int j=1;j<=pgSize;j++) {
                    try {
                        try {
                            WebElement closeButtonForDialog = driver.findElement(By.xpath("//button[@aria-label='Close']"));
                            if (closeButtonForDialog.isDisplayed()) {
                                closeButtonForDialog.click();
                            }
                        } catch (Exception e) {
                            System.out.println("Dialog not found! Proceeding with the rest of the code.");
                        }
                        WebElement pageI = driver.findElement(By.xpath("(//div[@class='k4FSCT'])[" + j + "]"));
                        String carName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='k4FSCT']//div//h4[@class='gN7yGT'])[" + j + "]"))).getText();
                        String carPrice = driver.findElement(By.xpath("(//div[@class='k4FSCT']//div[@class='YlkCzK']//div[@class='Lxkk9T']//h4[@class='ulx4Y8'])[" + j + "]")).getText();
                        String carMileage = driver.findElement(By.xpath("(//div[@class='k4FSCT']//div[@class='YlkCzK']//div[@class='EeLi0s']//p[@class='HczmlC'])[" + j + "]")).getText();

                        String monthlyPayment = driver.findElement(By.xpath("(//div[@class='k4FSCT']//div[@class='YlkCzK']//div[@class='Lxkk9T']//button)[" + j + "]")).getText();
                        String carLocation = driver.findElement(By.xpath("(//div[@class='k4FSCT']//div[@class='MOU7hw']//div[@class='caX8XQ']//div//p[@class='HLgC_C'])[" + j + "]")).getText();
                        String phoneXPath = "(//div[@class='k4FSCT']//div[@class='q7Zi1K Qc4mOg']//button[@class='HaLmAx _9laKps NfgtRG SUJbPV'])[" + j + "]";
                        List<WebElement> phoneElements = driver.findElements(By.xpath(phoneXPath));
                        phoneNumber = !phoneElements.isEmpty() ? phoneElements.get(0).getText() : "No Information Found";
                        pageI.click();
                        try {
                            WebElement closeButtonForDialog = driver.findElement(By.xpath("//button[@aria-label='Close']"));
                            if (closeButtonForDialog.isDisplayed()) {
                                closeButtonForDialog.click();
                            }
                        } catch (Exception e) {
                            System.out.println("Dialog not found! Proceeding with the rest of the code.");
                        }
                        List<WebElement> carFuelConsumption = driver.findElements(By.xpath("//div[@class='mhYSgs']//section//div//ul[@class='dTIusl']//li[@class='K74no_']//div[h4='Fuel Consumption']/p[@class='qj8lUO']"));
                        fuelConsumption = !carFuelConsumption.isEmpty() ? carFuelConsumption.get(0).getText() : "No Information Found";
                        System.out.println(fuelConsumption);
                        driver.findElement(By.xpath("//div[@class='ZBloTD RZq0ac']//span[@class='xF9OZq'][normalize-space()='All results']")).click();
                        String[] carDataRow = {carName, carPrice, carMileage, fuelConsumption, phoneNumber, monthlyPayment, carLocation};
                        carDataRows.add(carDataRow);

                    } catch(NoSuchElementException | StaleElementReferenceException e){
                        j--;
                    }
                }
                driver.findElement(By.xpath("//button[@aria-labelledby='bottomPaginationNext']//*[name()='svg']")).click();
            }
            String[] header = {"Car Name", "Car Price", "Car Mileage","Fuel Consumption","Phone Number","Payment","Address"};
            storeCarDataToCSV(domain,header);
        }
        catch (Exception e){
            System.out.println("Error! Retrying......");
            e.printStackTrace();
        }
    }

    public static void crawlGoAuto(String postalCode,int pagesToVisit) throws InterruptedException {
        try{
            driver.get("https://www.goauto.ca/");
            String domain=extractDomain(driver.getCurrentUrl());
            String fuelConsumption;String address;String phoneNumber;
            driver.findElement(By.xpath("//nav[@class='header_mainNav__GaPyH hidden md:flex gap-24 items-center']//a[@data-nav='headerMainNavigation'][normalize-space()='Shop']")).click();
            driver.findElement(By.xpath("//span[@class='flex items-center gap-8']")).click();
            Thread.sleep(1000);
            WebElement inputElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Search…']")));
            inputElement.sendKeys(postalCode);
            driver.findElement(By.xpath("//button[@class='w-full button_root__ebVgz button_contextLight__2lZAC button_fill__vifh5 button_large__mIRIA typ-button-large button_widthAuto__PPtZs button_primary__JpcLt']//span[@class='relative']")).click();
            Thread.sleep(1000);
            int currentPage=Integer.parseInt(driver.findElement(By.xpath("//nav[@class='pagination_pagination__2yOxT']//ul//li[@class='pagination_current__RQBwN']")).getText());
            List<WebElement> totalPage=driver.findElements(By.xpath("//nav[@class='pagination_pagination__2yOxT']//ul//li[@class='pagination_pageNum__4PcSb']"));
            int lastElement = Integer.parseInt(totalPage.get(totalPage.size() - 1).getText());
            for (int i = currentPage; i <= pagesToVisit; i++) {
                Thread.sleep(2000);
                List<WebElement> numberOfCars=driver.findElements(By.xpath("//div[@class='grid gap-24']//div[@class='grid-cols-4 mb-64']//div[@class='inventory_inventoryListing__vHmrR']//div[@class='background-hint_light__EI87j bg-white text-gray-700 inventory_inventoryCard__XCsAr typ-body-3 undefined inventory_isLinked__frz0l']"));
                int carsInThePage=numberOfCars.size();
                for(int j=1;j<=carsInThePage;j++) {
                    try {
                        WebElement pagei=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='grid gap-24']//div[@class='grid-cols-4 mb-64']//div[@class='inventory_inventoryListing__vHmrR']//div[@class='background-hint_light__EI87j bg-white text-gray-700 inventory_inventoryCard__XCsAr typ-body-3 undefined inventory_isLinked__frz0l'])["+j+"]")));
                        String mileage;
                        List<WebElement> mileageElement = driver.findElements(By.xpath("(//div[@class='inventory_content__DIqP5']//p[@class='inventory_trimMileage__CC0Yp']//span[@class='inventory_mileage__M6cGj'])["+j+"]"));
                        mileage = !mileageElement.isEmpty() ? mileageElement.get(0).getText() : "No Information Found";
                        String carName=driver.findElement(By.xpath("(//div[@class='inventory_content__DIqP5']//h4[@class='inventory_makeAndModel__jLJyd typ-body-2 !font-bold'])["+j+"]")).getText();
                        String price=driver.findElement(By.xpath("(//div[@class='inventory_content__DIqP5']//p[@class='inventory_pricing__GwjgT typ-body-1 !font-bold'])["+j+"]")).getText();
                        String payment = driver.findElements(By.xpath("(//div[@class='inventory_pricingPayment__8chJf']//span[@class='underline'])["+j+"]")).isEmpty() ? "No Information Found" : driver.findElement(By.xpath("(//div[@class='inventory_pricingPayment__8chJf']//span[@class='underline'])["+j+"]")).getText();
                        pagei.click();
                        List<WebElement> addressElement=driver.findElements(By.xpath("//p[@class='vdp-header_dealership__n6DWR typ-body-2']"));
                        List<WebElement> phoneNumberElement=driver.findElements(By.xpath("//a[@class='styled-link text-gray-700']"));
                        List<WebElement> fuelConsumptionInfo=driver.findElements(By.xpath("//div[@class='overview_overviewItem__55_CV typ-body-3']/div[p='Fuel Economy']/following-sibling::div/p"));
                        fuelConsumption = !fuelConsumptionInfo.isEmpty() ? fuelConsumptionInfo.get(0).getText() : "No Information Found";
                        address = !addressElement.isEmpty() ? addressElement.get(0).getText() : "No Information Found";
                        phoneNumber = !phoneNumberElement.isEmpty() ? phoneNumberElement.get(0).getText() : "No Information Found";
                        String[] carDataRow = {carName, price, mileage,fuelConsumption, phoneNumber,payment,address};
                        carDataRows.add(carDataRow);
                        Thread.sleep(500);
                        driver.navigate().back();
                    }
                    catch(StaleElementReferenceException | NoSuchElementException e) {
                        j--;
                    }
                }
                WebElement nextButton = null;
                try {
                    nextButton = driver.findElement(By.xpath("//nav[@class='pagination_pagination__2yOxT']//div[@class='pagination_next__UQxA3']//button[@class='button_root__ebVgz button_contextLight__2lZAC button_outline__aOaXB button_small__KgoXT typ-button-small button_widthAuto__PPtZs button_secondary__CsBrW']"));
                } catch (NoSuchElementException e) {
                    System.out.println("Next button not found. Exiting loop.");
                    break;
                }

                if (nextButton != null) {
                    nextButton.click();
                } else {
                    System.out.println("Next button is null. Exiting loop.");
                    break;
                }
            }
            String[] carHeaders = {"Car Name", "Car Price", "Car Mileage","Fuel Consumption","Phone Number","Payment","Address"};
            storeCarDataToCSV(domain,carHeaders);
        }
        catch(Exception e){
            System.out.println("Error! Retrying......");
            e.printStackTrace();
        }
    }

    public static void crawlDriveAxis(String postalCode,int pagesToVisit) throws InterruptedException {
        String carName;String mileage;String carPrice;
        try {
            driver.get("https://www.driveaxis.ca/");
            String domain = extractDomain(driver.getCurrentUrl());
            Thread.sleep(1000);
            driver.findElement(By.xpath("//ul[@id='navbar-nav']//li[@class='nav-item']//a[@href='/vehicle-listing']")).click();
            driver.findElement(By.xpath("//div[@class='tooltip']//input[@id='desktop-ax-filter-zip']")).sendKeys(postalCode);
            Thread.sleep(2000);
            driver.findElement(By.xpath("//div[@class='bs-icon']//*[name()='svg']")).click();
//            int maxPagesToVisit =30;
            int visitedPages = 1;
            while (visitedPages <= pagesToVisit) {
                WebElement nextButton = driver.findElement(By.xpath("//a[@class='arrow']//*[local-name()='svg' and @data-icon='chevron-circle-right']"));
                if (nextButton != null) {
                    List<WebElement> cars = driver.findElements(By.xpath("//div[@id='vlp-inventory-wrapper']//div[@class='vehicle-tile']"));
                    int carsInThePage = cars.size();
                    for (int j = 1; j <= carsInThePage; j++) {
                        try {
                            WebElement singleCar = driver.findElement(By.xpath("(//div[@id='vlp-inventory-wrapper']//div[@class='vehicle-tile'])[" + j + "]"));
                            String paymentType = driver.findElement(By.xpath("(//div[@class='vehicle-tile-right']//div[@class='vehicle-biweekly'])[" + j + "]")).getText();
                            String[] lines = paymentType.split("\n");
                            String amount = lines.length > 1 ? lines[1].trim() : "";
                            String frequency = lines.length > 2 ? lines[2].trim() : "";
                            String paymentMethod = amount + " " + frequency;
                            singleCar.click();
                            List<WebElement> carNameElement=wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//h1[@id='vdp-info-vehicle-name']")));
                            List<WebElement> mileageElement = driver.findElements(By.xpath("//div[@class='vdp-basic-info']//span[@class='info-km']"));
                            List<WebElement> carPriceElement = driver.findElements(By.xpath("//div[@class='vdp-price-info']//div//span[@class='info-price desktop-view vdp-payment-header']"));
                            carName = carNameElement.isEmpty() ? "No Information Found" : carNameElement.get(0).getText();
                            mileage = mileageElement.isEmpty() ? "No Information Found" : mileageElement.get(0).getText();
                            carPrice = carPriceElement.isEmpty() ? "No Information Found" : carPriceElement.get(0).getText();
                            String phoneNumber = "1-888-802-3504";
                            String fuelConsumption = " ";
                            String address = "No Information Found!";
                            String[] carDataRow = {carName, carPrice, mileage, fuelConsumption, phoneNumber, paymentMethod, address};
                            carDataRows.add(carDataRow);
                            driver.navigate().back();
                        } catch (StaleElementReferenceException e) {
                            j--;
                        }
                    }
                    nextButton = driver.findElement(By.xpath("//a[@class='arrow']//*[local-name()='svg' and @data-icon='chevron-circle-right']"));
                    nextButton.click();
                    visitedPages++;
                } else {
                    break;
                }
            }
            String[] carHeaders = {"Car Name", "Car Price", "Car Mileage", "Fuel Consumption", "Phone Number", "Payment", "Address"};
            storeCarDataToCSV(domain, carHeaders);
        }
        catch(Exception e){
            System.out.println("Error! Retrying......");
            e.printStackTrace();
        }
    }

    public static String extractDomain(String url) {
        String domain = "";
        int wwwIndex = url.indexOf("www.");
        if (wwwIndex != -1) {
            domain = url.substring(wwwIndex + 4);
            int slashIndex = domain.indexOf("/");
            if (slashIndex != -1) {
                domain = domain.substring(0, slashIndex);
            }
        }
        return domain;
    }

    public static void storeCarDataToCSV(String fileName,String[] headers) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName + "[1].csv", true))) {
            writer.writeNext(headers);

            for (String[] carDataRow : carDataRows) {
                writer.writeNext(carDataRow);
            }

            System.out.println("Car data has been added to the CSV file successfully!");
            carDataRows.clear();
        } catch (IOException e) {
            System.out.println("Please check the log for error"+e.getMessage());
        }
    }

//    @SuppressWarnings("deprecation")
//    public static void main(String[] args) throws InterruptedException {
//
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//        websitesToCrawl();
//
//        Thread.sleep(1000);
//        driver.quit();
//    }

}
