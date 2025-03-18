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
import com.example.price_analysis_app.Account.Account;
import com.example.price_analysis_app.Account.SessionManager;
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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ItemDisplay extends AppCompatActivity implements Icallable {
    private RecyclerView linkList;
    private Account currentAccount=SessionManager.getCurrentAccount();
    private TextView name;
    private TextView specs;
    private ImageView img;
    private boolean isCommentsInflated = false;
    private CommentAdapter commentAdapter;
    private View commentsView;
    private Context context;
    private ActivityResultLauncher<Intent> commentActivityLauncher;
    private FirebaseFirestore db;
    String selectedCollection;
   private static List<Comment> commentList = new ArrayList<>();
   List<String> bookmarks = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectedCollection = getIntent().getStringExtra("selectedOption");
        // Retrieve the selected item passed from the previous activity
        Item selectedObject = (Item) getIntent().getParcelableExtra("selectedObject");
        Log.d("UUUUUUUUUUUUUUU", selectedObject.getDocId());
        name = findViewById(R.id.titleItemTv);
        img = findViewById(R.id.imageView2);
        specs = findViewById(R.id.buttonTechnical);
        specs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailView(selectedObject);
            }
        });


        commentAdapter = new CommentAdapter(this, commentList);
        Account currentAccount = SessionManager.getCurrentAccount();
        //List<String> bookmarks = currentAccount.getBookmarkedItems();
        Button bookmarkItem = findViewById(R.id.bookmarkButton);
        bookmarkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmark(selectedObject,mAuth);
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


//        commentList.add(new Comment("Alice","1","g","Great product!",5));
//        commentList.add(new Comment("Bob","1" ,"h","Could be better.", 4));

       Button showCommentsButton = findViewById(R.id.showCommentsButton);
