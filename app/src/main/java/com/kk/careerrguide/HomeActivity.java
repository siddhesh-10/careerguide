package com.kk.careerrguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_home);
        Intent intent=getIntent();
        String userName=intent.getStringExtra("userName");
        welcome=(TextView) findViewById(R.id.usernameView);
        welcome.setText("Welcome "+userName);
    }
}