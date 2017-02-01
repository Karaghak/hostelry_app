package com.example.erik.erp_hotel_industry;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.erik.erp_hotel_industry.category_classes.CategoryPage;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;
import com.example.erik.erp_hotel_industry.orders_classes.OrdersPage;
import com.example.erik.erp_hotel_industry.product_classes.ProductPage;
import com.example.erik.erp_hotel_industry.supplier_classes.SupplierPage;

public class MainPage extends MenuSimple {

    private final static String DATABASE_NAME = "ERP_DB.db";
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        db = new Database(this);



        /**
        erp_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SUPPLIER);
        erp_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCT_SUPPLIER);
        erp_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCT);
        erp_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
        erp_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDER);
        erp_db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDER_PRODUCT);
         */






    }


    public void categoryPage(View view){
        Intent i = new Intent(getApplicationContext(), CategoryPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    public void productsPage(View view){
        Intent i = new Intent(getApplicationContext(), ProductPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    public void supplierPage(View view){
        Intent i = new Intent(getApplicationContext(), SupplierPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    public void ordersPage(View view){
        Intent i = new Intent(getApplicationContext(), OrdersPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    public void openGoogleDrive(View view){
        Intent i = new Intent(getApplicationContext(), DriveConnection.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }







}
