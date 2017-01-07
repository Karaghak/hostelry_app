package com.example.erik.erp_hotel_industry.product_classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Erik on 02/07/2016.
 */
public class Product implements Parcelable {

    int id;
    String name;
    String category;
    double price;
    String supplierName;
    private ArrayList<String> types;
    private int units;
    private String type;

    public Product(){
        types = new ArrayList<>();
        addTypes(types);
    }

    public Product(int id, String name, String category, String type, int units, double price, String supplierName) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.units = units;
        this.price = price;
        this.supplierName = supplierName;
        types = new ArrayList<>();
        addTypes(types);
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public ArrayList<String> getTypes(){
        return types;
    }


    private void addTypes(ArrayList<String> list){
        list.add("unit");
        list.add("kilo");
        list.add("package");
    }

    @Override
    public String toString() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeDouble(price);
        dest.writeString(supplierName);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        category = in.readString();
        price = in.readDouble();
        supplierName = in.readString();
    }
}
