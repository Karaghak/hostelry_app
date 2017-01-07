package com.example.erik.erp_hotel_industry.supplier_classes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.erik.erp_hotel_industry.menus.MenuAdd;
import com.example.erik.erp_hotel_industry.R;

import java.util.ArrayList;

/**
 * Created by Erik on 27/09/2016.
 */
public class SupplierPage extends MenuAdd {

    private static String DATABASE_NAME = "";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_page_activity);

        // Load elements
        listView = (ListView) this.findViewById(R.id.supplier_listView);

        // Load parameters
        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        // Open database and load products
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ArrayList<Supplier> suppliers = new ArrayList<>();
        getProducts(db, suppliers);
        ArrayAdapter<Supplier> adapter = new ArrayAdapter<Supplier>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suppliers);
        listView.setAdapter(adapter);

        db.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Supplier supplier = (Supplier) listView.getItemAtPosition(position);
                itemDetailPage(view, supplier);
            }
        });
    }

    /**
     * Method that gets all the suppliers in the database
     * @param db
     * @param suppliers
     */
    private void getProducts(SQLiteDatabase db, ArrayList<Supplier> suppliers){
        String query = "SELECT * FROM Supplier";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                suppliers.add(new Supplier(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            }while (cursor.moveToNext());
        }
    }

    /**
     * Method that open the addSupplier Activity
     * @param view
     */
    @Override
    public void addItemPage(View view){
        Intent i = new Intent(getApplicationContext(), Add_supplier.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    /**
     * Method for open an especific supplier
     * @param view
     * @param supplier
     */
    private void itemDetailPage(View view, Supplier supplier) {
        Intent i = new Intent(getApplicationContext(), SupplierDetail.class);
        i.putExtra("db_name", DATABASE_NAME);
        i.putExtra("supplier", supplier);
        startActivity(i);
    }
}
