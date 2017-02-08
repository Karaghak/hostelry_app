package com.example.erik.erp_hotel_industry.category_classes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
 * Created by Erik on 28/09/2016.
 */
public class CategoryPage extends MenuAdd {

    private SQLiteDatabase db;
    private static String DATABASE_NAME = "";
    private ListView listView;
    private int itemId;
    private final static String TABLE_NAME = "Category";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page_activity);
        System.out.println("prueba");

        // Load elements
        listView = (ListView) this.findViewById(R.id.listViewCategory);
        // Load parameters
        Bundle b = getIntent().getExtras();
        if (b != null) DATABASE_NAME = b.getString("db_name");

        // Open database
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // Create ArrayList for the listView
        ArrayList<Category> categories = new ArrayList<>();
        getCategories(db, categories);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categories);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Category item = (Category) listView.getItemAtPosition(position);
                itemId = item.getId();
                startActionMode(mActionModeCallback);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) listView.getItemAtPosition(position);
                itemDetailPage(category);
            }
        });
    }


    /**
     * Method that retrieves all the categories from the database
     *
     * @param db   the database
     * @param list the arraylist for saving the categories
     * @return the list
     */
    private ArrayList<Category> getCategories(SQLiteDatabase db, ArrayList<Category> list) {
        String query = "SELECT * FROM Category";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Category(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * Method that open add categories activity
     */
    @Override
    public void addItemPage(View view) {
        Intent i = new Intent(getApplicationContext(), Add_category.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    private void itemDetailPage(Category category) {
        Intent i = new Intent(getApplicationContext(), CategoryDetail.class);
        i.putExtra("db_name", DATABASE_NAME);
        i.putExtra("category", category);
        startActivity(i);
        finish();
    }


    /**
     * Callback for the contextual action mode
     */
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







