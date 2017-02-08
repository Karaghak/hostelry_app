package com.example.erik.erp_hotel_industry.supplier_classes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private SQLiteDatabase db;
    private final static String TABLE_NAME = "Supplier";
    private int itemId;
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
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ArrayList<Supplier> suppliers = new ArrayList<>();
        getProducts(db, suppliers);
        ArrayAdapter<Supplier> adapter = new ArrayAdapter<Supplier>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suppliers);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Supplier supplier = (Supplier) listView.getItemAtPosition(position);
                itemDetailPage(supplier);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Supplier item = (Supplier) listView.getItemAtPosition(position);
                itemId = item.getId();
                startActionMode(mActionModeCallback);
                return true;
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
     * @param supplier
     */
    private void itemDetailPage(Supplier supplier) {
        Intent i = new Intent(getApplicationContext(), SupplierDetail.class);
        i.putExtra("db_name", DATABASE_NAME);
        i.putExtra("supplier", supplier);
        startActivity(i);
        finish();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menudelete, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deleteItem:
                    deleteItem(db, TABLE_NAME, itemId);
                    mode.finish(); // Action picked, so close the CAB
                    finish();
                    startActivity(getIntent());
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode = null;
        }
    };
}
