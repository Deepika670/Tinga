package com.sytiqhub.tinga.manager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sytiqhub.tinga.beans.OrderFoodBean;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "OrdersManager";
    private static final String TABLE_ORDERS_FOOD = "ztbl_orders_food";

    private static final String KEY_ID = "id";
    private static final String KEY_FOOD_ID = "food_id";
    private static final String KEY_FOOD_NAME = "food_name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TOTAL_PRICE = "total_price";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDER_FOOD_TABLE = "CREATE TABLE " + TABLE_ORDERS_FOOD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FOOD_ID + " TEXT UNIQUE, " + KEY_FOOD_NAME + " TEXT, "
                + KEY_QUANTITY + " INTEGER," + KEY_TOTAL_PRICE + " INTEGER" +")";
        db.execSQL(CREATE_ORDER_FOOD_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS_FOOD);
        // Create tables again
        onCreate(db);
    }


    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS_FOOD);
        // Create tables again
        onCreate(db);
    }
    // code to add the new contact
    public void addOrderFood(OrderFoodBean order) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("id", String.valueOf(getOrderedFoodCount()));
        Log.d(KEY_FOOD_NAME, order.getFoodName());
        Log.d(KEY_FOOD_ID, order.getFoodId()); // Contact Name
        Log.d(KEY_QUANTITY, String.valueOf(order.getQuantity()));
        Log.d(KEY_TOTAL_PRICE, String.valueOf(order.getTotalPrice()));

        ContentValues values = new ContentValues();
        values.put("id", getOrderedFoodCount());
        values.put(KEY_FOOD_NAME, order.getFoodName());
        values.put(KEY_FOOD_ID, order.getFoodId()); // Contact Name
        values.put(KEY_QUANTITY, order.getQuantity()); // Contact Phone
        values.put(KEY_TOTAL_PRICE, order.getTotalPrice()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_ORDERS_FOOD, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

 /*   public void delete_data(){
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS_FOOD,"food_id IN",)
    }
*/
   /* // code to get the single contact
    OrderFoodBean getContent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }*/

    // code to get all contacts in a list view
    public List<OrderFoodBean> getAllContent() {
        List<OrderFoodBean> contactList = new ArrayList<OrderFoodBean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDERS_FOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderFoodBean order = new OrderFoodBean();
                order.setFoodId(cursor.getString(1));
                order.setFoodName(cursor.getString(2));
                order.setQuantity(Integer.parseInt(cursor.getString(3)));
                order.setTotalPrice(Integer.parseInt(cursor.getString(4)));
                // Adding contact to list
                contactList.add(order);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    int totalprice = 0;
    // code to get all contacts in a list view
    public int getTotalPrice() {
        List<OrderFoodBean> contactList = new ArrayList<OrderFoodBean>();
        // Select All Query
        String selectQuery = "SELECT "+KEY_TOTAL_PRICE+","+KEY_QUANTITY+" FROM " + TABLE_ORDERS_FOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                totalprice = totalprice +Integer.parseInt(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        // return contact list
        Log.d("total price",String.valueOf(totalprice));
        return totalprice;
    }

    public int getOrderedFoodCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ORDERS_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

   // code to update the single contact
    public int updateQuantity(String food_id,int quatity,int price) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUANTITY, quatity);
        values.put(KEY_TOTAL_PRICE, price);

        // updating row
        return db.update(TABLE_ORDERS_FOOD, values, KEY_FOOD_ID + " = ?",
                new String[] { String.valueOf(food_id)  });
    }

     // Deleting single contact
    public void deleteOrderFood(String food_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS_FOOD, KEY_FOOD_ID + " = ?",
                new String[] { food_id });
        db.close();
    }
}