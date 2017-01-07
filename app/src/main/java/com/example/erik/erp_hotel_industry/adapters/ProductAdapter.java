package com.example.erik.erp_hotel_industry.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.product_classes.Product;

import java.util.ArrayList;

/**
 * Created by Erik on 04/09/2016.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private ArrayList<Product> products;

    public ProductAdapter(Activity context, ArrayList<Product> products){
        super(context, R.layout.adapter_two_textview, products);
        this.context = context;
        this.products = products;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.adapter_two_textview, null, true);

        TextView productName = (TextView) rowView.findViewById(R.id.firstTextView);
        TextView productSupplier= (TextView) rowView.findViewById(R.id.secondTextView);

        productName.setText(products.get(position).getName());
        productSupplier.setText(products.get(position).getSupplierName());

        return rowView;
    }


}
