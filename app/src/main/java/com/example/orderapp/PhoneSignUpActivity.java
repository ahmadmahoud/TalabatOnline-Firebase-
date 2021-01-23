package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.concurrent.TimeUnit;


public class PhoneSignUpActivity extends AppCompatActivity {
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private static final String TAG = "PhoneAuthActivity";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private TextView statusText;
    private EditText phoneText, textCode, countryCode;
    private View process;
    private Button sendCodeButton, resendCodeButton, verifyCodeButton;


    //  private static final String TAG = "PhoneAuthActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign_up);
        statusText = findViewById(R.id.statusText);
        phoneText = findViewById(R.id.phone_number);
        textCode = findViewById(R.id.code_text);
        countryCode = findViewById(R.id.country_code);
        sendCodeButton = findViewById(R.id.send_code);
        resendCodeButton = findViewById(R.id.resend_code);
        verifyCodeButton = findViewById(R.id.verify_code);
        process = findViewById(R.id.progress);
        resendCodeButton.setEnabled(false);
        verifyCodeButton.setEnabled(false);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+201220407005")       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void sendCode(View view) {
        String phoneNo = phoneText.getText().toString().trim();
        String countryNo = countryCode.getText().toString().trim();
        String phoneNumber = "+" + countryNo + phoneNo;
//        if (countryNo.isEmpty()||phoneNo.isEmpty()){
//            Toast.makeText(this, "Please Fill Data", Toast.LENGTH_SHORT).show();
//        }else {
        process.setVisibility(View.VISIBLE);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.i(TAG, "onVerificationCompleted: " + credential.getSmsCode());
            statusText.setText("Signed IN");
            sendCodeButton.setEnabled(false);
            resendCodeButton.setEnabled(false);
            textCode.setText("");
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.i(TAG, "onVerificationFailed: " + e.getLocalizedMessage());
            Log.e(TAG, "onVerificationFailed: ", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Log.i(TAG, "onCodeSent: " + s);
            Log.i(TAG, "onCodeSent: " + forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
            verifyCodeButton.setEnabled(true);
            sendCodeButton.setEnabled(false);
            resendCodeButton.setEnabled(true);
        }
    };

    public void verifyCode(View view) {
        String code = textCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    statusText.setText("Signed IN");
                    verifyCodeButton.setEnabled(false);
                    resendCodeButton.setEnabled(false);
                    textCode.setText("");
                    FirebaseUser user = task.getResult().getUser();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
        });
    }

    public void resendCode(View view) {
        String phoneNumber = phoneText.getText().toString().trim();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}