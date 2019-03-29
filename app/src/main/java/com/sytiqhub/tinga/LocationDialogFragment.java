package com.sytiqhub.tinga;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sytiqhub.tinga.manager.PreferenceManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog, container, false);

    }
    PreferenceManager prefs;
    Button current_location, new_address;
    RecyclerView recycler_addresses;
    ProgressDialog progress;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        current_location = view.findViewById(R.id.btn_current_location);
        new_address = view.findViewById(R.id.btn_another_address);
        recycler_addresses = view.findViewById(R.id.recycler);
        progress = new ProgressDialog(getActivity());

        prefs= new PreferenceManager(getActivity());
        final LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setMessage("Fetching your locaton, Please Wait...!");
                progress.show();
                //LocationListener locationListener = new MyLocationListener();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
/*
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
*/
                Location location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                prefs.setLatitude(String.valueOf(location.getLatitude()));
                prefs.setLongitude(String.valueOf(location.getLongitude()));
                Log.v("akhilll", prefs.getLatitude());
                Log.v("akhilll", prefs.getLongitude());
                Geocoder gcd = new Geocoder(getContext(),
                        Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        prefs.setLocation(address);
                        Toast.makeText(getActivity(), "Current location has been se", Toast.LENGTH_SHORT).show();
                    }
                    Log.v("akhilll", prefs.getLocation());
                    progress.dismiss();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                    dismiss();
                }

            }
        });

        new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Light_NoTitleBar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

  /*
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {


            PreferenceManager prefs= new PreferenceManager(getActivity());
*//*
            Toast.makeText(getContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
*//*
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("akhilll", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("akhilll", latitude);

            *//*------- To get city name from coordinates -------- *//*
            String cityName = null;
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getAddressLine(0));
                    cityName = addresses.get(0).getAddressLine(0);
                    prefs.setLocation(cityName);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;





        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

*/
}