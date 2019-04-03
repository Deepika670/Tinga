package com.sytiqhub.tinga.adapters;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.OrderBean;


import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private List<OrderBean> mValues;
    private final OnListFragmentInteractionListener1 mListener;

    public MyOrdersAdapter(List<OrderBean> items, OnListFragmentInteractionListener1 listener) {
        mValues = items;
        mListener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {

        if(mValues.size()<=0){

        }else{

            //holder.mItem = mValues.get(position);
            holder.r_name.setText(mValues.get(position).getRestaurant_name());
            holder.r_address.setText(mValues.get(position).getRestaurant_address());
            holder.order_date.setText(mValues.get(position).getOrder_date());
            holder.total_amount.setText(mValues.get(position).getTotal_price()+"/-");
            holder.status.setText(mValues.get(position).getStatus());

/*
            holder.mView.setOnClickListener(new View.OnClickListener() {
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
        public final TextView r_name;
        public final TextView r_address;
        //public final TextView items;
        public final TextView order_date;
        public final TextView total_amount;
        public final TextView status;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            r_name = view.findViewById(R.id.restaurant_name);
            r_address = view.findViewById(R.id.restaurant_address);
            order_date = view.findViewById(R.id.order_date);
            total_amount = view.findViewById(R.id.total_amount);
            status = view.findViewById(R.id.status);
            //type_image = view.findViewById(R.id.type_image);


        }

    }



}

