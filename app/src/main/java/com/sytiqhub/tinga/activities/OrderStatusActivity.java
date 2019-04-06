package com.sytiqhub.tinga.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sytiqhub.tinga.R;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {


    TextView text;
    HorizontalStepView setpview;
    List<StepBean> stepsBeanList;
    DatabaseReference mdatabase;
    int order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Orders");

        text = findViewById(R.id.text_status);

        order_id = getIntent().getIntExtra("order_id",0);

        Log.e("akhillll", String.valueOf(order_id));

        text.setText("Your Order is Pending with the restaurant");

        /*setpview = findViewById(R.id.step_view);
        stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Your Order is Pending with the restaurant",1);
        stepsBeanList.add(stepBean0);

        setpview.setStepViewTexts(stepsBeanList)
                .setTextSize(16)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(OrderStatusActivity.this, android.R.color.white))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(OrderStatusActivity.this, R.color.uncompleted_text_color))
                .setStepViewComplectedTextColor(ContextCompat.getColor(OrderStatusActivity.this, android.R.color.white))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(OrderStatusActivity.this, R.color.uncompleted_text_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(OrderStatusActivity.this, R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(OrderStatusActivity.this, R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(OrderStatusActivity.this, R.drawable.attention));
*/
        mdatabase.child(String.valueOf(order_id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.child("delivery").getValue(String.class).equals("Accepted")){
                    if(dataSnapshot.hasChild("delivery_name")){
                       /* StepBean stepBean2 = new StepBean("Our Executive '"+dataSnapshot.child("delivery_name").toString()+"' is on the way to collect your order...",1);
                        stepsBeanList.add(stepBean2);*/
                        text.setText("Our Executive '"+dataSnapshot.child("delivery_name").getValue().toString()+"' is on the way to collect your order...");

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.child("restaurant").getValue(String.class).equals("Accepted")){
                   /* StepBean stepBean1 = new StepBean("Order is accepted. Your Food is being prepared...",1);
                    stepsBeanList.add(stepBean1);*/
                    text.setText("1. Order is accepted. Your Food is being prepared...");

                }else /*if(dataSnapshot.child("delivery").equals("Accepted")){
                    StepBean stepBean2 = new StepBean("Our Executive is on the way to collect your order...",1);
                    stepsBeanList.add(stepBean2);
                }else */if(dataSnapshot.child("food_status").getValue(String.class).equals("Prepared")){
                    /*StepBean stepBean2 = new StepBean("Your food is ready...",1);
                    stepsBeanList.add(stepBean2);*/
                    text.setText("1. Your food is ready...");
                }else if(dataSnapshot.child("delivery").getValue(String.class).equals("Collected")){
                  /*  StepBean stepBean2 = new StepBean("Our Executive collected your order and is on the way to you...",1);
                    stepsBeanList.add(stepBean2);*/
                    text.setText("1. Our Executive collected your order and is on the way to you...");
                }else if(dataSnapshot.child("status").getValue(String.class).equals("Delivered")){
                    /*StepBean stepBean2 = new StepBean("Your order is delivered...",1);
                    stepsBeanList.add(stepBean2);*/
                    text.setText("1. Your order is delivered...");
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    /*private void showSetpView0()
    {
        HorizontalStepView setpview = findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Your Order is Pending with the restaurant",1);
        StepBean stepBean1 = new StepBean("Your Food is being prepared...",1);
        StepBean stepBean2 = new StepBean("Our Executive is on the way to collect your order...",1);
        StepBean stepBean3 = new StepBean("Our Executive is on the way to you...",1);
        StepBean stepBean4 = new StepBean("The food is delivered...",1);
//        StepBean stepBean5 = new StepBean("The food is delivered...",-1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);
//        stepsBeanList.add(stepBean5);


        setpview.setStepViewTexts(stepsBeanList)
                .setTextSize(16)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(OrderStatusActivity.this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(OrderStatusActivity.this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(OrderStatusActivity.this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(OrderStatusActivity.this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(OrderStatusActivity.this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(OrderStatusActivity.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(OrderStatusActivity.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon
    }
        */
        
}