//        showCommentsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isCommentsInflated) {
//
//                    ViewStub stub = findViewById(R.id.commentsViewStub);
//                    commentsView = stub.inflate();
//                    RecyclerView commentsRv = commentsView.findViewById(R.id.commentsRv);
//
//                    commentsRv.setLayoutManager(new LinearLayoutManager(ItemDisplay.this));
//                    commentsRv.setAdapter(commentAdapter);
//
//                    isCommentsInflated = true;
//                    fetchCommentsFromFirestore(selectedCollection, selectedObject.getDocId());
//                } else {
//                    if (commentsView.getVisibility() == View.VISIBLE) {
//                        commentsView.setVisibility(View.GONE);
//                    } else {
//                        commentsView.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });
        showCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCommentsInflated) {
                    // Inflate the view and set up the RecyclerView
                    ViewStub stub = findViewById(R.id.commentsViewStub);
                    commentsView = stub.inflate();
                    RecyclerView commentsRv = commentsView.findViewById(R.id.commentsRv);
                    commentsRv.setLayoutManager(new LinearLayoutManager(ItemDisplay.this));
                    commentsRv.setAdapter(commentAdapter);
                    isCommentsInflated = true;
                    // Fetch comments only once
                    fetchCommentsFromFirestore(selectedCollection, selectedObject.getDocId());
                } else {
                    // Just toggle visibility without re-fetching
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

//    private void pushCommentToFirebase(String documentId, Comment comment) {
//
//        DocumentReference docRef = db.collection(selectedCollection).document(documentId);
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("comments", FieldValue.arrayUnion(comment.toString()));
//
//                        docRef.update("comments", FieldValue.arrayUnion(comment.toString()))
//                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Comment added successfully"))
//                                .addOnFailureListener(e -> Log.e("Firestore", "Error adding comment", e));
//                    }
private void pushCommentToFirebase(String documentId, Comment comment) {
    DocumentReference docRef = db.collection(selectedCollection).document(documentId);
    // Build a map for the new comment
    Map<String, Object> commentMap = new HashMap<>();
    commentMap.put("accountName", comment.getAccountName());
    commentMap.put("accountId", currentAccount.getId());
    commentMap.put("accountId", comment.getAccountId());
    commentMap.put("description", comment.getDescription());
    commentMap.put("bar", comment.getBar());

    // Use arrayUnion to append the map to the "comments" array
    docRef.update("comments", FieldValue.arrayUnion(commentMap))
            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Comment added successfully"))
            .addOnFailureListener(e -> Log.e("Firestore", "Error adding comment", e));
}



    private void addComment(Item selectedObject){
        Intent intent =new Intent(ItemDisplay.this, CommentsActivity.class);
        intent.putExtra("itemforcomment",selectedObject);
        commentActivityLauncher.launch(intent);
    }
//    private void fetchCommentsFromFirestore(String collectionName, String docId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(collectionName)
//                .document(docId)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<Comment> tempComments = new ArrayList<>();
//                    for (DocumentSnapshot doc : ) {
//                        // Retrieve the specific fields
//                        String accountName = doc.getString("accountName");
//                        String description = doc.getString("description");
//                        Double barDouble = doc.getDouble("bar");
//
//                        float bar = (barDouble != null) ? barDouble.floatValue() : 0f;
//                        Comment comment = new Comment(accountName, docId, description, bar);
//                        tempComments.add(comment);
//
//                    }
//
//                    // Update your adapter's list and notify changes
//                    commentList.clear();
//                    commentList.addAll(tempComments);
//                    commentAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching comments", e));
//    }
private void fetchCommentsFromFirestore(String collectionName, String docId) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(collectionName)
            .document(docId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Retrieve the "comments" field as an array of maps
                    List<Map<String, Object>> commentsField =
                            (List<Map<String, Object>>) documentSnapshot.get("comments");

                    // Temporary list to hold the comments we parse
                    List<Comment> tempComments = new ArrayList<>();

                    if (commentsField != null) {
                        for (Map<String, Object> commentMap : commentsField) {
                            // Extract each field from the map
                            String accountName = (String) commentMap.get("accountName");
                            String accountId = (String) commentMap.get("accountId");
                            String description = (String) commentMap.get("description");
                            Number barNumber   = (Number) commentMap.get("bar");

                            float bar = (barNumber != null) ? barNumber.floatValue() : 0f;


                            Comment comment = new Comment(accountName,currentAccount.getId(), docId, description, bar);

                            tempComments.add(comment);
                        }
                    }

                    // Clear your existing list and add the new comments
                    commentList.clear();
                    commentList.addAll(tempComments);
                    commentAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Firestore", "No such document");
                }
            })
            .addOnFailureListener(e -> Log.e("Firestore", "Error fetching comments", e));
}

    public void toggleBookmark (Item selectedObject, FirebaseAuth mAuth) {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.e("Firestore", "User not authenticated.");
            return;
        }
        String uid = firebaseUser.getUid();
        DocumentReference accountDocRef = db.collection("conturi").document(uid);
        accountDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> bookmarks = (List<String>) documentSnapshot.get("bookmarkedItems");

                        if (bookmarks != null && bookmarks.contains(selectedObject.getDocId())) {
                            // Item exists, remove it using FieldValue.arrayRemove()
                                accountDocRef.update("bookmarkedItems", FieldValue.arrayRemove(selectedObject.getDocId()))
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Item removed from bookmarks"))
                                    .addOnFailureListener(e -> Log.e("Firestore", "Error removing item from bookmarks", e));
                        } else {
                            // Item doesn't exist, add it using FieldValue.arrayUnion()
                            accountDocRef.update("bookmarkedItems", FieldValue.arrayUnion(selectedObject.getDocId()))
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Item added to bookmarks"))
                                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding item to bookmarks", e));
                        }
                    } else {
                        Log.e("Firestore", "Account document does not exist.");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching account document", e));
        //        if (db.collection("conturi").document(firebaseUser.getUid()).collection("bookmarkedItems").contains(selectedObject.productCode)) {
//            // Remove the item from bookmarks
//            db.collection("conturi").document(firebaseUser.getUid())
//                    .update("bookmarkedItems", FieldValue.arrayRemove(selectedObject.productCode))
//                    .addOnSuccessListener(aVoid -> {
//                        // Update local state if necessary
//                        //currentAccount.getBookmarkedItems().remove(selectedObject);
//                        Log.d("Firestore", "Item removed from bookmarks");
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle failure
//                        Log.e("Firestore", "Error removing item from bookmarks", e);
//                    });
//        } else {
//            // Add the item to bookmarks
//            db.collection("conturi").document(firebaseUser.getUid())
//                    .update("bookmarkedItems", FieldValue.arrayUnion(selectedObject.productCode))
//                    .addOnSuccessListener(aVoid -> {
//                        // Update local state if necessary
//                        //currentAccount.getBookmarkedItems().add(selectedObject.productCode);
//                        Log.d("Firestore", "Item added to bookmarks");
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle failure
//                        Log.e("Firestore", "Error adding item to bookmarks", e);
//                    });
//
//        }
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
    ChatMessage systemMessage = new ChatMessage("system", "You are a product analyst.");
    ChatMessage userMessage = new ChatMessage("user", "Analyze the following product specifications and provide a detailed review with pros and cons. List reasons why the product is good and why it might be prone to malfunction: " + technicalSpecs);

    List<ChatMessage> messages = new ArrayList<>();
messages.add(systemMessage);
messages.add(userMessage);

    ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", messages);

}