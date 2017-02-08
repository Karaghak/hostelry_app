package com.example.erik.erp_hotel_industry.product_classes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.erik.erp_hotel_industry.Database;
import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuDelete;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;

import java.util.ArrayList;

public class ProductDetail extends MenuDelete {

    private static String DATABASE_NAME = "";
    private SQLiteDatabase db;
    private EditText editTextName;
    private Spinner spinnerCategory;
    private Spinner spinnerSupplier;
    private EditText editTextPrice;
    private Button buttonModify;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_activity);

        Bundle b = getIntent().getExtras();
        if(b != null){
            DATABASE_NAME = b.getString("db_name");
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            product = getIntent().getParcelableExtra("product");
        }


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        spinnerSupplier = (Spinner) findViewById(R.id.spinnerSupplier);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        buttonModify = (Button) findViewById(R.id.buttonModify);

        loadValuesEditText();

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(v);
            }
        });


    }

    private void loadValuesEditText() {
        Intent i = getIntent();
        if(i != null) {
            Product product = i.getParcelableExtra("product");
            editTextName.setText(product.getName());
            editTextPrice.setText(String.valueOf((product.getPrice())));
            createSupplierAdapter();
            createCategoryAdapter();
        }
    }




    public void updateProduct(View view){
        int id = product.getId();

        String query = "UPDATE Product" +
                " SET Name = ?, Category = ?, Price = ?, SupplierName = ?" +
                " WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        // Start transaction
        db.beginTransaction();
        stmt.bindString(1, getName());
        stmt.bindString(2, getCategory());
        stmt.bindDouble(3, getPrice());
        stmt.bindString(4, getSupplier());
        stmt.bindDouble(5, id);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        // End transaction
        db.close();
        // Open product page after delete
        returnToPage(view);
    }

    @Override
    public void deleteItem(View view){
        Product product = getIntent().getParcelableExtra("product");
        int id = product.getId();
        String query = "DELETE FROM Product WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        // Start transaction
        db.beginTransaction();
        stmt.bindDouble(1, id);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        // End transaction
        db.close();
        // Open product page after delete
        returnToPage(view);
    }

    private void returnToPage(View view) {
        Intent i = new Intent(getApplicationContext(), ProductPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
        finish();
    }



    private String getSupplier(){
        return spinnerSupplier.getSelectedItem().toString();
    }

    private String getCategory(){
        return spinnerCategory.getSelectedItem().toString();
    }

    private String getName(){
        return editTextName.getText().toString();
    }

    private Double getPrice(){
        return Double.valueOf(editTextPrice.getText().toString());
    }

    private void createSupplierAdapter() {
        ArrayList<String> listSupplier = new ArrayList<>();
        ArrayAdapter<String> adapterSupplier = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSupplier);
        adapterSupplier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadSpinnerSupplier(listSupplier);
        spinnerSupplier.setAdapter(adapterSupplier);
    }

    private void createCategoryAdapter() {
        ArrayList<String> listCategory = new ArrayList<>();

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadSpinnerCategory(listCategory);
        spinnerCategory.setAdapter(adapterCategory);
    }

    private void loadSpinnerSupplier(ArrayList<String> list){
        String query = "SELECT Name from Supplier";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
    }

    private void loadSpinnerCategory(ArrayList<String> list){
        String query = "SELECT Name from Category ORDER BY Name ASC";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
    }
}
