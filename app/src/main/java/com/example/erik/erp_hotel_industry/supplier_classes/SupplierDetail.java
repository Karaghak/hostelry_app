package com.example.erik.erp_hotel_industry.supplier_classes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuCall;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;

/**
 * Created by Erik on 27/09/2016.
 */
public class SupplierDetail extends MenuCall {

    private EditText etName;
    private EditText etPhoneNumber;
    private Button btnModify;
    private Supplier supplier;

    private static String DATABASE_NAME = "";
    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_detail_activity);

        // Open database
        Bundle b = getIntent().getExtras();
        if (b != null) {
            DATABASE_NAME = b.getString("db_name");
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            supplier = getIntent().getParcelableExtra("supplier");
        }


        // Instantiating elements
        etName = (EditText) findViewById(R.id.editTextName);
        etPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        btnModify = (Button) findViewById(R.id.buttonModify);

        // Fill fields
        fillFields();

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSupplier(v);
            }
        });
    }

    /**
     * Method that fill the supplier fields
     */
    private void fillFields() {
        etName.setText(supplier.getName());
        etPhoneNumber.setText(String.valueOf(supplier.getPhoneNumber()));
    }

    /**
     * Method that update the supplier with the parameters in the editText
     * @param view
     */
    private void updateSupplier(View view) {
        String name = etName.getText().toString();
        int phoneNumber = Integer.valueOf(etPhoneNumber.getText().toString());
        int id = supplier.getId();

        String query = "UPDATE Supplier" +
                " SET Name = ?, PhoneNumber = ?" +
                " WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        // Start transactions
        db.beginTransaction();
        stmt.bindString(1, name);
        stmt.bindDouble(2, phoneNumber);
        stmt.bindDouble(3, id);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        returnToPage(view);
    }

    /**
     * Method that open the activity SupplierPage
     * @param view
     */
    private void returnToPage(View view) {
        Intent i = new Intent(getApplicationContext(), SupplierPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    @Override
    public void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+etPhoneNumber.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }


}
