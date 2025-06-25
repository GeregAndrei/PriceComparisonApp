package com.example.price_analysis_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.price_analysis_app.Account.BookmarkActivity;
import com.example.price_analysis_app.Account.LoginActivity;
import com.example.price_analysis_app.Menu.HomeActivity;

public class TempMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_temp_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonHome=findViewById(R.id.btnHome);
        Button buttonAccount=findViewById(R.id.btnAccount);
        Button buttonBookmarks=findViewById(R.id.btnBookmarks);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testHome();
            }

        });
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testLogin();
            }
        });
        buttonBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testBookmark();
            }
        });

    }


    public void testHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void testLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void testBookmark() {
        Intent intent = new Intent(this, BookmarkActivity.class);
        startActivity(intent);
    }
}