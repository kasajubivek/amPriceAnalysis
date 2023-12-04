package features;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class BestCarDeal {

    static Set<Double> uniquePricesNewCars = new HashSet<>();
    static Set<Double> uniquePricesUsedCars = new HashSet<>();
    static List<Car> bestDealsNewCars = new ArrayList<>();
    static List<Car> bestDealsUsedCars = new ArrayList<>();
    static List<Car> newCars = new ArrayList<>();
    static List<Car> usedCars = new ArrayList<>();

    public static void main(String[] args) {

        List<Car> _all_cars_data_ = new ArrayList<>();

        String[] _cars_data_ = {"src/main/resources/driveaxis.ca.csv","src/main/resources/goauto.ca.csv","src/main/resources/cargurus.ca.csv"};

        for(String _car_data_: _cars_data_){
            List _data_ = readCarsFromCSV(_car_data_);
            _all_cars_data_.addAll(_data_);
        }


        Car[] cars = _all_cars_data_.toArray(new Car[0]);


        mergeSort(cars, 0, cars.length - 1, "fuelConsumption");
        mergeSort(cars, 0, cars.length - 1, "price");


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

    }


    public static void mergeSort(Car[] _cars_arr_, int low, int high, String compareBy) {
        if (low < high) {
            int mid = (low + high) / 2;

            mergeSort(_cars_arr_, low, mid, compareBy);
            mergeSort(_cars_arr_, mid + 1, high, compareBy);

            merge(_cars_arr_, low, mid, high, compareBy);
        }
    }

    public static void merge(Car[] _cars_arr_, int low, int mid, int high, String compareBy) {

        int _i_ = low;
        int _j_ = mid + 1;
        int _k_ = low;

        Car[] _sorted_cars_ =  new Car[_cars_arr_.length];


        while(_i_ <= mid && _j_ <= high){

            if(compareBy.equals("fuelConsumption")?(_cars_arr_[_i_].getFuelConsumption() <= _cars_arr_[_j_].getFuelConsumption()):(_cars_arr_[_i_].getPrice() <= _cars_arr_[_j_].getPrice())){
                _sorted_cars_[_k_] = _cars_arr_[_i_];
                _i_++;
            }else {
                _sorted_cars_[_k_] = _cars_arr_[_j_];
                _j_++;
            }

            _k_++;

        }


        if(_i_ > mid){
            while (_j_ <= high){
                _sorted_cars_[_k_] = _cars_arr_[_j_];
                _j_++;
                _k_++;
            }
        }else {
            while (_i_ <= mid){
                _sorted_cars_[_k_] = _cars_arr_[_i_];
                _i_++;
                _k_++;
            }
        }


        for(int _it_ = low; _it_ <= high; _it_++){
            _cars_arr_[_it_] = _sorted_cars_[_it_];
        }

    }


    public static List readCarsFromCSV(String filePath) {
        double price = 0, fuelConsumption = 0;
        String name = "", priceString, mileage = "", fuelConsumptionString;
        List<Car> cars = new ArrayList<>();
        Matcher matcher_1, matcher_2;
        String regex_1 = "([0-9.]+) L/100km", regex_2 = "([0-9.]+) City / ([0-9.]+) Highway";
        Pattern pattern_1 = Pattern.compile(regex_1);
        Pattern pattern_2 = Pattern.compile(regex_2);

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = csvReader.readAll();

            // Assuming the CSV structure is: Name, Price, Mileage
            for (int i = 1; i <= 10 && i < records.size(); i++) {
                String[] record = records.get(i);

                if(record[0] != null && !record[0].contains("No Information Found")){
                    name = record[0].trim();
                }

                if(record[1] != null && !record[0].contains("No Information Found")){
                    priceString = record[1].replaceAll("[^\\d.]+", "");

                    price = Double.parseDouble(priceString);
                }

                if(!record[2].contains("New") && !record[2].contains("new") && !record[0].contains("No Information Found") && record[2] != null){
                    mileage = record[2].replaceAll("[^\\d.]+", "").trim();
                }else {
                    mileage = "0";
                }

                fuelConsumptionString = record[3];

                if(fuelConsumptionString.contains("L/100km")){
                    matcher_1 = pattern_1.matcher(fuelConsumptionString);

                    if(matcher_1.find()){
                        fuelConsumption = Double.parseDouble(matcher_1.group(1));
                    }
                }else if(fuelConsumptionString.contains("Highway")){
                    matcher_2 = pattern_2.matcher(fuelConsumptionString);

                    if(matcher_2.find()){
                        fuelConsumption = Math.max(Double.parseDouble(matcher_2.group(1)), Double.parseDouble(matcher_2.group(2)));
                    }
                }


                Car car = new Car(name, price, mileage, fuelConsumption);
                cars.add(car);
            }
        }catch (NullPointerException e) {
            System.out.println("Error! Null Pointer Exception occurred while reading the files!");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error! Number Format Exception occurred while reading the files!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error! IO Exception occurred while reading the files!");
            e.printStackTrace();
        } catch (CsvException e) {
            System.out.println("Error! CSV Exception occurred while reading the files!");
            e.printStackTrace();
        }

        return cars;
    }

    public static void printCars(List<Car> cars) {
        for (Car car : cars) {
            System.out.println(car);
        }
    }
}

class Car {
    private String name;
    private double price;
    private String mileage;
    private double fuelConsumption;

    public Car(String name, double price, String mileage, double fuelConsumption) {
        this.name = name;
        this.price = price;
        this.mileage = mileage;
        this.fuelConsumption = fuelConsumption;
    }

    public String getName() { return name; }

    public double getPrice() {
        return price;
    }

    public String getMileage() {
        return mileage;
    }

    public double getFuelConsumption() { return fuelConsumption; }

    @Override
    public String toString() {
        return name + " | $" + price + " | " + mileage + "km | " + fuelConsumption + " L/100km\n";
    }
}






