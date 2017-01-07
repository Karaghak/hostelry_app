package com.example.erik.erp_hotel_industry.menus;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.erik.erp_hotel_industry.R;

/**
 * Created by Erik on 28/12/2016.
 */

public class MenuAdd extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuadd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addItem:
                addItemPage(this.findViewById(android.R.id.content).getRootView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItemPage(View rootView) {
    }

    /*
    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudelete, menu);
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                //onPrepareOptionsMenu(menudelete)
        }
        return true;
    }*/




}
