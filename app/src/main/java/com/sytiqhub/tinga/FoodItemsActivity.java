package com.sytiqhub.tinga;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.adapters.FoodGridAdapter;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.ArrayList;
import java.util.List;

public class FoodItemsActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    ImageView action_image;
    RecyclerView veg,nonveg,egg;
    FoodGridAdapter adapter,adapter2,adapter3;
    public List<FoodBean> ITEMS = new ArrayList<FoodBean>();
    LinearLayout veglayout,nonveglayout,egglayout,fulllayout;
    TextView text_err;
    SwipeRefreshLayout pullToRefresh;
    String r_id,r_name;
    DatabaseHandler db;
   // Button go_to_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        db = new DatabaseHandler(this);
        PreferenceManager prefs = new PreferenceManager(FoodItemsActivity.this);
        action_image = findViewById(R.id.actionbar_image);
        String r_image = prefs.getRestaurantImage();
        Picasso.get().load(r_image).into(action_image);

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh_fooditems);

        r_id = prefs.getRestaurantID();
        r_name = prefs.getRestaurantName();

        //recommended = findViewById(R.id.recycler_recommended);
        veg = findViewById(R.id.recycler_veg);
        veg.setNestedScrollingEnabled(false);
        nonveg = findViewById(R.id.recycler_nonveg);
        nonveg.setNestedScrollingEnabled(false);
        egg = findViewById(R.id.recycler_egg);
        text_err = findViewById(R.id.full_error);

        veglayout = findViewById(R.id.layout_veg);
        nonveglayout = findViewById(R.id.layout_nonveg);
        egglayout = findViewById(R.id.layout_egg);

        db.reset();
        addContent(1);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addContent(2);
            }
        });

        Log.d("akhillll",r_id);

        getSupportActionBar().setTitle(r_name);

    }

    public void addContent(int i){

        TingaManager tingaManager = new TingaManager();

        tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "ALL", i, new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {

                ITEMS = detailsMovies;

                if(ITEMS.size()<=0){
                    //fulllayout.setVisibility(View.VISIBLE);

                    veglayout.setVisibility(View.GONE);
                    nonveglayout.setVisibility(View.GONE);
                    egglayout.setVisibility(View.GONE);
                    // go_to_cart.setVisibility(View.GONE);

                    text_err.setVisibility(View.VISIBLE);

                }else{
                    //fulllayout.setVisibility(View.VISIBLE);

                    veglayout.setVisibility(View.VISIBLE);
                    nonveglayout.setVisibility(View.VISIBLE);
                    egglayout.setVisibility(View.VISIBLE);
                    //  go_to_cart.setVisibility(View.VISIBLE);
                    text_err.setVisibility(View.GONE);

                    List<FoodBean> veglist = new ArrayList<>();
                    List<FoodBean> nonveglist = new ArrayList<>();
                    List<FoodBean> egglist = new ArrayList<>();
                    Log.d("list",String.valueOf(ITEMS.size()));

                    for(int i = 0;i<ITEMS.size();i++){
                        if(ITEMS.get(i).getTypetag().equalsIgnoreCase("VEG")){
                            veglist.add(ITEMS.get(i));
                        }else if(ITEMS.get(i).getTypetag().equalsIgnoreCase("NON-VEG")){
                            nonveglist.add(ITEMS.get(i));
                        }else{
                            egglist.add(ITEMS.get(i));
                        }
                    }

                    Log.d("veg list",String.valueOf(veglist.size()));
                    Log.d("nonveg list",String.valueOf(nonveglist.size()));
                    Log.d("egg list",String.valueOf(egglist.size()));

                    if(veglist.size()<=0){
                        veglayout.setVisibility(View.GONE);
                    }else{
                        veglayout.setVisibility(View.VISIBLE);
                        //veg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this,3,GridLayoutManager.HORIZONTAL,false));
                        LinearLayoutManager llm = new LinearLayoutManager(FoodItemsActivity.this);
                        //llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        llm.setSmoothScrollbarEnabled(true);
                        veg.setLayoutManager(llm);

                        // veg.setLayoutManager(new LinearLayoutManager(FoodItemsActivity.this));
                        adapter = new FoodGridAdapter(getApplicationContext(),veglist, new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(FoodBean item) {
                                Log.d("f_price",item.getPrice());
                                Intent i = new Intent(FoodItemsActivity.this,FoodDescriptionActivity.class);
                                i.putExtra("f_id",item.getId());
                                i.putExtra("f_name",item.getName());
                                i.putExtra("f_desc",item.getDesc());
                                i.putExtra("f_price",item.getPrice());
                                i.putExtra("f_status",item.getStatus());
                                i.putExtra("f_image",item.getImage_path());
                                startActivity(i);
                            }
                        });
                        veg.setAdapter(adapter);
                    }

                    if(nonveglist.size()<=0){
                        nonveglayout.setVisibility(View.GONE);
                    }else{
                        nonveglayout.setVisibility(View.VISIBLE);
                        LinearLayoutManager llm = new LinearLayoutManager(FoodItemsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        llm.setSmoothScrollbarEnabled(true);
                        nonveg.setLayoutManager(llm);
                        //nonveg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this,3,GridLayoutManager.HORIZONTAL, false));
                        adapter2 = new FoodGridAdapter(getApplicationContext(),nonveglist, new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(FoodBean item) {
                                Log.d("f_price",item.getPrice());
                                Intent i = new Intent(FoodItemsActivity.this,FoodDescriptionActivity.class);
                                i.putExtra("f_id",item.getId());
                                i.putExtra("f_name",item.getName());
                                i.putExtra("f_desc",item.getDesc());
                                i.putExtra("f_price",item.getPrice());
                                i.putExtra("f_status",item.getStatus());
                                i.putExtra("f_image",item.getImage_path());
                                startActivity(i);
                            }
                        });
                        nonveg.setAdapter(adapter2);
                    }

                    if(egglist.size()<=0){
                        egglayout.setVisibility(View.GONE);
                    }else{
                        egglayout.setVisibility(View.VISIBLE);
                        //egg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this,3,GridLayoutManager.HORIZONTAL,false));
                        LinearLayoutManager llm = new LinearLayoutManager(FoodItemsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        llm.setSmoothScrollbarEnabled(true);
                        egg.setLayoutManager(llm);
                        //egg.setLayoutManager(new LinearLayoutManager(FoodItemsActivity.this));
                        adapter3 = new FoodGridAdapter(getApplicationContext(),egglist, new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(FoodBean item) {
                                Log.d("f_price",item.getPrice());
                                Intent i = new Intent(FoodItemsActivity.this,FoodDescriptionActivity.class);
                                i.putExtra("f_id",item.getId());
                                i.putExtra("f_name",item.getName());
                                i.putExtra("f_desc",item.getDesc());
                                i.putExtra("f_price",item.getPrice());
                                i.putExtra("f_status",item.getStatus());
                                i.putExtra("f_image",item.getImage_path());
                                startActivity(i);
                            }
                        });
                        egg.setAdapter(adapter3);
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }

        });
        if (pullToRefresh.isRefreshing()) {
            pullToRefresh.setRefreshing(false);
        }

    }


    @Override
    public void onListFragmentInteraction(FoodBean item) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_food_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        DatabaseHandler db = new DatabaseHandler(this);
        switch (item.getItemId())
        {
            case R.id.action_cart:
                if(db.getOrderedFoodCount()<1){
                    Toast.makeText(this, "Please add items to cart...", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(FoodItemsActivity.this,CartActivity.class);
                    startActivity(i);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        final DatabaseHandler db = new DatabaseHandler(FoodItemsActivity.this);
        if(db.getOrderedFoodCount()<1){
            super.onBackPressed();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Discard")
                    .setMessage("Do you want to discard cart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            db.reset();
                            Intent i = new Intent(FoodItemsActivity.this,HomeActivity.class);
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

    }

}
