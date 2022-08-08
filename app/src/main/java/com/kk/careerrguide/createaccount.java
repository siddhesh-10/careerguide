package com.kk.careerrguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class createaccount extends AppCompatActivity {

    TextInputEditText regname,regemail,regmobile,regpassword;
    Button regbutton,regloginvbutton;

    FirebaseDatabase rootnode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        regname = findViewById(R.id.reg_Name);
        regemail = findViewById(R.id.reg_Email);
        regmobile = findViewById(R.id.reg_Mobile);
        regpassword = findViewById(R.id.reg_Password);
        regbutton = findViewById(R.id.reg_Button);
        regloginvbutton = findViewById(R.id.reg_Login_Button);

        regloginvbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(createaccount.this,Login.class);
                startActivity(intent);
            }
        });



        //Button signUpBtn = (Button) findViewById(R.id.reg_Button);

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootnode = FirebaseDatabase.getInstance();
                reference = rootnode.getReference("users");

                //Getting values
                String name = regname.getEditableText().toString();
                String email = regemail.getEditableText().toString();
                String mobile = regmobile.getEditableText().toString();
                String password = regpassword.getEditableText().toString();
                String phonenumber ="+91"+mobile;

                if(validate(name,password,email,mobile)==false)
                {
                    return;
                }



                Query checkuser=FirebaseDatabase.getInstance().getReference("users").orderByChild("mobile").equalTo(phonenumber);

                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "user with this number is already exist"
                                    ,
                                    Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                        else
                        {
//                            UserHelpher helpherClass = new UserHelpher(name,email,phonenumber,password);
//                            reference.child(mobile).setValue(helpherClass);

                            Intent intent = new Intent(createaccount.this,OtpActivity.class);
                            intent.putExtra("phoneNo",phonenumber);
                            intent.putExtra("userName",name);
                            intent.putExtra("password",password);
                            intent.putExtra("email",email);
                            startActivity(intent);
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
        });
    }
    boolean validate(String name,String password,String email,String mobile)
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
        if( password.length() <5)
        {
            Toast.makeText(getApplicationContext(),
                    "Password length should greater than 4",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if(password.length()>255 || name.length()>255 || email.length()>255)
        {
            Toast.makeText(getApplicationContext(),
                    "enter valid details",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
        {
            Toast.makeText(getApplicationContext(),
                    "enter valid email",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if(pat.matcher(email).matches()==false)
        {
            Toast.makeText(getApplicationContext(),
                    "enter valid email",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }
}