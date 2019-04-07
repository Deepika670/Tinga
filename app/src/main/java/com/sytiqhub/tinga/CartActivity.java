package com.sytiqhub.tinga;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.sytiqhub.tinga.activities.OrderStatusActivity;
import com.sytiqhub.tinga.adapters.CartItemsAdapter;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.beans.TrackingBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity{

    RecyclerView recycler_items;
    TextView txt_restaurant_name,txt_restaurant_address;
    ImageView img_restaurant_image;
    RadioGroup radioGroup;
    EditText et_address;
    TextView delivery_lat_long,cart_total_price;
    LinearLayout ll_select_location;
    CartItemsAdapter cartItemsAdapter;
    Button btn_placeorder,btn_discard;
    List<OrderFoodBean> list = new ArrayList<>();
    ProgressDialog progressBar;
    String uid;
    RadioButton radioButton;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHandler(CartActivity.this);
        final PreferenceManager prefs = new PreferenceManager(CartActivity.this);
        final TingaManager tinga = new TingaManager();
        recycler_items = findViewById(R.id.cart_items);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        txt_restaurant_name = findViewById(R.id.restaurant_name);
        txt_restaurant_address = findViewById(R.id.restaurant_address);
        img_restaurant_image = findViewById(R.id.restaurant_image);
        radioGroup = findViewById(R.id.payment_group);
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Placing order, Please wait...");
        progressBar.setCancelable(false);



        cart_total_price = findViewById(R.id.cart_total_price);

        et_address = findViewById(R.id.cart_address);

        btn_placeorder = findViewById(R.id.btn_place_order);
        btn_discard = findViewById(R.id.btn_discard_cart);
        //delivery_lat_long = findViewById(R.id.tv_delivery_location);
        //ll_select_location = findViewById(R.id.select_delivery_location);

        cartItemsAdapter = new CartItemsAdapter(db.getAllContent());
        recycler_items.setLayoutManager(new LinearLayoutManager(this));
        recycler_items.setAdapter(cartItemsAdapter);

        cart_total_price.setText(db.getTotalPrice()+"/-");

        txt_restaurant_name.setText(prefs.getRestaurantName());
        txt_restaurant_address.setText(prefs.getRestaurantAddress());

//      Picasso.get().load(prefs.getRestaurantImage()).into(img_restaurant_image);

        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                
                if(et_address.getText().toString().length()<=0){
                    et_address.setError("Please enter delivery address");
                }else if(selectedId == 0){
                    Toast.makeText(CartActivity.this, "Please select payment mode...", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.show();
                    Date date = new Date();
                    String strDateFormat = "yyyy-MM-dd";
                    DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.ENGLISH);
                    String formattedDate= dateFormat.format(date);
                    String address = et_address.getText().toString();
                    radioButton = findViewById(selectedId);

                    final OrderBean orderBean = new OrderBean();
                    orderBean.setRestaurant_id(prefs.getRestaurantID());
                    orderBean.setRestaurant_name(prefs.getRestaurantName());
                    orderBean.setRestaurant_address(prefs.getRestaurantAddress());
                    orderBean.setOrder_date(formattedDate);
                    orderBean.setTotal_price(String.valueOf(db.getTotalPrice()));
                    orderBean.setAddress(address);
                    orderBean.setPayment_mode("Cash On Delivery");
                    orderBean.setLat("");
                    orderBean.setLon("");
                    orderBean.setStatus("Pending");

                    List<OrderFoodBean> orderFoodBeans = db.getAllContent();
                    String food_items = new Gson().toJson(orderFoodBeans);
                    Log.v("food_items json",food_items);
                    Log.v("order bean",orderBean.toString());
                    Log.v("uid",uid);

                    tinga.CheckItemStatusAndPlaceOrder(CartActivity.this, uid, orderBean, food_items, new TingaManager.PlaceOrderCallBack() {
                        @Override
                        public void onSuccess(final int orderid) {

                            /*DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(String.valueOf(orderid));

                            TrackingBean bean = new TrackingBean();
                            bean.setOrderid(String.valueOf(orderid));
                            bean.setDelivery("Pending");
                            bean.setDelivery_name("None");
                            bean.setFood_status("Pending");
                            bean.setRestaurant("Pending");
                            bean.setStatus("Pending");

                            mdatabase.setValue(bean);*/

                            /*
                            mdatabase.child("orderid").setValue(orderid);
                            mdatabase.child("status").setValue("Pending");
                            mdatabase.child("restaurant").setValue("Pending");
                            mdatabase.child("food_status").setValue("Pending");
                            mdatabase.child("delivery").setValue("Pending");
                            mdatabase.child("delivery_name").setValue("None");

*/

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                                    if(progressBar.isShowing()){
                                        progressBar.dismiss();
                                    }

                                    db.reset();

                                    Intent i = new Intent(CartActivity.this, OrderStatusActivity.class);
                                    i.putExtra("order_id",orderid);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                            }, 1500);

                        }

                        @Override
                        public void onFail(String msg) {
                            Log.d("CheckItem error",msg);
                            if(progressBar.isShowing()){
                                progressBar.dismiss();
                            }
                            Toast.makeText(CartActivity.this, "Unable to place order. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.d("akhillll", orderBean.getOrder_date());
                }
            }
        });


        btn_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
                alert.setTitle("Discard")
                        .setMessage("Do you want to discard cart?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.reset();
                                Intent i = new Intent(CartActivity.this,HomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alert.show();

            }
        });

    }


}
