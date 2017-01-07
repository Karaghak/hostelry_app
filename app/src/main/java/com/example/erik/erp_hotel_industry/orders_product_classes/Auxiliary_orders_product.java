package com.example.erik.erp_hotel_industry.orders_product_classes;

/**
 * Created by Erik on 29/10/2016.
 */

public class Auxiliary_orders_product {
    String name;
    int quantity;
    private double price;

    public Auxiliary_orders_product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public double getPrice(){
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
