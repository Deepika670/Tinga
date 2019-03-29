package com.sytiqhub.tinga.manager;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.beans.UserBean;

public class PreferenceManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "TingaPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";
    public static final String KEY_UID = "uid";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_VERIFICATION = "mobile_verification";

    public static final String KEY_PROFILE_STATUS = "profile_status";
    public static final String KEY_LOGIN_ID = "login_id";

    public static final String KEY_FOOD_LIST = "food_list";

    public static final String KEY_RESTAURANT_ID = "restaurant_id";

    public static final String KEY_RESTAURANT_NAME = "restaurant_name";

    public static final String KEY_RESTAURANT_IMAGE = "restaurant_image";

    public static final String KEY_ORDER_FOOD_LIST = "order_food_list";

    public static final String KEY_CURRENT_LATITUDE = "current_lat";

    public static final String KEY_CURRENT_LONGITUDE = "current_long";

    public static final String KEY_CURRENT_ADDRESS = "address";

    public static final String KEY_CURRENT_CITY = "city";

    public static final String KEY_CURRENT_STATE = "state";

    public static final String KEY_CURRENT_COUNTRY = "country";

    public static final String KEY_CURRENT_ZIPCODE = "zipcode";

    //public static final String KEY_FOOD_LIST = "food_list";

    // Constructor
    public PreferenceManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String uid,int login_id,String profile_status){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //editor.putString(KEY_FNAME, fname);

        editor.putString(KEY_UID, uid);
        editor.putInt(KEY_LOGIN_ID, login_id);
        editor.putString(KEY_PROFILE_STATUS, profile_status);

//        editor.putString(KEY_MOBILE, mobile);
//        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    public void setUserDetails(UserBean userBean){

        editor.putString(KEY_FNAME, userBean.getFname());

        editor.putString(KEY_LNAME, userBean.getLname());

        editor.putString(KEY_UID, userBean.getUid());

        //editor.putBoolean(KEY_MOBILE_VERIFICATION, userBean.getMobile_verified());

        editor.putString(KEY_MOBILE, userBean.getPhone_number());

        editor.putString(KEY_EMAIL, userBean.getEmail());

        editor.commit();

    }



    public void setMobile_verified(int mobile_verified){

        editor.putInt(KEY_MOBILE_VERIFICATION, mobile_verified);

        editor.commit();

    }

    public int getMobile_verified(){

        return pref.getInt(KEY_MOBILE_VERIFICATION,0);

    }

    public UserBean getUserDetails(){

        UserBean userBean = new UserBean();
        HashMap<String, String> user = new HashMap<String, String>();

        userBean.setFname(pref.getString(KEY_FNAME, null));

        userBean.setLname(pref.getString(KEY_LNAME, null));

        userBean.setUid(pref.getString(KEY_UID, null));

        userBean.setPhone_number(pref.getString(KEY_MOBILE, null));

        userBean.setMobile_verified(pref.getInt(KEY_MOBILE_VERIFICATION, 0));

        userBean.setEmail(pref.getString(KEY_EMAIL, null));

        return userBean;
    }


    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    public void setLocation(String loc){

        editor.putString(KEY_CURRENT_ADDRESS, loc);

        editor.commit();

    }

    public String getLocation(){

        return pref.getString(KEY_CURRENT_ADDRESS,null);

    }

    public void setLongitude(String lon){

        editor.putString(KEY_CURRENT_LONGITUDE, lon);

        editor.commit();

    }

    public String getLongitude(){

        return pref.getString(KEY_CURRENT_LONGITUDE,null);

    }


    public void setLatitude(String lat){

        editor.putString(KEY_CURRENT_LATITUDE, lat);

        editor.commit();

    }

    public String getLatitude(){

        return pref.getString(KEY_CURRENT_LATITUDE,null);

    }

    public void setLocationDetails(String city,String state,String country,String zipcode){

        editor.putString(KEY_CURRENT_CITY, city);
        editor.putString(KEY_CURRENT_STATE, state);
        editor.putString(KEY_CURRENT_COUNTRY, country);
        editor.putString(KEY_CURRENT_ZIPCODE, zipcode);

        editor.commit();

    }

    public HashMap<String, String> getLocationDetails(){


        HashMap<String, String> location = new HashMap<String, String>();

        location.put("city",pref.getString(KEY_CURRENT_CITY, null));
        location.put("state",pref.getString(KEY_CURRENT_STATE, null));
        location.put("country",pref.getString(KEY_CURRENT_COUNTRY, null));
        location.put("zipcode",pref.getString(KEY_CURRENT_ZIPCODE, null));

        return location;
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public int getLoginID(){

        return pref.getInt("login_id",0);

    }
    public void setUID(String uid){

        editor.putString(KEY_UID, uid);

        editor.commit();

    }

    public String getUID(){

        return pref.getString("uid",null);

    }

    public void setRestaurantName(String restaurantName){

        editor.putString(KEY_RESTAURANT_NAME, restaurantName);

        editor.commit();

    }

    public String getRestaurantName(){

        return pref.getString(KEY_RESTAURANT_NAME,null);

    }

    public void setRestaurantID(String restaurantID){

        editor.putString(KEY_RESTAURANT_ID, restaurantID);

        editor.commit();

    }

    public String getRestaurantID(){

        return pref.getString(KEY_RESTAURANT_ID,null);

    }

    public void setRestaurantImage(String restaurantImage){

        editor.putString(KEY_RESTAURANT_IMAGE, restaurantImage);

        editor.commit();

    }

    public String getRestaurantImage(){

        return pref.getString(KEY_RESTAURANT_IMAGE,null);

    }

    public void setRestaurantAddress(String restaurantAddress){

        editor.putString("restaurant_address", restaurantAddress);

        editor.commit();

    }

    public String getRestaurantAddress(){

        return pref.getString("restaurant_address",null);

    }

    public void setProfileStatus(String profile_status){

        editor.putString(KEY_PROFILE_STATUS, profile_status);

        editor.commit();

    }

    public String getProfileStatus(){

        return pref.getString("profile_status",null);

    }

    public void setOrderFood(OrderFoodBean orderBean){

        editor.putString("food_id", orderBean.getFoodId());

        editor.putString("food_name", orderBean.getFoodName());

        editor.putInt("quantity", orderBean.getQuantity());

        editor.putInt("price", orderBean.getTotalPrice());

        editor.commit();

    }

    public OrderFoodBean getOrderFood(){

        OrderFoodBean ord = new OrderFoodBean();

        ord.setFoodId(pref.getString("food_id",null));
        ord.setFoodName(pref.getString("food_name",null));
        ord.setQuantity(pref.getInt("quantity",0));
        ord.setTotalPrice(pref.getInt("price",0));

        return ord;
    }


    public void setOrderFoodList(ArrayList<OrderFoodBean> list){

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_ORDER_FOOD_LIST, json);
        editor.apply();     // This line is IMPORTANT !!!

    }

    public ArrayList<OrderFoodBean> getOrderFoodList(){

        Gson gson = new Gson();
        String json = pref.getString(KEY_ORDER_FOOD_LIST, null);
        Type type = new TypeToken<ArrayList<OrderFoodBean>>() {}.getType();
        return gson.fromJson(json, type);

    }



}
