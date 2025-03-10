package com.example.price_analysis_app.Items;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import coil.target.ImageViewTarget;

public class ItemDisplay extends AppCompatActivity implements Icallable {
    private RecyclerView linkList;
    private TextView name;
    private TextView specs;
    private ImageView img;
    private boolean isCommentsInflated = false;
    private CommentAdapter commentAdapter;
    private View commentsView;

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
        // Retrieve the selected item passed from the previous activity
        Item selectedObject = (Item) getIntent().getParcelableExtra("selectedObject");
        name=findViewById(R.id.titleItemTv);
        img =findViewById(R.id.imageView2);
        specs=findViewById(R.id.buttonTechnical);
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
        name.setText(selectedObject.name);
        //url to img library
        Glide.with(this).load(selectedObject.getImgUrl()).into(img);
        if(selectedObject.name ==null){
            Log.d("LOL",selectedObject.toString());
            }

        //dummy list
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment("Alice", "Great product!", 5));
        commentList.add(new Comment("Bob", "Could be better.", 4));
        commentList.add(new Comment("Bob2", "Could be better.", 3));
        commentList.add(new Comment("Bob3", "Could be better.", 2));
        commentAdapter = new CommentAdapter(this, commentList);

        Button showCommentsButton = findViewById(R.id.showCommentsButton);
        showCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCommentsInflated) {
                    ViewStub stub = findViewById(R.id.commentsViewStub);
                    commentsView = stub.inflate();
                    RecyclerView commentsRv = commentsView.findViewById(R.id.commentsRv);
                    commentsRv.setLayoutManager(new LinearLayoutManager(ItemDisplay.this));
                    commentsRv.setAdapter(commentAdapter);
                    isCommentsInflated = true;
                } else {
                    if (commentsView.getVisibility() == View.VISIBLE) {
                        commentsView.setVisibility(View.GONE);
                    } else {
                        commentsView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }
    private void openDetailView(Item selectedObject) {
        Intent intent = new Intent(this, Technical.class);
        intent.putExtra("selectedObject",selectedObject);

        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onLinkClicked(Link item) {
            String url = item.getSiteLink().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);

    }
}