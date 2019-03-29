package com.sytiqhub.tinga;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sytiqhub.tinga.auth.MainActivity;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

public class HomeActivity1 extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView tv;
    private FirebaseAuth.AuthStateListener mAuthListener;
    PreferenceManager prefs;
    TingaManager tingaManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = new PreferenceManager(HomeActivity1.this);

        tingaManager = new TingaManager();

        tv = findViewById(R.id.tv_uid);

        tv.setText(prefs.getUID());

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in");
                }
                else{
                    Intent i = new Intent(HomeActivity1.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };


        (findViewById(R.id.sign_out)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Provider List: ",user.getProviders().get(0));
                String provider = user.getProviders().get(0);
                tingaManager.Logout(HomeActivity1.this,prefs.getLoginID(),provider);

            }
        });

    }

    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }

}
