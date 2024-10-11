package com.quant.cons;

import com.quant.annotations.ColumnName;

public class Product {

    @ColumnName("Product ID")
    private long id;
    private String brand;
    private String name;
    private long amount;
    private double price;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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
