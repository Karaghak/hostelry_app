package com.example.erik.erp_hotel_industry.supplier_classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erik on 27/09/2016.
 */
public class Supplier implements Parcelable {
    private int id;
    private String name;
    private int phoneNumber;

    public Supplier(int id, String name, int phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name;  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(phoneNumber);
    }

    public static final Parcelable.Creator<Supplier> CREATOR = new Parcelable.Creator<Supplier>() {
        public Supplier createFromParcel(Parcel in) {
            return new Supplier(in);
        }

        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Supplier(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readInt();
    }
}
