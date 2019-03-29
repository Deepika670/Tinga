package com.sytiqhub.tinga;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sytiqhub.tinga.adapters.RestaurantItemAdapter;
import com.sytiqhub.tinga.beans.RestaurantBean;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    public OrderFragment() {

    }
    EditText search;

    public List<RestaurantBean> ITEMS = new ArrayList<RestaurantBean>();
    RestaurantItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    RecyclerView recyclerView;
    View view;
    SwipeRefreshLayout pullToRefresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        search = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.recycler_list);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh_order);
        addContent(1);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addContent(2);
            }
        });

        return view;
    }

    public void addContent(int i){

        TingaManager tingaManager = new TingaManager();
        tingaManager.getAllRestaurants(getActivity(), i, new TingaManager.RestaurantCallBack() {
            @Override
            public void onSuccess(List<RestaurantBean> detailsMovies) {
                ITEMS = detailsMovies;
                Log.d("ID: ",ITEMS.get(0).getId());
                adapter = new RestaurantItemAdapter(ITEMS, mListener);
                recyclerView.setAdapter(adapter);

                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //after the change calling the method and passing the search input
                        filter(editable.toString());
                    }
                });
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(getActivity(), "Failed retrieve data...", Toast.LENGTH_SHORT).show();
            }
        });
        if (pullToRefresh.isRefreshing()) {
            pullToRefresh.setRefreshing(false);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(RestaurantBean item);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<RestaurantBean> filterdNames = new ArrayList<>();
        //looping through existing elements
        for(int i=0;i<ITEMS.size();i++){
            //if the existing elements contains the search input
            if (ITEMS.get(i).getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(ITEMS.get(i));
            }
        }
        //calling a method of the adapter class and passing the filtered list
        if(adapter != null)
        {
            adapter.filterList(filterdNames);
        }

    }


}
