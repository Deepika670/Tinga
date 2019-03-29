package com.sytiqhub.tinga;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener1;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener2;
import com.sytiqhub.tinga.adapters.ProfileAdapter;
import com.sytiqhub.tinga.auth.ProfileFillActivity;
import com.sytiqhub.tinga.beans.UserBean;
import com.sytiqhub.tinga.manager.PreferenceManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }
    String[] ITEMS = { "Guidelines", "Terms & Conditions", "Privacy Policy", "Logout"};
    RecyclerView recycler;
    TextView txt_name,txt_email,txt_phno,txt_location,txt_edit_profile;
    ImageView img_profile_pic;
    FirebaseAuth firebaseAuth;
    OnListFragmentInteractionListener2 mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        img_profile_pic = view.findViewById(R.id.profile_picture);
        txt_name = view.findViewById(R.id.profile_name);
        txt_email = view.findViewById(R.id.profile_email);
        txt_phno = view.findViewById(R.id.profile_phno);
        txt_location = view.findViewById(R.id.profile_location);
        txt_edit_profile = view.findViewById(R.id.profile_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        recycler = view.findViewById(R.id.profile_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        PreferenceManager prefs = new PreferenceManager(getActivity());
        UserBean userBean = prefs.getUserDetails();
        txt_name.setText(userBean.getFname()+" "+userBean.getLname());
        txt_email.setText(userBean.getEmail());
        txt_phno.setText(userBean.getPhone_number());
        txt_location.setText(prefs.getLocationDetails().get("city"));

        recycler.setAdapter(new ProfileAdapter(ITEMS, mListener));

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener2) {
            mListener = (OnListFragmentInteractionListener2) context;
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
