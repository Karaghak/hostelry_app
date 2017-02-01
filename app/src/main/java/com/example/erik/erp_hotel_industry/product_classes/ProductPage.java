package com.example.erik.erp_hotel_industry.product_classes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    private ListView listView;
    private Button button;

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
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ArrayList<Product> products = new ArrayList<>();
        getProducts(db, products);

        ProductAdapter productAdapter = new ProductAdapter(ProductPage.this, products);
        listView.setAdapter(productAdapter);

        db.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) listView.getItemAtPosition(position);
                itemDetailPage(view, product);
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

    private void itemDetailPage(View view, Product product) {
        Intent i = new Intent(getApplicationContext(), ProductDetail.class);
        i.putExtra("db_name", DATABASE_NAME);
        i.putExtra("product", product);
        startActivity(i);
    }
}
