package com.kk.careerrguide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    Button createAccount;
    private TextInputLayout passwordTextView,phonenumberTextView;
    private Button lbtn;
    private Button lcrete;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lcrete=(Button) findViewById(R.id.lcreatebtn);

        lcrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,createaccount.class);
                startActivity(intent);
            }

        });

        lbtn=(Button) findViewById(R.id.lbtn);

        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phonenumberTextView=(TextInputLayout)findViewById(R.id.lmobile);


                passwordTextView = (TextInputLayout)findViewById(R.id.lpassword);
                loginuser();



            }

        });

    }
    private void loginuser()
    {
        String password,mobile;

        password = passwordTextView.getEditText().getText().toString();
        mobile = phonenumberTextView.getEditText().getText().toString();

        if(validate(password,mobile)==false)
        {
            return;
        }
        String phonenumber ="+91"+mobile;
        Query checkuser=FirebaseDatabase.getInstance().getReference("users").orderByChild("mobile").equalTo(phonenumber);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String systempassword=snapshot.child(phonenumber).child("password").getValue(String.class);

                    if(systempassword.equals(password))
                    {
                        String fname=snapshot.child(phonenumber).child("name").getValue(String.class);
                        Toast.makeText(
                                getApplicationContext(),
                                "hi "+fname
                                ,
                                Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(Login.this,HomeActivity.class);
                        intent.putExtra("userName",fname);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(
                                getApplicationContext(),
                                "not valid mobile number or password"
                                ,
                                Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(
                            getApplicationContext(),
                            "not valid mobile number or password"
                            ,
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        getApplicationContext(),
                        "not valid mobile number or password"
                        ,
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }
        });
    }
    boolean validate(String password,String mobile)
    {
        if( password.length()==0 || mobile.length()==0 )
        {
            Toast.makeText(getApplicationContext(),
                    "Please enter all fields",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if( mobile.length() !=10)
        {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid mobile number",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if(password.length()>255 )
        {
            Toast.makeText(getApplicationContext(),
                    "enter valid details",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        return true;
    }
}