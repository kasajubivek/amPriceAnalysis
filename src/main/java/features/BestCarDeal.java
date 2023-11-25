package features;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class BestCarDeal {

    static Set<Double> uniquePrices = new HashSet<>();
    static List<Car> bestDeals = new ArrayList<>();

    public static void main(String[] args) {
//        Car[] cars = {
//                new Car("Car E", 30000, 32),
//                new Car("Car G", 30000, 19),
//                new Car("Car O", 29000, 19),
//                new Car("Car F", 30000, 21),
//                new Car("Car B", 18000, 25),
//                new Car("Car B", 18000, 26),
//                new Car("Car C", 30000, 28),
//                new Car("Car D", 30000, 28),
//                new Car("Car A", 25000, 30),
//        };


        List<Car> _all_cars_data_ = new ArrayList<>();

        String[] _cars_data_ = {"src/main/resources/cargurus.ca.csv"};

        for(String _car_data_: _cars_data_){
            List _data_ = readCarsFromCSV(_car_data_);
            _all_cars_data_.addAll(_data_);
        }


        Car[] cars = _all_cars_data_.toArray(new Car[0]);


        mergeSort(cars, 0, cars.length - 1, "mileage");
        mergeSort(cars, 0, cars.length - 1, "price");

        System.out.println("Cars sorted by mileage and then by price:\n");

        for (Car car : cars) {
            System.out.println(car);

            if (!uniquePrices.contains(car.getPrice())) {
                uniquePrices.add(car.getPrice());
                bestDeals.add(car);
            }
        }

        System.out.println("\nBest deals for cars according to the price range and mileage:\n");
        for (Car car : bestDeals) {
            System.out.println(car);
        }

    }


    private static void mergeSort(Car[] _cars_arr_, int low, int high, String compareBy) {
        if (low < high) {
            int mid = (low + high) / 2;

            mergeSort(_cars_arr_, low, mid, compareBy);
            mergeSort(_cars_arr_, mid + 1, high, compareBy);

            merge(_cars_arr_, low, mid, high, compareBy);
        }
    }

    private static void merge(Car[] _cars_arr_, int low, int mid, int high, String compareBy) {

        int _i_ = low;
        int _j_ = mid + 1;
        int _k_ = low;

        Car[] _sorted_cars_ =  new Car[_cars_arr_.length];


        while(_i_ <= mid && _j_ <= high){

            if(compareBy.equals("mileage")?(_cars_arr_[_i_].getMileage() >= _cars_arr_[_j_].getMileage()):(_cars_arr_[_i_].getPrice() <= _cars_arr_[_j_].getPrice())){
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


    private static List readCarsFromCSV(String filePath) {
        List<Car> cars = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = csvReader.readAll();

            // Assuming the CSV structure is: Name, Price, Mileage
            for (int i = 1; i <= 15 && i < records.size(); i++) {
                String[] record = records.get(i);

                String name = record[0].trim();
                String priceString = record[1].replaceAll("[^\\d.]+", "");
                double price = Double.parseDouble(priceString);
                double mileage = Double.parseDouble(record[2].replaceAll("[^\\d.]+", "").trim());

                Car car = new Car(name, price, mileage);
                cars.add(car);
            }
        }catch (IOException | CsvException e) {
            System.out.println("Error occurred while reading the files!");
            e.printStackTrace();
        }

        return cars;
    }

    private static void printCars(List<Car> cars) {
        for (Car car : cars) {
            System.out.println(car);
        }
    }
}






