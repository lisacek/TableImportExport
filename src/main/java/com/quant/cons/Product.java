package com.quant.cons;

public class Product {

    private final long id;
    private String brand;
    private String name;
    private long amount;
    private double price;

    public Product(long id, String brand, String name, long amount, double price) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return String.format("%.2f Kč", price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
