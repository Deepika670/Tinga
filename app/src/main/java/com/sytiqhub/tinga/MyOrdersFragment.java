
package com.sytiqhub.tinga;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sytiqhub.tinga.adapters.MyOrdersAdapter;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener1;
import com.sytiqhub.tinga.adapters.RestaurantItemAdapter;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.RestaurantBean;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }
    public List<OrderBean> ITEMS = new ArrayList<OrderBean>();
    private OnListFragmentInteractionListener1 mListener;

    RecyclerView recycler;
    TextView txt;
    MyOrdersAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        recycler = view.findViewById(R.id.myorders_recycler);
        txt = view.findViewById(R.id.no_items);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        TingaManager tingaManager = new TingaManager();

        adapter = new MyOrdersAdapter(ITEMS,mListener);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        tingaManager.getAllOrders(getActivity(), uid, new TingaManager.OrdersCallBack() {
            @Override
            public void onSuccess(List<OrderBean> detailsMovies) {
                ITEMS = detailsMovies;
                if(ITEMS.size()<=0){
                    txt.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                }else{
                    Log.d("ID: ",ITEMS.get(0).getOrder_id());
                    recycler.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.GONE);

                    adapter = new MyOrdersAdapter(ITEMS, mListener);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getActivity(), "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderFragment.OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener1) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
