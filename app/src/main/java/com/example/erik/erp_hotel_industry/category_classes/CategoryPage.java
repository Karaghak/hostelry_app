package com.example.erik.erp_hotel_industry.category_classes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.erik.erp_hotel_industry.menus.MenuAdd;
import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;

import java.util.ArrayList;

/**
 * Created by Erik on 28/09/2016.
 */
public class CategoryPage extends MenuAdd {

    private static String DATABASE_NAME = "";
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page_activity);


        // Load elements
        listView = (ListView) this.findViewById(R.id.listViewCategory);
        // Load parameters
        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        // Open database
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        // Create ArrayList for the listView
        ArrayList<Category> categories = new ArrayList<>();
        getCategories(db, categories);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categories);
        listView.setAdapter(adapter);

        db.close();

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
     * @param db the database
     * @param list the arraylist for saving the categories
     * @return the list
     */
    private ArrayList<Category> getCategories(SQLiteDatabase db, ArrayList<Category> list){
        String query = "SELECT * FROM Category";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                list.add(new Category(cursor.getInt(0), cursor.getString(1)));
            }while (cursor.moveToNext());
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
    }






}
