package com.example.price_analysis_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Startup extends AppCompatActivity {
    Button btnRegisterAccount;
    Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        btnRegisterAccount = findViewById(R.id.btnAccount);
        btnSkip= findViewById(R.id.btnSkipAccount);
        btnRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Startup.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Startup.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}