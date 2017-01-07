package com.example.erik.erp_hotel_industry.supplier_classes;

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

public class Add_supplier extends MenuSimple {

    private static String DATABASE_NAME = "";
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private Button buttonAccept;

    // Text supplier values
    String name;
    int phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_add_activity);

        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        // Open database
        final SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // Initialize elements
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        buttonAccept = (Button) findViewById(R.id.supplier_add_button);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readAndLoadProductValues();
                if(allFieldsFilled()){
                    insertSupplier(db, name, phoneNumber);
                    returnToPage(v);
                }else{
                    toastError();
                }
            }
        });
    }

    /**
     * Method that reads the supplier values on the editText
     */
    private void readAndLoadProductValues(){
        name = editTextName.getText().toString();
        if(!editTextPhoneNumber.getText().toString().equals("")){
            phoneNumber = Integer.parseInt(editTextPhoneNumber.getText().toString());
        }else{
            toastError();
        }
    }

    /**
     * Method that executes the add supplier sql
     * @param db
     * @param name
     * @param phoneNumber
     */
    private void insertSupplier(SQLiteDatabase db, String name, int phoneNumber)
    {
        String query = "INSERT INTO Supplier (Name, PhoneNumber) VALUES (?, ?)";
        SQLiteStatement stmt = db.compileStatement(query);
        db.beginTransaction();
        stmt.bindString(1, name);
        stmt.bindDouble(2, phoneNumber);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean allFieldsFilled(){
        if(!name.equals("") && !editTextPhoneNumber.getText().toString().equals("")) return true;
        return false;
    }

    private void returnToPage(View view){
        Intent i = new Intent(getApplicationContext(), SupplierPage.class);
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
