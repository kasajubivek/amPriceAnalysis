package features;

public class Car {
    private String name;
    private double price;
    private double mileage;

    public Car(String name, double price, double mileage) {
        this.name = name;
        this.price = price;
        this.mileage = mileage;
    }

    public String getName() { return name; }

    public double getPrice() {
        return price;
    }

    public double getMileage() {
        return mileage;
    }


    @Override
    public String toString() {
        return name + " | $" + price + " | " + mileage + " km";
    }
}