package com.example.erik.erp_hotel_industry.category_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuDelete;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;

/**
 * Created by Erik on 28/09/2016.
 */
public class CategoryDetail extends MenuDelete {

    private static String DATABASE_NAME = "";
    private SQLiteDatabase db;
    private EditText editTextName;
    private Category category;
    private Button buttonModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail_activity);

        // Open database
        Bundle b = getIntent().getExtras();
        if (b != null) {
            DATABASE_NAME = b.getString("db_name");
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            category = getIntent().getParcelableExtra("category");
        }

        // Instantiating elements
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonModify = (Button) findViewById(R.id.buttonModify);

        fillEditText();

        // Button modify action
        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    updateCategory(db, v);
                }else{
                    toastError();
                }
            }
        });
    }

    /**
     * Method for update category with the new parameters
     * @param db
     * @param view
     */
    private void updateCategory(SQLiteDatabase db, View view) {
        String query = "UPDATE Category SET Name = ? " +
                "WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.bindString(1, editTextName.getText().toString());
        stmt.bindDouble(2, category.getId());

        db.beginTransaction();
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        returnToPage(view);
    }

    /**
     * Method that check if all the fields are correctly filled.
     *
     * @return
     */
    private boolean checkFields() {
        if (!editTextName.getText().toString().equals("")) return true;
        return false;
    }

    @Override
    public void deleteItem(View view) {
        Category category = getIntent().getParcelableExtra("category");
        int id = category.getId();
        String query = "DELETE FROM Category WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        // Start transaction
        db.beginTransaction();
        stmt.bindDouble(1, id);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        // End transaction
        db.close();
        // Open page after delete
        returnToPage(view);
    }

    private void returnToPage(View view) {
        Intent i = new Intent(getApplicationContext(), CategoryPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    private void fillEditText() {
        editTextName.setText(category.getName());
    }

    private void toastError(){
        Context context = getApplicationContext();
        CharSequence text = "Some fields are not correctly filled.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



}
