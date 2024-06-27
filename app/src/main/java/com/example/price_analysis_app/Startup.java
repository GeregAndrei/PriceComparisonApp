package com.example.price_analysis_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavHostController;

import com.example.price_analysis_app.account.LoginActivity;
import com.example.price_analysis_app.uiStuff.HomeActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Startup extends AppCompatActivity {
    private Button btnRegisterAccount;
    private Button btnSkip;
NavHostController navHostController=new NavHostController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        btnRegisterAccount = findViewById(R.id.btnAccount);
        btnSkip = findViewById(R.id.btnSkipAccount);
        SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);
        if (isFirstLaunch) {
            btnRegisterAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Startup.this, LoginActivity.class);
                    prefs.edit().putBoolean("isFirstLaunch", false).apply();
                    startActivity(intent);
                }
            });
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Startup.this, HomeActivity.class);
                    prefs.edit().putBoolean("isFirstLaunch", false).apply();
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();

        }
    }
}