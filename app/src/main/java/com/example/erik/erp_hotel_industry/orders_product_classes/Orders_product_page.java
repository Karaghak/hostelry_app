package com.example.erik.
        erp_hotel_industry.orders_product_classes;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.adapters.Orders_Product_Adapter;
import com.example.erik.erp_hotel_industry.product_classes.Product;

import java.util.ArrayList;

/**
 * Created by Erik on 27/10/2016.
 */
public class Orders_product_page extends Activity {

    private static String DATABASE_NAME = "";
    private ListView listView;
    private int orderID;
    private ArrayList<Auxiliary_orders_product> productList;
    private TextView textViewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_product_page_activity);

        // Load elements
        listView = (ListView) this.findViewById(R.id.listViewOrdersProduct);
        textViewPrice = (TextView) this.findViewById(R.id.textViewPrice);

        // Load parameters
        Bundle b = getIntent().getExtras();
        if (b != null) {
            DATABASE_NAME = b.getString("db_name");
            orderID = b.getInt("orderID");
        } else {
            try {
                throw new Exception("Critical error when trying to get the supplier name ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        productList = getProducts(db, orderID);
        Orders_Product_Adapter adapter = new Orders_Product_Adapter(Orders_product_page.this, productList);
        listView.setAdapter(adapter);

        double totalPrice = getTotalPrice(productList);
        textViewPrice.setText(textViewPrice.getText() + String.valueOf(totalPrice) + "€");

        db.close();
    }

    private ArrayList<Auxiliary_orders_product> getProducts(SQLiteDatabase db, int order_id){
        ArrayList<Auxiliary_orders_product> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Order_Product WHERE ID_Order = ?", new String[]{String.valueOf(order_id)});
        if(cursor.moveToFirst()){
            do{
                int product_id = cursor.getInt(2);
                result.add(new Auxiliary_orders_product(getProductsName(db, product_id), cursor.getInt(3),
                        getProductsPrice(db, product_id)));
            }while (cursor.moveToNext());
        }
        return result;
    }

    private String getProductsName(SQLiteDatabase db, int product_id){
        Cursor cursor = db.rawQuery("SELECT Name FROM Product WHERE ID = ?",
                new String[]{String.valueOf(product_id)});
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        return null;
    }

    private double getProductsPrice(SQLiteDatabase db, int product_id){
        Cursor cursor = db.rawQuery("SELECT Price FROM Product WHERE ID = ?",
                new String[]{String.valueOf(product_id)});
        if(cursor.moveToFirst()){
            return cursor.getDouble(0);
        }
        return 0;
    }

    private double getTotalPrice(ArrayList<Auxiliary_orders_product> list){
        double result = 0;
        for (Auxiliary_orders_product product :
                list) {
            result += product.getPrice() * product.getQuantity();
        }
        return result;
    }
}
