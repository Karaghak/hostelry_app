package com.example.erik.erp_hotel_industry.product_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.erik.erp_hotel_industry.Database;
import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;

import java.util.ArrayList;

public class Add_product extends MenuSimple {

    private SQLiteDatabase db;
    private static String DATABASE_NAME = "";
    private EditText editTextName;
    private Spinner spinnerCategory;
    private Spinner spinnerSupplier;
    private Spinner spinnerTypes;
    private EditText editTextTypeQuantity;
    private EditText editTextPrice;
    private Button buttonAccept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add_activity);

        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        // Open database
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // Initialize elements
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerSupplier = (Spinner) findViewById(R.id.spinnerSupplier);
        spinnerTypes = (Spinner) findViewById(R.id.spinnerTypes);
        editTextTypeQuantity = (EditText) findViewById(R.id.editTextTypeQuantity);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        buttonAccept = (Button) findViewById(R.id.buttonAccept);

        editTextTypeQuantity.setEnabled(false);



        // Category spinner adapter
        createCategoryAdapter();

        // Supplier spinner adapter
        createSupplierAdapter();

        // Types spinner adapter
        createTypesAdapter();



        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerTypes.getSelectedItem().toString().equals("package")) editTextTypeQuantity.setEnabled(true
                );
                else{
                    editTextTypeQuantity.setText("1");
                    editTextTypeQuantity.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(true){
                    insertProduct();
                    returnToPage(v);
                }
                else{
                    toastError();
                }
            }
        });


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

    private void createTypesAdapter(){
        ArrayList<String> types = new Product().getTypes();

        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypes.setAdapter(adapterTypes);
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

    /**
     * Inserts the product into the database
     */
    private void insertProduct()
    {
        String query = "INSERT INTO Product (Name, Category, Type, Units, Price, SupplierName) VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(query);
        db.beginTransaction();
        stmt.bindString(1, getName());
        stmt.bindString(2, getCategory());
        stmt.bindString(3, getType());
        stmt.bindDouble(4, getQuantity());
        stmt.bindDouble(5, getPrice());
        stmt.bindString(6, getSupplier());
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * Checks if all the fields are correctly filled
     * @return true if all correct
     */
    private boolean allFieldsFilled(){
        if(!getName().equals("") && getPrice() > 0
                && checkType()) return true;
        return false;
    }

    private boolean checkType(){
        if(spinnerTypes.getSelectedItem().toString().equals("package")){
            if(!editTextTypeQuantity.getText().toString().equals("")){
                return true;
            }
        }
        return false;
    }

    private void returnToPage(View view){
        Intent i = new Intent(getApplicationContext(), ProductPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
        finish();
    }

    private void toastError(){
        CharSequence text = "Some fields are not correctly filled.";

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private double getQuantity(){
        String quantity = spinnerTypes.getSelectedItem().toString();
        if(quantity.equals("kilo") || quantity.equals("unit")){
            editTextTypeQuantity.setText("1");
        }
        return  Double.valueOf(editTextTypeQuantity.getText().toString());
    }

    private String getType(){
        return spinnerTypes.getSelectedItem().toString();
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

}

