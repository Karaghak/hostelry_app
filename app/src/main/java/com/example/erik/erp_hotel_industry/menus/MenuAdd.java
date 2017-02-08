package com.example.erik.erp_hotel_industry.menus;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.category_classes.Category;

/**
 * Created by Erik on 28/12/2016.
 */

public class MenuAdd<T> extends AppCompatActivity {

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

    public void deleteItem(SQLiteDatabase db, String tableName, int id) {
        String query = "DELETE FROM " + tableName + " WHERE ID = ?";
        SQLiteStatement stmt = db.compileStatement(query);
        // Start transaction
        db.beginTransaction();
        stmt.bindDouble(1, id);
        stmt.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        // End transaction
        db.close();
    }

    /**
     * Returns the item id from the listView
     * @param position
     * @return
     */
    /*
    public double getItemId(ListView listView, int position){
        T item = (T) listView.getItemAtPosition(position);
        return category.getId();
    }
     */

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
