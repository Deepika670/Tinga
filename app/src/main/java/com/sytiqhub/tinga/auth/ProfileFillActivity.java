package com.sytiqhub.tinga.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.beans.UserBean;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

public class ProfileFillActivity extends Activity {

    EditText tv_fname,tv_lname,tv_mobile,tv_email;
    Button b_continue;
    //Button verify;

    TingaManager tingaManager;
    String uid,type;
    UserBean userBean;
    FirebaseAuth mAuth;
    PreferenceManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fill);

        tingaManager = new TingaManager();

        mAuth = FirebaseAuth.getInstance();
        prefs = new PreferenceManager(ProfileFillActivity.this);
        uid = getIntent().getStringExtra("uid");
        type = getIntent().getStringExtra("type");

        tv_fname = findViewById(R.id.profile_fname);
        tv_lname = findViewById(R.id.profile_lname);
        tv_mobile = findViewById(R.id.profile_mobile);
        tv_email = findViewById(R.id.profile_email);
        b_continue = findViewById(R.id.btn_continue);
        //verify = findViewById(R.id.btn_verify);

        FirebaseUser user = mAuth.getCurrentUser();



        Log.d("Provider List: ",user.getProviders().get(0));
        String provider = user.getProviders().get(0);
        if(provider.contains("facebook.com")){
            Profile profile = Profile.getCurrentProfile();
            tv_fname.setText(profile.getFirstName());
            tv_lname.setText(profile.getLastName());
            tv_mobile.setText(mAuth.getCurrentUser().getPhoneNumber());
            tv_email.setText(mAuth.getCurrentUser().getEmail());
        }else if(provider.contains("google.com")){
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ProfileFillActivity.this);
            tv_fname.setText(acct.getDisplayName());
            tv_lname.setText(acct.getFamilyName());
            tv_mobile.setText(mAuth.getCurrentUser().getPhoneNumber());
            tv_email.setText(acct.getEmail());
        }else{
            tv_mobile.setText(mAuth.getCurrentUser().getPhoneNumber());
            tv_mobile.setEnabled(false);
            //tv_mobile.setBackgroundColor(Color.parseColor("#dedee6"));
            //verify.setEnabled(false);
        }

       /* verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(prefs.getMobile_verified()==1){

                 Toast.makeText(ProfileFillActivity.this,"Your Mobile number is verified",Toast.LENGTH_SHORT).show();

             }else{
                 final String mobile = tv_mobile.getText().toString();
                 if(mobile.equals("")||mobile.isEmpty()||TextUtils.isEmpty(mobile)){
                     tv_mobile.setError("Please enter Mobile number");
                 }else{
                     final String phone = tv_mobile.getText().toString();

                     tingaManager.checkMobile(ProfileFillActivity.this, mobile, new TingaManager.MobileCallBack() {
                         @Override
                         public void onSuccess(int successCode,int verification,String uidArgs) {

                             if(successCode == 0){
                                 Intent i = new Intent(ProfileFillActivity.this,PhoneAuth.class);
                                 i.putExtra("mode","verify");
                                 i.putExtra("mobile",phone);
                                 startActivity(i);
                             }else{
                                 if(verification == 1){
                                     if(uidArgs.equalsIgnoreCase(uid)){
                                         Toast.makeText(ProfileFillActivity.this, "Mobile number already exists in Tinga database. Try different number!",Toast.LENGTH_SHORT).show();
                                     }else{
                                         Toast.makeText(ProfileFillActivity.this, "Mobile number already exists in Tinga database, but not associated with this account.",Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             }
                         }

                         @Override
                         public void onFail(String msg) {

                         }
                     });

                 }
             }
            }
        });*/
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        b_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(TextUtils.isEmpty(tv_fname.getText().toString())){

                tv_fname.setError("Please enter First name");

            }else if(TextUtils.isEmpty(tv_lname.getText().toString())){

                tv_lname.setError("Please enter Last name");

            }else if(TextUtils.isEmpty(tv_mobile.getText().toString())){

                tv_mobile.setError("Please Enter your mobile number(Eg: +919*********)");

            }else if(tv_mobile.getText().toString().length()>13 || tv_mobile.getText().toString().length()<13){

                tv_mobile.setError("Please Enter your mobile number in proper format(Eg: +919*********)");

            }else if(TextUtils.isEmpty(tv_email.getText().toString())){

                tv_email.setError("Please enter Email ID");

            }else if (!tv_email.getText().toString().matches(emailPattern)){

                tv_email.setError("Please enter valid Email ID");

            }else{

                String phone = tv_mobile.getText().toString();

                userBean = new UserBean();
                userBean.setUid(uid);
                userBean.setFname(tv_fname.getText().toString());
                userBean.setLname(tv_lname.getText().toString());
                userBean.setMobile_verified(1);
                userBean.setPhone_number(phone);
                userBean.setEmail(tv_email.getText().toString());

                Log.d("uid",userBean.getUid());
                Log.d("fname",userBean.getFname());
                Log.d("lname",userBean.getLname());
                Log.d("mobile",userBean.getPhone_number());
                Log.d("Mobile_verified",String.valueOf(userBean.getMobile_verified()));
                Log.d("email",userBean.getEmail());

                tingaManager.UpdateProfile(ProfileFillActivity.this,userBean);

/*                if(prefs.getMobile_verified()==1){
                    Toast.makeText(ProfileFillActivity.this,"Your Mobile number is verified",Toast.LENGTH_SHORT).show();
                    Log.d("uid activity",uid);

                    String phone = tv_mobile.getText().toString();

                    userBean = new UserBean();
                    userBean.setUid(uid);
                    userBean.setFname(tv_fname.getText().toString());
                    userBean.setLname(tv_lname.getText().toString());
                    userBean.setMobile_verified(prefs.getMobile_verified());
                    userBean.setPhone_number(phone);
                    userBean.setEmail(tv_email.getText().toString());

                    Log.d("uid",userBean.getUid());
                    Log.d("fname",userBean.getFname());
                    Log.d("lname",userBean.getLname());
                    Log.d("mobile",userBean.getPhone_number());
                    Log.d("Mobile_verified",String.valueOf(userBean.getMobile_verified()));
                    Log.d("email",userBean.getEmail());

                    tingaManager.UpdateProfile(ProfileFillActivity.this,userBean);

                }else{

                    Toast.makeText(ProfileFillActivity.this,"Please verify your mobile number.",Toast.LENGTH_SHORT).show();

                }*/

            }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getMobile_verified()==1){
            tv_mobile.setEnabled(false);
            //  tv_mobile.setBackgroundColor(Color.parseColor("#dedee6"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("akhillll","onStoppp");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("akhillll","onDestroyyyy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFillActivity.this);
        builder.setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("Provider List: ",user.getProviders().get(0));
                        String provider = user.getProviders().get(0);
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit().clear();
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit().apply();
                        tingaManager.Logout1(ProfileFillActivity.this,prefs.getLoginID(),provider);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
    }
}
