package com.sytiqhub.tinga.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.OrderFragment.OnListFragmentInteractionListener;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.RestaurantBean;


import java.util.ArrayList;
import java.util.List;

public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.ViewHolder> {

    private List<RestaurantBean> mValues;
    private final OnListFragmentInteractionListener mListener;

    public RestaurantItemAdapter(List<RestaurantBean> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tv_name.setText(mValues.get(position).getName());

        holder.tv_rating.setBackgroundResource(R.drawable.round);
        GradientDrawable drawable = (GradientDrawable) holder.tv_rating.getBackground();

        if(mValues.get(position).getStatus().equalsIgnoreCase("Open")){
            holder.tv_status.setTextColor(Color.parseColor("#006400"));
            holder.tv_status.setText("Available");
        }else{
           // holder.layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.tv_status.setTextColor(Color.parseColor("#8B0000"));
            holder.tv_status.setText("Currently not available");

        }

        if(mValues.get(position).getRating().equalsIgnoreCase("New")){
            drawable.setColor(Color.parseColor("#CECDCB"));
            holder.tv_rating.setText(mValues.get(position).getRating());
        }else if(Float.parseFloat(mValues.get(position).getRating()) <= 5 && Float.parseFloat(mValues.get(position).getRating()) >= 4){
            drawable.setColor(Color.parseColor("#006400"));
            holder.tv_rating.setText(mValues.get(position).getRating());
        }else if(Float.parseFloat(mValues.get(position).getRating()) < 4 && Float.parseFloat(mValues.get(position).getRating()) >= 3){
            drawable.setColor(Color.parseColor("#008000"));
            holder.tv_rating.setText(mValues.get(position).getRating());
        }else if(Float.parseFloat(mValues.get(position).getRating()) < 3){
            drawable.setColor(Color.parseColor("#8B0000"));
            holder.tv_rating.setText(mValues.get(position).getRating());
        }
       // holder.tv_timings.setText(mValues.get(position).getTimings());
        Log.d("akhilll",String.valueOf(!(mValues.get(position).getImage_path().isEmpty()) || !(mValues.get(position).getImage_path().equals(""))));
        if(!(mValues.get(position).getImage_path().isEmpty()) || !(mValues.get(position).getImage_path().equals(""))){
            Picasso.get().load(mValues.get(position).getImage_path()).into(holder.image);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.onListFragmentInteraction(mValues.get(position));

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tv_name;
        public final TextView tv_rating;
        public final TextView tv_status;
        public RestaurantBean mItem;
        public ImageView image;
        public LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tv_name = view.findViewById(R.id.restaurant_name);
            tv_rating = view.findViewById(R.id.rating);
            tv_status = view.findViewById(R.id.status);
            image = view.findViewById(R.id.restaurant_image);
            layout = view.findViewById(R.id.item_frame);
        }
    }

    public void filterList(ArrayList<RestaurantBean> filterdNames) {
        this.mValues = filterdNames;
        notifyDataSetChanged();
    }
}