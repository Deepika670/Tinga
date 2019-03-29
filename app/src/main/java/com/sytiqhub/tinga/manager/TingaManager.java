package com.sytiqhub.tinga.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.sytiqhub.tinga.AppController;
import com.sytiqhub.tinga.HomeActivity;
import com.sytiqhub.tinga.auth.ProfileFillActivity;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.RestaurantBean;
import com.sytiqhub.tinga.beans.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class TingaManager {

    private ProgressDialog pDialog;
    FirebaseAuth mAuth;
    PreferenceManager prefs;
    public TingaManager(){

    }

    private int success_code;
    private HashMap<String, String> response_log;
    public void checkLogin(final Activity activity, final String uid) {

        String tag_string_req = "req_login";
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Checking details...");
        //pDialog.setCancelable(false);
        showDialog();
        prefs = new PreferenceManager(activity);

        response_log = new HashMap<String, String>();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHECK_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                Log.d("Akhilllll", "Login Response: " + response);


                try {

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObj = jArray.getJSONObject(i);

                        success_code = jObj.getInt("success");
                        Log.d("Akhilllll", "Login Response success: " + success_code);

                        if (success_code==1) {
                            pDialog.setMessage("Signing in ...");
                            String uid = jObj.getString("uid");
                            String message = jObj.getString("message");
                            String fname = jObj.getString("fname");
                            String lname = jObj.getString("lname");
                            String phone_number = jObj.getString("phone_number");
                            int mobile_verified = jObj.getInt("mobile_verified");
                            String email = jObj.getString("email");
                            int login_id = jObj.getInt("login_id");
                            UserBean userbean = new UserBean();
                            userbean.setUid(uid);
                            userbean.setFname(fname);
                            userbean.setLname(lname);
                            userbean.setPhone_number(phone_number);
                            userbean.setEmail(email);
                            userbean.setMobile_verified(mobile_verified);

                            prefs.createLoginSession(uid,login_id,"full");
                            prefs.setUserDetails(userbean);
                            Intent intent = new Intent(activity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                            hideDialog();
                            activity.finish();

                        }else if(success_code==2){
                            pDialog.setMessage("Signing in ...");
                            String uid = jObj.getString("uid");
                            String message = jObj.getString("message");
                            int login_id = jObj.getInt("login_id");

                            prefs.createLoginSession(uid,login_id,"half");

                            Intent intent = new Intent(activity,ProfileFillActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("type","login");
                            intent.putExtra("uid",uid);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                            Toast.makeText(activity,
                                    "Please fill the details to complete login", Toast.LENGTH_LONG).show();
                            hideDialog();
                            activity.finish();

                        }else{

                            String uid = jObj.getString("uid");
                            String message = jObj.getString("message");

                            SignInUser(activity,uid);

                        }

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());

                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void SignInUser(final Activity activity, final String uid) {

        String tag_string_req = "signin_user";
        pDialog = new ProgressDialog(activity);
       // pDialog.setCancelable(false);
        pDialog.setMessage("Signing up, Please wait ...");
        showDialog();
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");

                        if (success_code == 1) {

                            String uid = jObj.getString("uid");
                            String message = jObj.getString("message");
                            int login_id = jObj.getInt("login_id");

                            prefs.createLoginSession(uid,login_id,"half");

                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(activity,ProfileFillActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("uid",uid);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                            activity.finish();

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }


    public void UpdateProfile(final Activity activity, final UserBean userBean) {

        Log.d("uid tingamanager: ",userBean.getUid());
        String tag_string_req = "update_profile";
        pDialog = new ProgressDialog(activity);
        //pDialog.setCancelable(false);
        pDialog.setMessage("Updating details, Please wait ...");
        showDialog();
        prefs = new PreferenceManager(activity);
        response_log = new HashMap<String, String>();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Updating Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));

                        if (success_code == 1) {

                            String uid = jObj.getString("uid");
                            String message = jObj.getString("message");

                            prefs.setProfileStatus("full");
                            prefs.setUserDetails(userBean);
                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(activity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("uid",uid);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                            activity.finish();

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message + " Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userBean.getUid());
                params.put("fname", userBean.getFname());
                params.put("lname", userBean.getLname());
                params.put("phone_number", userBean.getPhone_number());
                params.put("mobile_verified", String.valueOf(userBean.getMobile_verified()));
                params.put("email", userBean.getEmail());
                return params;
            }

        };

        Log.e("Akhilllll String req:", strReq.toString());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return response_log;
    }

    public void Logout(final Activity activity, final int id,final String provider) {

        mAuth = FirebaseAuth.getInstance();
        prefs = new PreferenceManager(activity);
        String tag_string_req = "logout";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging out, Please wait ...");
        showDialog();
        response_log = new HashMap<String, String>();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOG_OUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "logout Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));
                        if (success_code == 1) {

                            String message = jObj.getString("message");
                            if(provider.contains("facebook.com")){
                                mAuth.signOut();
                                LoginManager.getInstance().logOut();
                                prefs.logoutUser();
                            }else if(provider.contains("google.com")){
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(activity.getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
                                mGoogleSignInClient.signOut();
                                mAuth.signOut();
                                prefs.logoutUser();
                            }else{
                                FirebaseAuth.getInstance().signOut();
                                prefs.logoutUser();
                            }

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message + " Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private List<RestaurantBean> beans;
    public void getAllRestaurants(final Activity activity, final int i, final RestaurantCallBack onCallBack) {

        beans = new ArrayList<>();

        String tag_string_req = "getAllRestaurants";
        if(i==1){
            pDialog = new ProgressDialog(activity);
            pDialog.setCancelable(false);
            pDialog.setMessage("Getting all Restaurants, Please wait ...");
            showDialog();
        }
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ALL_RESTAURANTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Restaurants Response: " + response);
                if(i==1){
                    hideDialog();
                }

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success_code : ",String.valueOf(success_code));


                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            Log.d("data_array : ",data_array.toString());

                            String uid = data_array.getString("restaurant_id");
                            String name = data_array.getString("name");
                            String address = data_array.getString("address");
                            String city = data_array.getString("city");
                            String state = data_array.getString("state");
                            String country = data_array.getString("country");
                            String location_lat = data_array.getString("location_lat");
                            String location_long = data_array.getString("location_long");
                            String cuisine = data_array.getString("cuisine");
                            String timings = data_array.getString("timings");
                            String rating;

                            if(data_array.getString("rating").equals("") || data_array.getString("rating").equals(null)){
                                rating = "New";
                            }else{
                                rating = data_array.getString("rating");
                            }

                            String status = data_array.getString("status");
                            String image_path = data_array.getString("image_path");

                            Log.d("imagepath: ",image_path);


                            Log.d("rating: ",rating);
                            Log.d("status: ",status);
                            //int login_id = jObj.getInt("login_id");
                            Log.d("uid: ",uid);

                            RestaurantBean bean = new RestaurantBean();
                            bean.setId(uid);
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setCity(city);
                            bean.setState(state);
                            bean.setCountry(country);
                            bean.setLatitude(location_lat);
                            bean.setLongitude(location_long);
                            bean.setCuisine(cuisine);
                            bean.setTimings(timings);
                            bean.setRating(rating);
                            bean.setStatus(status);
                            bean.setImage_path(image_path);
                            beans.add(bean);

                            Log.d("Items: ",beans.toString());
                            //prefs.createLoginSession(uid,login_id,"half");

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(beans);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                //params.put("uid", uid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        Log.d("Items1: ",beans.get(0).getId());

    }

    public void getNearByRestaurants(final Activity activity, final String latitude, final String longitude, final RestaurantCallBack onCallBack) {

        beans = new ArrayList<>();

        String tag_string_req = "getNearByRestaurants";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        pDialog.setMessage("Getting Near By Restaurants, Please wait ...");
        showDialog();
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ALL_RESTAURANTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Restaurants Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success_code : ",String.valueOf(success_code));


                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            Log.d("data_array : ",data_array.toString());

                            String uid = data_array.getString("restaurant_id");
                            String name = data_array.getString("name");
                            String address = data_array.getString("address");
                            String city = data_array.getString("city");
                            String state = data_array.getString("state");
                            String country = data_array.getString("country");
                            String location_lat = data_array.getString("location_lat");
                            String location_long = data_array.getString("location_long");
                            String cuisine = data_array.getString("cuisine");
                            String timings = data_array.getString("timings");
                            String rating;

                            if(data_array.getString("rating").equals("") || data_array.getString("rating").equals(null)){
                                rating = "New";
                            }else{
                                rating = data_array.getString("rating");
                            }

                            String status = data_array.getString("status");
                            String image_path = data_array.getString("image_path");

                            Log.d("imagepath: ",image_path);

                            Log.d("rating: ",rating);
                            Log.d("status: ",status);
                            //int login_id = jObj.getInt("login_id");
                            Log.d("uid: ",uid);

                            RestaurantBean bean = new RestaurantBean();
                            bean.setId(uid);
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setCity(city);
                            bean.setState(state);
                            bean.setCountry(country);
                            bean.setLatitude(location_lat);
                            bean.setLongitude(location_long);
                            bean.setCuisine(cuisine);
                            bean.setTimings(timings);
                            bean.setRating(rating);
                            bean.setStatus(status);
                            bean.setImage_path(image_path);

                            beans.add(bean);

                            Log.d("Items: ",beans.toString());
                            //prefs.createLoginSession(uid,login_id,"half");

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(beans);


                } catch (JSONException e) {
                    // JSON error
                    hideDialog();
                    e.printStackTrace();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", latitude);
                params.put("long", longitude);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        Log.d("Items1: ",beans.get(0).getId());

    }

    private List<FoodBean> fbeans;
    public void getFoodItems(final Activity activity, final String rid, final String keyword, final FoodCallBack onCallBack) {

        fbeans = new ArrayList<>();
        String tag_string_req = "getFoodItems";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(true);
        pDialog.setMessage("Getting Food Items, Please wait ...");
        showDialog();
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_FOOD_ITEMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Food Response: " + response);
                hideDialog();
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            Log.d("data_array : ",data_array.toString());

                            String fid = data_array.getString("food_id");
                            String name = data_array.getString("name");
                            String price = data_array.getString("price");
                            String desc = data_array.getString("desc");
                            String typetag = data_array.getString("typetag");
                            String status = data_array.getString("status");
                            String recommended = data_array.getString("recommended");
                            String image_path = data_array.getString("image_path");

                            Log.d("imagepath: ",image_path);

                            Log.d("uid: ",fid);

                            FoodBean bean = new FoodBean();
                            bean.setId(fid);
                            bean.setName(name);
                            bean.setPrice(price);
                            bean.setDesc(desc);
                            bean.setTypetag(typetag);
                            bean.setStatus(status);
                            bean.setRecommended(recommended);
                            bean.setImage_path(image_path);

                            fbeans.add(bean);
                            hideDialog();
                            Log.d("Items: ",fbeans.toString());
                            //prefs.createLoginSession(uid,login_id,"half");

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");
                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(fbeans);


                } catch (JSONException e) {
                    // JSON error
                    hideDialog();
                    e.printStackTrace();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("r_id", rid);
                params.put("keyword", keyword);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void checkMobile(final Activity activity, final String mobile, final MobileCallBack onCallBack) {

        fbeans = new ArrayList<>();
        String tag_string_req = "checkmobileno";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(true);
        pDialog.setMessage("Checking Mobile number, Please wait ...");
        showDialog();
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHECK_MOBILE_NO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Food Response: " + response);
                hideDialog();
                pDialog.dismiss();
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");
                    int verification = 0;
                    String uid = "";
                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        success_code = jObj.getInt("success");
                        verification = jObj.getInt("verification");
                        uid = jObj.getString("uid");
                        Log.d("success_code : ",String.valueOf(success_code));
                    }

                    onCallBack.onSuccess(success_code,verification,uid);

                } catch (JSONException e) {
                    // JSON error
                    hideDialog();
                    e.printStackTrace();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                //params.put("r_id", rid);
                params.put("mobile", mobile);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void Logout1(final Activity activity, final int id,final String provider) {

        mAuth = FirebaseAuth.getInstance();
        prefs = new PreferenceManager(activity);
        String tag_string_req = "logout";
        response_log = new HashMap<String, String>();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOG_OUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "logout Response: " + response.toString());

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success code: ",String.valueOf(success_code));
                        if (success_code == 1) {

                            String message = jObj.getString("message");
                            if(provider.contains("facebook.com")){
                                mAuth.signOut();
                                LoginManager.getInstance().logOut();
                                prefs.logoutUser();
                            }else if(provider.contains("google.com")){
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(activity.getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
                                mGoogleSignInClient.signOut();
                                mAuth.signOut();
                                prefs.logoutUser();
                            }else{
                                FirebaseAuth.getInstance().signOut();
                                prefs.logoutUser();
                            }

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message + " Try again later.", Toast.LENGTH_LONG).show();

                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private List<OrderBean> orderbean;
    public void getAllOrders(final Activity activity, final String uid, final OrdersCallBack onCallBack) {

        orderbean = new ArrayList<>();

        String tag_string_req = "getorders";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        pDialog.setMessage("Getting all orders, Please wait ...");
        showDialog();
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ALL_ORDERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "orders Response: " + response);
                hideDialog();

                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            JSONObject arr = jObj.getJSONObject(String.valueOf(0));
                            JSONObject data_array = arr.getJSONObject("data");
                            Log.d("data_array : ",data_array.toString());

                            String order_id = data_array.getString("order_id");
                            String restaurant_name = data_array.getString("restaurant_name");
                            String restaurant_address = data_array.getString("restaurant_address");
                            String restaurant_city = data_array.getString("restaurant_city");
                            String order_status = data_array.getString("order_status");
                            String total_price = data_array.getString("total_price");
                            String order_date = data_array.getString("order_date");

                            String address = restaurant_address +", "+restaurant_city;

                            OrderBean bean = new OrderBean();
                            bean.setOrder_id(order_id);
                            bean.setRestaurant_name(restaurant_name);
                            bean.setRestaurant_address(address);
                            bean.setStatus(order_status);
                            bean.setTotal_price(total_price);
                            bean.setOrder_date(order_date);
                            orderbean.add(bean);

                            //prefs.createLoginSession(uid,login_id,"half");

                        } else if (success_code == 0) {

                            String message = jObj.getString("message");

                            Toast.makeText(activity,
                                    message+" Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(orderbean);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    //private List<OrderBean> orderbean;
    public void CheckItemStatusAndPlaceOrder(final Activity activity, final String uid, final OrderBean orderBean, final String items, final PlaceOrderCallBack onCallBack) {

        orderbean = new ArrayList<>();

        String tag_string_req = "checkitemstatus";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        pDialog.setMessage("Placing Order, Please wait ...");
        showDialog();
        response_log = new HashMap<>();
        prefs = new PreferenceManager(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHECK_ITEM_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "items status Response: " + response);
                hideDialog();
                int order_id = 0;
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    JSONArray jArray = jsonObj.getJSONArray("response");

                    Log.d("array length: ",String.valueOf(jArray.length()));
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        int success_code = jObj.getInt("success");
                        Log.d("success_code : ",String.valueOf(success_code));

                        if (success_code == 1) {

                            order_id = jObj.getInt("order_id");
                            String message = jObj.getString("message");



                        } else if (success_code == 0) {

                            String message = jObj.getString("message");
                            Log.d("order placing error",message);
                            onCallBack.onFail(message);
                            Toast.makeText(activity,
                                    "Unable to place order now. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }

                    onCallBack.onSuccess(order_id);
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    hideDialog();
                    //Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Akhilllll onErrorResp:", "Exception: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Akhilllll onErrorResp:", "Login Check Error: " + error.getMessage());
                //Toast.makeText(activity,error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("items", items);
                params.put("total_price", orderBean.getTotal_price());
                params.put("order_date", orderBean.getOrder_date());
                params.put("customer_id", uid);
                params.put("restaurant_id", orderBean.getRestaurant_id());
                params.put("order_address", orderBean.getAddress());
                params.put("order_payment_mode", orderBean.getPayment_mode());
                //params.put("order_lat", orderBean.getLat());
                //params.put("order_lon", orderBean.getLon());
                params.put("order_status", orderBean.getStatus());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public interface OrdersCallBack {
        void onSuccess(List<OrderBean> detailsMovies);

        void onFail(String msg);
    }

    public interface PlaceOrderCallBack {
        void onSuccess(int orderid);

        void onFail(String msg);
    }

    public interface RestaurantCallBack {
        void onSuccess(List<RestaurantBean> detailsMovies);

        void onFail(String msg);
    }

    public interface FoodCallBack {
        void onSuccess(List<FoodBean> detailsMovies);

        void onFail(String msg);
    }

    public interface MobileCallBack {
        void onSuccess(int successCode,int verification,String uid);

        void onFail(String msg);
    }




}
