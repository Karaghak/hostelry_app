package com.example.erik.erp_hotel_industry.adapters;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.orders_product_classes.Auxiliary_orders_product;
import com.example.erik.erp_hotel_industry.orders_product_classes.Orders_product;

import java.util.ArrayList;

/**
 * Created by Erik on 28/10/2016.
 */

public class Orders_Product_Adapter extends ArrayAdapter<Auxiliary_orders_product> {

    private Activity context;
    private ArrayList<Auxiliary_orders_product> products;

    public Orders_Product_Adapter(Activity context, ArrayList<Auxiliary_orders_product> products){
        super(context, R.layout.adapter_two_textview, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.adapter_two_textview, null, true);

        TextView productName = (TextView) rowView.findViewById(R.id.firstTextView);
        TextView quantity = (TextView) rowView.findViewById(R.id.secondTextView);

        productName.setText(products.get(position).getName());
        quantity.setText(String.valueOf(products.get(position).getQuantity()));


        return rowView;
    }



}
