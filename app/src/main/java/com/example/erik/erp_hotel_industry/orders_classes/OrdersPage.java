package com.example.erik.erp_hotel_industry.orders_classes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.adapters.OrderAdapter;
import com.example.erik.erp_hotel_industry.menus.MenuAdd;
import com.example.erik.erp_hotel_industry.orders_product_classes.Orders_product_page;
import com.example.erik.erp_hotel_industry.supplier_classes.Add_supplier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Erik on 06/10/2016.
 */
public class OrdersPage extends MenuAdd {

    private static String DATABASE_NAME = "";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page_activity);

        listView = (ListView) findViewById(R.id.listViewOrder_Page);

        // Load parameters
        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        // Open database and load products
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ArrayList<Order> orders = new ArrayList<>();
        getOrders(db, orders);


        OrderAdapter order_adapter = new OrderAdapter(OrdersPage.this, orders);
        listView.setAdapter(order_adapter);

        db.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ordersProduct(view, getOrderID(position));
            }
        });
    }



    private void getOrders(SQLiteDatabase db, ArrayList<Order> orders) {
        String query = "SELECT * FROM Orders";
        Cursor cursor = db.rawQuery(query, null);
        Date date = new Date();
        if(cursor.moveToFirst()){
            do {
                String dateString = cursor.getString(3);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                orders.add(new Order(cursor.getInt(0), cursor.getInt(1),
                        cursor.getString(2), date, cursor.getDouble(4)));
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void addItemPage(View view){
        Intent i = new Intent(getApplicationContext(), Add_order.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    private int getOrderID(int position) {
        Order order = (Order) listView.getItemAtPosition(position);
        return order.getId();
    }

    private void ordersProduct(View view, int orderID){
        Intent i = new Intent(getApplicationContext(), Orders_product_page.class);
        Bundle b = new Bundle();
        b.putString("db_name", DATABASE_NAME);
        i.putExtras(b);
        i.putExtra("orderID", orderID);
        startActivity(i);
    }



}
