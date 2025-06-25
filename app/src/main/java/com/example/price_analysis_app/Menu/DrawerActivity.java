package com.example.price_analysis_app.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.example.price_analysis_app.R;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.price_analysis_app.Account.BookmarkActivity;
import com.example.price_analysis_app.Account.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);


        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected void setContentLayout(@LayoutRes int layoutResID) {
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (!(this instanceof HomeActivity)) {
                startActivity(new Intent(this, HomeActivity.class));
            }
        } else if (id == R.id.nav_login) {
            if (!(this instanceof LoginActivity)) {
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else if (id == R.id.nav_bookmarks) {
            if (!(this instanceof BookmarkActivity)) {
                startActivity(new Intent(this, BookmarkActivity.class));
            }
        }

        drawerLayout.closeDrawers();
        return true;
    }
}
