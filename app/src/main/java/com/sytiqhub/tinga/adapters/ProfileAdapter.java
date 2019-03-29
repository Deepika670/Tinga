package com.sytiqhub.tinga.adapters;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.FoodBean;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private String[] mValues;
    private final OnListFragmentInteractionListener2 mListener;

    public ProfileAdapter(String[] items, OnListFragmentInteractionListener2 listener) {
        mValues = items;
        mListener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ProfileAdapter.ViewHolder holder, final int position) {

        if(mValues.length>0){
            //holder.mItem = mValues.get(position);
            holder.button.setText(mValues[position]);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {

                        mListener.onListFragmentInteraction(position);

                    }
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final Button button;
        public ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            button = view.findViewById(R.id.profile_button);


        }

    }



}
