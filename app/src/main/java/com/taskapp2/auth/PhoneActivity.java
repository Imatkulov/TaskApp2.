package com.taskapp2.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.taskapp2.MainActivity;
import com.taskapp2.R;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuth";

    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
    private Button resendButton;
    private Button signoutButton;
    private TextView statusText;
    String number;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            callbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
//          Стреллка назад
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneText = (EditText) findViewById(R.id.phoneText);
        codeText = (EditText) findViewById(R.id.codeText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        resendButton = (Button) findViewById(R.id.resendButton);
        signoutButton = (Button) findViewById(R.id.signoutButton);
        statusText = (TextView) findViewById(R.id.statusText);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);

        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);
        signoutButton.setEnabled(false);
        statusText.setText("Signed Out");

        fbAuth = FirebaseAuth.getInstance();
    }

    public void sendCode(View view) {

        number = ccp.getFullNumberWithPlus();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callbacks);
    }

    private void setUpVerificatonCallbacks() {

        callbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        signoutButton.setEnabled(true);
                        statusText.setText("Signed In");
                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signIn(credential);
                    }
                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);
                    }

                };
    }

    public void verifyCode(View view) {

        String code = codeText.getText().toString();
        if (TextUtils.isEmpty(code)){
            codeText.setError("Укажите номер");
        }

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signIn(credential);
    }

    private void signIn(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signoutButton.setEnabled(true);
                            codeText.setText("");
                            statusText.setText("Signed In");
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();

                            Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
                            intent.putExtra("phone", phoneNumber);
                            startActivity(intent);
                            finish();

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {

        number = ccp.getFullNumberWithPlus();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks,
                resendToken);
    }

    public void signOut(View view) {
        fbAuth.signOut();
        statusText.setText("Signed Out");
        signoutButton.setEnabled(false);
        sendButton.setEnabled(true);
        sendButton.setError("Fast fast");
    }

    //          Функции назад!!!
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

//
//    EditText editPhone;
//    Spinner editCode;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
//
////    AppCompatSpinner spinnerCountries;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone);
//z
//
//        editPhone = findViewById(R.id.editPhone);
//        editCode = findViewById(R.id.editCode);
//        String selected = editCode.getSelectedItem().toString();
//        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
//        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Log.e("TAG", "onVerificationCompleted");
//                signIn(phoneAuthCredential);
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Log.e("TAG", "onVerificationFailed");
//
//            }
//        };
//    }
//    private void signIn(PhoneAuthCredential phoneAuthCredential) {
//        FirebaseAuth.getInstance()
//                .signInWithCredential(phoneAuthCredential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
//                    finish();
//                    Toast.makeText(PhoneActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(PhoneActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
//    }
//    public void onClick(View view) {
//        String phone = editPhone.getText().toString().trim() + editPhone.getText().toString().trim();
//        Log.d("TAG", "onClick: " +view);
//        Toast.makeText(PhoneActivity.this, "Back to button", Toast.LENGTH_SHORT);
//        if (TextUtils.isEmpty(phone)){
//            editPhone.setError("Укажите номер телефона");
//            return;
//        }
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone,
//                60,
//                TimeUnit.SECONDS,
//                this,
//                callbacks);
//    }
//}
