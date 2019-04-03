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

public class FoodGridAdapter extends RecyclerView.Adapter<FoodGridAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new FoodGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodGridAdapter.ViewHolder holder, final int position) {

        final DatabaseHandler db = new DatabaseHandler(context);
        if(!(mValues.size()<=0)){

            holder.mItem = mValues.get(position);
            holder.tv_name.setText(mValues.get(position).getName());
            holder.tv_price.setText("Price: "+mValues.get(position).getPrice()+"/-");

/*
            if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){
                holder.tv_status.setTextColor(Color.parseColor("#006400"));
                holder.tv_status.setText("Available");
            }else{
                // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
                holder.tv_status.setText("Not available");


            }
*/

            if(mValues.get(position).getStatus().equalsIgnoreCase("Available")){
                holder.tv_status.setTextColor(Color.parseColor("#006400"));
                holder.tv_status.setText("Available");
            }else{
                // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
                holder.tv_status.setText("Currently not available");
                holder.btn_add.setVisibility(View.GONE);
                holder.btn_remove.setVisibility(View.GONE);
                holder.tv_count.setVisibility(View.GONE);
            }

            //Picasso.get().load(mValues.get(position).getImage_path()).into(holder.image);

            if(mValues.get(position).getTypetag().equalsIgnoreCase("VEG")){
                Picasso.get().load(R.drawable.veg).into(holder.type_image);
            }else if(mValues.get(position).getTypetag().equalsIgnoreCase("NON-VEG")){
                Picasso.get().load(R.drawable.nonveg).into(holder.type_image);
            }else if(mValues.get(position).getTypetag().equalsIgnoreCase("EGG")){
                Picasso.get().load(R.drawable.egg).into(holder.type_image);
            }
            int c = Integer.parseInt(holder.tv_count.getText().toString());
            holder.tv_count.setText(String.valueOf(c));

            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int c = Integer.parseInt(holder.tv_count.getText().toString());
                    if(c==6){
                        Toast.makeText(context, "Max quantity is 6...", Toast.LENGTH_SHORT).show();
                        //Snackbar.make((context.findViewById(R.id.layout)),"Max quantity is 6...",Snackbar.LENGTH_SHORT).show();
                    }else if(c==0){
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
                        //Toast.makeText(context, "Item added to cart...", Toast.LENGTH_SHORT).show();
                        //Snackbar.make((findViewById(R.id.layout)),"Item added to cart...",Snackbar.LENGTH_SHORT).show();

                    }else{
                        c++;
                        db.updateQuantity(mValues.get(position).getId(),c,c*Integer.parseInt(mValues.get(position).getPrice()));
                        holder.tv_count.setText(String.valueOf(c));
                        Log.d("akhilll add",String.valueOf(c));
                    }
                }
            });

            holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int c = Integer.parseInt(holder.tv_count.getText().toString());

                    if(c == 1){
                        c--;
                        db.deleteOrderFood(mValues.get(position).getId());
                        Log.d("akhilll remove",String.valueOf(c));
                        holder.btn_remove.setVisibility(View.GONE);
                        holder.tv_count.setVisibility(View.GONE);
                        holder.tv_count.setText(String.valueOf(c));
                    }else if(c > 0){
                        c--;
                        db.updateQuantity(mValues.get(position).getId(),c,c*Integer.parseInt(mValues.get(position).getPrice()));
                        holder.tv_count.setText(String.valueOf(c));
                        Log.d("akhilll remove",String.valueOf(c));
                    }

                }
            });

           /* holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {

                        mListener.onListFragmentInteraction(mValues.get(position));

                    }
                }
            });

*/
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
