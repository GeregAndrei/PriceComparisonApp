package com.example.price_analysis_app.Items;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.R;
import com.example.price_analysis_app.uiStuff.Technical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemDisplay extends AppCompatActivity {
    private RecyclerView linkList;
    private TextView name;
    private TextView specs;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_display);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Item selectedObject = (Item) getIntent().getParcelableExtra("selectedObject");
        name=findViewById(R.id.titleItemTv);
        img =findViewById(R.id.imageView2);
        specs=findViewById(R.id.technicalTv);
        specs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailView(selectedObject);
            }
        });

        linkList=findViewById(R.id.linkListRv);
        linkList.setLayoutManager(new LinearLayoutManager(this));
        LinkAdapter linkAdapter=new LinkAdapter(this,selectedObject.getLinkList());
        linkList.setAdapter(linkAdapter);
        if(selectedObject != null){
            name.setText(selectedObject.name);
            Glide.with(this).load(selectedObject.getImgUrl()).into(img);
        }
        if(selectedObject.name ==null){
            Log.d("LOL",selectedObject.toString());
        }

    }
    private void openDetailView(Item selectedObject) {
        Intent intent = new Intent(this, Technical.class);
        intent.putExtra("selectedObject",selectedObject);

        startActivity(intent);
    }

}