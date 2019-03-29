package com.sytiqhub.tinga.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sytiqhub.tinga.AppController;
import com.sytiqhub.tinga.R;
import com.sytiqhub.tinga.manager.PreferenceManager;
import com.sytiqhub.tinga.manager.TingaManager;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    private String mVerificationId;

    EditText phone,otp;
    Button b_continue,b_signin;
    private FirebaseAuth mAuth;
    TingaManager tingaManager;
    String mode="login",mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mode = getIntent().getExtras().getString("mode");
        mobile = getIntent().getExtras().getString("mobile");

        mAuth = FirebaseAuth.getInstance();


        tingaManager = new TingaManager();

        phone = findViewById(R.id.edit_phone);
        otp = findViewById(R.id.edit_otp);

        if(mode.equalsIgnoreCase("verify")){
            phone.setText(mobile);
            phone.setEnabled(false);
            //phone.setBackgroundColor(Color.parseColor("#dedee6"));
        }

        b_continue = findViewById(R.id.button_continue);
        b_signin = findViewById(R.id.button_signin);

        //b_signin.setEnabled(false);

        b_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString().trim();
                final String str_phno = phone.getText().toString();
                if(str_phno.isEmpty()){

                    phone.setError("Please Enter your mobile number(Eg: +919*********)");

                }else if(str_phno.length()<13||str_phno.length()>13){

                    phone.setError("Please enter mobile number along with '+' and country code (Eg: +91**********)");

                }else{

                    sendVerificationCode(str_phno);

/*
                    tingaManager.checkMobile(PhoneAuth.this, str_phno, new TingaManager.MobileCallBack() {
                        @Override
                        public void onSuccess(int successCode,int verification,String uid) {


                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
*/

                }

            }
        });

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("Enter valid code");
                    otp.requestFocus();
                    return;
                }

                //verifying the code entered manually
                Log.d("2: ",code);
                verifyVerificationCode(code);
            }
        });

    }

    private void sendVerificationCode(String mobile) {
        Log.d("3: ",mobile);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                100,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            phone.setEnabled(false);
            b_continue.setEnabled(false);
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //super.onCodeSent(s, forceResendingToken);
            Log.d("4: ",s);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {

        //creating the credential

        Log.d("5: ",mVerificationId);
        //signing the user
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
        catch (Exception e) {
            Log.i("exception",e.toString());
            Toast.makeText(PhoneAuth.this,"Invalid credentials",Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneAuth.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Log.d("Akhilllll uid: ", task.getResult().getUser().getUid());
                            PreferenceManager prefs = new PreferenceManager(PhoneAuth.this);

                            if(mode.equalsIgnoreCase("login")){
                                prefs.setMobile_verified(1);
                                tingaManager.checkLogin(PhoneAuth.this,task.getResult().getUser().getUid());
                            }else if(mode.equalsIgnoreCase("verify")){
                                prefs.setMobile_verified(1);
                                finish();
                            }
/*
                            Intent intent = new Intent(PhoneAuth.this, ProfileFillActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("uid",task.getResult().getUser().getUid());
                            startActivity(intent);
*/
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Log.d("Akhilllll error: ", message);

                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.layout), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppController app = new AppController();
        app.cancelPendingRequests("req_login");
        app.cancelPendingRequests("signin_user");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController app = new AppController();
        app.cancelPendingRequests("req_login");
        app.cancelPendingRequests("signin_user");


    }

}
