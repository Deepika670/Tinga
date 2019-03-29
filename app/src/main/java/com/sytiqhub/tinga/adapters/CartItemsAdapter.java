package com.sytiqhub.tinga.adapters;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.FoodBean;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.OrderFoodBean;

import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {

    private List<OrderFoodBean> mValues;

    public CartItemsAdapter(List<OrderFoodBean> items) {
        mValues = items;

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new CartItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final CartItemsAdapter.ViewHolder holder, final int position) {

        if(mValues.size()<=0){

        }else{
            Log.d("akhilllll foodname",mValues.get(position).getFoodName());
            holder.mItem = mValues.get(position);
            holder.tv_name.setText(mValues.get(position).getFoodName());
            holder.tv_price.setText(mValues.get(position).getTotalPrice()+"/-");
            holder.tv_quantity.setText("Q: "+mValues.get(position).getQuantity());

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
        public final TextView tv_quantity;
        public OrderFoodBean mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            tv_name = view.findViewById(R.id.item_name);
            tv_price = view.findViewById(R.id.item_price);
            tv_quantity = view.findViewById(R.id.item_quantity);

        }

    }



}
