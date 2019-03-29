package com.sytiqhub.tinga;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

public class FoodDescriptionActivity extends AppCompatActivity {

    ImageView food_image;
    TextView tv_title,tv_desc,tv_price,tv_count;
    ImageButton ib_add,ib_remove;
    public static int count;
    //Button go_to_cart;
    FloatingActionButton add_to_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //toolbar.setTitleTextColor(Color.parseColor("#000000"));
        //getSupportActionBar().setTitle("");

        food_image = findViewById(R.id.food_image);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
//        tv_count = findViewById(R.id.tv_count);
        tv_price = findViewById(R.id.tv_price);
/*
        ib_add  = findViewById(R.id.ib_add);
        ib_remove = findViewById(R.id.ib_remove);
*/

  //      add_to_cart = findViewById(R.id.fab_add_cart);


      //  go_to_cart = findViewById(R.id.btn_go_to_cart);

       /* go_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(FoodDescriptionActivity.this,CartActivity.class);
                startActivity(i);

            }
        });
*/
  /*      count = 0;
        tv_count.setText(String.valueOf(count));
*/
        String f_image = getIntent().getExtras().getString("f_image");
        Picasso.get().load(f_image).into(food_image);

        final String f_id = getIntent().getExtras().getString("f_id");
        final String f_name = getIntent().getExtras().getString("f_name");
        String f_desc = getIntent().getExtras().getString("f_desc");
        final String f_price = getIntent().getExtras().getString("f_price");
        Log.d("f_desc",f_desc);
        Log.d("f_price",f_price);
        Log.d("f_name",f_name);

        tv_title.setText(f_name);
        tv_desc.setText(f_desc);
        tv_price.setText("Rs. "+f_price+"/-");

  /*      ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==6){
                    Snackbar.make((findViewById(R.id.layout)),"Max quantity is 6...",Snackbar.LENGTH_SHORT).show();
                }else{
                    count++;
                    tv_count.setText(String.valueOf(count));
                    Log.d("akhilll add",String.valueOf(count));
                }
            }
        });

        ib_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(count<=0)){
                    count--;
                }
                tv_count.setText(String.valueOf(count));
                Log.d("akhilll remove",String.valueOf(count));
            }
        });
*/
        final PreferenceManager prefs = new PreferenceManager(this);

       /* add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String count = tv_count.getText().toString();
                if(Integer.parseInt(count) <= 0){
                    Snackbar.make((findViewById(R.id.layout)),"Please select the quantity...",Snackbar.LENGTH_SHORT).show();
                }else{

                    DatabaseHandler db = new DatabaseHandler(FoodDescriptionActivity.this);
                    OrderFoodBean orderFoodBean = new OrderFoodBean();
                    orderFoodBean.setFoodId(f_id);
                    orderFoodBean.setQuantity(Integer.parseInt(count));
                    orderFoodBean.setTotalPrice(Integer.parseInt(count)*Integer.parseInt(f_price));
                    orderFoodBean.setFoodName(tv_title.getText().toString());
                    Log.d("akhilllll foodname",tv_title.getText().toString());
                    Log.d("akhilllll foodname",f_name);

                    prefs.setOrderFood(orderFoodBean);

                    db.addOrderFood(orderFoodBean);

                    Snackbar.make((findViewById(R.id.layout)),"Item added to cart...",Snackbar.LENGTH_SHORT).show();


                }
            }
        });*/
    }
}