package com.example.erik.erp_hotel_industry.orders_product_classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erik on 27/10/2016.
 */
public class Orders_product implements Parcelable {

    private int id;
    private int ID_Order;
    private int ID_Product;
    private int quantity;

    public Orders_product(int id, int ID_Order, int ID_Product, int quantity) {
        this.id = id;
        this.ID_Order = ID_Order;
        this.ID_Product = ID_Product;
        this.quantity = quantity;
    }




    protected Orders_product(Parcel in) {
        id = in.readInt();
        ID_Order = in.readInt();
        ID_Product = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Orders_product> CREATOR = new Creator<Orders_product>() {
        @Override
        public Orders_product createFromParcel(Parcel in) {
            return new Orders_product(in);
        }

        @Override
        public Orders_product[] newArray(int size) {
            return new Orders_product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ID_Order);
        dest.writeInt(ID_Product);
        dest.writeInt(quantity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getID_Order() {
        return ID_Order;
    }

    public void setID_Order(int ID_Order) {
        this.ID_Order = ID_Order;
    }

    public int getID_Product() {
        return ID_Product;
    }

    public void setID_Product(int ID_Product) {
        this.ID_Product = ID_Product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
