package com.example.erik.erp_hotel_industry.orders_classes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.erik.erp_hotel_industry.R;
import com.example.erik.erp_hotel_industry.menus.MenuSimple;
import com.example.erik.erp_hotel_industry.orders_product_classes.Auxiliary_orders_product;
import com.example.erik.erp_hotel_industry.product_classes.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Erik on 06/10/2016.
 */
public class Add_order extends MenuSimple {

    private static String DATABASE_NAME = "";
    private HashMap<Product, Integer> productsList;

    private CalendarView calendarView;
    private Spinner spinnerSupplier;
    private Spinner spinnerProduct;
    private EditText editTextQuantity;
    private Button buttonAdd;
    private Button buttonAddOrder;
    private SQLiteDatabase db;
    private Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_add_activity);

        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");

        productsList = new HashMap<>();
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        date = new Date();

        // Initialize components
        calendarView = (CalendarView) findViewById(R.id.calendarOrder);
        spinnerSupplier = (Spinner) findViewById(R.id.spinnerSupplier);
        spinnerProduct = (Spinner) findViewById(R.id.spinnerProduct);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAddOrder = (Button) findViewById(R.id.buttonAddOrder);

        // Set the calendar
        calendarView.setDate(Calendar.getInstance().getTimeInMillis());
        date.setTime(Calendar.getInstance().getTimeInMillis());

        loadSupplierSpinner();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                changeDate(year, month, dayOfMonth);
            }
        });

        buttonAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder(v, productsList);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFieldsFilled()){
                    addProduct(productsList, getProductFromSpinner(spinnerProduct), getQuantity(editTextQuantity));
                    printToast(getProductFromSpinner(spinnerProduct).getName(), getQuantity(editTextQuantity));
                }else{
                    toastError();
                }
            }
        });

        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isQuantitySelected();
            }
        });
    }

    private void changeDate(int year, int month, int dayOfMonth) {
        GregorianCalendar gc = new GregorianCalendar(year, month, dayOfMonth);
        date = gc.getTime();
    }

    private void loadSupplierSpinner(){
        ArrayList<String> supplierNames = new ArrayList<>();

        loadSupplierInfo(supplierNames);
        createSupplierAdapter(supplierNames);
    }

    public void loadProductSpinner(View v){
        String name = spinnerSupplier.getSelectedItem().toString();
        ArrayList<Product> products = new ArrayList<>();

        loadProductInfo(products, name);
        createProdutAdapter(products);
    }

    private void loadSupplierInfo(ArrayList<String> list){
        String query = "SELECT Name FROM SUPPLIER";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
    }

    private void createSupplierAdapter(ArrayList<String> list) {
        ArrayAdapter<String> adapterSupplier = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapterSupplier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSupplier.setAdapter(adapterSupplier);
    }

    private void loadProductInfo(ArrayList<Product> products, String name){
        String query = "SELECT * FROM Product Where SupplierName = '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                products.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getDouble(5), cursor.getString(6)));
            }while (cursor.moveToNext());
        }
    }

    private void createProdutAdapter(ArrayList<Product> products) {
        ArrayAdapter<Product> adapterProduct = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, products);
        adapterProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(adapterProduct);
    }

    /**
     * Method that adds the new order and the calls the method insertProductsInOrder
     * to create the properly relationship
     * @param v
     */
    public void addOrder(View v, HashMap<Product, Integer> productsList){
        String supplierName = spinnerSupplier.getSelectedItem().toString();
        int supplier_id = getSupplierID(supplierName);
        int orderID;
        double final_price = 0.0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String dateString = dateFormat.format(date);

        // Inserting the order into the database
        db.beginTransaction();

        ContentValues cv = new ContentValues();
        cv.put("ID_Supplier", supplier_id);
        cv.put("Supplier_Name", supplierName);
        cv.put("Order_Date", dateString);
        cv.put("Final_Price", final_price);

        orderID = (int) db.insert("Orders", null, cv);
        db.setTransactionSuccessful();
        db.endTransaction();

        insertProductsInOrder(db, productsList, orderID);

        // Close the database
        db.close();
        ordersPage(v);
    }

    /**
     * Method that inserts the relationship between the new order and all
     * the products selected
     * @param db
     * @param productsList
     */
    private void insertProductsInOrder(SQLiteDatabase db, HashMap<Product, Integer> productsList, int orderID){

        String query = "INSERT INTO Order_Product (ID_Order, ID_Product, Quantity) VALUES (?, ?, ?)";
        for (Product product :
                productsList.keySet()) {
            int quantity = productsList.get(product);
            SQLiteStatement stmt = db.compileStatement(query);
            db.beginTransaction();
            stmt.bindDouble(1, orderID);
            stmt.bindDouble(2, product.getId());
            stmt.bindDouble(3, quantity);
            stmt.execute();
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }


    /**
     * Method that add the product selected and his quantity to a dictionary in order
     * to save them in the database if the order is completed.
     * @param productsList
     * @param product
     * @param quantity
     */
    private void addProduct(HashMap<Product, Integer> productsList, Product product, int quantity){
        productsList.put(product, quantity);
    }

    /**
     * Auxiliary method that retrieves the amount of product selected.
     * @param editTextQuantity
     * @return
     */
    private int getQuantity(EditText editTextQuantity){
        return Integer.valueOf(editTextQuantity.getText().toString());
    }

    /**
     * Auxiliary method that retrieves the product selected.
     * @param spinnerProduct
     * @return
     */
    private Product getProductFromSpinner(Spinner spinnerProduct){
        return (Product) spinnerProduct.getSelectedItem();
    }


    private int getSupplierID(String supplierName){
        String query = "SELECT ID From Supplier WHERE Name = '" + supplierName + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public void ordersPage(View view){
        Intent i = new Intent(getApplicationContext(), OrdersPage.class);
        i.putExtra("db_name", DATABASE_NAME);
        startActivity(i);
    }

    private void isAtLeastOneProductSelected(){

    }

    /**
     * Auxiliary method for control the status of the button add, preventing NullPointerException
     */
    private void isQuantitySelected(){
        if(editTextQuantity.getText().length() > 0) buttonAdd.setEnabled(true);
        else{
            buttonAdd.setEnabled(false);
        }
    }

    /**
     * Method that print a toast with the product info in the moment that
     * add button is clicked.
     * @param productName
     * @param quantity
     */
    private void printToast(String productName, int quantity){
        Context context = getApplicationContext();
        CharSequence text = "Product: " + productName + ", Quantity: " + quantity + " added";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private boolean allFieldsFilled(){
        if(getProductFromSpinner(spinnerProduct) != null && getQuantity(editTextQuantity) > 0) return true;
        return false;
    }

    private void toastError(){
        Context context = getApplicationContext();
        CharSequence text = "Some fields are not correctly filled.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
