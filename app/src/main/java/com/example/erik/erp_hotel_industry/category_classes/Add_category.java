package com.example.erik.erp_hotel_industry.category_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;
import com.example.erik.erp_hotel_industry.orders_classes.OrdersPage;

import java.sql.PreparedStatement;


/**
 * Created by Erik on 28/09/2016.
 */
public class Add_category extends MenuSimple {

    private SQLiteDatabase db;
    private static String DATABASE_NAME = "";
    private EditText editTextName;
    private Button buttonAdd;
    // Category parameters
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_add_activity);


        // Load parameters
        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        // Open database
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    addCategory(db, name);
                    returnToPage();
                }else{
                    toastError();
                }
            }
        });
    }

    public void addCategory(SQLiteDatabase db, String name){
        String query = "INSERT INTO Category(Name) VALUES (?)";
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.bindString(1, name);
        db.beginTransaction();
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    private void returnToPage(){
        Intent i = new Intent(getApplicationContext(), CategoryPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
        finish();
    }

    private boolean checkFields(){
        name = editTextName.getText().toString();
        if(!name.equals("")) return true;
        return false;
    }

    private void toastError(){
        Context context = getApplicationContext();
        CharSequence text = "Some fields are not correctly filled.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



}
