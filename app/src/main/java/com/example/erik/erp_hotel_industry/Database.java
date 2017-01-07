package com.example.erik.erp_hotel_industry;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.erik.erp_hotel_industry.category_classes.CategoryPage;
import com.example.erik.erp_hotel_industry.orders_classes.OrdersPage;
import com.example.erik.erp_hotel_industry.product_classes.ProductPage;
import com.example.erik.erp_hotel_industry.supplier_classes.SupplierPage;

/**
 * Created by Erik on 02/07/2016.
 */
public class Database extends SQLiteOpenHelper {

    private SQLiteDatabase erp_db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ERP_DB.db";
    private static final String TABLE_NAME_SUPPLIER = "Supplier";
    private static final String TABLE_NAME_PRODUCT = "Product";
    private static final String TABLE_NAME_CATEGORY = "Category";
    private static final String TABLE_NAME_PRODUCT_SUPPLIER = "Product_Supplier";
    private static final String TABLE_NAME_ORDER = "Orders";
    private static final String TABLE_NAME_ORDER_PRODUCT = "Order_Product";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for suppliers
        createTableSupplier();

        // Create all tables for products
        // Table PRODUCT
        createTableProduct();
        createTableOrder();
        createTableCategory();
        erp_db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_PRODUCT_SUPPLIER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Product_ID INTEGER NOT NULL, Supplier_ID INTEGER NOT NULL);");



        erp_db.beginTransaction();
        erp_db.execSQL("INSERT INTO Category (Name) VALUES ('Bebida')");
        erp_db.execSQL("INSERT INTO Category (Name) VALUES ('Café')");
        erp_db.execSQL("INSERT INTO Category (Name) VALUES ('Carne')");
        erp_db.setTransactionSuccessful();
        erp_db.endTransaction();

        erp_db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertProduct(SQLiteDatabase db, String name, String category,
                               String type, double units, double price, String supplierName)
    {
        String query = "INSERT INTO Product (Name, Category, Type, Units, Price, SupplierName) VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.bindString(1, name);
        stmt.bindString(2, category);
        stmt.bindString(3, type);
        stmt.bindDouble(4, units);
        stmt.bindDouble(5, price);
        stmt.bindString(6, supplierName);

        db.beginTransaction();
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    private void createTableCategory(){
        erp_db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_CATEGORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Name VARCHAR(12) NOT NULL);");
    }
    private void createTableProduct(){
        erp_db.execSQL("CREATE TABLE  " +
                TABLE_NAME_PRODUCT + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Name VARCHAR(12) NOT NULL, Category VARCHAR(12) NOT NULL," +
                " Type VARCHAR(12) NOT NULL, Units REAL NOT NULL,  Price REAL NOT NULL, SupplierName VARCHAR NOT NULL);");

        erp_db.beginTransaction();
        erp_db.execSQL("INSERT INTO Product (Name, Category, Type, Units, Price, SupplierName)" +
                " VALUES ('Mahou', 'Bebida', 'package', '24', '0.6', 'Mahou')");
        erp_db.execSQL("INSERT INTO Product (Name, Category, Type, Units, Price, SupplierName)" +
                " VALUES ('Sin alcohol', 'Bebida', 'package', '24', '0.7', 'Mahou')");
        erp_db.setTransactionSuccessful();
        erp_db.endTransaction();
    }
    private void createTableSupplier(){
        erp_db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_SUPPLIER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", Name VARCHAR(12) NOT NULL, PhoneNumber INTEGER);");

        erp_db.beginTransaction();
        erp_db.execSQL("INSERT INTO Supplier (Name) VALUES ('Mahou')");
        erp_db.execSQL("INSERT INTO Supplier (Name) VALUES ('Friocarne')");
        erp_db.execSQL("INSERT INTO Supplier (Name) VALUES ('Templo')");
        erp_db.setTransactionSuccessful();
        erp_db.endTransaction();
    }
    private void createTableOrder(){
        erp_db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_ORDER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", ID_Supplier INTEGER, Supplier_Name VARCHAR(20), Order_Date DATE" +
                ", Final_Price DOUBLE);");

        erp_db.beginTransaction();
        erp_db.execSQL("INSERT INTO Orders (Supplier_Name, Order_Date) VALUES ('Prueba', '2016-Oct-05')");
        //erp_db.execSQL("INSERT INTO Order (Name) VALUES ('Cafeé')");
        //erp_db.execSQL("INSERT INTO Order (Name) VALUES ('Carne')");
        erp_db.setTransactionSuccessful();
        erp_db.endTransaction();

        erp_db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_ORDER_PRODUCT + " (ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", ID_Order INTEGER, ID_Product INTEGER, Quantity INTEGER);");
    }


}
