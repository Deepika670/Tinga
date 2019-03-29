package com.sytiqhub.tinga;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener1;
import com.sytiqhub.tinga.adapters.OnListFragmentInteractionListener2;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.beans.OrderBean;
import com.sytiqhub.tinga.beans.RestaurantBean;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OrderFragment.OnListFragmentInteractionListener, OnListFragmentInteractionListener1, OnListFragmentInteractionListener2, GoogleApiClient.OnConnectionFailedListener {

    private TextView mTextMessage;
    FirebaseAuth mAuth;
    TextView tv_address,tv_location;
    private FirebaseAuth.AuthStateListener mAuthListener;
    PreferenceManager prefs;
    TingaManager tingaManager;
    ProgressDialog progress;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextAppearance(HomeActivity.this,R.style.FullscreenActionBarStyle);
        progress = new ProgressDialog(HomeActivity.this);

        loadFragment(new OrderFragment());
        prefs = new PreferenceManager(HomeActivity.this);
       /* mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, null)
                .build();*/
        tingaManager = new TingaManager();
        mAuth = FirebaseAuth.getInstance();


        Places.initialize(getApplicationContext(), "AIzaSyB4xS7Rf3B6ixqR3GJSjmo03gopQvUT-cU");

        placesClient = Places.createClient(this);
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        checkFirstRun();

        mTextMessage = (TextView) findViewById(R.id.message);
        tv_location = findViewById(R.id.location_address);
        tv_location.setPaintFlags(tv_location.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationDialog();

            }
        });
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new OrderFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_myorders:
                    fragment = new MyOrdersFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.navigation_location:

                AlertDialog.Builder adb = new AlertDialog.Builder(this);

                adb.setTitle("Select your Location");
                adb.setMessage("Please update your location with below options");
                adb.setPositiveButton("Use Current Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLatLong(dialog);

                    }
                });
/*
                adb.setNegativeButton("Choose your location on Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Toast.makeText(HomeActivity.this, "This feature will come soon", Toast.LENGTH_SHORT).show();
                        try {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this, "Google Play Services is not available.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
*/
                AlertDialog alert = adb.create();
                alert.show();

                break;
           /* case R.id.navigation_signout:
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Provider List: ",user.getProviders().get(0));
                String provider = user.getProviders().get(0);
                tingaManager.Logout(HomeActivity.this,prefs.getLoginID(),provider);
                break;*/
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void locationDialog(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("Select your Location");
        adb.setMessage("Please update your location with below options");
        adb.setPositiveButton("Use Current Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getLatLong(dialog);

            }
        });

/*
        adb.setNegativeButton("Choose your location on Map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(HomeActivity.this, "This feature will come soon", Toast.LENGTH_SHORT).show();
                try {
// Set the fields to specify which types of place data to return.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(HomeActivity.this);
                    startActivityForResult(intent, 111);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
*/
        AlertDialog alert = adb.create();
        alert.show();

    }

    public void getLatLong(DialogInterface dialog){
        progress.setMessage("Fetching your locaton, Please Wait...!");
        progress.show();
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final LocationManager locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        prefs.setLatitude(String.valueOf(location.getLatitude()));
        prefs.setLongitude(String.valueOf(location.getLongitude()));
        Log.v("akhilll", prefs.getLatitude());
        Log.v("akhilll", prefs.getLongitude());
        Geocoder gcd = new Geocoder(HomeActivity.this,
                Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                prefs.setLocation(address);
                prefs.setLocationDetails(addresses.get(0).getLocality(),addresses.get(0).getAdminArea(),addresses.get(0).getCountryName(),addresses.get(0).getPostalCode());
                Toast.makeText(HomeActivity.this, "Current location has been set to "+address, Toast.LENGTH_SHORT).show();
                tv_location.setText(address);
            }
            Log.v("akhilll", prefs.getLocation());
            progress.dismiss();
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            progress.dismiss();
            dialog.dismiss();
        }
    }

    public void getLatLong(){

        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final LocationManager locationManager = (LocationManager)
                getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for(String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        prefs.setLatitude(String.valueOf(bestLocation.getLatitude()));
        prefs.setLongitude(String.valueOf(bestLocation.getLongitude()));
        Log.v("akhilll", prefs.getLatitude());
        Log.v("akhilll", prefs.getLongitude());
        Geocoder gcd = new Geocoder(HomeActivity.this,
                Locale.getDefault());

        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(bestLocation.getLatitude(),
                    bestLocation.getLongitude(), 1);
            if (addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                prefs.setLocation(address);
                prefs.setLocationDetails(addresses.get(0).getLocality(),addresses.get(0).getAdminArea(),addresses.get(0).getCountryName(),addresses.get(0).getPostalCode());
                //Toast.makeText(HomeActivity.this, "Current location has been set to "+address, Toast.LENGTH_SHORT).show();
                tv_location.setText(address);
                //Toast.makeText(HomeActivity.this, "Current location has been set to "+address, Toast.LENGTH_SHORT).show();
                tv_location.setText(address);
            }
            Log.v("akhilll", prefs.getLocation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage() + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("Akhillll", "Place: " + place.getName() + ", " + place.getId());
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                Toast.makeText(this, latitude+", "+longitude, Toast.LENGTH_SHORT).show();
                tv_location.setText(address);
                prefs.setLocation(address);
                prefs.setLatitude(String.valueOf(latitude));
                prefs.setLongitude(String.valueOf(longitude));
                //prefs.setLocationDetails(place.get,addresses.get(0).getAdminArea(),addresses.get(0).getCountryName(),addresses.get(0).getPostalCode());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Akhillll", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }


    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getLatLong();
                }
            });

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
       //Toast.makeText(HomeActivity.this, "Current location has been set to "+address, Toast.LENGTH_SHORT).show();
        tv_location.setText(prefs.getLocation());

        //mGoogleApiClient.connect();
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

                MainActivity.displayLocationSettingsRequest(HomeActivity.this);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        getLatLong();
                    }
                });

            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
       // mGoogleApiClient.disconnect();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Exit")
                .setMessage("Do you want exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .commit();
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alert.show();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit().clear().apply();
    }



    @Override
    public void onListFragmentInteraction(RestaurantBean item) {

        Intent i = new Intent(HomeActivity.this,FoodItemsActivity.class);
        PreferenceManager prefs = new PreferenceManager(HomeActivity.this);
        prefs.setRestaurantID(item.getId());
        prefs.setRestaurantName(item.getName());
        prefs.setRestaurantImage(item.getImage_path());
        prefs.setRestaurantAddress(item.getAddress());
        startActivity(i);

    }

    @Override
    public void onListFragmentInteraction(OrderBean item) {

        Intent i = new Intent(HomeActivity.this,OrderDetailsActivity.class);
        startActivity(i);

    }

    @Override
    public void onListFragmentInteraction(int position) {

        switch (position){
            case 0:
                break;
            case 1:
                break;
             case 2:
                break;
            case 3:
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Provider List: ",user.getProviders().get(0));
                String provider = user.getProviders().get(0);
                tingaManager.Logout(HomeActivity.this,prefs.getLoginID(),provider);

                break;
        }

    }


}
