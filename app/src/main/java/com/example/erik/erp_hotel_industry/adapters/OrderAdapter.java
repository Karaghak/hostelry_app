package com.example.erik.erp_hotel_industry.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.orders_classes.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Erik on 20/10/2016.
 */
public class OrderAdapter extends ArrayAdapter<Order> {
    private Activity context;
    private ArrayList<Order> orders;

    public OrderAdapter(Activity context, ArrayList<Order> orders){
        super(context, R.layout.adapter_two_textview, orders);
        this.context = context;
        this.orders = orders;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.adapter_two_textview, null, true);

        TextView orderDate = (TextView) rowView.findViewById(R.id.firstTextView);
        TextView supplierName = (TextView) rowView.findViewById(R.id.secondTextView);

        orderDate.setText(getDate(position));
        supplierName.setText(orders.get(position).getSupplier_name());



        return rowView;
    }

    private String getDate(int position)
    {
        Date date = orders.get(position).getOrder_date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        return dateFormat.format(date);
    }


}
