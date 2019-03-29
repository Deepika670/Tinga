package com.sytiqhub.tinga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.adapters.FoodGridAdapter;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ROugh {

/*
    (findViewById(R.id.sign_out)).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("Provider List: ",user.getProviders().get(0));
            String provider = user.getProviders().get(0);
            tingaManager.Logout(HomeActivity1.this,prefs.getLoginID(),provider);

        }
    });
*/
/* go_to_cart = findViewById(R.id.btn_go_to_cart);

        go_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(FoodItemsActivity.this,CartActivity.class);
                startActivity(i);

            }
        });*/
    //   fulllayout = findViewById(R.id.layout_full);


       /* tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "VEG", new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {

                ITEMS = detailsMovies;
                //Log.d("ID: ",ITEMS.get(0).getId());
                veg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this, 2));
                adapter = new FoodGridAdapter(ITEMS, new OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(FoodBean item) {

                    }
                });
                veg.setAdapter(adapter);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });

        tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "NON-VEG", new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {
                ITEMS = detailsMovies;
                //Log.d("ID: ",ITEMS.get(0).getId());
                adapter = new FoodGridAdapter(ITEMS, new OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(FoodBean item) {

                    }
                });
                nonveg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this, 2));
                nonveg.setAdapter(adapter);

            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });

        tingaManager.getFoodItems(FoodItemsActivity.this, r_id, "EGG", new TingaManager.FoodCallBack() {
            @Override
            public void onSuccess(List<FoodBean> detailsMovies) {
                ITEMS = detailsMovies;
                //Log.d("ID: ",ITEMS.get(0).getId());
                adapter = new FoodGridAdapter(ITEMS, new OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(FoodBean item) {

                    }
                });
                egg.setLayoutManager(new GridLayoutManager(FoodItemsActivity.this, 2));
                egg.setAdapter(adapter);

            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(FoodItemsActivity.this, "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });*/


/*


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sytiqhub.tinga", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
*/

/*
    int totalprice = 0;
    // code to get all contacts in a list view
    public int getTotalPrice() {
        List<OrderFoodBean> contactList = new ArrayList<OrderFoodBean>();
        // Select All Query
        String selectQuery = "SELECT  `total_price`,`quantity` FROM " + TABLE_ORDERS_FOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                totalprice = totalprice +(Integer.parseInt(cursor.getString(0)) * Integer.parseInt(cursor.getString(1)));
*/
/*
                OrderFoodBean order = new OrderFoodBean();
                order.setFoodId(cursor.getString(1));
                order.setQuantity(Integer.parseInt(cursor.getString(2)));
                order.setTotalPrice(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                contactList.add(order);
*//*

            } while (cursor.moveToNext());
        }
        // return contact list
        return totalprice;
    }
*/


/*
    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog
            Toast.makeText(HomeActivity.this, "Pick a location", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            adb.setTitle("Select your Location");
            adb.setMessage("Please update your location with below options");
            adb.setPositiveButton("Use Current Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getLatLong(dialog);

                }
            });
            adb.setNegativeButton("Choose your location on Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //Toast.makeText(HomeActivity.this, "This feature will come soon", Toast.LENGTH_SHORT).show();
                    try {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        startActivityForResult(builder.build(HomeActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                        Toast.makeText(HomeActivity.this, "Google Play Services is not available.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
            AlertDialog alert = adb.create();
            alert.show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }
*/


/*

package com.sytiqhub.tinga.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.FoodDescriptionActivity;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;
import com.sytiqhub.tinga.manager.DatabaseHandler;

import java.util.List;

    public class FoodGridAdapter extends RecyclerView.Adapter<com.sytiqhub.tinga.adapters.FoodGridAdapter.ViewHolder> {

        private List<FoodBean> mValues;
        private final OnListFragmentInteractionListener mListener;
        private Context context;
        public FoodGridAdapter(Context mcontext,List<FoodBean> items, OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
            context = mcontext;
        }
        private static int count=0;

        @NotNull
        @Override
        public com.sytiqhub.tinga.adapters.FoodGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);
            return new com.sytiqhub.tinga.adapters.FoodGridAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final com.sytiqhub.tinga.adapters.FoodGridAdapter.ViewHolder holder, final int position) {

            final DatabaseHandler db = new DatabaseHandler(context);
            if(!(mValues.size()<=0)){

                holder.mItem = mValues.get(position);
                holder.tv_name.setText(mValues.get(position).getName());
                holder.tv_price.setText("Price: "+mValues.get(position).getPrice()+"/-");

*/
/*
            if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){
                holder.tv_status.setTextColor(Color.parseColor("#006400"));
                holder.tv_status.setText("Available");
            }else{
                // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
                holder.tv_status.setText("Not available");


            }
*//*


                if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){
                    holder.tv_status.setTextColor(Color.parseColor("#006400"));
                    holder.tv_status.setText("Available");
                }else{
                    // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                    holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
                    holder.tv_status.setText("Currently not available");

                }

                //Picasso.get().load(mValues.get(position).getImage_path()).into(holder.image);

                if(mValues.get(position).getTypetag().equalsIgnoreCase("VEG")){
                    Picasso.get().load(R.drawable.veg).into(holder.type_image);
                }else if(mValues.get(position).getTypetag().equalsIgnoreCase("NON-VEG")){
                    Picasso.get().load(R.drawable.nonveg).into(holder.type_image);
                }else if(mValues.get(position).getTypetag().equalsIgnoreCase("EGG")){
                    Picasso.get().load(R.drawable.egg).into(holder.type_image);
                }

                holder.tv_count.setText(String.valueOf(count));

                holder.btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int c = Integer.parseInt(holder.tv_count.getText().toString());
                        if(c==6){
                            Toast.makeText(context, "Max quantity is 6...", Toast.LENGTH_SHORT).show();
                            //Snackbar.make((context.findViewById(R.id.layout)),"Max quantity is 6...",Snackbar.LENGTH_SHORT).show();
                        }else if(c<1){
                            c++;

                            holder.btn_remove.setVisibility(View.VISIBLE);
                            holder.tv_count.setVisibility(View.VISIBLE);
                            holder.tv_count.setText(String.valueOf(c));

                            OrderFoodBean orderFoodBean = new OrderFoodBean();
                            orderFoodBean.setFoodId(mValues.get(position).getId());
                            orderFoodBean.setQuantity(Integer.parseInt(holder.tv_count.getText().toString()));
                            orderFoodBean.setTotalPrice(Integer.parseInt(holder.tv_count.getText().toString())*Integer.parseInt(mValues.get(position).getPrice()));
                            //orderFoodBean.setTotalPrice(120);
                            orderFoodBean.setFoodName(holder.tv_name.getText().toString());
                            Log.d("akhilllll foodname",holder.tv_name.getText().toString());
                            //Log.d("akhilllll foodname",f_name);

                            //prefs.setOrderFood(orderFoodBean);
                            db.addOrderFood(orderFoodBean);
                            Toast.makeText(context, "Item added to cart...", Toast.LENGTH_SHORT).show();
                            //Snackbar.make((findViewById(R.id.layout)),"Item added to cart...",Snackbar.LENGTH_SHORT).show();

                        }else{
                            count++;
                            db.updateQuantity(mValues.get(position).getId(),count);
                            holder.tv_count.setText(String.valueOf(count));
                            Log.d("akhilll add",String.valueOf(count));
                        }
                    }
                });

                holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(count == 1){
                            count--;
                            db.deleteOrderFood(mValues.get(position).getId());
                            holder.btn_remove.setVisibility(View.GONE);
                            holder.tv_count.setVisibility(View.GONE);
                        }else if(count > 0){
                            count--;
                            holder.tv_count.setText(String.valueOf(count));
                            Log.d("akhilll remove",String.valueOf(count));
                        }

                    }
                });

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {

                            mListener.onListFragmentInteraction(mValues.get(position));

                        }
                    }
                });


            }

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView tv_name;
            public final TextView tv_price;
            public final TextView tv_status;
            public final TextView tv_count;
            public final ImageButton btn_add;
            public final ImageButton btn_remove;
            public FoodBean mItem;
            public ImageView image,type_image;
            public LinearLayout layout;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                tv_name = view.findViewById(R.id.food_name);
                tv_price = view.findViewById(R.id.food_price);
                tv_status = view.findViewById(R.id.food_status);
                //image = view.findViewById(R.id.food_image);
                btn_add = view.findViewById(R.id.ib_add);
                btn_remove = view.findViewById(R.id.ib_remove);
                tv_count = view.findViewById(R.id.tv_count);
                type_image = view.findViewById(R.id.type_image);
                layout = view.findViewById(R.id.food_layout);

            }

        }
    }
*/


}
