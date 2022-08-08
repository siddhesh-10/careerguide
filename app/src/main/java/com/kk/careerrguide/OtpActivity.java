package com.kk.careerrguide;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    EditText otpformuser;
    String _phoneNo;
    String codeBySystem;
    String userName;
    String email;
    String password;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        otpformuser = (EditText) (findViewById(R.id.etOtp));

        _phoneNo = getIntent().getStringExtra("phoneNo");
        userName = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");


        
        sendVerificationCodeToUser(_phoneNo);
        


    }

    private void sendVerificationCodeToUser(String phoneNo) {

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNo,
//                60,
//                TimeUnit.SECONDS,
//                this,
//                mCallbacks
//        );


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
       // mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;

                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();
                    if (code!=null){
                        otpformuser.setText(code);
                        verifyCode(code);
                    }

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OtpActivity.this, "pppp"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("rupesh","rr"+e.getMessage());
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                            rootnode = FirebaseDatabase.getInstance();
                            reference = rootnode.getReference("users");
                            UserHelpher helpherClass = new UserHelpher(userName,email,_phoneNo,password);
                            reference.child(_phoneNo).setValue(helpherClass);
                            Intent intent = new Intent(OtpActivity.this,HomeActivity.class);
                            intent.putExtra("userName",userName);
                            startActivity(intent);

                        } else {
                            
                            
                            
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(OtpActivity.this, "NOT", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    public void callNextScreenFromOTP(View view){
        String code = otpformuser.getText().toString();

        if (!code.isEmpty()){
            verifyCode(code);
        }
    }

}