package com.example.price_analysis_app.Items;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.Links.Link;
import com.example.price_analysis_app.Links.LinkAdapter;
import com.example.price_analysis_app.comments.Comment;
import com.example.price_analysis_app.comments.CommentAdapter;
import com.example.price_analysis_app.R;
import com.example.price_analysis_app.comments.CommentsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ItemDisplay extends AppCompatActivity implements Icallable {
    private RecyclerView linkList;
    private TextView name;
    private TextView specs;
    private ImageView img;
    private boolean isCommentsInflated = false;
    private CommentAdapter commentAdapter;
    private View commentsView;
    private Context context;
    private ActivityResultLauncher<Intent> commentActivityLauncher;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db=FirebaseFirestore.getInstance();
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
        Log.d("UUUUUUUUUUUUUUU",selectedObject.getDocId());
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
        commentList.add(new Comment("Alice","a","Great product!",5));
        commentList.add(new Comment("Bob","b" ,"Could be better.", 4));
        commentList.add(new Comment("Bob2","c" ,"Could be better.", 3));
        commentList.add(new Comment("Bob3", "d","Could be better.", 2));
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
        Button addcomment =findViewById(R.id.buttonAddComment);
        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addComment(selectedObject);
            }
        });
        commentActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Comment newComment = data.getParcelableExtra("newComment");
                            // Assuming selectedObject is available (e.g., passed to this activity)

                            selectedObject.addComment(newComment);
                            commentAdapter.notifyDataSetChanged();
                            pushCommentToFirebase(selectedObject.getDocId(), newComment);
                        }
                    }
                }
        );



    }

    private void pushCommentToFirebase(String documentId, Comment comment) {

        DocumentReference docRef = db.collection("combine_frigorifice").document(documentId);
                        Map<String, Object> data = new HashMap<>();
                        data.put("comments", FieldValue.arrayUnion(comment.toString()));

                        docRef.update("comments", FieldValue.arrayUnion(comment.toString()))
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Comment added successfully"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error adding comment", e));
                    }



    private void addComment(Item selectedObject){
        Intent intent =new Intent(ItemDisplay.this, CommentsActivity.class);
        intent.putExtra("itemforcomment",selectedObject);
        commentActivityLauncher.launch(intent);
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