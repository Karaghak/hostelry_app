package com.example.erik.erp_hotel_industry.product_classes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuDelete;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;

public class ProductDetail extends MenuDelete {

    private final static String DATABASE_NAME = "ERP_DB.db";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_activity);

        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        final EditText editTextName = (EditText) findViewById(R.id.editTextName);
        final EditText editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        final EditText editTextSupplier = (EditText) findViewById(R.id.editTextSupplier);
        final EditText editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        Button buttonModify = (Button) findViewById(R.id.buttonModify);

        loadValuesEditText(editTextName, editTextCategory, editTextSupplier, editTextPrice);




        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(v, editTextName, editTextCategory, editTextSupplier, editTextPrice);
            }
        });


    }

    private void loadValuesEditText(EditText editTextName, EditText editTextCategory, EditText editTextSupplier, EditText editTextPrice) {
        Intent i = getIntent();
        if(i != null) {
            Product product = i.getParcelableExtra("product");
            editTextName.setText(product.getName());
            editTextCategory.setText(product.getCategory());
            editTextSupplier.setText(product.getSupplierName());
            editTextPrice.setText(String.valueOf((product.getPrice())));
        }
    }

    public void updateProduct(View view, EditText editTextName, EditText editTextCategory,
                              EditText editTextSupplier, EditText editTextPrice){
        Product product = getIntent().getParcelableExtra("product");
        int id = product.getId();

        // Read all fields
        String name = editTextName.getText().toString();
        String category = editTextCategory.getText().toString();
        String supplier = editTextSupplier.getText().toString();
        Double price = Double.valueOf(editTextPrice.getText().toString());

        String query = "UPDATE Product" +
                " SET Name = ?, Category = ?, Price = ?, SupplierName = ?" +
                " WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        // Start transaction
        db.beginTransaction();
        stmt.bindString(1, name);
        stmt.bindString(2, category);
        stmt.bindDouble(3, price);
        stmt.bindString(4, supplier);
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
    }

}
