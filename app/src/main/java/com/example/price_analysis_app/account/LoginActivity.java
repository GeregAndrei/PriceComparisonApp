package com.example.price_analysis_app.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.price_analysis_app.R;
import com.example.price_analysis_app.uiStuff.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    private Button btn;
    private Button btnForgot;
    private EditText usr;
    private EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        usr =findViewById(R.id.edtUsername);
        pass=findViewById(R.id.edtPassword);
        btn = findViewById(R.id.btnLogIn);
        btnForgot=findViewById(R.id.btnForgot);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this, HomeActivity.class);
                SharedPreferences prefs = getSharedPreferences("Preferences",MODE_PRIVATE);
                prefs.edit().putBoolean("isFirstLaunch", true).apply();
                startActivity(intent);
            }
        });
    }


}