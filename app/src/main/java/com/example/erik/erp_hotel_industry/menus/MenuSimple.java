package com.example.erik.erp_hotel_industry.menus;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.erik.erp_hotel_industry.R;

/**
 * Created by Erik on 28/12/2016.
 */

public class MenuSimple extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menusimple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
