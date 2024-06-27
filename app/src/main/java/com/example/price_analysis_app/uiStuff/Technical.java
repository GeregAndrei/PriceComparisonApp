package com.example.price_analysis_app.uiStuff;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.price_analysis_app.Items.Item;
import com.example.price_analysis_app.R;

public class Technical extends AppCompatActivity {
    private WebView technical;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_technical);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Item selectedObject = (Item) getIntent().getParcelableExtra("selectedObject");
       technical =findViewById(R.id.technicalWebView);
       technical.loadData(selectedObject.getTechnicalChar(),"text/html","UTF-8");
       
    }
}