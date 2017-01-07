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
        final SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

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
        createCategoryAdapter(db);

        // Supplier spinner adapter
        createSupplierAdapter(db);

        // Types spinner adapter
        createTypesAdapter();



        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerTypes.getSelectedItem().toString().equals("package")) editTextTypeQuantity.setEnabled(true
                );
                else{
                    editTextTypeQuantity.setText("");
                    editTextTypeQuantity.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(allFieldsFilled()){
                    insertProduct(db, editTextName.getText().toString(),
                            spinnerCategory.getSelectedItem().toString(),
                            spinnerTypes.getSelectedItem().toString(),
                            Double.valueOf(editTextTypeQuantity.getText().toString()),
                            Double.valueOf(editTextPrice.getText().toString()),
                            spinnerSupplier.getSelectedItem().toString());
                    returnToPage(v);
                }
                else{
                    toastError();
                }
            }
        });


    }

    private void createSupplierAdapter(SQLiteDatabase db) {
        ArrayList<String> listSupplier = new ArrayList<>();
        ArrayAdapter<String> adapterSupplier = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSupplier);
        adapterSupplier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadSpinnerSupplier(db, listSupplier);
        spinnerSupplier.setAdapter(adapterSupplier);
    }

    private void createCategoryAdapter(SQLiteDatabase db) {
        ArrayList<String> listCategory = new ArrayList<>();

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadSpinnerCategory(db, listCategory);
        spinnerCategory.setAdapter(adapterCategory);
    }

    private void createTypesAdapter(){
        ArrayList<String> types = new Product().getTypes();

        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypes.setAdapter(adapterTypes);
    }


    private void loadSpinnerSupplier(SQLiteDatabase db, ArrayList<String> list){
        String query = "SELECT Name from Supplier";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
    }

    private void loadSpinnerCategory(SQLiteDatabase db, ArrayList<String> list){
        String query = "SELECT Name from Category ORDER BY Name ASC";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
    }


    private void insertProduct(SQLiteDatabase db, String name, String category, String type, double typeQuantity,
                               double price, String supplierName)
    {
        String query = "INSERT INTO Product (Name, Category, Type, Units, Price, SupplierName) VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(query);
        db.beginTransaction();
        stmt.bindString(1, name);
        stmt.bindString(2, category);
        stmt.bindString(3, type);
        stmt.bindDouble(4, typeQuantity);
        stmt.bindDouble(5, price);
        stmt.bindString(6, supplierName);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean allFieldsFilled(){
        if(!editTextName.getText().toString().equals("") && Double.valueOf(editTextPrice.getText().toString()) > 0
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
    }



    private void toastError(){
        Context context = getApplicationContext();
        CharSequence text = "Some fields are not correctly filled.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}

