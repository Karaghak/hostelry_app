package com.example.erik.erp_hotel_industry.orders_classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Erik on 06/10/2016.
 */
public class Order implements Parcelable {
    int id;
    int id_supplier;
    String supplier_name;
    Date order_date;
    double final_price;

    public Order(int id, int id_supplier, String supplier_name, Date order_date, double final_price) {
        this.id = id;
        this.id_supplier = id_supplier;
        this.supplier_name = supplier_name;
        this.order_date = order_date;
        this.final_price = final_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_supplier);
        dest.writeString(supplier_name);
        //dest.writeString(order_date.toString());
        dest.writeDouble(final_price);
    }

    private Order(Parcel in) {
        id = in.readInt();
        id_supplier = in.readInt();
        supplier_name = in.readString();
        //order_date = in.readString();
        final_price = in.readDouble();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(int id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public double getFinal_price() {
        return final_price;
    }

    public void setFinal_price(double final_price) {
        this.final_price = final_price;
    }


}
