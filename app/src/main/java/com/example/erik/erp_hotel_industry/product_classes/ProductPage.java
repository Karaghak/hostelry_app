package com.example.erik.erp_hotel_industry.product_classes;

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
import android.widget.Button;
import android.widget.ListView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.adapters.ProductAdapter;
import com.example.erik.erp_hotel_industry.menus.MenuAdd;

import java.util.ArrayList;

public class ProductPage extends MenuAdd {

    private static String DATABASE_NAME = "";
    private SQLiteDatabase db;
    private final static String TABLE_NAME = "Product";
    private int itemId;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page_activity);

        // Load elements
        listView = (ListView) this.findViewById(R.id.productList);

        // Load parameters
        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        // Open database and load products
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ArrayList<Product> products = new ArrayList<>();
        getProducts(db, products);

        ProductAdapter productAdapter = new ProductAdapter(ProductPage.this, products);
        listView.setAdapter(productAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) listView.getItemAtPosition(position);
                itemDetailPage(product);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Product item = (Product) listView.getItemAtPosition(position);
                itemId = item.getId();
                startActionMode(mActionModeCallback);
                return true;
            }
        });
    }

    private void getProducts(SQLiteDatabase db, ArrayList<Product> products){
        String query = "SELECT * FROM Product";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                products.add(new Product(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getDouble(5), cursor.getString(6)));
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void addItemPage(View view){
        Intent i = new Intent(getApplicationContext(), Add_product.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    private void itemDetailPage(Product product) {
        Intent i = new Intent(getApplicationContext(), ProductDetail.class);
        i.putExtra("db_name", DATABASE_NAME);
        i.putExtra("product", product);
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
